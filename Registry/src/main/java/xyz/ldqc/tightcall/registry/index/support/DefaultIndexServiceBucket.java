package xyz.ldqc.tightcall.registry.index.support;

import xyz.ldqc.tightcall.registry.index.IndexServiceBucket;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fetters
 */
public class DefaultIndexServiceBucket implements IndexServiceBucket {

    private final Map<String, ServiceDefinition> serviceDefinitionMap = new HashMap<>();

    @Override
    public void register(ServiceDefinition serviceDefinition) {
        serviceDefinitionMap.put(serviceDefinition.getPath(), serviceDefinition);
    }

    @Override
    public List<ServiceDefinition> listAll() {
        return new ArrayList<>(serviceDefinitionMap.values());
    }

}
