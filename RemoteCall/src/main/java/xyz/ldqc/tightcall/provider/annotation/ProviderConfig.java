package xyz.ldqc.tightcall.provider.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Fetters
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProviderConfig {

    int port() default 6770;
}
