package xyz.ldqc.tightcall.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fetters
 */
public class ClassUtil {

    public static String[] getMethodParamTypes(Method method){
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<String> typeList = Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.toList());
        String[] types = new String[typeList.size()];
        for (int i = 0; i < types.length; i++) {
            types[i] = typeList.get(i);
        }
        return types;
    }


    /**
     * Check whether the specified class is a CGLIB-generated class.
     * @param clazz the class to check
     */
    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    /**
     * Check whether the specified class name is a CGLIB-generated class.
     * @param className the class name to check
     */
    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains("$$"));
    }

    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }
}
