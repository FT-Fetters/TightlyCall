package xyz.ldqc.tightcall.registry.server.response;

import xyz.ldqc.tightcall.protocol.SerialNumber;

/**
 * @author Fetters
 */
public class AbstractResponse implements SerialNumber {

    protected String type;

    protected int serialNumber;

    public String getType(){
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int getSerialNumber() {
        return serialNumber;
    }

    @Override
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
}
