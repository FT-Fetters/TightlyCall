package xyz.ldqc.tightcall.registry.index.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.registry.index.IndexRoom;
import xyz.ldqc.tightcall.registry.index.IndexServiceBucket;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Fetters
 */
public abstract class AbstractMapIndexRoom implements IndexRoom {

    private static final Logger log = LoggerFactory.getLogger(AbstractMapIndexRoom.class);

    protected Map<String, IndexServiceBucket> map;

    @Override
    public List<String> listAllServiceName() {
        return new ArrayList<>(map.keySet());
    }

    @Override
    public void register(String serviceName, ServiceDefinition serviceDefinition) {
        IndexServiceBucket indexServiceBucket = map.get(serviceName);
        if (indexServiceBucket == null){
            indexServiceBucket = new DefaultIndexServiceBucket();
            map.put(serviceName, indexServiceBucket);
        }
        log.info("register service: {}, path: {}", serviceName, serviceDefinition.getPath());
        indexServiceBucket.register(serviceDefinition);
    }
}
