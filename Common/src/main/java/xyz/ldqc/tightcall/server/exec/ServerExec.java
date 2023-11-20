package xyz.ldqc.tightcall.server.exec;

import xyz.ldqc.tightcall.chain.Chainable;

/**
 * @author Fetters
 */
public interface ServerExec extends Chainable {

    /**
     * 启动执行器
     */
    public void start();
}
