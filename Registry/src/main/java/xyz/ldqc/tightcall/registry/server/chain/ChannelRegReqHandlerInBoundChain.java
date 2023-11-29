package xyz.ldqc.tightcall.registry.server.chain;

import xyz.ldqc.tightcall.chain.support.AbstractChannelFilterSkipHandlerInBoundChain;
import xyz.ldqc.tightcall.registry.server.request.RegisterRequest;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public class ChannelRegReqHandlerInBoundChain extends AbstractChannelFilterSkipHandlerInBoundChain {
    public ChannelRegReqHandlerInBoundChain() {
        super(RegisterRequest.class);
    }

    @Override
    public void doHandler(Channel channel, Object obj) {
        RegisterRequest regReq = (RegisterRequest) obj;
        ServiceDefinition serviceDefinition = regReq.getServiceDefinition();
        System.out.println("serviceDefinition.getClazz().getName() = " + serviceDefinition.getClazz());
    }
}
