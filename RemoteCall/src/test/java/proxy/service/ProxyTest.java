package proxy.service;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallClient;

import java.lang.reflect.Method;

public class ProxyTest {

    @Test
    public void cglibTest(){

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{ServiceA.class});
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("1234");
                Class<?>[] interfaces = o.getClass().getInterfaces();
                TightlyCallClient tightlyCallClient = null;
                for (Class<?> itf : interfaces) {
                    TightlyCallClient client = itf.getAnnotation(TightlyCallClient.class);
                    if (client != null){
                        tightlyCallClient = client;
                    }
                }
                if (tightlyCallClient == null){
                    throw new RuntimeException();
                }
                return tightlyCallClient.serviceName();
            }
        });
        ServiceA serviceA = (ServiceA) enhancer.create();
        String test = serviceA.test(true);
        System.out.println(test);
    }
}
