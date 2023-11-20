package xyz.ldqc.tightcall.client.message;

/**
 * 消息队列接口类
 * @author Fetters
 */
public interface MessageQueue<T> {

    /**
     * 提供消息
     * @param msg 消息对象
     * @return 是否成功
     */
    boolean offer(T msg);

    /**
     * 消费消息
     * @return 获取消息对象
     */
    T consume();

    /**
     * 返回剩余的消息
     * @return 消息的数量
     */
    int remaining();

}
