package xyz.ldqc.tightcall.registry.server.request;

/**
 * @author Fetters
 */
public class RegisterRequest extends AbstractRequest {

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 目标端口
     */
    private int targetPort;

    /**
     * 目标url
     */
    private ServiceDefinition serviceDefinition;

    public int getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    public RegisterRequest() {
        this.type = "REG";
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ServiceDefinition getServiceDefinition() {
        return serviceDefinition;
    }

    public void setServiceDefinition(ServiceDefinition serviceDefinition) {
        this.serviceDefinition = serviceDefinition;
    }
}
