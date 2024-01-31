package xyz.ldqc.tightcall.buffer;

import xyz.ldqc.tightcall.exception.ByteDataException;
import xyz.ldqc.tightcall.util.DigestUtil;
import xyz.ldqc.tightcall.util.IntegerUtils;

import java.nio.ByteBuffer;

/**
 * @author Fetters
 */
public class SimpleByteData extends AbstractByteData {

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

  /**
   * 指定初始大小的构造
   *
   * @param capacity 初始大小
   */
  public SimpleByteData(int capacity) {
    this(capacity, Math.max(capacity, DEFAULT_MAX_CAPACITY));
  }

  /**
   * 指定初始大小及最大容量的构造
   *
   * @param capacity    初始容量
   * @param maxCapacity 最大容量
   */
  public SimpleByteData(int capacity, int maxCapacity) {
    if (capacity > maxCapacity) {
      // 当初始容量大于最大容量，抛出异常，创建对象失败
      throw new ByteDataException("capacity can not greater than max capacity");
    }
    // 分配堆内存并初始化属性
    this.hp = alloc(capacity);
    this.maxCapacity = maxCapacity;
    tail = -1;
    readPos = 0;
    size = 0;
  }

  /**
   * 通过ByteBuffer创建ByteData对象，不指定最大容量，设置未默认最大容量
   *
   * @param byteBuffer 要传入的ByteBuffer
   */
  public SimpleByteData(ByteBuffer byteBuffer) {
    this(byteBuffer, DEFAULT_MAX_CAPACITY);
  }


  /**
   * 通过ByteBuffer创建ByteData对象，并指定最大容量
   *
   * @param byteBuffer  要初始化的ByteBuffer
   * @param maxCapacity 最大容量
   */
  public SimpleByteData(ByteBuffer byteBuffer, int maxCapacity) {
    int remaining = byteBuffer.position();
    byteBuffer.flip();
    // 如果传入的ByteBuffer内容过长
    if (remaining > maxCapacity) {
      throw new ByteDataException(
          "The byte array length of ByteBuffer is greater than the maximum length");
    }
    // 计算需要分配的数组大小
    int byteLen = IntegerUtils.setAllBitsToOne(remaining) + 1;
    this.hp = alloc(byteLen);
    // 从ByteBuffer获取字节数组存入hp中
    byteBuffer.get(hp, 0, remaining);
    this.tail = remaining - 1;
    this.readPos = 0;
    this.maxCapacity = maxCapacity;
    this.size = remaining;
  }

  /**
   * 通过传入字节数组初始化构造函数
   *
   * @param bytes 传入的字节数组
   */
  public SimpleByteData(byte[] bytes) {
    // 先通过指定大小容量构造函数创建对象，再通过写入的方法写入数据
    this(bytes.length);
    this.writeBytes(bytes);
  }


  /**
   * 比较方法
   *
   * @param o the object to be compared.
   */
  @Override
  public int compareTo(AbstractByteData o) {
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
    byte[] hp0 = alloc(targetCap);
    // 判断是否超过末尾循环
    if (tail >= 0 && readPos > tail) {
      // 尾部长度
      int tailLen = hp.length - readPos;
      // 复制第一部分
      System.arraycopy(hp, readPos, hp0, 0, tailLen);
      // 复制第二部分
      System.arraycopy(hp, 0, hp0, tailLen, size - tailLen);
    } else {
      System.arraycopy(hp, readPos, hp0, 0, size);
    }
    readPos = 0;
    hp = hp0;
    tail = size - 1;
  }

  @Override
  public AbstractByteData writeByte(byte b) {
    ensureCapEnough(size + 1);
    this.setByte((tail + 1) % hp.length, b);
    size++;
    tail = (tail + 1) % hp.length;
    return this;
  }

  @Override
  public AbstractByteData writeBytes(byte[] bs) {
    if (bs == null){
      return this;
    }
    ensureCapEnough(size + bs.length);
    appendBytes(bs);
    size += bs.length;
    return this;
  }

  @Override
  public AbstractByteData writeBytes(byte[] bs, int src, int len) {
    if (bs == null){
      return this;
    }
    ensureCapEnough(size + len);
    // TODO: 更改为不使用多一次的拷贝，而直接进行复制
    byte[] tbs = new byte[len];
    System.arraycopy(bs, src, tbs, 0, len);
    appendBytes(tbs);
    size += len;
    return this;
  }

  @Override
  public AbstractByteData writeLong(long l) {
    byte[] bytes = DigestUtil.long2byte(l);
    return writeBytes(bytes);
  }

  @Override
  public long readLong() {
    int remaining = remaining();
    if (remaining < DigestUtil.LONG_OCCUPIES_BYTE_LEN) {
      throw new ByteDataException(
          "Read long type, must the byte len greater or equal than 8, but the byte len is "
              + remaining);
    }
    byte[] bytes = readBytes(DigestUtil.LONG_OCCUPIES_BYTE_LEN);
    return DigestUtil.byte2long(bytes);
  }

  @Override
  public byte readByte() {
    checkReadableByte(1);
    byte b = getByte(readPos);
    size--;
    readPos = (readPos + 1) % hp.length;
    return b;
  }

  @Override
  public void readBytes(byte[] bytes) {
    int readLen = bytes.length;
    if (readLen == 0) {
      return;
    }
    checkReadableByte(readLen);
    doReadBytes(bytes);
    refreshRead(readLen);
  }

  /**
   * 读取指定长度的数据并返回新的字节数组
   *
   * @param len 尧都区的长度
   */
  @Override
  public byte[] readBytes(int len) {
    checkReadableByte(len);
    byte[] allocBytes = alloc(len);
    doReadBytes(allocBytes);
    refreshRead(len);
    return allocBytes;
  }

  /**
   * 读取所有数据并返回新的字节数组
   */
  @Override
  public byte[] readBytes() {
    return readBytes(size);
  }

  @Override
  public int getInt() {
    byte[] bytes = readBytes(4);
    return getInt0(bytes);
  }

  @Override
  public String readString() {
    return new String(readBytes());
  }

  @Override
  public int remaining() {
    return this.size;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
  }

  @Override
  public String toString() {
    byte[] allocBytes = alloc(size);
    doReadBytes(allocBytes);
    return new String(allocBytes);
  }

  /**
   * 确保hp数组容量足够
   *
   * @param size 需要的大小
   */
  private void ensureCapEnough(int size) {
    if (size > maxCapacity) {
      throw new ByteDataException("maximum capacity exceeded");
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
      throw new ByteDataException("Illegal read length");
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
      throw new ByteDataException("Index out of range");
    }
    hp[i] = b;
  }

  private byte getByte(int i) {
    if (i > hp.length - 1) {
      throw new ByteDataException("Index out of size");
    }
    return hp[i];
  }


  /**
   * 执行读入操作
   *
   * @param bytes 要存入的数组
   */
  private void doReadBytes(byte[] bytes) {
    int readLen = bytes.length;
    // 读取的数量从读指针开始如果超过了hp的大小，则需要分两部分读取
    if (readPos + readLen > hp.length) {
      // 复制第一部分
      int firstPartLen = hp.length - readPos;
      System.arraycopy(hp, readPos, bytes, 0, firstPartLen);
      // 复制第二部分
      System.arraycopy(hp, 0, bytes, firstPartLen, readLen - firstPartLen);
    } else {
      // 因为没有被截断，可以直接全量复制
      System.arraycopy(hp, readPos, bytes, 0, readLen);
    }
  }

  /**
   * 追加字节数组
   */
  private void appendBytes(byte[] bytes) {
    if (tail >= readPos) {
      // 如果尾指针在读指针的右边，则有可能会把要追加的数据分为两部分
      if (tail + bytes.length >= hp.length) {
        // 如果超出则需要分两部分进行追加
        int firstPartLen = hp.length - (tail + 1);
        int tail0 = bytes.length - firstPartLen - 1;
        // 填充第一部分的的字节
        fillBytes(tail + 1, bytes, 0, firstPartLen - 1);
        // 填充剩余字节
        fillBytes(0, bytes, firstPartLen, bytes.length - 1);
        tail = tail0;
      } else {
        // 无需截断，直接填充
        fillBytes(tail + 1, bytes, 0, bytes.length - 1);
        tail = tail + bytes.length;
      }
    } else {
      // 无需截断，直接填充
      fillBytes(tail + 1, bytes, 0, bytes.length - 1);
      tail = tail + bytes.length;
    }
  }

  /**
   * 填充字节
   *
   * @param startIndex hp开始的位置（包括）
   * @param src        要填充的字节数组
   * @param from       开始的位置
   * @param to         结束的位置
   */
  private void fillBytes(int startIndex, byte[] src, int from, int to) {
    int len = to - from + 1;
    // 判断开始的位置与填充的长度是否合法
    if (len < 0 || from < 0 || to >= src.length) {
      throw new ByteDataException("Fill failed");
    }
    System.arraycopy(src, from, hp, startIndex, len);
  }

  /**
   * 刷新读取指定长度后的信息
   */
  private void refreshRead(int readLen) {
    readPos = (readPos + readLen) % hp.length;
    size -= readLen;
  }

  private int getInt0(byte[] bytes) {
    int len = bytes.length;
    if (len != Integer.BYTES) {
      throw new ByteDataException("Illegal byte array length");
    }
    return bytes[3] & 0xFF |
        (bytes[2] & 0xFF) << 8 |
        (bytes[1] & 0xFF) << 16 |
        (bytes[0] & 0xFF) << 24;
  }
}
