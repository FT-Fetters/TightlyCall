package xyz.ldqc.tightcall.provider.chain;

import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.common.request.CallRequest;
import xyz.ldqc.tightcall.common.response.CallResponse;
import xyz.ldqc.tightcall.provider.service.ServiceContainer;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public class ChannelCallRequestHandlerInBound implements InboundChain, ChannelHandler {

    private Chain nextChain;

    private final ServiceContainer serviceContainer;

    public ChannelCallRequestHandlerInBound(ServiceContainer serviceContainer){
        this.serviceContainer = serviceContainer;
    }

    @Override
    public void doChain(Channel channel, Object obj) {
        doHandler(channel, obj);
    }

    @Override
    public void setNextChain(Chain chain) {
        this.nextChain = chain;
    }

    @Override
    public void doHandler(Channel channel, Object obj) {
        CallRequest callRequest = (CallRequest) obj;
        String path = callRequest.getPath();
        Object[] args = callRequest.getArgs();
        Object ret = serviceContainer.invoke(path, args);
        CallResponse callResponse = new CallResponse();
        callResponse.setSerialNumber(callRequest.getSerialNumber());
        callResponse.setBody(ret);
        nextChain.doChain(channel, callResponse);
    }
}
