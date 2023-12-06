package xyz.ldqc.tightcall.common.response;

import xyz.ldqc.tightcall.registry.server.response.AbstractResponse;

/**
 * @author Fetters
 */
public class CallResponse extends AbstractResponse {

    public CallResponse(){
        this.type = "CALL";
    }

    private Object body;

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
