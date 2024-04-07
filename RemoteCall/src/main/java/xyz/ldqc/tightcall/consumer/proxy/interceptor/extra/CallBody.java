package xyz.ldqc.tightcall.consumer.proxy.interceptor.extra;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fetters
 */
public class CallBody {

    private final Class<?>[] types;

    private final Map<String, Object[]> callArgMap;

    public CallBody(Class<?>... types) {
        this.types = types;
        this.callArgMap = new HashMap<>();
    }

    public CallBody set(String address, Object... args) {
        for (int i = 0; i < args.length; i++) {
            if (!types[i].isAssignableFrom(args[i].getClass())) {
                throw new IllegalArgumentException(
                    String.format("Need type %s but is %s", types[i], args[i].getClass()));
            }
        }
        callArgMap.put(address, args);
        return this;
    }

    public Map<String, Object[]> getAll() {
        return callArgMap;
    }


}
