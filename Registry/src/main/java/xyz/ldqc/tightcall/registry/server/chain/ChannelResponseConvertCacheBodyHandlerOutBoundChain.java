package xyz.ldqc.tightcall.registry.server.chain;

import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.OutboundChain;
import xyz.ldqc.tightcall.exception.ChainException;
import xyz.ldqc.tightcall.protocol.CacheBody;
import xyz.ldqc.tightcall.registry.server.response.AbstractResponse;
import xyz.ldqc.tightcall.serializer.Serializer;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public class ChannelResponseConvertCacheBodyHandlerOutBoundChain implements OutboundChain, ChannelHandler {

    private final Serializer serializer;

    public ChannelResponseConvertCacheBodyHandlerOutBoundChain(Serializer serializer){
        this.serializer = serializer;
    }

    private Chain nextChain;

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
        if (!AbstractResponse.class.isAssignableFrom(obj.getClass())) {
            throw new ChainException("cannot covert cache body");
        }
        AbstractResponse abstractResponse = (AbstractResponse) obj;
        byte[] bytes = serializer.serialize(obj);
        CacheBody cacheBody = new CacheBody(bytes);
        cacheBody.setSerialNumber(abstractResponse.getSerialNumber());
        nextChain.doChain(channel, cacheBody);
    }
}
