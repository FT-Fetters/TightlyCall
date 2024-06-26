package xyz.ldqc.tightcall.consumer.call;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import xyz.ldqc.tightcall.common.response.CallResponse;
import xyz.ldqc.tightcall.registry.server.response.AbstractResponse;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * CallClient连接池
 *
 * @author Fetters
 */
public class CallClientPool {

    private final Map<InetSocketAddress, CallClient> pool;

    private final HeartThread heartThread;

    private final ExecutorService asyncThreadPool;

    public CallClientPool() {
        this.pool = new HashMap<>();
        this.heartThread = new HeartThread(pool);
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.asyncThreadPool = new ThreadPoolExecutor(
            availableProcessors / 2, availableProcessors, 60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(availableProcessors * 10),
            r -> new Thread(r, "call-client-thread")
        );
    }

    public Object doCall(InetSocketAddress target, Object req) {
        target = new InetSocketAddress(target.getHostString(), target.getPort());
        CallClient callClient = pool.get(target);
        if (callClient == null) {
            callClient = newConnect(target);
            pool.put(target, callClient);
        }
        CallResponse response = callClient.doCall(req);
        heartThread.updateCallTime(target);
        return response.getBody();
    }

    public Future<Object> doAsyncCall(InetSocketAddress target, Object req) {
        return asyncThreadPool.submit(() -> doCall(target, req));
    }

    private CallClient newConnect(InetSocketAddress target) {
        return CallClient.builder().target(target).boot();
    }

    private static class HeartThread extends Thread {

        private final Map<InetSocketAddress, Long> timeMap;
        private final Map<InetSocketAddress, CallClient> pool;


        private boolean terminate = false;

        public HeartThread(Map<InetSocketAddress, CallClient> pool) {
            this.pool = pool;
            this.timeMap = new HashMap<>();
        }

        public void terminate() {
            this.terminate = true;
        }


        @Override
        public void run() {
            while (!terminate) {
                doCheck();
                try {
                    wait(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void doCheck() {
            for (InetSocketAddress address : timeMap.keySet()) {
                long curTime = System.currentTimeMillis();
                Long lastCallTime = timeMap.get(address);
                if (curTime - lastCallTime > 1000 * 60 * 5) {
                    // 超时5分钟未调用移除客户端
                    CallClient callClient = pool.get(address);
                    callClient.terminate();
                    pool.remove(address);
                    timeMap.remove(address);
                }
            }
        }

        public void updateCallTime(InetSocketAddress address) {
            timeMap.put(address, System.currentTimeMillis());
        }
    }
}
