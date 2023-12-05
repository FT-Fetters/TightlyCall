package proxy.service;

import xyz.ldqc.tightcall.consumer.annotation.TightlyCallClient;

@TightlyCallClient(serviceName = "test")
public interface ServiceA {

    String test(boolean b);
}
