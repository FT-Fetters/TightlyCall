package xyz.ldqc.tightcall.provider.annotation;

import xyz.ldqc.tightcall.provider.register.ServiceRegisterFactory;
import xyz.ldqc.tightcall.scanner.ServiceScanner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Fetters
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenScan {
    String packageName();

    Class<? extends ServiceScanner> scanner();

    ServiceRegisterFactory.Type type();
}
