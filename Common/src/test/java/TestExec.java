import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.buffer.SimpleByteData;
import xyz.ldqc.tightcall.chain.Chain;
import xyz.ldqc.tightcall.chain.InboundChain;
import xyz.ldqc.tightcall.chain.support.ChannelPostHandlerOutBoundChain;
import xyz.ldqc.tightcall.chain.support.ChannelPreHandlerInBoundChain;
import xyz.ldqc.tightcall.chain.support.ChannelResultPoolHandlerInBoundChain;
import xyz.ldqc.tightcall.chain.support.DefaultChannelChainGroup;
import xyz.ldqc.tightcall.client.exce.support.NioClientExec;
import xyz.ldqc.tightcall.protocol.CacheBody;
import xyz.ldqc.tightcall.protocol.ProtocolConstant;
import xyz.ldqc.tightcall.protocol.ProtocolDataFactory;
import xyz.ldqc.tightcall.server.ServerApplication;
import xyz.ldqc.tightcall.server.exec.support.NioServerExec;
import xyz.ldqc.tightcall.server.handler.ChannelHandler;

import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.util.concurrent.locks.LockSupport;

public class TestExec {

    private static final Logger log = LoggerFactory.getLogger(TestExec.class);

    @Test
    public void testNioServerExec(){
        NioServerExec nioServerExec = new NioServerExec(6770, 1);
        DefaultChannelChainGroup chainGroup = new DefaultChannelChainGroup();
        chainGroup.addLast(new ChannelPreHandlerInBoundChain());
        chainGroup.addLast(new TestChain());
        chainGroup.addLast(new ChannelPostHandlerOutBoundChain());
        nioServerExec.setChainGroup(chainGroup);
        nioServerExec.start();
        LockSupport.park();
    }

    @Test
    public void testNioClientExec() {
        NioClientExec nioClientExec = new NioClientExec(new InetSocketAddress("localhost", 6770));
        DefaultChannelChainGroup chainGroup = new DefaultChannelChainGroup();
        chainGroup.addLast(new ChannelPreHandlerInBoundChain());
        nioClientExec.setChainGroup(chainGroup);
        nioClientExec.start();
        log.debug("send result: {}", nioClientExec.writeAndWait("test write"));
    }

    private static class TestChain implements InboundChain, ChannelHandler{

        private Chain nextChain;

        public TestChain(){

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
            byte[] bytes = "Hello TightlyCall".getBytes();
            cacheBody.getData().writeBytes(bytes);
            cacheBody.setLen(cacheBody.getLen() + bytes.length);
            nextChain.doChain(channel, cacheBody);
        }
    }

    @Test
    public void testServerApplication(){
        ServerApplication serverApplication = ServerApplication.builder()
                .bind(6770)
                .execNum(8)
                .executor(NioServerExec.class)
                .chain(new DefaultChannelChainGroup().addLast(new TestChain()))
                .boot();
        LockSupport.park();
    }
}
