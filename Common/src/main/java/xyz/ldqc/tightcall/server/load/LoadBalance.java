package xyz.ldqc.tightcall.server.load;

import java.util.Collection;

/**
 * @author Fetters
 */
public interface LoadBalance<T> {

    /**
     * 获取负载均衡的结果
     */
    T load();

    void add(T cluster);
    void addAll(Collection<T> clusters);

    void addAll(T... clusters);
}
