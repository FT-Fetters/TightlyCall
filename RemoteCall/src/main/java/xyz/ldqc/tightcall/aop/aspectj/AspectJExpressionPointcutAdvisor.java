package xyz.ldqc.tightcall.aop.aspectj;

import com.ldqc.lans.aop.Pointcut;
import com.ldqc.lans.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;

/**
 * 使用AspectJ pointcut 表达式的advisor
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    // 切面
    private AspectJExpressionPointcut pointcut;
    // 具体的拦截方法
    private Advice advice;
    // 表达式
    private String expression;

    public void setExpression(String expression){
        this.expression = expression;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public Pointcut getPointcut() {
        if (pointcut == null){
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }
}
