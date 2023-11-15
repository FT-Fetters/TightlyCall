package xyz.ldqc.tightcall.buffer;

/**
 * 字节数组 增强类
 * @author Fetters
 */
public abstract class AbstractByteData implements Comparable<AbstractByteData>{

    /**
     * 写入单个字节
     * @param b 要写入的字节
     * @return self
     */
    public abstract AbstractByteData writeByte(byte b);
    public abstract AbstractByteData writeBytes(byte[] bs);

    public abstract byte readByte();

    public abstract void readBytes(byte[] bytes);

    public abstract byte[] readBytes(int len);

    public abstract byte[] readBytes();

    public abstract int getInt();

    public abstract String readString();

    public abstract int remaining();

}
