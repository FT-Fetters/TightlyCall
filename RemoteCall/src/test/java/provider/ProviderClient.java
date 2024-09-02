package provider;


import inject.InjectTest;
import java.util.concurrent.locks.LockSupport;
import org.junit.Test;
import process.AfterProcessTest;
import process.PreProcessTest;
import xyz.ldqc.tightcall.provider.ProviderApplication;
import xyz.ldqc.tightcall.provider.annotation.OpenScan;
import xyz.ldqc.tightcall.provider.annotation.ProviderConfig;
import xyz.ldqc.tightcall.provider.register.ServiceRegisterFactory;

@OpenScan(packageName = "tc", type = ServiceRegisterFactory.Type.DEFAULT)
//@OpenRegClient(host = "127.0.0.1", port = 1234, name = "test")
@ProviderConfig(port = 6770)
public class ProviderClient {


    @Test
    public void getProviderApplication(){
        // 提供服务
        ProviderApplication run = ProviderApplication.run(ProviderClient.class);
        InjectTest injectTest = new InjectTest("inj !!");
        run.getServiceContainer().addInject(InjectTest.class, injectTest);
        run.getServiceContainer().addBeforeServiceProcess(new PreProcessTest());
        run.getServiceContainer().addAfterServiceProcess(new AfterProcessTest());
        LockSupport.park();
    }

}
