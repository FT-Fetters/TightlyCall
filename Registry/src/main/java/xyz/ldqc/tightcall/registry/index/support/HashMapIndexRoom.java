package xyz.ldqc.tightcall.registry.index.support;

import xyz.ldqc.tightcall.registry.server.request.ServiceDefinition;

import java.util.HashMap;

/**
 * @author Fetters
 */
public class HashMapIndexRoom extends AbstractMapIndexRoom {

    public HashMapIndexRoom() {
        map = new HashMap<>();
    }

}
