package xyz.ldqc.tightcall.registry.server.request;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * @author Fetters
 */
public class ServiceDefinition {

    private String path;

    private String clazz;

    private String method;

    private String[] paramTypes;

    private String host;

    private int port;

    public String getPath() {
        return path;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(String[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
