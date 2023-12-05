package xyz.ldqc.tightcall.common.request;

import xyz.ldqc.tightcall.registry.server.request.AbstractRequest;

/**
 * @author Fetters
 */
public class CallRequest extends AbstractRequest {

    public CallRequest(){
        this.type = "CALL";
    }
}
