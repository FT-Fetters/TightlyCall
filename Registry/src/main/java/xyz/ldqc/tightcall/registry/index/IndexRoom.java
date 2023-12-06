package xyz.ldqc.tightcall.registry.index;

import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.util.List;

public interface IndexRoom {

    List<String> listAllServiceName();

    void register(String serviceName, ServiceDefinition serviceDefinition);

    IndexServiceBucket getBucket(String serviceName);

}
