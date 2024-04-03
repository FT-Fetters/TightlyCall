package xyz.ldqc.tightcall.provider.annotation;

import xyz.ldqc.tightcall.provider.register.ServiceRegisterFactory;
import xyz.ldqc.tightcall.provider.register.ServiceRegisterFactory.Type;
import xyz.ldqc.tightcall.scanner.ServiceScanner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import xyz.ldqc.tightcall.scanner.support.DefaultServiceScanner;

/**
 * @author Fetters
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenScan {
    String packageName();

    Class<? extends ServiceScanner> scanner() default DefaultServiceScanner.class;

    ServiceRegisterFactory.Type type() default Type.DEFAULT;
}
