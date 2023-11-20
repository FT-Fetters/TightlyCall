package xyz.ldqc.tightcall.chain.support;

import xyz.ldqc.tightcall.chain.*;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;

/**
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
            return this;
        }
        if (chain instanceof OutboundChain) {
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
        return this;
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
}
