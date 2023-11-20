package xyz.ldqc.tightcall.client.message.support;

import xyz.ldqc.tightcall.client.message.MessageQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * 阻塞消息队列
 * @author Fetters
 */
public class BlockingMessageQueue<T> implements MessageQueue<T> {

    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    public BlockingMessageQueue (){

    }

    @Override
    public boolean offer(T msg) {
        return queue.offer(msg);
    }

    @Override
    public T consume() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int remaining() {
        return queue.size();
    }
}
