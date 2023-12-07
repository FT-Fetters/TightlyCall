package com.tc.prov.service;

import xyz.ldqc.tightcall.provider.annotation.OpenMapping;
import xyz.ldqc.tightcall.provider.annotation.OpenService;

@OpenService
@OpenMapping("/service/a")
public class ServiceA {

    @OpenMapping("/test")
    public String test(boolean b){
        StringBuilder sb = new StringBuilder();

        if (b){
            for (int i = 0; i < 1000; i++) {
                sb.append("true");
            }
            return sb.toString();
        }else {
            for (int i = 0; i < 1000; i++) {
                sb.append("false");
            }
            return sb.toString();
        }
    }


}
