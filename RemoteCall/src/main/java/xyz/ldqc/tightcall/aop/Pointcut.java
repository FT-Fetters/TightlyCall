package xyz.ldqc.tightcall.aop;


/**
 * 切入点接口，用于获取ClassFilter以及MethodMatcher两个类
 */
public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();

}
