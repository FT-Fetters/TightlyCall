package xyz.ldqc.tightcall.consumer.proxy;

import xyz.ldqc.tightcall.consumer.aware.DiscoveryClientAware;
import xyz.ldqc.tightcall.consumer.discovery.DiscoveryClient;

import java.util.List;
import java.util.Map;

/**
 * @author Fetters
 */
public interface ClientProxy extends DiscoveryClientAware {

    void setPackageName(String packageName);

    Map<Class<?>, Object> doProxy(Class<?> runClass);


}
