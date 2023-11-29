package xyz.ldqc.tightcall.registry.server.request;

import java.lang.reflect.Method;

/**
 * @author Fetters
 */
public class ServiceDefinition {

    private String path;

    private String clazz;

    private String method;

    private String[] paramTypes;

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

}
