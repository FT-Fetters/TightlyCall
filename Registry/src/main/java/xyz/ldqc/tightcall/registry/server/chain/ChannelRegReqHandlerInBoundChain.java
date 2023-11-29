package xyz.ldqc.tightcall.registry.server.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.ldqc.tightcall.chain.support.AbstractChannelFilterSkipHandlerInBoundChain;
import xyz.ldqc.tightcall.registry.index.IndexRoom;
import xyz.ldqc.tightcall.registry.server.request.RegisterRequest;
import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

/**
 * @author Fetters
 */
public class ChannelRegReqHandlerInBoundChain extends AbstractChannelFilterSkipHandlerInBoundChain {

    private static final Logger log = LoggerFactory.getLogger(ChannelRegReqHandlerInBoundChain.class);

    private final IndexRoom indexRoom;

    public ChannelRegReqHandlerInBoundChain(IndexRoom indexRoom) {
        super(RegisterRequest.class);
        this.indexRoom = indexRoom;
    }

    @Override
    public void doHandler(Channel channel, Object obj) {
        RegisterRequest regReq = (RegisterRequest) obj;
        ServiceDefinition serviceDefinition = regReq.getServiceDefinition();
        String serviceName = regReq.getServiceName();
        indexRoom.register(serviceName, serviceDefinition);
        setServiceAddress(regReq, serviceDefinition, channel);
        nextChain.doChain(channel, obj);
    }

    private void setServiceAddress(RegisterRequest regReq, ServiceDefinition service, Channel channel){
        try {
            SocketChannel socketChannel = (SocketChannel) channel;
            InetSocketAddress remoteAddress = ((InetSocketAddress) socketChannel.getRemoteAddress());
            String host = remoteAddress.getHostString();
            int port = regReq.getTargetPort();
            service.setAddress(new InetSocketAddress(host, port));
        } catch (IOException e) {
            log.error("set address fail");
        }
    }
}
