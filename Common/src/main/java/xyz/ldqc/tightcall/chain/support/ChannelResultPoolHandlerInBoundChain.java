package xyz.ldqc.tightcall.chain.support;

import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.chain.OutboundChain;
import xyz.ldqc.tightcall.pool.ResultPool;
import xyz.ldqc.tightcall.protocol.CacheBody;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public class ChannelResultPoolHandlerInBoundChain implements InboundChain {

    private final ResultPool<Integer, Object> resultPool;

    public ChannelResultPoolHandlerInBoundChain(ResultPool<Integer, Object> resultPool){
        this.resultPool = resultPool;
    }

    @Override
    public void doChain(Channel channel, Object obj) {
        // 如果是CacheBody则直接解析序列号和数据并放入结果池中
        if (obj instanceof CacheBody){
            CacheBody cacheBody = (CacheBody) obj;
            int serialNumber = cacheBody.getSerialNumber();
            AbstractByteData data = cacheBody.getData();
            resultPool.put(serialNumber, data);
        }
    }

    @Override
    public void setNextChain(Chain chain) {}
}
