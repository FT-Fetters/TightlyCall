package xyz.ldqc.tightcall.provider.register.support;

import xyz.ldqc.tightcall.exception.ServiceRegisterException;
import xyz.ldqc.tightcall.provider.ProviderApplication;
import xyz.ldqc.tightcall.provider.annotation.OpenRegClient;
import xyz.ldqc.tightcall.provider.annotation.ProviderConfig;
import xyz.ldqc.tightcall.provider.register.RegisterClient;
import xyz.ldqc.tightcall.provider.register.ServiceRegister;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

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
        client.register(serviceDefinition);
    }

    @Override
    public void doReg(List<ServiceDefinition> serviceDefinitions) {
        checkAvailable();
        serviceDefinitions.forEach(client::register);
    }

    private void checkAvailable() {
        if (client == null) {
            Class<?> bootClazz = providerApplication.getBootClazz();
            OpenRegClient openRegClient = bootClazz.getAnnotation(OpenRegClient.class);
            ProviderConfig providerConfig = bootClazz.getAnnotation(ProviderConfig.class);
            if (openRegClient == null || providerConfig == null){
                throw new ServiceRegisterException("can not create register client");
            }
            this.client = RegisterClient.builder()
                    .target(new InetSocketAddress(openRegClient.host(), openRegClient.port()))
                    .serviceName(openRegClient.name())
                    .providerPort(providerConfig.port())
                    .boot();
        }
    }


    @Override
    public void setProviderApplication(ProviderApplication application) {
        this.providerApplication = application;
    }
}
