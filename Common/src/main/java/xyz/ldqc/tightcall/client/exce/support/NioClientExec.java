package xyz.ldqc.tightcall.client.exce.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.Chainable;
import xyz.ldqc.tightcall.chain.ChannelChainGroup;
import xyz.ldqc.tightcall.client.exce.ClientExec;
import xyz.ldqc.tightcall.client.message.MessageReceiveThread;
import xyz.ldqc.tightcall.client.message.MessageWriteThread;
import xyz.ldqc.tightcall.client.message.support.BlockingMessageQueue;
import xyz.ldqc.tightcall.pool.ResultPool;
import xyz.ldqc.tightcall.pool.support.BlockResultPool;
import xyz.ldqc.tightcall.protocol.CacheBody;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author Fetters
 */
public class NioClientExec implements ClientExec, Chainable {

    private static final Logger logger = LoggerFactory.getLogger(NioClientExec.class);

    private final InetSocketAddress socketAddress;

    private SocketChannel channel;

    private Selector selector;

    private MessageWriteThread writer;

    private ResultPool<Integer, Object> resultPool;

    private ChannelChainGroup chainGroup;

    private MessageReceiveThread receiver;

    private boolean terminate = false;

    public NioClientExec(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    public void setChainGroup(ChainGroup group) {
        this.chainGroup = (ChannelChainGroup) group;
    }

    private void terminate() {
        this.terminate = true;
    }


    @Override
    public void start() {
        doConnect();
        initWriter();
        initReceiver();
    }

    @Override
    public void write(Object o) {
        if (this.channel == null) {
            throw new RuntimeException("not connected yet");
        }
        writer.write(o);
    }

    @Override
    public Object writeAndWait(Object o) {
        if (this.channel == null) {
            throw new RuntimeException("not connected yet");
        }
        return writer.writeAndWait(o);
    }

    private void doConnect() {
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(socketAddress);
            socketChannel.configureBlocking(false);
            this.selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            this.channel = socketChannel;
        } catch (IOException e) {
            logger.error("connect fail, reason: {}", e.getMessage());
        }
    }

    private void initWriter() {
        this.resultPool = new BlockResultPool<>();
        this.writer = new MessageWriteThread(new BlockingMessageQueue<>(), resultPool, channel);
    }

    private void initReceiver() {
        this.receiver = new MessageReceiveThread(channel, resultPool, chainGroup);
    }
}
