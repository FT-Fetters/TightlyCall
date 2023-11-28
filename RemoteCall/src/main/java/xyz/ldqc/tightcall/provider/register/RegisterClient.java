package xyz.ldqc.tightcall.provider.register;

import xyz.ldqc.tightcall.chain.support.DefaultChannelChainGroup;
import xyz.ldqc.tightcall.client.ClientApplication;
import xyz.ldqc.tightcall.client.exce.support.NioClientExec;
import xyz.ldqc.tightcall.exception.RegisterClientException;
import xyz.ldqc.tightcall.protocol.ProtocolDataFactory;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;
import xyz.ldqc.tightcall.registry.server.request.RegisterRequest;

import java.net.InetSocketAddress;

/**
 * @author Fetters
 */
public class RegisterClient {

    private final ClientApplication client;

    private final String serviceName;

    private final int providerPort;

    private RegisterClient(ClientApplication client, String serviceName, int providerPort){
        this.client = client;
        this.serviceName = serviceName;
        this.providerPort = providerPort;
    }

    public static RegisterClientBuilder builder(){
        return new RegisterClientBuilder();
    }

    public void register(ServiceDefinition serviceDefinition){
        RegisterRequest registerRequest = buildRegisterRequest(serviceDefinition);
        client.write(registerRequest);
    }

    private RegisterRequest buildRegisterRequest(ServiceDefinition definition){
        RegisterRequest request = new RegisterRequest();
        request.setTargetPort(this.providerPort);
        request.setServiceName(this.serviceName);
        request.setServiceDefinition(definition);
        return request;
    }

    public static class RegisterClientBuilder{
        private InetSocketAddress target;

        private String serviceName;

        private int providerPort;

        public RegisterClientBuilder target(InetSocketAddress target){
            this.target = target;
            return this;
        }

        public RegisterClientBuilder serviceName(String serviceName){
            this.serviceName = serviceName;
            return this;
        }

        public RegisterClientBuilder providerPort(int providerPort){
            this.providerPort = providerPort;
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
            return new RegisterClient(clientApplication, this.serviceName, providerPort);
        }
    }
}
