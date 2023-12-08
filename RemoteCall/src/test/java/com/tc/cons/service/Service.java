package com.tc.cons.service;

import xyz.ldqc.tightcall.common.annotation.OpenMapping;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallClient;

@TightlyCallClient(serviceName = "test")
public interface Service {

    @OpenMapping("/service/a/test")
    String test(boolean b);

    @OpenMapping("/service/b/test")
    String test(String ab, int i);

}
