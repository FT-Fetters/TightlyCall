package xyz.ldqc.tightcall.chain;

import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

/**
 * @author Fetters
 */
public interface ChainGroup {

    ChainGroup addLast(Chain chain);

    ChainGroup addHead(Chain chain);


}
