package xyz.ldqc.tightcall.consumer.proxy.factory;

import java.lang.reflect.InvocationTargetException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Proxy;
import xyz.ldqc.tightcall.exception.ProxyException;
import xyz.ldqc.tightcall.provider.service.ByteBuddyToCglibAdapter;

/**
 * @author Fetters
 */
public class ProxyFactory {

    private static final int CGLIB_NOT_SUPPORT_JAVA_VERSION = 11;

    public Object getProxy(ProxySupport proxySupport) {
        // 获取当前运行java版本
        int javaVersion = Integer.parseInt(
            System.getProperty("java.version").split("\\.")[0]);
        // 如果版本大于11则使用JDK代理，否则使用CGLIB代理
        if (javaVersion > CGLIB_NOT_SUPPORT_JAVA_VERSION || !proxySupport.getTargetClass()
            .isInterface()) {
            if (proxySupport.getTargetClass().isInterface()) {
                return doGetJdkProxy(proxySupport);
            }
            return doByteBuddyProxy(proxySupport);
        } else {
            return doGetCglibProxy(proxySupport);
        }
    }

    private Object doGetCglibProxy(ProxySupport proxySupport) {
        ProxyMethodInterceptor interceptor = proxySupport.getInterceptor();
        Class<?> targetClass = proxySupport.getTargetClass();
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{targetClass});
        enhancer.setCallback(interceptor);
        return enhancer.create();
    }


    private Object doGetJdkProxy(ProxySupport proxySupport) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Class<?> targetClass = proxySupport.getTargetClass();

        MethodInterceptor interceptor = proxySupport.getInterceptor();
        return Proxy.newProxyInstance(classLoader,
            new Class[]{targetClass},
            (proxy, method, args) -> interceptor.intercept(proxy, method, args, null)
        );
    }

    private Object doByteBuddyProxy(ProxySupport proxySupport) {
        Class<?> targetClass = new ByteBuddy()
            .subclass(proxySupport.getTargetClass())
            .method(ElementMatchers.any())
            .intercept(
                MethodDelegation.to(new ByteBuddyToCglibAdapter(proxySupport.getInterceptor()))
            )
            .make()
            .load(ProxyFactory.class.getClassLoader())
            .getLoaded();
        try {
            return targetClass.getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new ProxyException(
                "Proxy " + proxySupport.getTargetClass() + " fail: " + e.getMessage());
        }
    }


}
