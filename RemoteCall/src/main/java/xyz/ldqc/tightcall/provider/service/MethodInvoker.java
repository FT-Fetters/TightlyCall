package xyz.ldqc.tightcall.provider.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Fetters
 */
public class MethodInvoker {

    public Object invoke(Object o, String methodName, Object[] args){
        Method method = getMethod(o, methodName);
        return doInvoke(o, method, args);
    }

    private Object doInvoke(Object o, Method method, Object[] args){
        try {
            return method.invoke(o, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Method getMethod(Object o, String methodName){
        try {
            return o.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
