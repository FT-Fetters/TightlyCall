package xyz.ldqc.tightcall.provider.server;

import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.support.DefaultChannelChainGroup;
import xyz.ldqc.tightcall.provider.chain.ChannelCallRequestHandlerInBound;
import xyz.ldqc.tightcall.provider.service.ServiceContainer;
import xyz.ldqc.tightcall.registry.server.chain.ChannelConvertRequestHandlerInBoundChain;
import xyz.ldqc.tightcall.registry.server.chain.ChannelRequestFilterBlockHandlerInBoundChain;
import xyz.ldqc.tightcall.registry.server.chain.ChannelResponseConvertCacheBodyHandlerOutBoundChain;
import xyz.ldqc.tightcall.registry.server.chain.ChannelResponseFilterBlockHandlerOutBoundChain;
import xyz.ldqc.tightcall.serializer.support.KryoSerializer;
import xyz.ldqc.tightcall.server.ServerApplication;
import xyz.ldqc.tightcall.server.exec.support.NioServerExec;

/**
 * @author Fetters
 */
public class ProviderServer {

    private final ServerApplication serverApplication;


    private ProviderServer(ServerApplication serverApplication){
        this.serverApplication = serverApplication;
    }

    public static ProviderServerBuilder builder(){
        return new ProviderServerBuilder();
    }

    public static class ProviderServerBuilder{

        private int port;

        private ServiceContainer serviceContainer;

        public ProviderServerBuilder port(int port){
            this.port = port;
            return this;
        }

        public ProviderServerBuilder serviceContainer(ServiceContainer serviceContainer){
            this.serviceContainer = serviceContainer;
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
            return new DefaultChannelChainGroup()
                    .addLast(new ChannelConvertRequestHandlerInBoundChain(KryoSerializer.serializer()))
                    .addLast(new ChannelRequestFilterBlockHandlerInBoundChain())
                    .addLast(new ChannelCallRequestHandlerInBound(serviceContainer))
                    .addLast(new ChannelResponseFilterBlockHandlerOutBoundChain())
                    .addLast(new ChannelResponseConvertCacheBodyHandlerOutBoundChain(KryoSerializer.serializer()));
        }
    }
}
