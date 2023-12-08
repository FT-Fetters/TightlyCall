package xyz.ldqc.tightcall.consumer.proxy.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import xyz.ldqc.tightcall.common.annotation.OpenMapping;
import xyz.ldqc.tightcall.common.request.CallRequest;
import xyz.ldqc.tightcall.consumer.annotation.TightlyCallClient;
import xyz.ldqc.tightcall.consumer.call.CallClientPool;
import xyz.ldqc.tightcall.consumer.discovery.DiscoveryClient;
import xyz.ldqc.tightcall.exception.ClientException;
import xyz.ldqc.tightcall.exception.ProxyException;
import xyz.ldqc.tightcall.exception.RegisterClientException;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;
import xyz.ldqc.tightcall.util.Path;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Fetters
 */
public class ClientMethodInterceptor implements MethodInterceptor {


    private static final CallClientPool CALL_CLIENT_POOL = new CallClientPool();

    private final DiscoveryClient discoveryClient;

    public ClientMethodInterceptor(DiscoveryClient discoveryClient){
        this.discoveryClient = discoveryClient;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        OpenMapping methodMapping = checkCallable(method);
        if (methodMapping == null){
            return null;
        }
        Class<?>[] interfaces = o.getClass().getInterfaces();
        TightlyCallClient tightlyCallClient = getTightlyCallClientByInterfaces(interfaces);
        if (tightlyCallClient == null){
            throw new ClientException("cannot find tightly call client");
        }
        String targetServiceName = tightlyCallClient.serviceName();
        Path path = getPath(o, methodMapping);
        ServiceDefinition targetService = getTargetService(targetServiceName, path);
        InetSocketAddress targetAddress = InetSocketAddress.createUnresolved(targetService.getHost(), targetService.getPort());
        CallRequest callRequest = buildCallRequest(targetService, args);

        return CALL_CLIENT_POOL.doCall(targetAddress, callRequest);

    }

    private OpenMapping checkCallable(Method method){
        OpenMapping annotation = method.getAnnotation(OpenMapping.class);
        if (annotation == null){
            Annotation[] annotations = method.getAnnotations();
            for (Annotation an : annotations) {
                if (OpenMapping.class.isAssignableFrom(an.getClass())) {
                    annotation = (OpenMapping) an;
                    break;
                }
            }
        }
        return annotation;
    }

    private CallRequest buildCallRequest(ServiceDefinition serviceDefinition, Object[] args){
        String path = serviceDefinition.getPath();
        String[] paramTypes = serviceDefinition.getParamTypes();
        CallRequest callRequest = new CallRequest();
        callRequest.setArgs(args);
        callRequest.setPath(path);
        callRequest.setArgTypes(paramTypes);
        return callRequest;
    }

    private ServiceDefinition getTargetService(String targetServiceName, Path path) {
        List<ServiceDefinition> serviceDefinitions = discoveryClient.discoverService(targetServiceName);
        AtomicReference<ServiceDefinition> atomicTargetService = new AtomicReference<>();
        serviceDefinitions.forEach(s -> {
            if (s.getPath().equals(path.getPath())){
                atomicTargetService.set(s);
            }
        });
        ServiceDefinition targetService = atomicTargetService.get();
        if (targetService == null){
            throw new RegisterClientException("unknown service");
        }
        return targetService;
    }

    private TightlyCallClient getTightlyCallClientByInterfaces(Class<?>[] interfaces){
        TightlyCallClient tightlyCallClient = null;
        for (Class<?> clientInterface : interfaces) {
            TightlyCallClient tmp = clientInterface.getAnnotation(TightlyCallClient.class);
            if (tmp != null){
                tightlyCallClient = tmp;
                break;
            }
        }
        return tightlyCallClient;
    }

    private Path getPath(Object o, OpenMapping methodMapping){
        Path path = new Path();
        Class<?>[] interfaces = o.getClass().getInterfaces();
        OpenMapping objOpenMapping = null;
        for (Class<?> inter : interfaces) {
            OpenMapping annotation = inter.getAnnotation(OpenMapping.class);
            if (annotation != null){
                objOpenMapping = annotation;
                break;
            }
        }
        if (objOpenMapping != null){
            path.append(objOpenMapping.value());
        }
        if (methodMapping == null){
            throw new ProxyException("error path");
        }
        path.append(methodMapping.value());
        return path;
    }


}
