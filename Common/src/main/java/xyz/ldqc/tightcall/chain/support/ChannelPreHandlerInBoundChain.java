package xyz.ldqc.tightcall.chain.support;

import xyz.ldqc.tightcall.buffer.ByteData;
import xyz.ldqc.tightcall.buffer.SimpleByteData;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
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
 * @author Fetters
 */
public class ChannelPreHandlerInBoundChain implements InboundChain, ChannelHandler {

    private final ConcurrentHashMap<Channel, CacheBody> cacheMap = new ConcurrentHashMap<>();

    private final Chain nextChain;

    public ChannelPreHandlerInBoundChain(Chain nextChain) {
        this.nextChain = nextChain;
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
        ByteData byteData = readDataFromChanel(socketChannel);
        // 如果回传的数据是Null代表断开连接
        if (byteData == null){
            selectionKey.cancel();
            cacheMap.remove(channel);
            return;
        }
        if (cacheBody == null){
            // 缓存为空代表是第一批的数据
            handleHeadData(socketChannel, byteData);
        }else if (cacheBody.getLen() == -1){
            // 已读取过但请求头未读取完成
            ByteData bodyData = cacheBody.getData();
            bodyData.writeBytes(byteData.readBytes());
            handleHeadData(socketChannel, bodyData);
        }else {
            appendData(socketChannel, byteData);
        }
    }

    private void handleHeadData(SocketChannel socketChannel, ByteData byteData){
        if (byteData.remaining() < ProtocolConstant.PROTOCOL_HEAD_LEN){
            // 读取到的字节长度小于PROTOCOL_HEAD_LEN代表首次的数据
            CacheBody cacheBody = new CacheBody();
            cacheBody.setTmpData(byteData);
            cacheMap.put(socketChannel, cacheBody);
            return;
        }
        byte[] magicNumber = byteData.readBytes(ProtocolConstant.MAGIC_NUMBER.length);
        // 判断魔数是否相同，否则协议不统一则直接抛出异常
        if (!ByteUtil.compareByteArray(magicNumber, ProtocolConstant.MAGIC_NUMBER)){
            try {
                throw new ProtocolException("unknown protocol");
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }
        }
        // 版本号
        byte version = byteData.readByte();
        // 请求序列号
        byte[] serialNumber = byteData.readBytes(3);
        // 正文长度
        int contentLen = byteData.getInt();
        CacheBody cacheBody = new CacheBody(contentLen, version, serialNumber);
        if (byteData.remaining() > 0){
            cacheBody.setData(byteData);
            // 如果一次性读取则直接doChain
            if (byteData.remaining() == contentLen){
                cacheMap.remove(socketChannel);
                nextChain.doChain(socketChannel, cacheBody);
            }
        }
    }

    private void appendData(SocketChannel socketChannel, ByteData byteData){
        CacheBody cacheBody = cacheMap.get(socketChannel);
        if (cacheBody == null){
            try {
                throw new ProtocolException("error");
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            }
        }
        ByteData bodyData = cacheBody.getData();
        bodyData.writeBytes(byteData.readBytes());
        if (bodyData.remaining() == cacheBody.getLen()){
            cacheMap.remove(socketChannel);
            nextChain.doChain(socketChannel, cacheBody);
        }
    }

    /**
     * 从channel读取数据
     */
    private ByteData readDataFromChanel(SocketChannel socketChannel){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            if (socketChannel.read(buffer) == -1) {
                return null;
            }
            return new SimpleByteData(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void doChain(Channel channel, Object obj) {
        doHandler(channel, obj);
    }

    private static class CacheBody {
        private int len = -1;

        private ByteData data;

        private ByteData tmpData;

        private byte version;

        private byte[] serialNumber;

        public CacheBody(){

        }

        public CacheBody(int len, byte version, byte[] serialNumber){
            this.len = len;
            this.version = version;
            this.serialNumber = serialNumber;
        }

        public int getLen() {
            return len;
        }

        public void setLen(int len) {
            this.len = len;
        }

        public ByteData getData() {
            return data;
        }

        public void setData(ByteData data) {
            this.data = data;
        }

        public ByteData getTmpData() {
            return tmpData;
        }

        public void setTmpData(ByteData tmpData) {
            this.tmpData = tmpData;
        }

        public byte getVersion() {
            return version;
        }

        public void setVersion(byte version) {
            this.version = version;
        }

        public byte[] getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(byte[] serialNumber) {
            this.serialNumber = serialNumber;
        }
    }
}
