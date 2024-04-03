package xyz.ldqc.tightcall.consumer.proxy.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallClient;
import xyz.ldqc.tightcall.consumer.discovery.DiscoveryClient;
import xyz.ldqc.tightcall.consumer.proxy.ClientProxy;
import xyz.ldqc.tightcall.consumer.proxy.factory.ProxyFactory;
import xyz.ldqc.tightcall.consumer.proxy.factory.ProxySupport;
import xyz.ldqc.tightcall.consumer.proxy.interceptor.ClientMethodInterceptor;
import xyz.ldqc.tightcall.consumer.proxy.interceptor.DirectAddressClientMethodInterceptor;
import xyz.ldqc.tightcall.util.PackageUtil;

/**
 * @author Fetters
 */
public class CglibClientProxy implements ClientProxy {

    private String packageName;

    private DiscoveryClient discoveryClient;

    public CglibClientProxy(){
        // No need to init anything
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
    public Map<Class<?>, Object> doProxy(Class<?> runClass) {
        List<Class<?>> classes = scanAllClasses(runClass);
        Map<Class<?>, Object> proxyMap = new HashMap<>(classes.size());
        ProxyFactory proxyFactory = new ProxyFactory();
        for (Class<?> clazz : classes) {
            ProxySupport proxySupport = getProxySupport(clazz);
            Object clientInstance = proxyFactory.getProxy(proxySupport);
            proxyMap.put(clazz, clientInstance);
        }
        return proxyMap;
    }

    private ProxySupport getProxySupport(Class<?> clazz) {
        ProxySupport proxySupport = new ProxySupport();
        TightlyCallClient clientAnnotation = clazz.getAnnotation(TightlyCallClient.class);
        String[] address = clientAnnotation.address();
        if (address != null && address.length > 0){
            proxySupport.setInterceptor(new DirectAddressClientMethodInterceptor(address));
        }else {
            proxySupport.setInterceptor(new ClientMethodInterceptor(discoveryClient));
        }
        proxySupport.setTargetClass(clazz);
        return proxySupport;
    }


    public List<Class<?>> scanAllClasses(Class<?> runClass){
        List<Class<?>> packageClasses = PackageUtil.getPackageClasses(packageName, runClass);
        packageClasses = packageClasses.stream()
                .filter(f -> f.getAnnotation(TightlyCallClient.class) != null)
                .collect(Collectors.toList());
        return packageClasses;
    }
}
