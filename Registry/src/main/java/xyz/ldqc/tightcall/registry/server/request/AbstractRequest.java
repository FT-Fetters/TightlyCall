package xyz.ldqc.tightcall.registry.server.request;

import xyz.ldqc.tightcall.protocol.SerialNumber;

/**
 * @author Fetters
 */
public abstract class AbstractRequest implements SerialNumber {

    protected String type;

    protected int serialNumber;

    public String getType(){
        return this.type;
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
