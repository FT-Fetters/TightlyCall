package xyz.ldqc.tightcall.server;

import xyz.ldqc.tightcall.server.exec.ServerExec;

public class ServerApplication {


    private static class Builder {
        private int port;

        private ServerExec exec;

        public Builder bind(int port){
            this.port = port;
            return this;
        }

        public Builder executor(ServerExec exec){
            this.exec = exec;
            return this;
        }




    }
}
