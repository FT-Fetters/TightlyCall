package xyz.ldqc.tightcall.server.load.support;

import xyz.ldqc.tightcall.server.load.LoadBalance;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Fetters
 */
public class RandomLoadBalance<T> implements LoadBalance<T> {

    private final List<T> balanceContainer;

    private final Random random;

    public RandomLoadBalance(){
        this.balanceContainer = new ArrayList<T>();
        random = new Random(System.currentTimeMillis());
    }

    public RandomLoadBalance(List<T> cluster, List<T> balanceContainer){
        this.balanceContainer = balanceContainer;
        random = new Random(System.currentTimeMillis());
    }

    public RandomLoadBalance(List<T> cluster, long randomSeed){
        this.balanceContainer = cluster;
        this.random = new Random(randomSeed);
    }

    public RandomLoadBalance(T[] cluster){
        this.balanceContainer = Arrays.asList(cluster);
        random = new Random(System.currentTimeMillis());
    }

    public RandomLoadBalance(T[] cluster, long randomSeed){
        this.balanceContainer = Arrays.asList(cluster);
        random = new Random(randomSeed);
    }
    @Override
    public T load() {
        return balanceContainer.get(random.nextInt(balanceContainer.size()));
    }

    @Override
    public void add(T cluster){
        this.balanceContainer.add(cluster);
    }

    @SafeVarargs
    @Override
    public final void addAll(T... clusters) {
        List<T> list = Arrays.stream(clusters).collect(Collectors.toList());
        this.balanceContainer.addAll(list);
    }

    @Override
    public void addAll(Collection<T> clusters){
        this.balanceContainer.addAll(clusters);
    }
}
