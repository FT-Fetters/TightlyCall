package xyz.ldqc.tightcall.chain.support;

import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.chain.OutboundChain;

import java.nio.channels.Channel;

/**
 * 过滤器，遇到指定的类才会继续执行否则直接返回
 * @author Fetters
 */
public abstract class AbstractChannelFilterBlockHandlerOutBoundChain implements OutboundChain {

    private final Class<?> clazz;

    protected Chain nextChain;

    public AbstractChannelFilterBlockHandlerOutBoundChain(Class<?> clazz){
        this.clazz = clazz;
    }


    @Override
    public void doChain(Channel channel, Object obj) {
        // 如果传入的对象不是要求的类的子类或者相同类则直接返回不会继续执行调用链
        if (!clazz.isAssignableFrom(obj.getClass())){
            return;
        }
        nextChain.doChain(channel, obj);
    }

    @Override
    public void setNextChain(Chain chain) {
        this.nextChain = chain;
    }

}
