package xyz.ldqc.tightcall.server.exec.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.Chainable;
import xyz.ldqc.tightcall.chain.ChannelChainGroup;
import xyz.ldqc.tightcall.exception.ServerException;
import xyz.ldqc.tightcall.server.exec.ServerExec;
import xyz.ldqc.tightcall.server.load.LoadBalance;
import xyz.ldqc.tightcall.server.load.support.RandomLoadBalance;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Fetters
 */
public class NioServerExec implements ServerExec {

    private static final Logger logger = LoggerFactory.getLogger(NioServerExec.class);

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

    private ChannelChainGroup chainGroup;

    private final int port;

    private final int execNum;

    AcceptSelectorThread acceptSelectorThread;


    public NioServerExec() {
        this.port = DEFAULT_PORT;
        this.execNum = DEFAULT_EXEC_NUM;
    }

    public NioServerExec(int port){
        this.port = port;
        this.execNum = DEFAULT_EXEC_NUM;
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
        // 开启接收连接线程
        this.acceptSelectorThread = new AcceptSelectorThread(this.port, this.execNum, this.chainGroup);
        this.acceptSelectorThread.start();
        logger.info("server running on port {}", port);
    }


    @Override
    public void setChainGroup(ChainGroup group) {
        this.chainGroup = (ChannelChainGroup) group;
    }


    /**
     * 接受选择线程类
     */
    private static class AcceptSelectorThread extends Thread {

        private static final Logger logger = LoggerFactory.getLogger(AcceptSelectorThread.class);


        private final int port;

        private final int execNum;

        private Selector selector;

        private Worker[] workers;

        private final ChannelChainGroup chainGroup;

        private boolean terminate = false;

        private final LoadBalance<Worker> loadBalance;

        public AcceptSelectorThread(int port, int execNum, ChannelChainGroup chainGroup) {
            this(port, execNum, chainGroup, new RandomLoadBalance<>());
        }

        public AcceptSelectorThread(int port, int execNum, ChannelChainGroup chainGroup, LoadBalance<Worker> loadBalance) {
            this.port = port;
            this.execNum = execNum;
            this.chainGroup = chainGroup;
            this.loadBalance = loadBalance;
        }

        @Override
        public void run() {
            init();
            wakeWorker();
            doLoop();
        }

        // 终止
        public void terminate() {
            this.terminate = true;
            for (Worker worker : workers) {
                worker.terminate();
            }
        }

        /**
         * 开始accept线程循环
         */
        private void doLoop() {
            while (!terminate) {
                try {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    watchAcceptKeys(selectionKeys);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        /**
         * 查看每一个accept key
         */
        private void watchAcceptKeys(Set<SelectionKey> selectionKeys) {
            Iterator<SelectionKey> iter = selectionKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    try {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel sc = serverSocketChannel.accept();
                        sc.configureBlocking(false);
                        logger.info("new connected {}", sc.getRemoteAddress());

                        Worker worker = loadBalance.load();
                        worker.distributeWork(sc);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        /**
         * 初始化该线程的基本元素
         */
        private void init() {
            Thread.currentThread().setName("nio-exec-accept");
            try {
                ServerSocketChannel ssc = ServerSocketChannel.open();
                this.selector = Selector.open();
                // 设置非阻塞
                ssc.configureBlocking(false);
                // 设置感兴趣的事件
                SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT, null);
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
            ExecutorService workerThreadPool = Executors.newFixedThreadPool(execNum);
            // 初始化每一个工人
            for (int i = 0; i < execNum; i++) {
                workers[i] = new Worker(i, workerThreadPool);
                workers[i].setChainGroup(chainGroup);
            }
            // 将工人加入负载均衡
            this.loadBalance.addAll(workers);
        }
    }

    private static class Worker implements Runnable, Chainable {

        private static final Logger logger = LoggerFactory.getLogger(Worker.class);

        private boolean terminate = false;

        private final int number;

        private Selector selector;

        private ChannelChainGroup chainGroup;

        /**
         * 工作线程池
         */
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
                workerTheadPool.execute(this);
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
            Thread.currentThread().setUncaughtExceptionHandler(
                    (thread, throwable) -> logger.error("ThreadPool {} got exception", thread, throwable)
            );
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
            SocketChannel channel = (SocketChannel) key.channel();
            chainGroup.doChain(channel, key);
        }


        /**
         * 前置工作，为了将socket注册到该selector
         */
        private void preWork() {
            try {
                SocketChannel sc = socketChannelQueue.poll();
                if (sc != null) {
                    sc.register(selector, SelectionKey.OP_READ, null);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


        @Override
        public void setChainGroup(ChainGroup group) {
            this.chainGroup = (ChannelChainGroup) group;
        }
    }
}
