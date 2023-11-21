package xyz.ldqc.tightcall.client.exce;

import xyz.ldqc.tightcall.chain.Chainable;

/**
 * 客户端执行器接口
 * @author Fetters
 */
public interface ClientExec extends Chainable {


    /**
     * 启动客户端
     */
    void start();

    /**
     * 写入消息
     * @param o 消息对象
     */
    void write(Object o);

    /**
     * 写入消息并同步等待回传结果
     * @param o 消息对象
     * @return 消息结果
     */
    Object writeAndWait(Object o);
}
