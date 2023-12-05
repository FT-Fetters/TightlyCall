package xyz.ldqc.tightcall.consumer.proxy.factory;


import xyz.ldqc.tightcall.util.ClassUtil;

public class TargetSource {

    private final Object target;

    public TargetSource(Object target){
        this.target = target;
    }

    public Class<?>[] getTargetClass(){
        Class<?> clazz = this.target.getClass();
        clazz = ClassUtil.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
        return clazz.getInterfaces();
    }

    public Object getTarget(){
        return this.target;
    }

}
