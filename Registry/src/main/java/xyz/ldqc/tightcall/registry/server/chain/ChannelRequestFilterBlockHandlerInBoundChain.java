package xyz.ldqc.tightcall.registry.server.chain;

import xyz.ldqc.tightcall.chain.support.AbstractChannelFilterBlockHandlerInBoundChain;
import xyz.ldqc.tightcall.registry.server.request.AbstractRequest;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public class ChannelRequestFilterBlockHandlerInBoundChain extends AbstractChannelFilterBlockHandlerInBoundChain {
    public ChannelRequestFilterBlockHandlerInBoundChain() {
        super(AbstractRequest.class);
    }

    @Override
    public void doHandler(Channel channel, Object obj) {

    }
}
