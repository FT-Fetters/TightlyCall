package xyz.ldqc.tightcall.chain;

import xyz.ldqc.tightcall.server.handler.Handler;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public interface Chain {

    void doChain(Channel channel, Object obj);


}
