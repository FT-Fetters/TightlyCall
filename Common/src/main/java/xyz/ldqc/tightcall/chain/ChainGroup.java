package xyz.ldqc.tightcall.chain;

import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

/**
 * 调用链组
 * @author Fetters
 */
public interface ChainGroup {

    /**
     * 增加末尾链点
     * @param chain 链点
     * @return 调用链组
     */
    ChainGroup addLast(Chain chain);

    /**
     * 增加头部链点
     * @param chain 链点
     * @return 调用链组
     */
    ChainGroup addHead(Chain chain);


}
