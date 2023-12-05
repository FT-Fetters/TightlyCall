package xyz.ldqc.tightcall.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Fetters
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenMapping {

    String value();
}
