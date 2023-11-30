package xyz.ldqc.tightcall.aop;

/**
 * 类筛选器，用于匹配给定的类集
 */
public interface ClassFilter {

    boolean matches(Class<?> clazz);

}
