package xyz.ldqc.tightcall.aop.framework;

import com.ldqc.lans.aop.AdvisedSupport;

/**
 * 代理工厂主要解决是关于JDK和Cglib两种代理的选择问题
 */
public class ProxyFactory {

    private AdvisedSupport advisedSupport;


    public ProxyFactory(AdvisedSupport advisedSupport){
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy(){
        return createAopProxy().getProxy();
    }

    private AopProxy createAopProxy(){
        if (advisedSupport.isProxyTargetClass()){
            return new Cglib2AopProxy(advisedSupport);
        }

        return new JdkDynamicAopProxy(advisedSupport);
    }

}
