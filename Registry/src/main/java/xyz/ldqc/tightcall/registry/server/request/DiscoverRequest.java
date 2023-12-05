package xyz.ldqc.tightcall.registry.server.request;

/**
 * @author Fetters
 */
public class DiscoverRequest extends AbstractRequest{

    private String serviceName;

    public DiscoverRequest(String serviceName){
        this.type = "DISCOVER";
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
