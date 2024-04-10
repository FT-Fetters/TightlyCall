package xyz.ldqc.tightcall.provider.service;

import java.util.HashSet;
import java.util.Set;
import xyz.ldqc.tightcall.consumer.proxy.factory.ProxyFactory;
import xyz.ldqc.tightcall.consumer.proxy.factory.ProxySupport;
import xyz.ldqc.tightcall.exception.ServiceContainerException;
import xyz.ldqc.tightcall.provider.service.process.AfterServiceProcess;
import xyz.ldqc.tightcall.provider.service.process.BeforeServiceProcess;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    private final Map<Class<?>, Object> injectMap;

    private final Set<BeforeServiceProcess> beforeServiceProcesses;

    private final Set<AfterServiceProcess> afterServiceProcesses;

    private final ProxyFactory proxyFactory;

    public ServiceContainer(List<ServiceDefinition> serviceDefinitions,
        MethodInvoker methodInvoker) {
        targetObj = new HashMap<>();
        serviceMap = new HashMap<>();
        this.methodInvoker = methodInvoker;
        this.injectMap = new HashMap<>();
        this.proxyFactory = new ProxyFactory();
        this.beforeServiceProcesses = new HashSet<>();
        this.afterServiceProcesses = new HashSet<>();
        instanceService(serviceDefinitions);
        path2Service(serviceDefinitions);
    }

    public Object invoke(String path, Object[] args) throws Exception {
        ServiceDefinition serviceDefinition = serviceMap.get(path);
        String clazz = serviceDefinition.getClazz();
        Object o = targetObj.get(clazz);
        String methodName = serviceDefinition.getMethod();
        return methodInvoker.invoke(o, methodName, args);
    }

    private void path2Service(List<ServiceDefinition> serviceDefinitions) {
        for (ServiceDefinition serviceDefinition : serviceDefinitions) {
            this.serviceMap.put(serviceDefinition.getPath(), serviceDefinition);
        }
    }

    private void instanceService(List<ServiceDefinition> serviceDefinitions) {
        for (ServiceDefinition serviceDefinition : serviceDefinitions) {
            String className = serviceDefinition.getClazz();
//            Object obj = doInstance(className);
            Object obj = doProxyInstance(className);
            targetObj.put(className, obj);
        }
    }

    private Object doInstance(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ServiceContainerException("cannot instance " + className + " class");
        }
        Constructor<?> noneParamConstructor = null;
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length > 0) {
                continue;
            }
            noneParamConstructor = constructor;
            break;
        }
        if (noneParamConstructor == null) {
            throw new ServiceContainerException("cannot find none param constructor");
        }
        try {
            return noneParamConstructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object doProxyInstance(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ServiceContainerException("cannot instance " + className + " class");
        }
        ProxySupport proxySupport = new ProxySupport();
        proxySupport.setTargetClass(clazz);
        proxySupport.setInterceptor(
            new ProviderServiceMethodInterceptor(injectMap, beforeServiceProcesses ,afterServiceProcesses));
        return proxyFactory.getProxy(proxySupport);
    }

    public void addInject(Class<?> clazz, Object o) {
        injectMap.put(clazz, o);
    }

    public void addBeforeServiceProcess(BeforeServiceProcess beforeServiceProcess) {
        beforeServiceProcesses.add(beforeServiceProcess);
    }

    public void addAfterServiceProcess(AfterServiceProcess afterServiceProcess) {
        afterServiceProcesses.add(afterServiceProcess);
    }
}

