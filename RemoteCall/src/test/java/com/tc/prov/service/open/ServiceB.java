package com.tc.prov.service.open;

import xyz.ldqc.tightcall.provider.annotation.OpenMapping;
import xyz.ldqc.tightcall.provider.annotation.OpenService;

@OpenService
@OpenMapping("/service/b")
public class ServiceB {

    @OpenMapping("/test")
    public String test(String ab, int i){
        return "123";
    }
}
