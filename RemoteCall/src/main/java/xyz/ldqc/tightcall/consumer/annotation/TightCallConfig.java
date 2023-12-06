package xyz.ldqc.tightcall.consumer.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author LENOVO
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TightCallConfig {

    String registerHost();

    int registerPort();
}
