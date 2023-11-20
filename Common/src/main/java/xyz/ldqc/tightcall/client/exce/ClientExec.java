package xyz.ldqc.tightcall.client.exce;

/**
 * @author Fetters
 */
public interface ClientExec {


    void start();

    void write(Object o);

    Object writeAndWait(Object o);
}
