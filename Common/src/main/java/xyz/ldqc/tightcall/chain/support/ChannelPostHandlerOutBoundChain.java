package xyz.ldqc.tightcall.chain.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.OutboundChain;
import xyz.ldqc.tightcall.exception.ChainException;
import xyz.ldqc.tightcall.protocol.CacheBody;
import xyz.ldqc.tightcall.protocol.ProtocolDataFactory;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

/**
 * 调用链的出站后置处理链点，用于发送数据
 * @author Fetters
 */
public class ChannelPostHandlerOutBoundChain extends AbstractChannelPostHandlerOutBoundChain {

    protected Logger logger = LoggerFactory.getLogger(ChannelPostHandlerOutBoundChain.class);

    @Override
    public void doHandler(Channel channel, Object obj) {
        handleSocketChannel(channel, obj);
    }

    private void handleSocketChannel(Channel channel, Object obj) {
        // 判断是否是SocketChannel
        SocketChannel socketChannel = checkIsSocketChannel(channel);
        if (socketChannel == null){
            return;
        }

        // 如果是CacheBody
        if (obj instanceof CacheBody){
            handleCacheBody(socketChannel, obj);
        }
    }

    /**
     * 处理CacheBody，解析后直接发送
     */
    private void handleCacheBody(SocketChannel socketChannel, Object obj){
        CacheBody cacheBody = (CacheBody) obj;
        if (cacheBody == null){
            throw new ChainException("cache body can not be null");
        }
        AbstractByteData data = cacheBody.getData();
        if (data == null){
            return;
        }
        byte[] bytes = data.readBytes();
        int serialNumber = cacheBody.getSerialNumber();
        byte[] dataArray = ProtocolDataFactory.create(bytes, serialNumber);
        doWrite(socketChannel, dataArray);
    }




    /**
     * 检查Channel是否为SocketChannel，转化后并返回
     */
    private SocketChannel checkIsSocketChannel(Channel channel){
        if (!(channel instanceof SocketChannel)) {
            return null;
        }
        return ((SocketChannel) channel);
    }
}
