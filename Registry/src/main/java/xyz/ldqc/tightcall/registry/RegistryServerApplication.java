package xyz.ldqc.tightcall.registry;

import xyz.ldqc.tightcall.registry.index.IndexRoom;

/**
 * @author Fetters
 */
public class RegistryServerApplication {

    public void run() {

    }

    public static Builder builder(){
        return new Builder();
    }

    /**
     * 注册中心应用建造者模式
     */
    private static class Builder {

        private IndexRoom indexRoom;

        private int port;

        public Builder indexRoom(IndexRoom indexRoom){
            this.indexRoom = indexRoom;
            return this;
        }

        public Builder bind(int port){
            this.port = port;
            return this;
        }
    }
}
