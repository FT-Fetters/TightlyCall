package xyz.ldqc.tightcall.provider.register.support;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.provider.ProviderApplication;
import xyz.ldqc.tightcall.provider.annotation.OpenRegClient;
import xyz.ldqc.tightcall.provider.annotation.ProviderConfig;
import xyz.ldqc.tightcall.provider.register.RegisterClient;
import xyz.ldqc.tightcall.provider.register.ServiceRegister;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Fetters
 */
public class DefaultServiceRegister implements ServiceRegister {

    private static final Logger log = LoggerFactory.getLogger(DefaultServiceRegister.class);
    private RegisterClient client;

    private ProviderApplication providerApplication;


    public DefaultServiceRegister() {
        // Noting to do
    }

    @Override
    public void doReg(ServiceDefinition serviceDefinition) {
        doReg(Collections.singletonList(serviceDefinition));
    }

    @Override
    public void doReg(List<ServiceDefinition> serviceDefinitions) {
        checkAvailable();
        if (client == null) {
            log.info("Register is disable");
            return;
        }
        serviceDefinitions.forEach(client::register);
    }

    private void checkAvailable() {
        if (client == null) {
            Class<?> bootClazz = providerApplication.getBootClazz();
            OpenRegClient openRegClient = bootClazz.getAnnotation(OpenRegClient.class);
            ProviderConfig providerConfig = bootClazz.getAnnotation(ProviderConfig.class);
            if (openRegClient == null) {
                return;
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
