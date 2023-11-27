package xyz.ldqc.tightcall.provider.register.support;

import xyz.ldqc.tightcall.exception.ServiceRegisterException;
import xyz.ldqc.tightcall.provider.ProviderApplication;
import xyz.ldqc.tightcall.provider.annotation.OpenClient;
import xyz.ldqc.tightcall.provider.register.RegisterClient;
import xyz.ldqc.tightcall.provider.register.ServiceRegister;
import xyz.ldqc.tightcall.provider.scanner.support.DefaultServiceScanner;
import xyz.ldqc.tightcall.provider.service.ServiceDefinition;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author LENOVO
 */
public class DefaultServiceRegister implements ServiceRegister {

    private RegisterClient client;

    private ProviderApplication providerApplication;


    public DefaultServiceRegister() {

    }

    @Override
    public void doReg(ServiceDefinition serviceDefinition) {
        checkAvailable();

    }

    @Override
    public void doReg(List<ServiceDefinition> serviceDefinitions) {
        checkAvailable();

    }

    private void checkAvailable() {
        if (client == null) {
            Class<?> bootClazz = providerApplication.getBootClazz();
            OpenClient openClient = bootClazz.getAnnotation(OpenClient.class);
            if (openClient == null){
                throw new ServiceRegisterException("can not create register client");
            }
            this.client = RegisterClient.builder()
                    .target(new InetSocketAddress(openClient.host(), openClient.port()))
                    .serviceName(openClient.name())
                    .boot();
        }
    }


    @Override
    public void setProviderApplication(ProviderApplication application) {
        this.providerApplication = application;
    }
}
