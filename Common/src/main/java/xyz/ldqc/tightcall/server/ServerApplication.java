package xyz.ldqc.tightcall.server;

import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.support.ChannelPostHandlerOutBoundChain;
import xyz.ldqc.tightcall.chain.support.ChannelPreHandlerInBoundChain;
import xyz.ldqc.tightcall.server.exec.ServerExec;
import xyz.ldqc.tightcall.server.exec.support.NioServerExec;

/**
 * @author Fetters
 */
public class ServerApplication {

    private final ServerExec exec;

    private ServerApplication(ServerExec exec){
        this.exec = exec;
    }

    public static ServerApplicationBuilder builder(){
        return new ServerApplicationBuilder();
    }



    public static class ServerApplicationBuilder {

        private int port = -1;

        private int execNum = -1;
        private Class<? extends ServerExec> execClazz;

        private ChainGroup chainGroup;

        public ServerApplicationBuilder bind(int port) {
            this.port = port;
            return this;
        }

        public ServerApplicationBuilder execNum(int execNum) {
            this.execNum = execNum;
            return this;
        }

        public ServerApplicationBuilder executor(Class<? extends ServerExec> execClazz) {
            this.execClazz = execClazz;
            return this;
        }

        public ServerApplicationBuilder chain(ChainGroup chainGroup) {
            this.chainGroup = chainGroup;
            return this;
        }

        public ServerApplication boot() {
            ServerExec exec = null;
            if (execClazz.isAssignableFrom(NioServerExec.class)) {
                if (port == -1){
                    throw new RuntimeException("port can not be empty");
                }
                if (execNum != -1){
                    exec = new NioServerExec(port, execNum);
                }else {
                    exec = new NioServerExec(port);
                }
                if (chainGroup != null){
                    chainGroup.addHead(new ChannelPreHandlerInBoundChain()).addLast(new ChannelPostHandlerOutBoundChain());
                }
            }
            if (exec != null){
                exec.setChainGroup(chainGroup);
                exec.start();
            }
            return new ServerApplication(exec);
        }
    }
}
