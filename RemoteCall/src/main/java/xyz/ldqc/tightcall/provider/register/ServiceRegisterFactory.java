package xyz.ldqc.tightcall.provider.register;

import xyz.ldqc.tightcall.provider.register.support.DefaultServiceRegister;

/**
 * @author Fetters
 */
public class ServiceRegisterFactory {

    public enum Type {
        /**
         * 默认类
         */
        DEFAULT
    }

    public static ServiceRegister getRegister(Type type){
        switch (type){
            default:
                return new DefaultServiceRegister();
        }
    }
}
