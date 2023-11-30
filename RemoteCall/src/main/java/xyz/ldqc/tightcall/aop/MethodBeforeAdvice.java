package xyz.ldqc.tightcall.aop;

import java.lang.reflect.Method;

public interface MethodBeforeAdvice extends BeforeAdvice{

    /**
     * 调用给定的方法之前的回调
     * @param method 正在被调用的方法
     * @param args 参数
     * @param target 方法调用的目标，即调用该方法的类，可能为<code>null</code>
     */
    void before(Method method, Object[] args, Object target) throws Throwable;

}
