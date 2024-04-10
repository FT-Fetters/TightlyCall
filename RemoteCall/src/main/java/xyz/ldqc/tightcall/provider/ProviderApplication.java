package xyz.ldqc.tightcall.provider;

import xyz.ldqc.tightcall.exception.ProviderException;
import xyz.ldqc.tightcall.provider.annotation.OpenScan;
import xyz.ldqc.tightcall.provider.annotation.ProviderConfig;
import xyz.ldqc.tightcall.provider.register.ServiceRegister;
import xyz.ldqc.tightcall.provider.register.ServiceRegisterFactory;
import xyz.ldqc.tightcall.provider.server.ProviderServer;
import xyz.ldqc.tightcall.provider.service.MethodInvoker;
import xyz.ldqc.tightcall.provider.service.ServiceContainer;
import xyz.ldqc.tightcall.scanner.ServiceScanner;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Fetters
 */
public class ProviderApplication {

    private final Class<?> bootClazz;

    private ProviderServer providerServer;

    private ServiceContainer serviceContainer;

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
        List<ServiceDefinition> serviceDefinitions = null;
        try {
            ServiceScanner serviceScanner = scannerClazz.getConstructor().newInstance();
            serviceScanner.setPackagePath(packageName);
            serviceScanner.setRunClass(bootClazz);
            serviceDefinitions = serviceScanner.doScan();
            ServiceRegister register = ServiceRegisterFactory.getRegister(type);
            register.setProviderApplication(this);
            register.doReg(serviceDefinitions);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        bootProviderServer(serviceDefinitions);
    }

    private void bootProviderServer(List<ServiceDefinition> serviceDefinitions){
        ProviderConfig providerConfig = bootClazz.getAnnotation(ProviderConfig.class);
        if (providerConfig == null){
            throw new ProviderException("cannot find provider config");
        }
        this.serviceContainer = new ServiceContainer(serviceDefinitions, new MethodInvoker());
        this.providerServer = ProviderServer.builder()
                .serviceContainer(serviceContainer)
                .port(providerConfig.port())
                .boot();
    }

    public Class<?> getBootClazz(){
        return bootClazz;
    }

    public ServiceContainer getServiceContainer() {
        return serviceContainer;
    }
}
