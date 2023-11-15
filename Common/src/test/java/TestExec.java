import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.chain.support.ChannelPreHandlerInBoundChain;
import xyz.ldqc.tightcall.server.exec.support.NioServerExec;

import java.util.concurrent.locks.LockSupport;

public class TestExec {

    private static final Logger log = LoggerFactory.getLogger(TestExec.class);

    @Test
    public void testNioServerExec(){
        NioServerExec nioServerExec = new NioServerExec(6770, 1);
        ChannelPreHandlerInBoundChain channelPreHandlerInBoundChain = new ChannelPreHandlerInBoundChain(null);
        nioServerExec.setChainHead(channelPreHandlerInBoundChain);
        nioServerExec.start();
        LockSupport.park();
    }
}
