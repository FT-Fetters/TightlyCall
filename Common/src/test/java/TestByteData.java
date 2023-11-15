import org.junit.Test;
import xyz.ldqc.tightcall.buffer.ByteData;
import xyz.ldqc.tightcall.buffer.SimpleByteData;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.locks.LockSupport;

public class TestByteData {


    @org.junit.Test
    public void testByteBuffet() {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);

        SimpleByteData simpleByteData = new SimpleByteData(buffer, 16);
        System.out.println("simpleByteData.remaining() = " + simpleByteData.remaining());


        System.out.println();
        System.out.println("simpleByteData.remaining() = " + simpleByteData.remaining());

        // 切换到读模式
//        buffer.flip();

        // 读取字节数组
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);

        // 打印字节数组内容
        for (byte b : byteArray) {
            System.out.println(b);
        }
        LockSupport.park();
    }

    @org.junit.Test
    public void testByteData() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 写入数据到ByteBuffer
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.put((byte) 4);
        ByteData byteData = new SimpleByteData(buffer);
        byteData.writeByte((byte) 5);
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

    @org.junit.Test
    public void testByteDataWriteBytes() {
        ByteData byteData = new SimpleByteData(4);

        byte[] bytes1 = new byte[]{1, 2, 3};

        byte[] bytes2 = new byte[]{4, 5};


        byteData.writeBytes(bytes1).writeBytes(bytes2);

        System.out.println(byteData.readByte());
        System.out.println(byteData.readByte());
        System.out.println(byteData.readByte());
        System.out.println(byteData.readByte());
        System.out.println(byteData.readByte());


        byteData.writeBytes(bytes1);

        System.out.println(byteData.readByte());
        System.out.println(byteData.readByte());

        byteData.writeBytes(bytes1).writeBytes(bytes1);

        System.out.println(byteData.readByte());
        System.out.println(byteData.readByte());
        System.out.println(byteData.readByte());

        System.out.println(byteData.readByte());


        byteData.writeBytes(bytes2);
        System.out.println("byteData = " + byteData);
    }

    @Test
    public void testByteDataReadBytes() {
        ByteData byteData = new SimpleByteData(4);

        byte[] bytes = new byte[]{1, 2, 3, 4, 5};

        byteData.writeBytes(bytes);

        byte[] readBytes = new byte[4];

        byteData.readBytes(readBytes);

        System.out.println(Arrays.toString(readBytes));

        byteData.writeBytes(bytes);

        byteData.readBytes(readBytes);

        System.out.println(Arrays.toString(readBytes));

        System.out.println(Arrays.toString(byteData.readBytes(1)));
        System.out.println(Arrays.toString(byteData.readBytes(1)));

    }

    @Test
    public void testByteDataGetInt() {
        ByteData byteData = new SimpleByteData();

        byte[] bytes = new byte[]{0, 0, 1, 120};

        byteData.writeBytes(bytes);

        System.out.println(byteData.getInt());

    }

    @Test
    public void testByteDataToString(){
        String testStr = "我ai你。~";

        byte[] bytes = testStr.getBytes();

        ByteData byteData = new SimpleByteData();

        testStr = "123123dada";

        byteData.writeBytes(bytes);

        bytes = testStr.getBytes();

        byteData.writeBytes(bytes);

        System.out.println(byteData.readString());
    }
}
