package xyz.ldqc.tightcall.provider.service;

import xyz.ldqc.tightcall.exception.ServiceContainerException;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fetters
 */
public class ServiceContainer {

    private final Map<String, Object> targetObj;

    private final Map<String, ServiceDefinition> serviceMap;

    private final MethodInvoker methodInvoker;

    public ServiceContainer(List<ServiceDefinition> serviceDefinitions, MethodInvoker methodInvoker){
        targetObj = new HashMap<>();
        serviceMap = new HashMap<>();
        this.methodInvoker = methodInvoker;
        instanceService(serviceDefinitions);
        path2Service(serviceDefinitions);
    }

    public Object invoke(String path, Object[] args){
        ServiceDefinition serviceDefinition = serviceMap.get(path);
        String clazz = serviceDefinition.getClazz();
        Object o = targetObj.get(clazz);
        String methodName = serviceDefinition.getMethod();
        return methodInvoker.invoke(o, methodName, args);
    }

    private void path2Service(List<ServiceDefinition> serviceDefinitions){
        for (ServiceDefinition serviceDefinition : serviceDefinitions) {
            this.serviceMap.put(serviceDefinition.getPath(), serviceDefinition);
        }
    }

    private void instanceService(List<ServiceDefinition> serviceDefinitions){
        for (ServiceDefinition serviceDefinition : serviceDefinitions) {
            String className = serviceDefinition.getClazz();
            Object obj = doInstance(className);
            targetObj.put(className, obj);
        }
    }

    private Object doInstance(String className){
        Class<?> clazz;
        try {
             clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ServiceContainerException("cannot instance "+ className +" class");
        }
        Constructor<?> noneParamConstructor = null;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length > 0) {
                continue;
            }
            noneParamConstructor = constructor;
            break;
        }
        if (noneParamConstructor == null){
            throw new ServiceContainerException("cannot find none param constructor");
        }
        try {
            return noneParamConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
