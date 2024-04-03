package com.tc;

import org.junit.Test;
import xyz.ldqc.tightcall.scanner.support.DefaultServiceScanner;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;
import xyz.ldqc.tightcall.util.PackageUtil;

import java.io.IOException;
import java.util.List;

public class ScannerTest {

    @Test
    public void packageUtilTest(){
        List<Class<?>> packageClasses = PackageUtil.getPackageClasses("com.tc", ScannerTest.class);
        for (Class<?> packageClass : packageClasses) {
            System.out.println(packageClass.getName());
        }
    }

    @Test
    public void serviceScannerTest(){
        DefaultServiceScanner defaultServiceScanner = new DefaultServiceScanner("com.tc", ScannerTest.class);
        List<ServiceDefinition> definitions = defaultServiceScanner.doScan();

    }

}
