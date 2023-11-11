package xyz.ldqc.tightcall.pool;

/**
 * @author Fetters
 */
public interface ResultPool<K,V> {

    void put(K key, V value);

    V getResult(K key);

    V getResult(K key, long time);
}
