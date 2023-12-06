package xyz.ldqc.tightcall.registry.server.chain;

import xyz.ldqc.tightcall.chain.support.AbstractChannelFilterBlockHandlerInBoundChain;
import xyz.ldqc.tightcall.registry.server.response.AbstractResponse;

/**
 * @author Fetters
 */
public class ChannelResponseFilterBlockHandlerInBoundChain extends AbstractChannelFilterBlockHandlerInBoundChain {
    public ChannelResponseFilterBlockHandlerInBoundChain() {
        super(AbstractResponse.class);
    }
}
