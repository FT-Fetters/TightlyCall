package xyz.ldqc.tightcall.chain.support;

import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.nio.channels.Channel;

/**
 * 过滤器，遇到指定的类才会继续执行否则直接跳到下一级
 * @author Fetters
 */
public abstract class AbstractChannelFilterSkipHandlerInBoundChain implements InboundChain, ChannelHandler {

    private final Class<?> clazz;

    protected Chain nextChain;

    public AbstractChannelFilterSkipHandlerInBoundChain(Class<?> clazz){
        this.clazz = clazz;
    }


    @Override
    public void doChain(Channel channel, Object obj) {
        // 如果传入的对象不是指定的类的子类或者相同类，则直接跳入下一个链点，否则则执行非抽象类的doHanlder方法
        if (!clazz.isAssignableFrom(obj.getClass())){
            if (nextChain != null){
                nextChain.doChain(channel, obj);
            }
            return;
        }
        doHandler(channel, obj);
    }

    @Override
    public void setNextChain(Chain chain) {
        this.nextChain = chain;
    }

}
