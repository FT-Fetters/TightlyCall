package xyz.ldqc.tightcall.client.message;

import xyz.ldqc.tightcall.chain.ChainGroup;
import xyz.ldqc.tightcall.chain.ChannelChainGroup;
import xyz.ldqc.tightcall.chain.support.ChannelResultPoolHandlerInBoundChain;
import xyz.ldqc.tightcall.pool.ResultPool;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 消息接收线程
 * @author Fetters
 */
public class MessageReceiveThread extends Thread{

    private final Selector selector;

    private final ChannelChainGroup chainGroup;

    private boolean terminate = false;

    public MessageReceiveThread(SocketChannel channel, ResultPool<Integer, Object> resultPool, ChainGroup group){
        try {
            // 将channel注册到该selector
            this.selector = Selector.open();
            channel.register(this.selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.chainGroup = (ChannelChainGroup) group;
        // 给调用链组的出站添加结果处理链
        this.chainGroup.addLast(new ChannelResultPoolHandlerInBoundChain(resultPool));
        this.start();
    }

    public void terminate(){
        this.terminate = true;
    }

    @Override
    public void run() {
        initThread();
        while (!terminate){
            try {
                this.selector.select();
                Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                watchKeys(selectionKeys);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initThread(){
        Thread.currentThread().setName("msg-receive");
    }

    private void watchKeys(Set<SelectionKey> selectionKeys) {
        Iterator<SelectionKey> iter = selectionKeys.iterator();
        while (iter.hasNext()){
            SelectionKey key = iter.next();
            iter.remove();
            if (key.isReadable()){
                watchReadableKey(key);
            }
        }
    }

    private void watchReadableKey(SelectionKey key){
        SocketChannel channel = (SocketChannel) key.channel();
        chainGroup.doChain(channel, key);
    }
}
