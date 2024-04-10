package xyz.ldqc.tightcall.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.consumer.annotation.TightCallConfig;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallScan;
import xyz.ldqc.tightcall.consumer.discovery.DiscoveryClient;
import xyz.ldqc.tightcall.consumer.proxy.ClientProxy;
import xyz.ldqc.tightcall.exception.ConsumerException;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * @author Fetters
 */
public class ConsumerApplication {

    private static final Logger log = LoggerFactory.getLogger(ConsumerApplication.class);

    private final Class<?> bootClazz;

    private DiscoveryClient discoveryClient;

    private Map<Class<?>, Object> callClientProxy;

    private ConsumerApplication(Class<?> bootClazz) {
        this.bootClazz = bootClazz;
    }

    public static ConsumerApplication run(Class<?> clazz){
        ConsumerApplication consApp = new ConsumerApplication(clazz);
        consApp.loadBootClass();
        return consApp;
    }

    @SuppressWarnings("unchecked")
    public <T> T getCallClient(Class<T> clientClass){
        if (callClientProxy == null){
            return null;
        }
        Object client = callClientProxy.get(clientClass);
        if (client == null){
            return null;
        }
        return ((T) client);
    }

    private void loadBootClass(){
        initDiscoveryClient();
        proxyService();
    }

    private void initDiscoveryClient(){
        TightCallConfig config = this.bootClazz.getAnnotation(TightCallConfig.class);
        if (config == null){
            log.info("Discovery client is disable");
            return;
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
        if (scanConfig == null){
            throw new ConsumerException("unknown tightly call scan path");
        }
        String packageName = scanConfig.packageName();
        Class<? extends ClientProxy> proxyClass = scanConfig.proxy();
        ClientProxy clientProxy = initClientProxy(proxyClass, packageName);
        callClientProxy = clientProxy.doProxy(bootClazz);
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
