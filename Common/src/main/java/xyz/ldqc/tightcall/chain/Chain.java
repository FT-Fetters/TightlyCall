package xyz.ldqc.tightcall.chain;

import xyz.ldqc.tightcall.server.handler.Handler;

import java.nio.channels.Channel;

/**
 * 调用链点
 * @author Fetters
 */
public interface Chain {

    /**
     * 传递调用链
     * @param channel channel
     * @param obj obj
     */
    void doChain(Channel channel, Object obj);

    /**
     * 设置下一个链点
     * @param chain 下一个链点
     */
    void setNextChain(Chain chain);

}
