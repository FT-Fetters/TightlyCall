package com.tc;

import org.junit.Test;
import xyz.ldqc.tightcall.provider.scanner.support.DefaultServiceScanner;
import xyz.ldqc.tightcall.provider.service.ServiceDefinition;
import xyz.ldqc.tightcall.util.PackageUtil;

import java.io.IOException;
import java.util.List;

public class ScannerTest {

    @Test
    public void packageUtilTest(){
        try {
            List<Class<?>> packageClasses = PackageUtil.getPackageClasses("com.tc");
            for (Class<?> packageClass : packageClasses) {
                System.out.println(packageClass.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void serviceScannerTest(){
        DefaultServiceScanner defaultServiceScanner = new DefaultServiceScanner("com.tc");
        List<ServiceDefinition> definitions = defaultServiceScanner.doScan();

    }

}
