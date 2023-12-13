package xyz.ldqc.tightcall.chain.support;

import org.slf4j.Logger;
import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.buffer.SimpleByteData;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

/**
 * @author Fetters
 */
public abstract class AbstractChannelPreHandlerInBoundChain implements InboundChain, ChannelHandler {

    protected Logger logger;

    protected Chain nextChain;

    /**
     * 从channel读取数据
     */
    protected AbstractByteData readDataFromChanel(SocketChannel socketChannel){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int readLen = socketChannel.read(buffer);
            if ( readLen == -1) {
                return null;
            }
            if (readLen == 0){
                return new SimpleByteData();
            }
            return new SimpleByteData(buffer);
        } catch (IOException e) {
            logger.info("remote {} force close connection", socketChannel);
            return null;
        }
    }

    @Override
    public void doChain(Channel channel, Object obj) {
        doHandler(channel, obj);
    }

    @Override
    public void setNextChain(Chain chain) {
        this.nextChain = chain;
    }
}
