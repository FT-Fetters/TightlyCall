package xyz.ldqc.tightcall.client.exce.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.ChannelChainGroup;
import xyz.ldqc.tightcall.chain.support.ChannelPostHandlerOutBoundChain;
import xyz.ldqc.tightcall.client.exce.ClientExec;
import xyz.ldqc.tightcall.client.message.MessageReceiveThread;
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
public class NioClientExec implements ClientExec {

    private static final Logger logger = LoggerFactory.getLogger(NioClientExec.class);

    private final InetSocketAddress socketAddress;

    private SocketChannel channel;

    private Selector selector;

    private ResultPool<Integer, Object> resultPool;

    private ChannelChainGroup chainGroup;

    private MessageReceiveThread receiver;


    public NioClientExec(InetSocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    @Override
    public void setChainGroup(ChainGroup group) {
        this.chainGroup = (ChannelChainGroup) group;
    }

    @Override
    public void terminate() {
        receiver.terminate();
        try {
            channel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void start() {
        doConnect();
        initReceiver();
        initOutBound();
    }

    @Override
    public CacheBody write(Object o) {
        if (this.channel == null) {
            throw new RuntimeException("not connected yet");
        }
        CacheBody cacheBody;
        if (CacheBody.class.isAssignableFrom(o.getClass())) {
            cacheBody = ((CacheBody) o);
        }else {
            cacheBody = new CacheBody(o);
        }
        chainGroup.doOutBoundChain(channel, cacheBody);
        return cacheBody;
    }

    @Override
    public Object writeAndWait(Object o) {
        CacheBody cacheBody = write(o);
        return resultPool.getResult(cacheBody.getSerialNumber());
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


    private void initReceiver() {
        this.resultPool = new BlockResultPool<>();
        this.receiver = new MessageReceiveThread(channel, resultPool, chainGroup);
    }

    private void initOutBound(){
        chainGroup.addLast(new ChannelPostHandlerOutBoundChain());
    }
}
