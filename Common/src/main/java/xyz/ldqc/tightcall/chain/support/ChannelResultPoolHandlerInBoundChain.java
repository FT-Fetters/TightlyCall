package xyz.ldqc.tightcall.chain.support;

import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.OutboundChain;
import xyz.ldqc.tightcall.pool.ResultPool;
import xyz.ldqc.tightcall.protocol.CacheBody;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public class ChannelResultPoolHandlerInBoundChain implements OutboundChain {

    private final ResultPool<Integer, Object> resultPool;

    private Chain nextChain;

    public ChannelResultPoolHandlerInBoundChain(ResultPool<Integer, Object> resultPool){
        this.resultPool = resultPool;
    }

    @Override
    public void doChain(Channel channel, Object obj) {
        if (obj instanceof CacheBody){
            CacheBody cacheBody = (CacheBody) obj;
            int serialNumber = cacheBody.getSerialNumber();
            AbstractByteData data = cacheBody.getData();
            resultPool.put(serialNumber, data);
        }
        if (nextChain != null){
            nextChain.doChain(channel, obj);
        }
    }

    @Override
    public void setNextChain(Chain chain) {
        this.nextChain = chain;
    }
}
