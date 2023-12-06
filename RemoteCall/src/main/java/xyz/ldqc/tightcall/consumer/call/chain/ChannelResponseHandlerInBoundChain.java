package xyz.ldqc.tightcall.consumer.call.chain;

import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import javax.xml.validation.SchemaFactoryConfigurationError;
import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public class ChannelResponseHandlerInBoundChain implements ChannelHandler, InboundChain {

    private Chain nextChain;

    @Override
    public void doHandler(Channel channel, Object obj) {

    }

    @Override
    public void doChain(Channel channel, Object obj) {

    }

    @Override
    public void setNextChain(Chain chain) {
        this.nextChain = chain;
    }
}
