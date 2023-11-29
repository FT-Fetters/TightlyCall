package xyz.ldqc.tightcall.provider.chain;

import xyz.ldqc.tightcall.buffer.SimpleByteData;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.OutboundChain;
import xyz.ldqc.tightcall.protocol.CacheBody;
import xyz.ldqc.tightcall.registry.server.request.AbstractRequest;
import xyz.ldqc.tightcall.serializer.support.KryoSerializer;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public class ChannelRequestOutBoundChain implements ChannelHandler, OutboundChain {

    private Chain nextChain;

    @Override
    public void doChain(Channel channel, Object obj) {
        if (obj.getClass().isAssignableFrom(CacheBody.class) ){
            CacheBody cacheBody = (CacheBody) obj;
            if (cacheBody.getData() == null
                    && cacheBody.getTmpObj() != null
                    && AbstractRequest.class.isAssignableFrom(cacheBody.getTmpObj().getClass())
            ){
                doHandler(channel, obj);
            }
        }
        nextChain.doChain(channel, obj);
    }

    @Override
    public void setNextChain(Chain chain) {
        this.nextChain = chain;
    }

    @Override
    public void doHandler(Channel channel, Object obj) {
        CacheBody cacheBody = (CacheBody) obj;
        Object tmpObj = cacheBody.getTmpObj();
        byte[] requestBytes = KryoSerializer.serializer().serialize(tmpObj);
        SimpleByteData byteData = new SimpleByteData(requestBytes);
        cacheBody.setData(byteData);
        cacheBody.setLen(requestBytes.length);
        cacheBody.setTmpObj(null);
    }
}
