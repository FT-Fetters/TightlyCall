package xyz.ldqc.tightcall.client.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.pool.ResultPool;
import xyz.ldqc.tightcall.protocol.CacheBody;
import xyz.ldqc.tightcall.protocol.ProtocolDataFactory;
import xyz.ldqc.tightcall.util.ByteUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 消息发送线程
 *
 * @author Fetters
 */
public class MessageWriteThread extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(MessageWriteThread.class);

    /**
     * 消息队列，用于存储消息，等待该线程读取消息
     */
    private final MessageQueue<CacheBody> queue;

    /**
     * 结果池，用于发送消息后返回的结果存储位置，使得调用线程能同步获取结果
     */
    private final ResultPool<Integer, Object> resultPool;

    private final SocketChannel target;

    private final ByteBuffer buffer = ByteBuffer.allocate(1024);

    private boolean terminate = false;

    public MessageWriteThread(MessageQueue<CacheBody> messageQueue, ResultPool<Integer, Object> resultPool, SocketChannel target) {
        this.queue = messageQueue;
        this.target = target;
        this.resultPool = resultPool;
        this.start();
    }

    public void terminate() {
        this.terminate = true;
    }

    public CacheBody write(Object o) {
        // 判断是否为为CacheBody对象，如果是则直接放入消息队列
        if (o instanceof CacheBody) {
            this.queue.offer(((CacheBody) o));
            return ((CacheBody) o);
        }
        // 如果不是CacheBody则转化后再放入队列
        byte[] objByteArray = ByteUtil.obj2ByteArray(o);
        CacheBody cacheBody = new CacheBody(objByteArray);
        this.queue.offer(cacheBody);
        return cacheBody;
    }

    public Object writeAndWait(Object o) {
        CacheBody cacheBody = write(o);
        // 同步获取结果
        return resultPool.getResult(cacheBody.getSerialNumber());
    }

    @Override
    public void run() {
        initThread();
        while (!terminate) {
            // 阻塞消费消息队列中的消息
            CacheBody body = queue.consume();
            if (body != null) {
                // 将ByteData转化为字节数组
                byte[] bytes = byteData2ByteArray(body);
                doWrite(bytes);
            }
        }
    }

    private void initThread() {
        Thread.currentThread().setName("msg-write");
    }

    /**
     * 将CacheBody中的数据转化为符合协议的字节数组
     * @param body CacheBody
     * @return 符合协议的字节数组
     */
    private byte[] byteData2ByteArray(CacheBody body) {
        AbstractByteData data = body.getData();
        int serialNumber = body.getSerialNumber();
        return ProtocolDataFactory.create(data.readBytes(), serialNumber);
    }

    private void doWrite(byte[] data) {
        // 数据总长度
        int totalLen = data.length;
        // 剩余发送的长度
        int remaining = totalLen;
        while (remaining > 0) {
            int offset = totalLen - remaining;
            // 本次写入的字节数
            int len = Math.min(buffer.remaining(), remaining);
            // 切换到写模式
            buffer.clear();
            buffer.put(data, offset, len);
            // 切换到读模式
            buffer.flip();
            remaining -= len;
            try {
                target.write(buffer);
            } catch (IOException e) {
                logger.error("send message error: {}", e.getMessage());
                return;
            }
        }
    }
}
