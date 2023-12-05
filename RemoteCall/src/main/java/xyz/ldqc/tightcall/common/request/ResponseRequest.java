package xyz.ldqc.tightcall.common.request;

import xyz.ldqc.tightcall.registry.server.request.AbstractRequest;

/**
 * @author Fetters
 */
public class ResponseRequest extends AbstractRequest {

    public ResponseRequest(){
        this.type = "RESPONSE";
    }
}
