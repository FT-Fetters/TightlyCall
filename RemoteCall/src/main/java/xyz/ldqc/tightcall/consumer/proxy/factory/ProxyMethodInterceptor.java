package xyz.ldqc.tightcall.consumer.proxy.factory;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author Fetters
 */
public interface ProxyMethodInterceptor extends MethodInterceptor {


    Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy,
        Callable<?> callable) throws Throwable;
}
