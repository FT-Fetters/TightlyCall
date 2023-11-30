package xyz.ldqc.tightcall.aop;

/**
 * 承担了Pointcut和Advice的组合
 * Pointcut用于获取JoinPoint
 * 而Advice决定于JointPoint执行什么操作
 */
public interface PointcutAdvisor extends Advisor{

    /**
     * 获取Pointcut
     */
    Pointcut getPointcut();
}
