package xyz.ldqc.tightcall.aop;

import org.aopalliance.aop.Advice;

/**
 * 持有advice的基本接口，过滤并确定advice的适用范围，使用该框架的用户无法调用
 * 允许不同类型的advice提供支持
 */
public interface Advisor {

    /**
     * 返回该切面的advice，该advice可能是接口，before advice 等
     * @return 如果切面匹配，则应应用的advice
     * @see org.aopalliance.intercept.MethodInterceptor
     * @see BeforeAdvice
     */
    Advice getAdvice();

}
