package xyz.ldqc.tightcall.registry.server.support;

import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.support.DefaultChannelChainGroup;
import xyz.ldqc.tightcall.registry.index.IndexRoom;
import xyz.ldqc.tightcall.registry.index.support.HashMapIndexRoom;
import xyz.ldqc.tightcall.registry.server.RegisterServer;
import xyz.ldqc.tightcall.registry.server.chain.ChannelConvertHandlerInBoundChain;
import xyz.ldqc.tightcall.registry.server.chain.ChannelRegReqHandlerInBoundChain;
import xyz.ldqc.tightcall.registry.server.chain.ChannelRequestFilterBlockHandlerInBoundChain;
import xyz.ldqc.tightcall.serializer.support.KryoSerializer;
import xyz.ldqc.tightcall.server.ServerApplication;
import xyz.ldqc.tightcall.server.exec.support.NioServerExec;

/**
 * @author Fetters
 */
public class DefaultRegisterServer implements RegisterServer {

    private final int port;

    private ServerApplication serverApplication;

    private final IndexRoom indexRoom = new HashMapIndexRoom();

    public DefaultRegisterServer(int port){
        this.port = port;
    }

    @Override
    public RegisterServer run() {
        this.serverApplication = ServerApplication.builder()
                .bind(this.port)
                .chain(buildChainGroup())
                .execNum(Runtime.getRuntime().availableProcessors())
                .executor(NioServerExec.class)
                .boot();
        return this;
    }

    private ChainGroup buildChainGroup(){
        DefaultChannelChainGroup chainGroup = new DefaultChannelChainGroup();
        chainGroup.addLast(new ChannelConvertHandlerInBoundChain(KryoSerializer.serializer()))
                .addLast(new ChannelRequestFilterBlockHandlerInBoundChain())
                .addLast(new ChannelRegReqHandlerInBoundChain(indexRoom));
        return chainGroup;
    }
}
