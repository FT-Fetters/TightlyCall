package com.tc.cons;

import com.tc.cons.service.Service;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.consumer.ConsumerApplication;
import xyz.ldqc.tightcall.consumer.annotation.TightCallConfig;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallScan;

import java.util.concurrent.locks.LockSupport;

@TightCallConfig(registerHost = "127.0.0.1", registerPort = 1234)
@TightlyCallScan(packageName = "com.tc.cons")
public class ConsumerTest {

    private final static Logger log = LoggerFactory.getLogger(ConsumerTest.class);

    @Test
    public void testConsumerApplication(){
        ConsumerApplication consumerApplication = ConsumerApplication.run(ConsumerTest.class);
        Service service = consumerApplication.getCallClient(Service.class);
        long startTime = System.currentTimeMillis();
        System.out.println(service.test(true));
        long firstConnectTime = System.currentTimeMillis();
        log.debug("第一次连接并执行耗时: {}", firstConnectTime - startTime);
        System.out.println(service.test(false));
        long connected = System.currentTimeMillis();
        log.debug("连接后执行第一次: {}", connected - firstConnectTime);
        System.out.println(service.test("123", 123));
        log.debug("连接后执行第二次: {}", System.currentTimeMillis() - connected);
        LockSupport.park();
    }
}
