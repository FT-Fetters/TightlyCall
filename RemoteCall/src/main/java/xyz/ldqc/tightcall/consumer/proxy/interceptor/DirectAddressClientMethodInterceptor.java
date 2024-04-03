package xyz.ldqc.tightcall.consumer.proxy.interceptor;

import java.lang.annotation.Annotation;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import xyz.ldqc.tightcall.common.annotation.OpenMapping;
import xyz.ldqc.tightcall.common.request.CallRequest;
import xyz.ldqc.tightcall.consumer.call.CallClientPool;
import xyz.ldqc.tightcall.consumer.proxy.interceptor.extra.CallBody;
import xyz.ldqc.tightcall.consumer.proxy.interceptor.extra.CallResult;
import xyz.ldqc.tightcall.exception.CallException;

/**
 * 直接访问地址客户端
 *
 * @author Fetters
 */
public class DirectAddressClientMethodInterceptor implements MethodInterceptor {

    private final String[] address;

    private static final CallClientPool CALL_CLIENT_POOL = new CallClientPool();


    public DirectAddressClientMethodInterceptor(String[] address) {
        this.address = address;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        OpenMapping methodMapping = checkCallable(method);
        if (methodMapping == null) {
            return null;
        }
        Class<?> returnType = method.getReturnType();
        if (!CallResult.class.isAssignableFrom(returnType)) {
            throw new WrongMethodTypeException("returnType must be CallResult");
        }

        CallRequest callRequest = null;
        CallBody callBody = null;
        if (objects.length > 1) {
            callRequest = getCallRequest(objects, methodMapping);
        } else if (objects.length == 1 && CallBody.class.isAssignableFrom(objects[0].getClass())) {
            callBody = (CallBody) objects[0];
        }
        Map<String, Object> result = new HashMap<>(address.length);
        for (String addr : address) {
            String[] addrSplit = addr.split(":");
            InetSocketAddress targetAddress = InetSocketAddress.createUnresolved(addrSplit[0],
                Integer.parseInt(addrSplit[1]));
            Object res = null;
            if (callRequest == null && callBody != null) {
                res = CALL_CLIENT_POOL.doCall(targetAddress,
                    getCallRequest(callBody.getAll().get(addr), methodMapping));
            } else {
                res = CALL_CLIENT_POOL.doCall(targetAddress, callRequest);
            }
            if (Exception.class.isAssignableFrom(res.getClass())){
                throw new CallException(((Exception) res).getMessage());
            }
            result.put(addr, res);
        }
        return new CallResult<>(result);
    }

    private CallRequest getCallRequest(Object[] objects, OpenMapping methodMapping) {
        String path = methodMapping.value();
        List<String> argTypeList = Arrays.stream(objects)
            .map(obj -> obj.getClass().toGenericString())
            .collect(Collectors.toList());
        String[] argTypes = new String[argTypeList.size()];
        argTypeList.toArray(argTypes);
        CallRequest callRequest = new CallRequest();
        callRequest.setArgs(objects);
        callRequest.setPath(path);
        callRequest.setArgTypes(argTypes);
        return callRequest;
    }

    private OpenMapping checkCallable(Method method) {
        OpenMapping annotation = method.getAnnotation(OpenMapping.class);
        if (annotation == null) {
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
}
