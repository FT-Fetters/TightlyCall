package xyz.ldqc.tightcall.consumer.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.support.DefaultChannelChainGroup;
import xyz.ldqc.tightcall.client.ClientApplication;
import xyz.ldqc.tightcall.client.exce.support.NioClientExec;
import xyz.ldqc.tightcall.provider.chain.ChannelRequestOutBoundChain;
import xyz.ldqc.tightcall.registry.server.chain.ChannelConvertResponseHandlerInBoundChain;
import xyz.ldqc.tightcall.registry.server.request.DiscoverRequest;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;
import xyz.ldqc.tightcall.registry.server.response.DiscoveryResponse;
import xyz.ldqc.tightcall.serializer.support.KryoSerializer;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author Fetters
 */
public class DiscoveryClient {

    public static final Logger log = LoggerFactory.getLogger(DiscoveryClient.class);

    private final ClientApplication clientApplication;

    private DiscoveryClient(ClientApplication clientApplication){
        this.clientApplication = clientApplication;
    }

    public List<ServiceDefinition> discoverService(String serviceName){
        DiscoverRequest discoverRequest = new DiscoverRequest(serviceName);
        Object o = clientApplication.writeAndWait(discoverRequest);
        DiscoveryResponse discoveryResponse = (DiscoveryResponse) o;
        return discoveryResponse.getServiceDefinitions();
    }

    public static DiscoveryClientBuilder builder(){
        return new DiscoveryClientBuilder();
    }


    public static class DiscoveryClientBuilder{
        private InetSocketAddress target;


        public DiscoveryClientBuilder target(InetSocketAddress target){
            this.target = target;
            return this;
        }


        public DiscoveryClient boot(){
            ClientApplication clientApplication = ClientApplication.builder()
                    .address(target)
                    .chain(buildChainGroup())
                    .executor(NioClientExec.class)
                    .boot();
            return new DiscoveryClient(clientApplication);
        }

        private ChainGroup buildChainGroup(){
            return new DefaultChannelChainGroup()
                    .addLast(new ChannelConvertResponseHandlerInBoundChain(KryoSerializer.serializer()))
                    .addLast(new ChannelRequestOutBoundChain());
        }
    }

}
