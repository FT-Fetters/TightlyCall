package xyz.ldqc.tightcall.registry;

import xyz.ldqc.tightcall.registry.exception.RegisterServerException;
import xyz.ldqc.tightcall.registry.index.IndexRoom;
import xyz.ldqc.tightcall.registry.server.RegisterServer;
import xyz.ldqc.tightcall.registry.server.support.DefaultRegisterServer;

/**
 * @author Fetters
 */
public class RegistryServerApplication {

    private final RegisterServer registerServer;

    private RegistryServerApplication(RegisterServer registerServer){
        this.registerServer = registerServer;
    }

    public static Builder builder(){
        return new Builder();
    }

    /**
     * 注册中心应用建造者模式
     */
    public static class Builder {

        private static final int MAX_PORT = 65535;

        private IndexRoom indexRoom;

        private int port;

        private Class<? extends RegisterServer> registerServerClass;

        public Builder indexRoom(IndexRoom indexRoom){
            this.indexRoom = indexRoom;
            return this;
        }

        public Builder bind(int port){
            this.port = port;
            return this;
        }

        public Builder registerServer(Class<? extends RegisterServer> registerServerClass){
            this.registerServerClass = registerServerClass;
            return this;
        }

        public RegistryServerApplication boot(){
            if (indexRoom == null){

            }

            if (port <= 0 || port > MAX_PORT){
                throw new RegisterServerException("error port");
            }

            if (registerServerClass == null){
                throw new RegisterServerException("null register server class");
            }
            RegisterServer registerServer0 = null;
            if (registerServerClass.isAssignableFrom(DefaultRegisterServer.class)){
                registerServer0 = defaultServer();
            }
            if (registerServer0 == null){
                throw new RegisterServerException("boot fail");
            }
            registerServer0.run();
            return new RegistryServerApplication(registerServer0);
        }

        private DefaultRegisterServer defaultServer(){
            return new DefaultRegisterServer(this.port);
        }
    }
}
