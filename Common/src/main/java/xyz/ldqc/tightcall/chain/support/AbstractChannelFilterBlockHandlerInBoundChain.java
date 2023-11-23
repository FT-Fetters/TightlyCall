package xyz.ldqc.tightcall.chain.support;

import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public abstract class AbstractChannelFilterBlockHandlerInBoundChain implements InboundChain, ChannelHandler {

    private final Class<?> clazz;

    private Chain nextChain;

    public AbstractChannelFilterBlockHandlerInBoundChain(Class<?> clazz){
        this.clazz = clazz;
    }


    @Override
    public void doChain(Channel channel, Object obj) {
        if (!clazz.isAssignableFrom(obj.getClass())){
            return;
        }
        doHandler(channel, obj);
    }

    @Override
    public void setNextChain(Chain chain) {
        this.nextChain = chain;
    }

}
