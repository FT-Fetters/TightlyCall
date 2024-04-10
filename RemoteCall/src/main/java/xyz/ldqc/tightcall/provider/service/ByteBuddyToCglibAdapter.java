package xyz.ldqc.tightcall.provider.service;

import net.bytebuddy.implementation.bind.annotation.*;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import xyz.ldqc.tightcall.consumer.proxy.factory.ProxyMethodInterceptor;

/**
 * @author Fetters
 */
public class ByteBuddyToCglibAdapter {

    private final ProxyMethodInterceptor methodInterceptor;

    public ByteBuddyToCglibAdapter(ProxyMethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    @RuntimeType
    public Object intercept(@This Object obj, @Origin Method method, @AllArguments Object[] args,
        @SuperCall Callable<?> zuper) throws Throwable {
        // 因为CGLIB的MethodProxy没有直接等价物在Byte Buddy中，我们使用Callable作为代理调用
        // 这里简化处理，不直接使用MethodProxy

        return methodInterceptor.intercept(obj, method, args, null, zuper);
    }


}
