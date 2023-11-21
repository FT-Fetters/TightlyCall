package xyz.ldqc.tightcall.client;

import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.support.ChannelPreHandlerInBoundChain;
import xyz.ldqc.tightcall.client.exce.ClientExec;
import xyz.ldqc.tightcall.client.exce.support.NioClientExec;

import java.net.InetSocketAddress;

/**
 * @author Fetters
 */
public class ClientApplication {

    private ClientExec exec;

    private ClientApplication(ClientExec exec) {
        this.exec = exec;
    }

    public static ClientApplicationBuilder builder() {
        return new ClientApplicationBuilder();
    }

    public Object writeAndWait(Object o){
        return exec.writeAndWait(o);
    }

    public void write(Object o){
        exec.write(o);
    }

    public static class ClientApplicationBuilder {

        private InetSocketAddress socketAddress;

        private ChainGroup chainGroup;

        private Class<? extends ClientExec> execClazz;

        public ClientApplicationBuilder address(InetSocketAddress socketAddress) {
            this.socketAddress = socketAddress;
            return this;
        }

        public ClientApplicationBuilder chain(ChainGroup chainGroup) {
            this.chainGroup = chainGroup;
            return this;
        }

        public ClientApplicationBuilder executor(Class<? extends ClientExec> execClazz) {
            this.execClazz = execClazz;
            return this;
        }

        public ClientApplication boot() {
            ClientExec exec = null;
            if (execClazz.isAssignableFrom(NioClientExec.class)) {
                exec = new NioClientExec(socketAddress);
                if (chainGroup != null) {
                    chainGroup.addHead(new ChannelPreHandlerInBoundChain());
                }
            }

            if (exec != null) {
                exec.setChainGroup(chainGroup);
                exec.start();
            }
            return new ClientApplication(exec);
        }
    }
}
