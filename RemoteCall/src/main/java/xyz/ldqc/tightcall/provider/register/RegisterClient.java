package xyz.ldqc.tightcall.provider.register;

import xyz.ldqc.tightcall.chain.support.DefaultChannelChainGroup;
import xyz.ldqc.tightcall.client.ClientApplication;
import xyz.ldqc.tightcall.client.exce.support.NioClientExec;
import xyz.ldqc.tightcall.exception.RegisterClientException;
import xyz.ldqc.tightcall.provider.service.ServiceDefinition;

import java.net.InetSocketAddress;

/**
 * @author Fetters
 */
public class RegisterClient {

    private final ClientApplication client;

    private final String serviceName;

    private RegisterClient(ClientApplication client, String serviceName){
        this.client = client;
        this.serviceName = serviceName;
    }

    public RegisterClientBuilder builder(){
        return new RegisterClientBuilder();
    }

    public void register(ServiceDefinition serviceDefinition){

    }



    public static class RegisterClientBuilder{
        private InetSocketAddress target;

        private String serviceName;

        public RegisterClientBuilder target(InetSocketAddress target){
            this.target = target;
            return this;
        }

        public RegisterClientBuilder serviceName(String serviceName){
            this.serviceName = serviceName;
            return this;
        }

        public RegisterClient boot(){
            if (serviceName == null || serviceName.isEmpty()){
                throw new RegisterClientException("service name can not be null or empty");
            }
            ClientApplication clientApplication = ClientApplication.builder()
                    .address(target)
                    .chain(new DefaultChannelChainGroup())
                    .executor(NioClientExec.class)
                    .boot();
            return new RegisterClient(clientApplication, this.serviceName);
        }
    }
}
