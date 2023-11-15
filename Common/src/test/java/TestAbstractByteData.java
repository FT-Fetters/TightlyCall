import org.junit.Test;
import xyz.ldqc.tightcall.buffer.AbstractByteData;
import xyz.ldqc.tightcall.buffer.SimpleByteData;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.locks.LockSupport;

public class TestAbstractByteData {


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
        AbstractByteData abstractByteData = new SimpleByteData(buffer);
        abstractByteData.writeByte((byte) 5);
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
        AbstractByteData abstractByteData = new SimpleByteData(4);
        abstractByteData.writeByte(((byte) 1))
                .writeByte(((byte) 2))
                .writeByte(((byte) 3));

        System.out.println("byteData.readByte() = " + abstractByteData.readByte());
        System.out.println("byteData.readByte() = " + abstractByteData.readByte());
        abstractByteData.writeByte(((byte) 4))
                .writeByte(((byte) 5))
                .writeByte(((byte) 6))
                .writeByte(((byte) 7));
        System.out.println("byteData.readByte() = " + abstractByteData.readByte());
        System.out.println("byteData.readByte() = " + abstractByteData.readByte());
        System.out.println("byteData.readByte() = " + abstractByteData.readByte());
        System.out.println("byteData.readByte() = " + abstractByteData.readByte());

        System.out.println("byteData.readByte() = " + abstractByteData.readByte());
    }

    @org.junit.Test
    public void testByteDataWriteBytes() {
        AbstractByteData abstractByteData = new SimpleByteData(4);

        byte[] bytes1 = new byte[]{1, 2, 3};

        byte[] bytes2 = new byte[]{4, 5};


        abstractByteData.writeBytes(bytes1).writeBytes(bytes2);

        System.out.println(abstractByteData.readByte());
        System.out.println(abstractByteData.readByte());
        System.out.println(abstractByteData.readByte());
        System.out.println(abstractByteData.readByte());
        System.out.println(abstractByteData.readByte());


        abstractByteData.writeBytes(bytes1);

        System.out.println(abstractByteData.readByte());
        System.out.println(abstractByteData.readByte());

        abstractByteData.writeBytes(bytes1).writeBytes(bytes1);

        System.out.println(abstractByteData.readByte());
        System.out.println(abstractByteData.readByte());
        System.out.println(abstractByteData.readByte());

        System.out.println(abstractByteData.readByte());


        abstractByteData.writeBytes(bytes2);
        System.out.println("byteData = " + abstractByteData);
    }

    @Test
    public void testByteDataReadBytes() {
        AbstractByteData abstractByteData = new SimpleByteData(4);

        byte[] bytes = new byte[]{1, 2, 3, 4, 5};

        abstractByteData.writeBytes(bytes);

        byte[] readBytes = new byte[4];

        abstractByteData.readBytes(readBytes);

        System.out.println(Arrays.toString(readBytes));

        abstractByteData.writeBytes(bytes);

        abstractByteData.readBytes(readBytes);

        System.out.println(Arrays.toString(readBytes));

        System.out.println(Arrays.toString(abstractByteData.readBytes(1)));
        System.out.println(Arrays.toString(abstractByteData.readBytes(1)));

    }

    @Test
    public void testByteDataGetInt() {
        AbstractByteData abstractByteData = new SimpleByteData();

        byte[] bytes = new byte[]{0, 0, 1, 120};

        abstractByteData.writeBytes(bytes);

        System.out.println(abstractByteData.getInt());

    }

    @Test
    public void testByteDataToString(){
        String testStr = "我ai你。~";

        byte[] bytes = testStr.getBytes();

        AbstractByteData abstractByteData = new SimpleByteData();

        testStr = "123123dada";

        abstractByteData.writeBytes(bytes);

        bytes = testStr.getBytes();

        abstractByteData.writeBytes(bytes);

        System.out.println(abstractByteData.readString());
    }
}
