package xyz.ldqc.tightcall.provider.service.process;

import java.lang.reflect.Method;

/**
 * @author Fetters
 */
public interface BeforeServiceProcess {

    /**
     * 前置处理方法
     * @param args 传入参数
     * @return 处理后的参数
     */
    void process(Method method, Object[] args);

}
