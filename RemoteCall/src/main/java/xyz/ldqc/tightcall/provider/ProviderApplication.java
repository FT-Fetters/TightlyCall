package xyz.ldqc.tightcall.provider;

import xyz.ldqc.tightcall.provider.annotation.OpenScan;
import xyz.ldqc.tightcall.provider.register.ServiceRegister;
import xyz.ldqc.tightcall.provider.register.ServiceRegisterFactory;
import xyz.ldqc.tightcall.scanner.ServiceScanner;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.util.List;

/**
 * @author Fetters
 */
public class ProviderApplication {

    private final Class<?> bootClazz;

    private ProviderApplication(Class<?> bootClazz){
        this.bootClazz = bootClazz;
    }

    public static ProviderApplication run(Class<?> clazz){
        ProviderApplication provApp = new ProviderApplication(clazz);
        provApp.loadBootClass();
        return provApp;
    }

    private void loadBootClass(){
        OpenScan scan = bootClazz.getAnnotation(OpenScan.class);
        String packageName = scan.packageName();
        Class<? extends ServiceScanner> scannerClazz = scan.scanner();
        ServiceRegisterFactory.Type type = scan.type();
        try {
            ServiceScanner serviceScanner = scannerClazz.newInstance();
            serviceScanner.setPackagePath(packageName);
            List<ServiceDefinition> definitions = serviceScanner.doScan();
            ServiceRegister register = ServiceRegisterFactory.getRegister(type);
            register.setProviderApplication(this);
            register.doReg(definitions);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> getBootClazz(){
        return bootClazz;
    }


}
