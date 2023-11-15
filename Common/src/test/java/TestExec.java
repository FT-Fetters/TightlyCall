import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.chain.support.ChannelPostHandlerOutBoundChain;
import xyz.ldqc.tightcall.chain.support.ChannelPreHandlerInBoundChain;
import xyz.ldqc.tightcall.protocol.CacheBody;
import xyz.ldqc.tightcall.server.exec.support.NioServerExec;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.nio.channels.Channel;
import java.util.concurrent.locks.LockSupport;

public class TestExec {

    private static final Logger log = LoggerFactory.getLogger(TestExec.class);

    @Test
    public void testNioServerExec(){
        NioServerExec nioServerExec = new NioServerExec(6770, 1);
        ChannelPostHandlerOutBoundChain channelPostHandlerOutBoundChain = new ChannelPostHandlerOutBoundChain();
        TestChain testChain = new TestChain(channelPostHandlerOutBoundChain);
        ChannelPreHandlerInBoundChain channelPreHandlerInBoundChain = new ChannelPreHandlerInBoundChain(testChain);
        nioServerExec.setChainHead(channelPreHandlerInBoundChain);
        nioServerExec.start();
        LockSupport.park();
    }

    private static class TestChain implements InboundChain, ChannelHandler{

        private final Chain nextChain;

        public TestChain(Chain nextChain){
            this.nextChain = nextChain;
        }

        @Override
        public void doChain(Channel channel, Object obj) {
            doHandler(channel, obj);
        }

        @Override
        public void doHandler(Channel channel, Object obj) {
            if (!(obj instanceof CacheBody)){
                return;
            }
            CacheBody cacheBody = (CacheBody) obj;
            byte[] bytes = "Hello TightlyCall".getBytes();
            cacheBody.getData().writeBytes(bytes);
            cacheBody.setLen(cacheBody.getLen() + bytes.length);
            nextChain.doChain(channel, cacheBody);
        }
    }
}
