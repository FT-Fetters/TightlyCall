package xyz.ldqc.tightcall.consumer.annotation;

import xyz.ldqc.tightcall.consumer.proxy.ClientProxy;
import xyz.ldqc.tightcall.consumer.proxy.support.CglibClientProxy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Fetters
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TightlyCallScan {

    String packageName();

    Class<? extends ClientProxy> proxy() default CglibClientProxy.class;

}
