package xyz.ldqc.tightcall.consumer.call;

import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.support.DefaultChannelChainGroup;
import xyz.ldqc.tightcall.client.ClientApplication;
import xyz.ldqc.tightcall.client.exce.support.NioClientExec;
import xyz.ldqc.tightcall.provider.chain.ChannelRequestOutBoundChain;
import xyz.ldqc.tightcall.registry.server.chain.ChannelConvertResponseHandlerInBoundChain;
import xyz.ldqc.tightcall.registry.server.chain.ChannelResponseFilterBlockHandlerOutBoundChain;
import xyz.ldqc.tightcall.registry.server.response.AbstractResponse;
import xyz.ldqc.tightcall.serializer.support.KryoSerializer;

import java.net.InetSocketAddress;

/**
 * @author Fetters
 */
public class CallClient {

    private final ClientApplication clientApplication;

    private CallClient(ClientApplication clientApplication){
        this.clientApplication = clientApplication;
    }


    public void terminate(){
        clientApplication.terminate();
    }

    public static CallClientBuilder builder(){
        return new CallClientBuilder();
    }


    public AbstractResponse doCall(Object req){
        return (AbstractResponse) clientApplication.writeAndWait(req);
    }

    public static class CallClientBuilder{

        private InetSocketAddress target;

        public CallClientBuilder target(InetSocketAddress target){
            this.target = target;
            return this;
        }

        public CallClient boot(){
            ClientApplication clientApplication = ClientApplication.builder()
                    .address(target)
                    .executor(NioClientExec.class)
                    .chain(buildCallClientChainGroup())
                    .boot();

            return new CallClient(clientApplication);
        }

        private ChainGroup buildCallClientChainGroup(){
            DefaultChannelChainGroup chainGroup = new DefaultChannelChainGroup();
            chainGroup.addLast(new ChannelResponseFilterBlockHandlerOutBoundChain());
            chainGroup.addLast(new ChannelConvertResponseHandlerInBoundChain(KryoSerializer.serializer()));
//            chainGroup.addLast(new ChannelResponseHandlerInBoundChain());
            chainGroup.addLast(new ChannelRequestOutBoundChain());
            return chainGroup;
        }

    }
}
