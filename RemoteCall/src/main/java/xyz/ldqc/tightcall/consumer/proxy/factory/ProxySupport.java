package xyz.ldqc.tightcall.consumer.proxy.factory;

import net.sf.cglib.proxy.MethodInterceptor;

/**
 * 代理信息封装
 * @author Fetters
 */
public class ProxySupport {

    private Class<?> targetClass;

    private ProxyMethodInterceptor interceptor;

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public ProxyMethodInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(ProxyMethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }
}
