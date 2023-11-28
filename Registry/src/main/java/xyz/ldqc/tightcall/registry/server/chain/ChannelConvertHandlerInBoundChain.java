package xyz.ldqc.tightcall.registry.server.chain;

import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.protocol.CacheBody;
import xyz.ldqc.tightcall.serializer.Serializer;
import xyz.ldqc.tightcall.registry.server.request.AbstractRequest;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public class ChannelConvertHandlerInBoundChain implements InboundChain, ChannelHandler {

    private Chain nextChain;

    private final Serializer serializer;

    public ChannelConvertHandlerInBoundChain(Serializer serializer){
        this.serializer = serializer;
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
        if (!(obj instanceof CacheBody)){
            return;
        }
        CacheBody cacheBody = (CacheBody) obj;
        AbstractRequest request = handlerCacheBody(cacheBody);
        nextChain.doChain(channel, request);
    }

    private AbstractRequest handlerCacheBody(CacheBody cacheBody){
        AbstractByteData byteData = cacheBody.getData();
        byte[] bytes = byteData.readBytes();
        return serializer.deserialize(bytes);
    }
}
