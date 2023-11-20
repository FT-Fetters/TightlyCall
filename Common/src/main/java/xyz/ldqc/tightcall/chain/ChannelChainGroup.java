package xyz.ldqc.tightcall.chain;

import java.nio.channels.SocketChannel;

/**
 * @author Fetters
 */
public interface ChannelChainGroup extends ChainGroup{

    void doChain(SocketChannel socketChannel, Object o);

}
