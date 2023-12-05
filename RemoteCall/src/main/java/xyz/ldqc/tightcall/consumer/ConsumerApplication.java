package xyz.ldqc.tightcall.consumer;

import xyz.ldqc.tightcall.consumer.annotation.TightCallConfig;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallScan;
import xyz.ldqc.tightcall.consumer.discovery.DiscoveryClient;
import xyz.ldqc.tightcall.consumer.proxy.ClientProxy;
import xyz.ldqc.tightcall.exception.ConsumerException;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Fetters
 */
public class ConsumerApplication {

    private final Class<?> bootClazz;

    private DiscoveryClient discoveryClient;

    private ConsumerApplication(Class<?> bootClazz) {
        this.bootClazz = bootClazz;
    }

    public static ConsumerApplication run(Class<?> clazz){
        ConsumerApplication consApp = new ConsumerApplication(clazz);
        consApp.loadBootClass();
        return consApp;
    }

    private void loadBootClass(){
        initDiscoveryClient();
        proxyService();
    }

    private void initDiscoveryClient(){
        TightCallConfig config = this.bootClazz.getAnnotation(TightCallConfig.class);
        if (config == null){
            throw new ConsumerException("unknown tightly call config annotation");
        }
        String host = config.registerHost();
        int port = config.registerPort();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
        this.discoveryClient = DiscoveryClient.builder()
                .target(inetSocketAddress)
                .boot();
    }

    private void proxyService(){
        TightlyCallScan scanConfig = this.bootClazz.getAnnotation(TightlyCallScan.class);
        String packageName = scanConfig.packageName();
        Class<? extends ClientProxy> proxyClass = scanConfig.proxy();
        ClientProxy clientProxy = initClientProxy(proxyClass, packageName);
        List<Object> proxyClient = clientProxy.doProxy();

    }

    private ClientProxy initClientProxy(Class<? extends ClientProxy> proxyClass, String packageName){
        try {
            ClientProxy clientProxy = proxyClass.newInstance();
            clientProxy.setPackageName(packageName);
            clientProxy.setDiscoveryClient(discoveryClient);
            return clientProxy;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ConsumerException("init client proxy error");
        }
    }

}
