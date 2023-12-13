package xyz.ldqc.tightcall.chain.support;

import org.slf4j.Logger;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.OutboundChain;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

/**
 * @author Fetters
 */
public abstract class AbstractChannelPostHandlerOutBoundChain implements OutboundChain, ChannelHandler {

    protected Logger logger;

    @Override
    public void doChain(Channel channel, Object obj) {
        doHandler(channel, obj);
    }

    @Override
    public void setNextChain(Chain chain) {

    }


    protected void doWrite(SocketChannel target , byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 数据总长度
        int totalLen = data.length;
        // 剩余发送的长度
        int remaining = totalLen;
        while (remaining > 0) {
            int offset = totalLen - remaining;
            // 切换到写模式
            buffer.clear();
            // 本次写入的字节数
            int len = Math.min(buffer.remaining(), remaining);
            buffer.put(data, offset, len);
            // 切换到读模式
            buffer.flip();
            remaining -= len;
            try {
                int writeLen = target.write(buffer);
                remaining += len - writeLen;
                logger.debug("write len: {}", writeLen);
            } catch (IOException e) {
                logger.error("send message error: {}", e.getMessage());
                return;
            }
        }
    }
}
