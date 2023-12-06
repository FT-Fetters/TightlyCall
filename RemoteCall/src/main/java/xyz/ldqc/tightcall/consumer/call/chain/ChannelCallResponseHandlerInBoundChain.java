package xyz.ldqc.tightcall.consumer.call.chain;

import xyz.ldqc.tightcall.chain.support.AbstractChannelFilterSkipHandlerInBoundChain;
import xyz.ldqc.tightcall.common.response.CallResponse;

import java.nio.channels.Channel;

/**
 * @author LENOVO
 */
public class ChannelCallResponseHandlerInBoundChain extends AbstractChannelFilterSkipHandlerInBoundChain {
    public ChannelCallResponseHandlerInBoundChain() {
        super(CallResponse.class);
    }

    @Override
    public void doHandler(Channel channel, Object obj) {
        CallResponse callResponse = (CallResponse) obj;
        nextChain.doChain(channel, callResponse);
    }
}
