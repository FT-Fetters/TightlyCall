package xyz.ldqc.tightcall.registry.server.response;

import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.util.List;

/**
 * @author Fetters
 */
public class DiscoveryResponse extends AbstractResponse{

    private final List<ServiceDefinition> serviceDefinitions;

    public DiscoveryResponse(List<ServiceDefinition> serviceDefinitions){
        this.type = "RESPONSE";
        this.serviceDefinitions = serviceDefinitions;
    }


    public List<ServiceDefinition> getServiceDefinitions(){
        return this.serviceDefinitions;
    }


}
