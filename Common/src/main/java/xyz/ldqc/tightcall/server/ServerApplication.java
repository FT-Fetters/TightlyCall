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

    public static Builder builder(){
        return new Builder();
    }



    public static class Builder {

        private int port = -1;

        private int execNum = -1;
        private Class<? extends ServerExec> execClazz;

        private ChainGroup chainGroup;

        public Builder bind(int port) {
            this.port = port;
            return this;
        }

        public Builder execNum(int execNum) {
            this.execNum = execNum;
            return this;
        }

        public Builder executor(Class<? extends ServerExec> execClazz) {
            this.execClazz = execClazz;
            return this;
        }

        public Builder chain(ChainGroup chainGroup) {
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
                chainGroup.addHead(new ChannelPreHandlerInBoundChain()).addLast(new ChannelPostHandlerOutBoundChain());

                exec.setChainGroup(chainGroup);
            }


            if (exec != null){
                exec.start();
            }
            return new ServerApplication(exec);
        }
    }
}
