package xyz.ldqc.tightcall.consumer.proxy.factory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Fetters
 */
public class ProxyFactory {

    public Object getProxy(ProxySupport proxySupport){
        // 获取当前运行java版本
        int javaVersion = Integer.parseInt(
                System.getProperty("java.version").split("\\.")[0]);
        // 如果版本大于11则使用JDK代理，否则使用CGLIB代理
        if (javaVersion > 11){
            return doGetJdkProxy(proxySupport);
        }else {
            return doGetCglibProxy(proxySupport);

        }
    }

    private Object doGetCglibProxy(ProxySupport proxySupport){
        MethodInterceptor interceptor = proxySupport.getInterceptor();
        Class<?> targetClass = proxySupport.getTargetClass();
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{targetClass});
        enhancer.setCallback(interceptor);
        return enhancer.create();
    }


    private Object doGetJdkProxy(ProxySupport proxySupport){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Class<?> targetClass = proxySupport.getTargetClass();

        MethodInterceptor interceptor = proxySupport.getInterceptor();
        return Proxy.newProxyInstance(classLoader,
                new Class[]{targetClass},
                (proxy, method, args) -> interceptor.intercept(proxy, method, args, null)
        );
    }
}
