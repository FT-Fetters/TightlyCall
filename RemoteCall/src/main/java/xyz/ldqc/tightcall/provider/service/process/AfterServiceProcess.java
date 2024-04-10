package xyz.ldqc.tightcall.provider.service.process;

import java.lang.reflect.Method;

/**
 * @author Fetters
 */
public interface AfterServiceProcess {

    /**
     * 后置处理方法
     * @param args 传入参数
     * @param ret 服务方法执行结果
     * @return 返回结果
     */
    Object process(Method method, Object[] args, Object ret);

}
