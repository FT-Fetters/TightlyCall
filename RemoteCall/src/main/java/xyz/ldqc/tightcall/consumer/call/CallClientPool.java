package xyz.ldqc.tightcall.consumer.call;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fetters
 */
public class CallClientPool {

    private final Map<InetSocketAddress, CallClient> pool;

    private final HeartThread heartThread;

    public CallClientPool(){
        this.pool = new HashMap<>();
        this.heartThread = new HeartThread(pool);
    }

    public Object doCall(InetSocketAddress target, Object req){
        CallClient callClient = pool.get(target);
        if (callClient == null){
            callClient = newConnect(target);
            pool.put(target, callClient);
        }
        Object response = callClient.doCall(req);
        heartThread.updateCallTime(target);
        return response;
    }

    private CallClient newConnect(InetSocketAddress target){
        return CallClient.builder().target(target).boot();
    }

    private static class HeartThread extends Thread {

        private final Map<InetSocketAddress, Long> timeMap;
        private final Map<InetSocketAddress, CallClient> pool;


        private boolean terminate = false;

        public HeartThread(Map<InetSocketAddress, CallClient> pool){
            this.pool = pool;
            this.timeMap = new HashMap<>();
        }

        public void terminate(){
            this.terminate = true;
        }



        @Override
        public void run() {
            while (!terminate){
                doCheck();
                try {
                    wait(1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void doCheck(){
            for (InetSocketAddress address : timeMap.keySet()) {
                long curTime = System.currentTimeMillis();
                Long lastCallTime = timeMap.get(address);
                if (curTime - lastCallTime > 1000 * 60 * 5){
                    // 超时5分钟未调用移除客户端
                    CallClient callClient = pool.get(address);
                    callClient.terminate();
                    pool.remove(address);
                    timeMap.remove(address);
                }
            }
        }

        public void updateCallTime(InetSocketAddress address){
            timeMap.put(address, System.currentTimeMillis());
        }
    }
}
