package xyz.ldqc.tightcall.provider.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.exception.MethodNotFoundException;
import xyz.ldqc.tightcall.exception.ProxyException;

/**
 * @author Fetters
 */
public class MethodInvoker {

    private static final Logger log = LoggerFactory.getLogger(MethodInvoker.class);

    public Object invoke(Object o, String methodName, Object[] args) throws Exception {
        Method method = getMethod(o, methodName, args);
        if (method == null){
            throw new MethodNotFoundException(String.format("Method '%s' not found in '%s'", methodName, o.getClass().getName()));
        }
        return doInvoke(o, method, args);
    }

    private Object doInvoke(Object o, Method method, Object[] args){
        try {
            return method.invoke(o, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Throwable targetException = e.getCause();
            log.error("invoke method error", targetException.getMessage(), targetException);
            throw new ProxyException(targetException.getMessage());
        }
    }

    private Method getMethod(Object o, String methodName, Object[] args){
        Method[] methods = o.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)
                            && compareTypes(args, method.getParameterTypes())) {
                return method;
            }
        }
        return null;
    }

    private boolean compareTypes(Object[] args, Class<?>[] types){
        Class<?>[] argsType = getArgsType(args);
        for (int i = 0; i < argsType.length; i++) {
            if (types[i].isPrimitive()){
                types[i] = getWrapperClass(types[i]);
            }
            if (!argsType[i].equals(types[i])) {
                return false;
            }
        }
        return true;
    }

    private Class<?>[] getArgsType(Object[] args){
        Class<?>[] types = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i].getClass();
            if (types[i].isPrimitive()){
                types[i] = getWrapperClass(types[i]);
            }
        }
        return types;
    }

    public Class<?> getWrapperClass(Class<?> clazz) {
        if (!clazz.isPrimitive()) {
            throw new IllegalArgumentException("illegal type");
        }
        if (clazz == boolean.class) {
            return Boolean.class;
        } else if (clazz == byte.class) {
            return Byte.class;
        } else if (clazz == short.class) {
            return Short.class;
        } else if (clazz == int.class) {
            return Integer.class;
        } else if (clazz == long.class) {
            return Long.class;
        } else if (clazz == float.class) {
            return Float.class;
        } else if (clazz == double.class) {
            return Double.class;
        } else if (clazz == char.class) {
            return Character.class;
        }

        throw new IllegalArgumentException("error type");
    }
}
