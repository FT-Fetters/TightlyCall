package xyz.ldqc.tightcall.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import xyz.ldqc.tightcall.aop.MethodBeforeAdvice;

/**
 * 用于包装MethodBeforeAdvice，框架内部使用，开发人员不应该直接使用此类
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    private MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor() {

    }

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice){
        this.advice = advice;
    }

    public MethodBeforeAdvice getAdvice() {
        return advice;
    }

    public void setAdvice(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        this.advice.before(methodInvocation.getMethod(),methodInvocation.getArguments(),methodInvocation.getThis());
        return methodInvocation.proceed();
    }
}
