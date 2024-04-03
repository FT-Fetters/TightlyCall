package xyz.ldqc.tightcall.consumer.proxy.interceptor.extra;

import java.util.Map;

/**
 * @author Fetters
 */
public class  CallResult<T> {

    private final Map<String, T> result;

    public CallResult(final Map<String, T> result)
    {
        this.result = result;
    }

    public Map<String, T> result(){
        return result;
    }

}
