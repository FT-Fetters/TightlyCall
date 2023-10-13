package xyz.ldqc.tightcall.buffer;

import java.nio.ByteBuffer;

/**
 * java.nio.ByteBuffer 增强类
 */
public abstract class ByteData implements Comparable<ByteData>{

    public abstract ByteData writeByte(byte b);
    public abstract ByteData writeBytes(byte[] bs);

}
