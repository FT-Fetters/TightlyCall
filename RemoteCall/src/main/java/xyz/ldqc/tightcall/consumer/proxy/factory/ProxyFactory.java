package xyz.ldqc.tightcall.consumer.proxy.factory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @author Fetters
 */
public class ProxyFactory {

    public Object getProxy(ProxySupport proxySupport){
        MethodInterceptor interceptor = proxySupport.getInterceptor();
        Class<?> targetClass = proxySupport.getTargetClass();
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{targetClass});
        enhancer.setCallback(interceptor);
        return enhancer.create();
    }
}
