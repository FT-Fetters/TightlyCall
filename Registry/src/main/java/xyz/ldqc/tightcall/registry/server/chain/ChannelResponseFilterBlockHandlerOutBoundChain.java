package xyz.ldqc.tightcall.registry.server.chain;

import xyz.ldqc.tightcall.chain.support.AbstractChannelFilterBlockHandlerOutBoundChain;
import xyz.ldqc.tightcall.registry.server.response.AbstractResponse;

/**
 * @author Fetters
 */
public class ChannelResponseFilterBlockHandlerOutBoundChain extends AbstractChannelFilterBlockHandlerOutBoundChain {
    public ChannelResponseFilterBlockHandlerOutBoundChain() {
        super(AbstractResponse.class);
    }
}
