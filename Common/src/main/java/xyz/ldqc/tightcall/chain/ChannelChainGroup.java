package xyz.ldqc.tightcall.chain;

import java.nio.channels.SocketChannel;

/**
 * SocketChannel调用链组
 * @author Fetters
 */
public interface ChannelChainGroup extends ChainGroup{

    /**
     * 通过传入SocketChannel进行调用链的调用
     * @param socketChannel 要传递的SocketChannel
     * @param o 附加对象
     */
    void doChain(SocketChannel socketChannel, Object o);

    /**
     * 调用出站调用链
     * @param socketChannel 要传递的SocketChannel
     * @param o 附加对象
     */
    void doOutBoundChain(SocketChannel socketChannel, Object o);

}
