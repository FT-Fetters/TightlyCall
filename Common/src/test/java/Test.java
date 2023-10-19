import xyz.ldqc.tightcall.buffer.ByteData;
import xyz.ldqc.tightcall.buffer.SimpleByteData;
import xyz.ldqc.tightcall.exception.ByteDataException;

import java.nio.ByteBuffer;

public class Test {

    @org.junit.Test
    public void testByteBuffet() {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        // 写入数据到ByteBuffer
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);

        // 切换到读模式
        buffer.flip();

        // 读取字节数组
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);

        // 打印字节数组内容
        for (byte b : byteArray) {
            System.out.println(b);
        }
    }

    @org.junit.Test
    public void testByteData() throws ByteDataException {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 写入数据到ByteBuffer
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.put((byte) 4);
        ByteData byteData = new SimpleByteData(buffer);
    }

    @org.junit.Test
    public void testOther() {
        System.out.println("setAllBitsToOne(4) = " + setAllBitsToOne(321));
    }

    public static int setAllBitsToOne(int num) {
        int result = num | (num - 1);
        result |= result >>> 1;
        result |= result >>> 2;
        result |= result >>> 4;
        result |= result >>> 8;
        result |= result >>> 16;
        return result;
    }

    @org.junit.Test
    public void testByteDataWriteSingleByte() {
        // 不根据ByteBuffer进行构造
        ByteData byteData = new SimpleByteData(4);
        byteData.writeByte(((byte) 1))
                .writeByte(((byte) 2))
                .writeByte(((byte) 3));

        System.out.println("byteData.readByte() = " + byteData.readByte());
        System.out.println("byteData.readByte() = " + byteData.readByte());
        byteData.writeByte(((byte) 4))
                .writeByte(((byte) 5))
                .writeByte(((byte) 6))
                .writeByte(((byte) 7));
        System.out.println("byteData.readByte() = " + byteData.readByte());
        System.out.println("byteData.readByte() = " + byteData.readByte());
        System.out.println("byteData.readByte() = " + byteData.readByte());
        System.out.println("byteData.readByte() = " + byteData.readByte());

        System.out.println("byteData.readByte() = " + byteData.readByte());
    }
}
