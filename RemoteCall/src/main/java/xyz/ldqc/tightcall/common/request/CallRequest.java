package xyz.ldqc.tightcall.common.request;

import xyz.ldqc.tightcall.registry.server.request.AbstractRequest;

/**
 * @author Fetters
 */
public class CallRequest extends AbstractRequest {

    private Object[] args;

    private String[] argTypes;

    private String path;

    public CallRequest(){
        this.type = "CALL";
    }


    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(String[] argTypes) {
        this.argTypes = argTypes;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
