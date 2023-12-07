import org.junit.Test;
import xyz.ldqc.tightcall.registry.RegistryServerApplication;
import xyz.ldqc.tightcall.registry.server.RegisterServer;
import xyz.ldqc.tightcall.registry.server.support.DefaultRegisterServer;

import java.util.concurrent.locks.LockSupport;

public class RegistryTest {

    @Test
    public void test(){
        Class<? extends RegisterServer> c = DefaultRegisterServer.class;
    }

    @Test
    public void testRegisterServerApplication(){
        RegistryServerApplication registryServerApplication = RegistryServerApplication.builder()
                .bind(1234)
                .registerServer(DefaultRegisterServer.class)
                .indexRoom(null)
                .boot();
        LockSupport.park();
    }
}
