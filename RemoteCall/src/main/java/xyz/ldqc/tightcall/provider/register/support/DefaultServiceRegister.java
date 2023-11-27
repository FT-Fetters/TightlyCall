package xyz.ldqc.tightcall.provider.register.support;

import xyz.ldqc.tightcall.provider.register.RegisterClient;
import xyz.ldqc.tightcall.provider.register.ServiceRegister;
import xyz.ldqc.tightcall.provider.scanner.support.DefaultServiceScanner;
import xyz.ldqc.tightcall.provider.service.ServiceDefinition;

import java.util.List;

/**
 * @author LENOVO
 */
public class DefaultServiceRegister implements ServiceRegister {

    private RegisterClient client;

    public DefaultServiceRegister(){

    }

    @Override
    public void doReg(ServiceDefinition serviceDefinition) {

    }

    @Override
    public void doReg(List<ServiceDefinition> serviceDefinitions) {

    }
}
