package xyz.ldqc.tightcall.consumer.proxy.support;

import xyz.ldqc.tightcall.consumer.annotation.TightlyCallClient;
import xyz.ldqc.tightcall.consumer.discovery.DiscoveryClient;
import xyz.ldqc.tightcall.consumer.proxy.ClientProxy;
import xyz.ldqc.tightcall.consumer.proxy.factory.ProxyFactory;
import xyz.ldqc.tightcall.consumer.proxy.factory.ProxySupport;
import xyz.ldqc.tightcall.consumer.proxy.interceptor.ClientMethodInterceptor;
import xyz.ldqc.tightcall.exception.ProxyException;
import xyz.ldqc.tightcall.util.PackageUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Fetters
 */
public class CglibClientProxy implements ClientProxy {

    private String packageName;

    private DiscoveryClient discoveryClient;

    public CglibClientProxy(){
    }

    @Override
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public Map<Class<?>, Object> doProxy() {
        List<Class<?>> classes = scanAllClasses();
        Map<Class<?>, Object> proxyMap = new HashMap<>();
        ProxyFactory proxyFactory = new ProxyFactory();
        for (Class<?> clazz : classes) {
            ProxySupport proxySupport = new ProxySupport();
            proxySupport.setInterceptor(new ClientMethodInterceptor(discoveryClient));
            proxySupport.setTargetClass(clazz);
            Object clientInstance = proxyFactory.getProxy(proxySupport);
            proxyMap.put(clazz, clientInstance);
        }
        return proxyMap;
    }



    public List<Class<?>> scanAllClasses(){
        try {
            List<Class<?>> packageClasses = PackageUtil.getPackageClasses(packageName);
            packageClasses = packageClasses.stream()
                    .filter(f -> f.getAnnotation(TightlyCallClient.class) != null)
                    .collect(Collectors.toList());
            return packageClasses;
        } catch (IOException e) {
            throw new ProxyException("scan classes fail");
        }
    }
}
