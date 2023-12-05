package com.tc.prov.service;

import xyz.ldqc.tightcall.provider.annotation.OpenMapping;
import xyz.ldqc.tightcall.provider.annotation.OpenService;

@OpenService
@OpenMapping("/service/a")
public class ServiceA {

    @OpenMapping("/test")
    public String test(boolean b){
        return "223";
    }


}
