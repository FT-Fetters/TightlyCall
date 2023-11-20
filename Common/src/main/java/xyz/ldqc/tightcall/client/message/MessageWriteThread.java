package xyz.ldqc.tightcall.client.message;

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

    private final MessageQueue<CacheBody> queue;

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
        if (o instanceof CacheBody) {
            this.queue.offer(((CacheBody) o));
            return ((CacheBody) o);
        }
        byte[] objByteArray = ByteUtil.obj2ByteArray(o);
        CacheBody cacheBody = new CacheBody(objByteArray);
        this.queue.offer(cacheBody);
        return cacheBody;
    }

    public Object writeAndWait(Object o) {
        CacheBody cacheBody = write(o);
        return resultPool.getResult(cacheBody.getSerialNumber());
    }

    @Override
    public void run() {
        initThread();
        while (!terminate) {
            CacheBody body = queue.consume();
            if (body != null) {
                byte[] bytes = byteData2ByteArray(body);
                doWrite(bytes);
            }
        }
    }

    private void initThread() {
        Thread.currentThread().setName("msg-write");
    }

    private byte[] byteData2ByteArray(CacheBody body) {
        AbstractByteData data = body.getData();
        int serialNumber = body.getSerialNumber();
        return ProtocolDataFactory.create(data.readBytes(), serialNumber);
    }

    private void doWrite(byte[] data) {
        int totalLen = data.length;
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
                throw new RuntimeException(e);
            }
        }
    }
}
