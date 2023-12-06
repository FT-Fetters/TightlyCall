package com.tc.cons.service;

import xyz.ldqc.tightcall.common.annotation.OpenMapping;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallClient;

@TightlyCallClient(serviceName = "test")
public interface ServiceA {

    @OpenMapping("/service/a/test")
    String test(boolean b);

}
