package xyz.ldqc.tightcall.server.load.support;

import xyz.ldqc.tightcall.server.load.LoadBalance;

import java.util.Collection;

/**
 * @author Fetters
 * @param <T>
 */
public class SequentialLoadBalance<T> implements LoadBalance<T> {
    @Override
    public T load() {
        return null;
    }

    @Override
    public void add(T cluster) {

    }

    @Override
    public void addAll(Collection<T> clusters) {

    }

    @Override
    public void addAll(T... clusters) {

    }
}
