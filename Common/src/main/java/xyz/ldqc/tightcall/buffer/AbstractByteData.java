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

    /**
     * 写入字节数组
     * @param bs 待写入的字节数组，写入后操作该对象不会对原数组产生影响
     * @return self
     */
    public abstract AbstractByteData writeBytes(byte[] bs);

    /**
     * 写入字节数字
     * @param bs 字节数字
     * @param src 开始位置
     * @param len 长度
     * @return self
     */
    public abstract AbstractByteData writeBytes(byte[] bs, int src, int len);

    /**
     * 写入长整数
     * @param l long
     * @return self
     */
    public abstract AbstractByteData writeLong(long l);

    /**
     * 读取一个长整数
     * @return long
     */
    public abstract long readLong();

    /**
     * 读取单个字节
     * @return byte
     */
    public abstract byte readByte();

    /**
     * 读取字节数组
     * @param bytes 传入一个字节数组，会将读取的字节数组存入字节数组中
     */
    public abstract void readBytes(byte[] bytes);

    /**
     * 通过指定长度读取字节数组
     * @param len 要读取的字节数
     * @return byte[]
     */
    public abstract byte[] readBytes(int len);

    /**
     * 读取所有字节内容并返回字节数组
     * @return byte[]
     */
    public abstract byte[] readBytes();

    /**
     * 从该对象读取4个字节并转化成整数
     * @return int
     */
    public abstract int getInt();

    /**
     * 将该对象的字节内容转化为字符串并返回
     * @return String
     */
    public abstract String readString();

    /**
     * 获取该对象还能读取多少字节数
     * @return int
     */
    public abstract int remaining();

    /**
     * 判断是否为空
     * @return bool
     */
    public abstract boolean isEmpty();

}
