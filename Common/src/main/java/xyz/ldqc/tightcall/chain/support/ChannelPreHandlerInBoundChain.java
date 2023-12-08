package xyz.ldqc.tightcall.chain.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.buffer.SimpleByteData;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.protocol.CacheBody;
import xyz.ldqc.tightcall.exception.ProtocolException;
import xyz.ldqc.tightcall.protocol.ProtocolConstant;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;
import xyz.ldqc.tightcall.util.ByteUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接收消息的前置处理链，用于获取符合协议的数据
 * @author Fetters
 */
public class ChannelPreHandlerInBoundChain implements InboundChain, ChannelHandler {

    private static final Logger logger = LoggerFactory.getLogger(ChannelPreHandlerInBoundChain.class);


    private final ConcurrentHashMap<Channel, CacheBody> cacheMap = new ConcurrentHashMap<>();

    private Chain nextChain;


    public ChannelPreHandlerInBoundChain() {

    }




    @Override
    public void doHandler(Channel channel, Object obj) {
        handleSocketChannel(channel, obj);
    }

    private void handleSocketChannel(Channel channel, Object obj) {
        // 判断是否是SocketChannel
        if (!(channel instanceof SocketChannel)) {
            return;
        }
        if (!(obj instanceof SelectionKey)){
            throw new RuntimeException("obj must be selection key");
        }
        SelectionKey selectionKey = (SelectionKey) obj;
        SocketChannel socketChannel = ((SocketChannel) channel);
        CacheBody cacheBody = cacheMap.get(channel);
        // 读取数据
        AbstractByteData abstractByteData = readDataFromChanel(socketChannel);
        // 如果回传的数据是Null代表断开连接
        if (abstractByteData == null){
            selectionKey.cancel();
            cacheMap.remove(channel);
            try {
                logger.info("{} disconnect", socketChannel.getRemoteAddress());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (cacheBody == null){
            // 缓存为空代表是第一批的数据
            handleHeadData(socketChannel, abstractByteData);
        }else if (cacheBody.getLen() == -1){
            // 已读取过但请求头未读取完成
            AbstractByteData bodyData = cacheBody.getTmpData();
            bodyData.writeBytes(abstractByteData.readBytes());
            handleHeadData(socketChannel, bodyData);
        }else {
            appendData(socketChannel, abstractByteData);
        }
    }

    private void handleHeadData(SocketChannel socketChannel, AbstractByteData abstractByteData){
        if (abstractByteData.remaining() < ProtocolConstant.PROTOCOL_HEAD_LEN){
            // 读取到的字节长度小于PROTOCOL_HEAD_LEN代表首次的数据
            logger.debug("byte data len: {}, str: {}", abstractByteData.remaining(), abstractByteData);
            CacheBody cacheBody = new CacheBody();
            cacheBody.setTmpData(abstractByteData);
            cacheMap.put(socketChannel, cacheBody);
            return;
        }
        byte[] magicNumber = abstractByteData.readBytes(ProtocolConstant.MAGIC_NUMBER.length);
        // 判断魔数是否相同，否则协议不统一则直接抛出异常
        if (!ByteUtil.compareByteArray(magicNumber, ProtocolConstant.MAGIC_NUMBER)){
            try {
                throw new ProtocolException("unknown protocol");
            } catch (ProtocolException e) {
                cacheMap.remove(socketChannel);
                logger.error(e.getMessage());
                return;
            }
        }
        // 版本号
        byte version = abstractByteData.readByte();
        // 请求序列号
        int serialNumber = abstractByteData.getInt();
        // 正文长度
        int contentLen = abstractByteData.getInt();
        CacheBody cacheBody = new CacheBody(contentLen, version, serialNumber);
        cacheMap.put(socketChannel, cacheBody);
        if (abstractByteData.remaining() > 0){
            cacheBody.setData(abstractByteData);
            // 如果一次性读取则直接doChain
            if (abstractByteData.remaining() == contentLen){
                cacheMap.remove(socketChannel);
                nextChain.doChain(socketChannel, cacheBody);
            }
        }
    }

    /**
     * 当数据未读取完成时，需要从缓存中读取出来并追加数据
     * @param socketChannel 正在发送的channel
     * @param abstractByteData 追加的数据
     */
    private void appendData(SocketChannel socketChannel, AbstractByteData abstractByteData){
        CacheBody cacheBody = cacheMap.get(socketChannel);
        if (cacheBody == null){
            try {
                throw new ProtocolException("error");
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }
        }
        AbstractByteData bodyData = cacheBody.getData();
        bodyData.writeBytes(abstractByteData.readBytes());
        if (bodyData.remaining() == cacheBody.getLen()){
            cacheMap.remove(socketChannel);
            nextChain.doChain(socketChannel, cacheBody);
        }
    }

    /**
     * 从channel读取数据
     */
    private AbstractByteData readDataFromChanel(SocketChannel socketChannel){
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
