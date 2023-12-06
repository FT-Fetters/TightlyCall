package xyz.ldqc.tightcall.registry.server.chain;

import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.protocol.CacheBody;
import xyz.ldqc.tightcall.registry.server.response.AbstractResponse;
import xyz.ldqc.tightcall.serializer.Serializer;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public class ChannelConvertResponseHandlerInBoundChain implements InboundChain, ChannelHandler {

    private Chain nextChain;

    private final Serializer serializer;

    public ChannelConvertResponseHandlerInBoundChain(Serializer serializer){
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
        AbstractResponse response = handlerCacheBody(cacheBody);
        response.setSerialNumber(cacheBody.getSerialNumber());
        nextChain.doChain(channel, response);
    }

    private AbstractResponse handlerCacheBody(CacheBody cacheBody){
        AbstractByteData byteData = cacheBody.getData();
        byte[] bytes = byteData.readBytes();
        return serializer.deserialize(bytes);
    }
}
