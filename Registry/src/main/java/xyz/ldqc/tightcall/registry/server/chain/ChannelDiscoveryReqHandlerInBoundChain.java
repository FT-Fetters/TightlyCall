package xyz.ldqc.tightcall.registry.server.chain;

import xyz.ldqc.tightcall.chain.support.AbstractChannelFilterSkipHandlerInBoundChain;
import xyz.ldqc.tightcall.registry.index.IndexRoom;
import xyz.ldqc.tightcall.registry.index.IndexServiceBucket;
import xyz.ldqc.tightcall.registry.server.request.DiscoverRequest;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;
import xyz.ldqc.tightcall.registry.server.response.DiscoveryResponse;

import java.nio.channels.Channel;
import java.util.List;

/**
 * @author Fetters
 */
public class ChannelDiscoveryReqHandlerInBoundChain extends AbstractChannelFilterSkipHandlerInBoundChain {

    private final IndexRoom indexRoom;

    public ChannelDiscoveryReqHandlerInBoundChain(IndexRoom indexRoom) {
        super(DiscoverRequest.class);
        this.indexRoom = indexRoom;
    }

    @Override
    public void doHandler(Channel channel, Object obj) {
        DiscoverRequest discoverRequest = (DiscoverRequest) obj;
        String serviceName = discoverRequest.getServiceName();
        IndexServiceBucket bucket = indexRoom.getBucket(serviceName);
        List<ServiceDefinition> serviceDefinitions = bucket.listAll();
        DiscoveryResponse discoveryResponse = new DiscoveryResponse(serviceDefinitions);
        discoveryResponse.setSerialNumber(discoverRequest.getSerialNumber());
        nextChain.doChain(channel, discoveryResponse);
    }
}
