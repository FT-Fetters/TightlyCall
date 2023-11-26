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
     * 目标host
     */
    private String targetHost;

    /**
     * 目标端口
     */
    private String targetPort;

    /**
     * 目标url
     */
    private String url;

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost = targetHost;
    }

    public String getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(String targetPort) {
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
