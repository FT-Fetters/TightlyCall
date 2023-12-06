package xyz.ldqc.tightcall.registry.index;

import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.util.List;

/**
 * @author Fetters
 */
public interface IndexServiceBucket {

    void register(ServiceDefinition serviceDefinition);

    List<ServiceDefinition> listAll();
}
