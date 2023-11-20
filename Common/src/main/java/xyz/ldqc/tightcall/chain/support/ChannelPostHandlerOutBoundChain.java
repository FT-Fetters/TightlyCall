package xyz.ldqc.tightcall.chain.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.OutboundChain;
import xyz.ldqc.tightcall.protocol.CacheBody;
import xyz.ldqc.tightcall.protocol.ProtocolDataFactory;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;
import xyz.ldqc.tightcall.util.ByteUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

/**
 * 调用链的出站后置处理链点，用于发送数据
 * @author Fetters
 */
public class ChannelPostHandlerOutBoundChain implements OutboundChain, ChannelHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChannelPostHandlerOutBoundChain.class);

    @Override
    public void doChain(Channel channel, Object obj) {
        doHandler(channel, obj);
    }

    @Override
    public void setNextChain(Chain chain) {

    }

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
            return;
        }

        // 其它对象的处理方法
        handleOtherObj(socketChannel, obj);
    }

    /**
     * 处理CacheBody，解析后直接发送
     */
    private void handleCacheBody(SocketChannel socketChannel, Object obj){
        CacheBody cacheBody = (CacheBody) obj;
        AbstractByteData data = cacheBody.getData();
        byte[] bytes = data.readBytes();
        int serialNumber = cacheBody.getSerialNumber();
        byte[] dataArray = ProtocolDataFactory.create(bytes, serialNumber);
        ByteBuffer byteBuffer = ByteBuffer.allocate(dataArray.length);
        byteBuffer.put(dataArray);
        sendByteBuffer(socketChannel, byteBuffer);
    }

    private void handleOtherObj(SocketChannel socketChannel, Object obj){
        byte[] objArray = obj2array(obj);
        if (objArray == null){
            return;
        }
        byte[] data = ProtocolDataFactory.create(objArray);
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length);
        byteBuffer.put(data);
        sendByteBuffer(socketChannel, byteBuffer);
    }

    private void sendByteBuffer(SocketChannel socketChannel, ByteBuffer buffer){
        try {
            // 需要切换到读模式才能让socketChannel读取到才能发送成功
            buffer.flip();
            socketChannel.write(buffer);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 对象转字节数组
     */
    private byte[] obj2array(Object obj){
        if (obj == null){
            return null;
        }
        byte[] byteArr;
        if (obj instanceof String){
            String objStr = (String) obj;
            byteArr = objStr.getBytes();
        }else {
            byteArr = ByteUtil.obj2ByteArray(obj);
        }
        return byteArr;
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
