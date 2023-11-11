package xyz.ldqc.tightcall.server.exec.support;

import xyz.ldqc.tightcall.exception.ServerException;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.Chainable;
import xyz.ldqc.tightcall.server.exec.ServerExec;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Fetters
 */
public class NioServerExec implements ServerExec, Chainable {

    /**
     * 默认端口
     */
    private static final int DEFAULT_PORT = 6770;
    /**
     * 默认执行器数量
     */
    private static final int DEFAULT_EXEC_NUM = 8;
    /**
     * 最大执行器数量
     */
    private static final int MAX_EXEC_NUM = 256;
    /**
     * 最大端口
     */
    private static final int MAX_PORT = 65535;

    private Chain chainHead;

    private int port;

    private int execNum;

    AcceptSelectorThread acceptSelectorThread;


    public NioServerExec() {
        new NioServerExec(DEFAULT_PORT, DEFAULT_EXEC_NUM);
    }

    public NioServerExec(int port, int execNum) {
        this.port = port;
        this.execNum = execNum;
    }

    /**
     * 检查基础参数是否合法
     */
    private void doCheckBaseParam(int port, int execNum) {
        checkPort(port);
        checkExecNum(execNum);
    }

    /**
     * 检查端口是否合法
     *
     * @param port 端口
     */
    private void checkPort(int port) {
        if (port < 0 || port > MAX_PORT) {
            try {
                throw new ServerException("illegal port");
            } catch (ServerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 检查执行器数量是否合法
     *
     * @param execNum 执行器数量
     */
    private void checkExecNum(int execNum) {
        if (execNum < 0 || execNum > MAX_EXEC_NUM) {
            try {
                throw new ServerException("illegal exec num");
            } catch (ServerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 运行之前检查所有是否准备就绪
     */
    private void ensureReady() {
        doCheckBaseParam(this.port, this.execNum);

    }


    @Override
    public void start() {
        ensureReady();
        start0();
    }

    private void start0() {
        this.acceptSelectorThread = new AcceptSelectorThread(this.port, this.execNum);
        this.acceptSelectorThread.start();
    }

    @Override
    public void setChainHead(Chain chain) {
        this.chainHead = chain;
    }


    /**
     * 接受选择线程类
     */
    private static class AcceptSelectorThread extends Thread {

        private final int port;

        private final int execNum;

        private Selector selector;

        private ServerSocketChannel ssc;

        private SelectionKey acceptKey;

        private Worker[] workers;

        private ExecutorService workerThreadPool;

        public AcceptSelectorThread(int port, int execNum) {
            this.port = port;
            this.execNum = execNum;
        }

        @Override
        public void run() {
            super.run();
            init();
            wakeWorker();
        }

        /**
         * 初始化该线程的基本元素
         */
        private void init() {
            Thread.currentThread().setName("nio-exec-accept");
            try {
                this.selector = Selector.open();
                this.ssc = ServerSocketChannel.open();
                // 设置非阻塞
                ssc.configureBlocking(false);
                // 设置感兴趣的事件
                this.acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT, null);
                // 绑定端口
                ssc.bind(new InetSocketAddress(this.port));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void wakeWorker() {
            // 初始化工作线程
            workers = new Worker[execNum];
            // 初始化工作线程池
            workerThreadPool = Executors.newFixedThreadPool(execNum);
            // 初始化每一个工人
            for (int i = 0; i < execNum; i++) {
                workers[i] = new Worker(i, workerThreadPool);
            }
        }
    }

    private static class Worker implements Runnable {

        private boolean terminate = false;

        private final int number;

        private Selector selector;

        private final ExecutorService workerTheadPool;

        private final ConcurrentLinkedQueue<SocketChannel> socketChannelQueue = new ConcurrentLinkedQueue<>();


        public Worker(int number, ExecutorService workerTheadPool) {
            this.number = number;
            this.workerTheadPool = workerTheadPool;
        }

        public void terminate() {
            this.terminate = true;
        }

        /**
         * 将accept线程接收到的channel分配给工作线程
         */
        public void distributeWork(SocketChannel socketChannel) {
            // 首次分配需要初始化
            if (selector == null) {
                init();
            }
            doDistribute(socketChannel);
        }

        private void doDistribute(SocketChannel socketChannel) {
            socketChannelQueue.add(socketChannel);
            selector.wakeup();
        }

        /**
         * 工人初始化方法
         */
        private void init() {
            try {
                selector = Selector.open();
                // 提交至工作线程
                workerTheadPool.submit(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            initThread();
            startWork();
        }

        /**
         * 初始化线程
         */
        private void initThread() {
            Thread.currentThread().setName("nio-exec-worker-" + number);
        }

        private void startWork() {
            while (!terminate) {
                doWork();
            }
        }

        private void doWork() {
            try {
                selector.select();
                preWork();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                doWork0(selectionKeys);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void doWork0(Set<SelectionKey> selectionKeys) {
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                focusKey(key);
            }
        }

        private void focusKey(SelectionKey key) {
            if (key.isReadable()) {
                readableKey(key);
            }
        }

        private void readableKey(SelectionKey key) {
            try (SocketChannel channel = (SocketChannel) key.channel()) {


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


        /**
         * 前置工作，为了将socket注册到该selector
         */
        private void preWork() {
            try (SocketChannel sc = socketChannelQueue.poll()) {
                if (sc != null) {
                    sc.register(selector, SelectionKey.OP_READ, null);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


    }
}
