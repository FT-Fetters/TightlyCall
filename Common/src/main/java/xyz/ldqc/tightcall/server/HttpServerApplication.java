package xyz.ldqc.tightcall.server;

import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.support.http.ChannelHttpPostHandlerOutBoundChain;
import xyz.ldqc.tightcall.chain.support.http.ChannelHttpPreHandlerInBoundChain;
import xyz.ldqc.tightcall.server.exec.ServerExec;
import xyz.ldqc.tightcall.server.exec.support.NioServerExec;

public class HttpServerApplication {

    private final ServerExec exec;

    private HttpServerApplication(ServerExec exec) {
        this.exec = exec;
    }

    public static HttpServerApplicationBuilder builder(){
        return new HttpServerApplicationBuilder();
    }

    public static class HttpServerApplicationBuilder {

        private int port = -1;

        private int execNum = -1;
        private Class<? extends ServerExec> execClazz;

        private ChainGroup chainGroup;

        public HttpServerApplicationBuilder bind(int port) {
            this.port = port;
            return this;
        }

        public HttpServerApplicationBuilder execNum(int execNum) {
            this.execNum = execNum;
            return this;
        }

        public HttpServerApplicationBuilder executor(Class<? extends ServerExec> execClazz) {
            this.execClazz = execClazz;
            return this;
        }

        public HttpServerApplicationBuilder chain(ChainGroup chainGroup) {
            this.chainGroup = chainGroup;
            return this;
        }

        public HttpServerApplication boot() {
            ServerExec exec = null;
            if (execClazz.isAssignableFrom(NioServerExec.class)) {
                if (port == -1) {
                    throw new RuntimeException("port can not be empty");
                }
                if (execNum != -1) {
                    exec = new NioServerExec(port, execNum);
                } else {
                    exec = new NioServerExec(port);
                }
                if (chainGroup != null) {
                    chainGroup.addHead(new ChannelHttpPreHandlerInBoundChain()).addLast(new ChannelHttpPostHandlerOutBoundChain());
                }
            }
            if (exec != null) {
                exec.setChainGroup(chainGroup);
                exec.start();
            }
            return new HttpServerApplication(exec);
        }


    }

}
