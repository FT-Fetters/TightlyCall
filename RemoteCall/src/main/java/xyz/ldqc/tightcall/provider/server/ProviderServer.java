package xyz.ldqc.tightcall.provider.server;

import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.support.DefaultChannelChainGroup;
import xyz.ldqc.tightcall.server.ServerApplication;
import xyz.ldqc.tightcall.server.exec.support.NioServerExec;

import java.net.InetSocketAddress;

/**
 * @author Fetters
 */
public class ProviderServer {

    private final ServerApplication serverApplication;

    private ProviderServer(ServerApplication serverApplication){
        this.serverApplication = serverApplication;
    }

    public static class ProviderServerBuilder{

        private int port;

        public ProviderServerBuilder port(int port){
            this.port = port;
            return this;
        }

        public ProviderServer boot(){
            ServerApplication server = ServerApplication.builder()
                    .executor(NioServerExec.class)
                    .chain(buildChainGroup())
                    .execNum(Runtime.getRuntime().availableProcessors())
                    .bind(this.port)
                    .boot();
            return new ProviderServer(server);
        }

        private ChainGroup buildChainGroup(){
            return new DefaultChannelChainGroup();
        }
    }
}
