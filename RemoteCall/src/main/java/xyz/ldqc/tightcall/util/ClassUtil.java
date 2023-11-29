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
}
