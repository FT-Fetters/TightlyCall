package xyz.ldqc.tightcall.registry.index.support;

import xyz.ldqc.tightcall.registry.index.IndexRoom;
import xyz.ldqc.tightcall.registry.index.IndexService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class MapIndexRoom implements IndexRoom {

    protected Map<String, IndexService> map;

    @Override
    public List<String> listAllServiceName() {
        return new ArrayList<>(map.keySet());
    }
}
