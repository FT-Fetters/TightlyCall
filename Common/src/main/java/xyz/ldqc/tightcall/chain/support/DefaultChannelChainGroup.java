package xyz.ldqc.tightcall.chain.support;

import xyz.ldqc.tightcall.chain.*;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;

/**
 * 调用链组
 * @author Fetters
 */
public class DefaultChannelChainGroup implements ChannelChainGroup {

    private final LinkedList<Chain> inBoundChainList;
    private final LinkedList<Chain> outBoundChainList;

    public DefaultChannelChainGroup() {
        this.inBoundChainList = new LinkedList<>();
        this.outBoundChainList = new LinkedList<>();
    }


    @Override
    public ChainGroup addLast(Chain chain) {
        if (chain instanceof InboundChain) {
            handleAddInBoundChain(chain);
            return this;
        }
        if (chain instanceof OutboundChain) {
            handleAddOutBoundChain(chain);
        }
        return this;
    }

    /**
     * 处理添加入站链点
     */
    private void handleAddOutBoundChain(Chain chain) {
        if (!outBoundChainList.isEmpty()) {
            Chain last = outBoundChainList.getLast();
            if (last != null) {
                last.setNextChain(chain);
            }
        }else {
            if (!inBoundChainList.isEmpty()){
                inBoundChainList.getLast().setNextChain(chain);
            }
        }
        outBoundChainList.addLast(chain);
    }

    /**
     * 处理添加出站链点
     */
    private void handleAddInBoundChain(Chain chain) {
        if (!inBoundChainList.isEmpty()) {
            Chain last = inBoundChainList.getLast();
            if (last != null) {
                last.setNextChain(chain);
            }
        }
        inBoundChainList.addLast(chain);
        if (!outBoundChainList.isEmpty()){
            chain.setNextChain(outBoundChainList.getFirst());
        }
    }

    @Override
    public ChainGroup addHead(Chain chain) {
        if (chain instanceof InboundChain) {
            if (!inBoundChainList.isEmpty()) {
                Chain first = inBoundChainList.getFirst();
                if (first != null) {
                    chain.setNextChain(first);
                }
            }
            inBoundChainList.addFirst(chain);
            return this;
        }
        if (chain instanceof OutboundChain) {
            if (!outBoundChainList.isEmpty()) {
                Chain last = inBoundChainList.getLast();
                if (last != null) {
                    last.setNextChain(chain);
                }
            }
            if (!outBoundChainList.isEmpty()){
                chain.setNextChain(outBoundChainList.getFirst());
            }
            outBoundChainList.addFirst(chain);
        }
        return this;
    }

    @Override
    public void doChain(SocketChannel socketChannel, Object o) {
        if (inBoundChainList.isEmpty()) {
            return;
        }
        Chain first = inBoundChainList.getFirst();
        if (first != null) {
            first.doChain(socketChannel, o);
        }
    }

    @Override
    public void doOutBoundChain(SocketChannel socketChannel, Object o) {
        if (outBoundChainList.isEmpty()){
            return;
        }
        Chain outBoundFirst = outBoundChainList.getFirst();
        if (outBoundFirst != null){
            outBoundFirst.doChain(socketChannel, o);
        }
    }
}
