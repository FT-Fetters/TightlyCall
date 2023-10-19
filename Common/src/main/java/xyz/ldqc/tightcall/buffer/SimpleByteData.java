package xyz.ldqc.tightcall.buffer;

import xyz.ldqc.tightcall.exception.ByteDataException;
import xyz.ldqc.tightcall.util.IntegerUtils;

import java.nio.ByteBuffer;

public class SimpleByteData extends ByteData {

    /**
     * 默认最大空间
     */
    private static final int DEFAULT_MAX_CAPACITY = 65535;

    /**
     * 默认初始空间
     */
    private static final int DEFAULT_INIT_CAPACITY = 1024;

    /**
     * heapArea 堆内存
     */
    private byte[] hp;

    /**
     * 最大空间
     */
    private final int maxCapacity;

    /**
     * 数据尾指针
     */
    private int tail;

    /**
     * 当前大小
     */
    private int size;

    /**
     * 读取指针
     */
    private int readPos;


    /**
     * 无参构造
     */
    public SimpleByteData() {
        this(DEFAULT_INIT_CAPACITY, DEFAULT_MAX_CAPACITY);
    }

    public SimpleByteData(int capacity) {
        this(capacity, DEFAULT_MAX_CAPACITY);
    }

    public SimpleByteData(int capacity, int maxCapacity) {
        if (capacity > maxCapacity) {
            try {
                throw new ByteDataException("capacity can not greater than max capacity");
            } catch (ByteDataException e) {
                throw new RuntimeException(e);
            }
        }
        this.hp = alloc(capacity);
        this.maxCapacity = maxCapacity;
        tail = -1;
        readPos = 0;
        size = 0;
    }


    public SimpleByteData(ByteBuffer byteBuffer) {
        this(byteBuffer, DEFAULT_MAX_CAPACITY);
    }


    public SimpleByteData(ByteBuffer byteBuffer, int maxCapacity) {
        byteBuffer.flip();
        int remaining = byteBuffer.remaining();
        // 如果传入的ByteBuffer内容过长
        if (remaining > maxCapacity) {
            try {
                throw new ByteDataException("The byte array length of ByteBuffer is greater than the maximum length");
            } catch (ByteDataException e) {
                throw new RuntimeException(e);
            }
        }
        // 计算需要分配的数组大小
        int byteLen = IntegerUtils.setAllBitsToOne(remaining) + 1;
        hp = alloc(byteLen);
        // 从ByteBuffer获取字节数组存入hp中
        byteBuffer.get(hp, 0, remaining);
        tail = remaining - 1;
        readPos = 0;
        this.maxCapacity = maxCapacity;
        size = remaining;
    }


    /**
     * 比较方法
     *
     * @param o the object to be compared.
     */
    @Override
    public int compareTo(ByteData o) {
        return 0;
    }

    /**
     * 分配指定空间大小的字节数组
     *
     * @param capacity 空间大小
     */
    private byte[] alloc(int capacity) {
        return new byte[capacity];
    }

    /**
     * 扩大存储字节数组
     *
     * @param minCap 最小容量
     */
    private void grow(int minCap) {
        minCap = Math.min(minCap, maxCapacity);
        int n = IntegerUtils.setAllBitsToOne(minCap);
        // 将目标大小设为原来的1.5倍
        int targetCap = n + (n >> 1);
        // 分配新的字节数组
        byte[] _hp = alloc(targetCap);
        // 判断是否超过末尾循环
        if (readPos > tail) {
            // 尾部长度
            int tailLen = hp.length - readPos;
            // 复制第一部分
            System.arraycopy(hp, readPos, _hp, 0, tailLen);
            // 复制第二部分
            System.arraycopy(hp, 0, _hp, tailLen , size - tailLen);
            readPos = 0;
        } else {
            System.arraycopy(hp, readPos, _hp, 0, size);
            readPos = 0;
        }
        hp = _hp;
        tail = size - 1;
    }

    @Override
    public ByteData writeByte(byte b) {
        ensureCapEnough(size + 1);
        this.setByte((tail + 1) % hp.length, b);
        size++;
        tail = (tail + 1) % hp.length;
        return this;
    }

    @Override
    public ByteData writeBytes(byte[] bs) {
        ensureCapEnough(size + bs.length);
        return null;
    }

    @Override
    public byte readByte() {
        checkReadableByte(1);
        byte b = getByte(readPos);
        size--;
        readPos = (readPos + 1) % hp.length;
        return b;
    }

    /**
     * 确保hp数组容量足够
     *
     * @param size 需要的大小
     */
    private void ensureCapEnough(int size) {
        if (size > maxCapacity) {
            try {
                throw new ByteDataException("maximum capacity exceeded");
            } catch (ByteDataException e) {
                throw new RuntimeException(e);
            }
        }
        if (size > this.hp.length) {
            grow(size);
        }
    }

    /**
     * 确定一定范围内的数据长度可读
     *
     * @param readByteLen 读取字节长度
     */
    private void checkReadableByte(int readByteLen) {
        if (readByteLen > size) {
            try {
                throw new ByteDataException("illegal read length");
            } catch (ByteDataException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 设置某位置的字节内容
     *
     * @param i 索引位置
     * @param b 要写入的字节
     */
    private void setByte(int i, byte b) {
        if (i > hp.length - 1) {
            try {
                throw new ByteDataException("index out of range");
            } catch (ByteDataException e) {
                throw new RuntimeException(e);
            }
        }
        hp[i] = b;
    }

    private byte getByte(int i) {
        if (i > hp.length - 1) {
            try {
                throw new ByteDataException("index out of size");
            } catch (ByteDataException e) {
                throw new RuntimeException(e);
            }
        }
        return hp[i];
    }
}
