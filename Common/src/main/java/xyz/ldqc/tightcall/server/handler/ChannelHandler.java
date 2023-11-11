package xyz.ldqc.tightcall.server.handler;

import java.nio.channels.Channel;

/**
 * @author Fetters
 */
public interface ChannelHandler extends Handler{


    /**
     * 执行处理
     * @param channel 要处理的channel
     * @param obj 携带对象
     */
    public void doHandler(Channel channel, Object obj);
}
