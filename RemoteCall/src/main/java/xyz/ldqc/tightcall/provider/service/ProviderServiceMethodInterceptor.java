package xyz.ldqc.tightcall.provider.service;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import net.sf.cglib.proxy.MethodProxy;
import xyz.ldqc.tightcall.common.annotation.Inject;
import xyz.ldqc.tightcall.consumer.proxy.factory.ProxyMethodInterceptor;
import xyz.ldqc.tightcall.provider.service.process.AfterServiceProcess;
import xyz.ldqc.tightcall.provider.service.process.BeforeServiceProcess;

/**
 * @author Fetters
 */
public class ProviderServiceMethodInterceptor implements ProxyMethodInterceptor {

    private final Map<Class<?>, Object> injectMap;

    private final Set<BeforeServiceProcess> beforeServiceProcesses;

    private final Set<AfterServiceProcess> afterServiceProcesses;

    public ProviderServiceMethodInterceptor(Map<Class<?>, Object> injectMap,
        Set<BeforeServiceProcess> beforeServiceProcesses,
        Set<AfterServiceProcess> afterServiceProcesses) {
        this.injectMap = injectMap;
        this.beforeServiceProcesses = beforeServiceProcesses;
        this.afterServiceProcesses = afterServiceProcesses;

    }

    private Object afterProcess(Method method, Object[] objects, Object ret) {
        if (!afterServiceProcesses.isEmpty()) {
            for (AfterServiceProcess afterServiceProcess : afterServiceProcesses) {
                ret = afterServiceProcess.process(method, objects, ret);
            }
        }
        return ret;
    }

    private void beforeProcess(Method method, Object[] args) {
        if (!beforeServiceProcesses.isEmpty()) {
            for (BeforeServiceProcess beforeServiceProcess : beforeServiceProcesses) {
                beforeServiceProcess.process(method, args);
            }
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy,
        Callable<?> callable) throws Throwable {

        beforeProcess(method, objects);

        Inject inject = method.getAnnotation(Inject.class);
        if (inject != null) {
            Class<?> returnType = method.getReturnType();
            if (injectMap.containsKey(returnType)) {
                return injectMap.get(returnType);
            } else {
                return callable.call();
            }
        }

        Object ret = callable.call();
        ret = afterProcess(method, objects, ret);
        return ret;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        throw new UnsupportedOperationException("not support");
    }
}
