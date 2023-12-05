package xyz.ldqc.tightcall.consumer.call.chain;

import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.Chainable;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public class ChannelResponseRequestHandlerInBoundChain implements ChannelHandler, InboundChain {

    @Override
    public void doHandler(Channel channel, Object obj) {

    }

    @Override
    public void doChain(Channel channel, Object obj) {

    }

    @Override
    public void setNextChain(Chain chain) {

    }
}
