package com.tc.prov;

import org.junit.Test;
import xyz.ldqc.tightcall.provider.ProviderApplication;
import xyz.ldqc.tightcall.provider.annotation.OpenRegClient;
import xyz.ldqc.tightcall.provider.annotation.OpenScan;
import xyz.ldqc.tightcall.provider.annotation.ProviderConfig;
import xyz.ldqc.tightcall.provider.register.ServiceRegisterFactory;
import xyz.ldqc.tightcall.scanner.support.DefaultServiceScanner;

import java.util.concurrent.locks.LockSupport;

@OpenScan(packageName = "com.tc", scanner = DefaultServiceScanner.class, type = ServiceRegisterFactory.Type.DEFAULT)
@OpenRegClient(host = "127.0.0.1", port = 1234, name = "test")
@ProviderConfig
public class ProviderTest {

    @Test
    public void testProviderApplication(){
        ProviderApplication run = ProviderApplication.run(ProviderTest.class);
        LockSupport.park();
    }


}
