package xyz.ldqc.tightcall.consumer.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.chain.support.DefaultChannelChainGroup;
import xyz.ldqc.tightcall.client.ClientApplication;
import xyz.ldqc.tightcall.client.exce.support.NioClientExec;
import xyz.ldqc.tightcall.provider.chain.ChannelRequestOutBoundChain;
import xyz.ldqc.tightcall.registry.server.request.DiscoverRequest;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

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

    private List<ServiceDefinition> discoverService(String serviceName){
        DiscoverRequest discoverRequest = new DiscoverRequest(serviceName);
        Object o = clientApplication.writeAndWait(discoverRequest);
        return null;
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
                    .chain(new DefaultChannelChainGroup().addLast(new ChannelRequestOutBoundChain()))
                    .executor(NioClientExec.class)
                    .boot();
            return new DiscoveryClient(clientApplication);
        }
    }

}
