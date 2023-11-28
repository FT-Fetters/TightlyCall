package xyz.ldqc.tightcall.registry.server.request;

import java.lang.reflect.Method;

/**
 * @author Fetters
 */
public class ServiceDefinition {

    private String path;

    private Class<?> clazz;

    private Method method;

    private Class<?>[] paramTypes;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }
}
