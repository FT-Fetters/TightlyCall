package xyz.ldqc.tightcall.consumer.aware;

import xyz.ldqc.tightcall.consumer.discovery.DiscoveryClient;

/**
 * @author Fetters
 */
public interface DiscoveryClientAware extends Aware{

    void setDiscoveryClient(DiscoveryClient discoveryClient);

}
