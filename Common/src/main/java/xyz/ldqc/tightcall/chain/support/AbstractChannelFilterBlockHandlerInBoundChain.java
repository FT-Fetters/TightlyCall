package xyz.ldqc.tightcall.chain.support;

import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.nio.channels.Channel;

/**
 * 过滤器，遇到指定的类才会继续执行否则直接返回
 * @author Fetters
 */
public abstract class AbstractChannelFilterBlockHandlerInBoundChain implements InboundChain {

    private final Class<?> clazz;

    protected Chain nextChain;

    public AbstractChannelFilterBlockHandlerInBoundChain(Class<?> clazz){
        this.clazz = clazz;
    }


    @Override
    public void doChain(Channel channel, Object obj) {
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
