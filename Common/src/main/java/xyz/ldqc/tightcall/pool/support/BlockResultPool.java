package xyz.ldqc.tightcall.pool.support;

import xyz.ldqc.tightcall.pool.ResultPool;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Fetters<br />
 * 阻塞结果池
 */
public class BlockResultPool<K, V> implements ResultPool<K, V> {

    private final ConcurrentHashMap<K, V> resMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<K, Thread> threadMap = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(
            16, r -> new Thread(r, "time-out-timer")
    );


    @Override
    public void put(K key, V value) {
        // 添加结果并唤醒读取线程
        resMap.put(key, value);
        Thread thread = threadMap.get(key);
        if (thread == null) {
            throw new RuntimeException("unknown thread");
        }
        LockSupport.unpark(thread);
    }

    @Override
    public V getResult(K key) {
        V result = resMap.get(key);
        // 如果获取到结果直接返回
        if (result != null) {
            return result;
        }
        // 阻塞该线程
        threadMap.put(key, Thread.currentThread());
        LockSupport.park();
        result = resMap.get(key);
        if (result == null) {
            throw new RuntimeException("null result");
        }
        resMap.remove(key);
        threadMap.remove(key);
        return result;
    }

    @Override
    public V getResult(K key, long time) {
        V result = resMap.get(key);
        // 如果获取到结果直接返回
        if (result != null) {
            return result;
        }
        // 阻塞该线程
        Thread thread = Thread.currentThread();
        threadMap.put(key, Thread.currentThread());
        // 超时唤醒
        scheduledExecutorService.schedule(() -> LockSupport.unpark(thread), time, TimeUnit.SECONDS);
        LockSupport.park();
        result = resMap.get(key);
        if (result == null) {
            throw new RuntimeException("time out or null result");
        }
        resMap.remove(key);
        threadMap.remove(key);
        return result;
    }
}
