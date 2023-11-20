package xyz.ldqc.tightcall.protocol;

import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.buffer.SimpleByteData;
import xyz.ldqc.tightcall.util.SnowflakeUtil;

/**
 * @author Fetters
 */
public class CacheBody {
    private int len = -1;

    private AbstractByteData data;

    private AbstractByteData tmpData;

    private byte version;

    private int serialNumber;

    public CacheBody() {

    }

    public CacheBody(int len, byte version, int serialNumber) {
        this.len = len;
        this.version = version;
        this.serialNumber = serialNumber;
    }

    public CacheBody(byte[] data){
        this.len = data.length;
        this.version = ((byte) 1);
        this.serialNumber = ((int) SnowflakeUtil.getInstance().nextId());
        SimpleByteData byteData = new SimpleByteData();
        byteData.writeBytes(data);
        this.data = byteData;
    }

    public CacheBody(AbstractByteData byteData){
        this.len = byteData.remaining();
        this.version = ((byte) 1);
        this.serialNumber = ((int) SnowflakeUtil.getInstance().nextId());
        this.data = byteData;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public AbstractByteData getData() {
        return data;
    }

    public void setData(AbstractByteData data) {
        this.data = data;
    }

    public AbstractByteData getTmpData() {
        return tmpData;
    }

    public void setTmpData(AbstractByteData tmpData) {
        this.tmpData = tmpData;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
}
