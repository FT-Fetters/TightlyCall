package xyz.ldqc.tightcall.registry.server.support;

import xyz.ldqc.tightcall.chain.support.DefaultChannelChainGroup;
import xyz.ldqc.tightcall.registry.server.RegisterServer;
import xyz.ldqc.tightcall.server.ServerApplication;
import xyz.ldqc.tightcall.server.exec.support.NioServerExec;

/**
 * @author Fetters
 */
public class DefaultRegisterServer implements RegisterServer {

    private int port;

    private ServerApplication serverApplication;

    public DefaultRegisterServer(int port){

    }

    @Override
    public RegisterServer run() {
        this.serverApplication = ServerApplication.builder()
                .bind(this.port)
                .chain(new DefaultChannelChainGroup())
                .execNum(Runtime.getRuntime().availableProcessors())
                .executor(NioServerExec.class)
                .boot();
        return this;
    }
}
