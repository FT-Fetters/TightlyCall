package xyz.ldqc.tightcall.chain;

/**
 * 可链式调用接口
 * @author Fetters
 */
public interface Chainable {

    /**
     * 为可调用链类设置调用链组
     * @param group 调用链组
     */
    public void setChainGroup(ChainGroup group);
}
