package xyz.ldqc.tightcall.aop;

import java.lang.reflect.Method;

/**
 * 用于检测目标方法是否符合条件，包含于{@link Pointcut}
 */
public interface MethodMatcher {

    boolean matches(Method method, Class<?> targetClas);

}
