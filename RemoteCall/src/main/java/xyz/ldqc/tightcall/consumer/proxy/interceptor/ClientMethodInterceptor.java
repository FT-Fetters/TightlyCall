package xyz.ldqc.tightcall.consumer.proxy.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallClient;
import xyz.ldqc.tightcall.exception.ClientException;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Fetters
 */
public class ClientMethodInterceptor implements MethodInterceptor {



    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Class<?>[] interfaces = o.getClass().getInterfaces();
        TightlyCallClient tightlyCallClient = getTightlyCallClientByInterfaces(interfaces);
        if (tightlyCallClient == null){
            throw new ClientException("cannot find tightly call client");
        }
        String targetServiceName = tightlyCallClient.serviceName();

        return null;

    }

    private TightlyCallClient getTightlyCallClientByInterfaces(Class<?>[] interfaces){
        TightlyCallClient tightlyCallClient = null;
        for (Class<?> clientInterface : interfaces) {
            TightlyCallClient tmp = clientInterface.getAnnotation(TightlyCallClient.class);
            if (tmp != null){
                tightlyCallClient = tmp;
                break;
            }
        }
        return tightlyCallClient;
    }


}
