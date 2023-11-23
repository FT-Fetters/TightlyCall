package xyz.ldqc.tightcall.registry.index.support;

import xyz.ldqc.tightcall.registry.index.IndexRoom;
import xyz.ldqc.tightcall.registry.index.IndexService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Fetters
 */
public abstract class AbstractMapIndexRoom implements IndexRoom {

    protected Map<String, IndexService> map;

    @Override
    public List<String> listAllServiceName() {
        return new ArrayList<>(map.keySet());
    }
}
