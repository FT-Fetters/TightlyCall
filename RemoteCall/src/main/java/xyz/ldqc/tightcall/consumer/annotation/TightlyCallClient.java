package xyz.ldqc.tightcall.consumer.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Fetters
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TightlyCallClient {

    String serviceName();

}
