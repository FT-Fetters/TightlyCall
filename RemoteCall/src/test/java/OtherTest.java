import com.tc.cons.service.Service;
import org.junit.Test;
import xyz.ldqc.tightcall.consumer.proxy.factory.ProxyFactory;
import xyz.ldqc.tightcall.consumer.proxy.factory.ProxySupport;
import xyz.ldqc.tightcall.consumer.proxy.interceptor.ClientMethodInterceptor;
import xyz.ldqc.tightcall.util.Path;

import java.net.InetSocketAddress;

public class OtherTest {


    @Test
    public void pathTest(){
        String path = "";

        Path p = new Path(path);

        path = "/te";
        p.append(path);
        System.out.println("p.getPath() = " + p.getPath());
        p.append("/");
        System.out.println("p.getPath() = " + p.getPath());

    }

    @Test
    public void testInetSocketAddress(){
        InetSocketAddress unresolved = InetSocketAddress.createUnresolved("127.0.0.1", 123);
        InetSocketAddress unresolved1 = InetSocketAddress.createUnresolved("127.0.0.1", 123);
        System.out.println(unresolved1.equals(unresolved));

    }

    @Test
    public void testGetJavaVersion(){
        String javaVersion = System.getProperty("java.version").split("\\.")[0];
        System.out.println("javaVersion = " + javaVersion);
    }

    @Test
    public void testJDKProxy(){
        System.setProperty("java.version", "17");
        ProxySupport proxySupport = new ProxySupport();
        proxySupport.setInterceptor(new ClientMethodInterceptor(null));
        proxySupport.setTargetClass(Service.class);
        ProxyFactory proxyFactory = new ProxyFactory();
        Object proxy = proxyFactory.getProxy(proxySupport);
        Service service = (Service) proxy;
        service.test(true);

    }

}
