package xyz.ldqc.tightcall.provider.register;

import xyz.ldqc.tightcall.provider.service.ServiceDefinition;

import java.util.List;

/**
 * @author Fetters
 */
public interface ServiceRegister {

    public void doReg(ServiceDefinition serviceDefinition);

    public void doReg(List<ServiceDefinition> serviceDefinitions);

}
