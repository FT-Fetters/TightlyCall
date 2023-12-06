package com.tc.cons;

import com.tc.cons.service.ServiceA;
import org.junit.Test;
import xyz.ldqc.tightcall.consumer.ConsumerApplication;
import xyz.ldqc.tightcall.consumer.annotation.TightCallConfig;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallScan;

import java.util.concurrent.locks.LockSupport;

@TightCallConfig(registerHost = "127.0.0.1", registerPort = 1234)
@TightlyCallScan(packageName = "com.tc.cons")
public class ConsumerTest {

    @Test
    public void testConsumerApplication(){
        ConsumerApplication consumerApplication = ConsumerApplication.run(ConsumerTest.class);
        ServiceA serviceA = consumerApplication.getCallClient(ServiceA.class);
        System.out.println(serviceA.test(true));
        System.out.println(serviceA.test(false));
        LockSupport.park();
    }
}
