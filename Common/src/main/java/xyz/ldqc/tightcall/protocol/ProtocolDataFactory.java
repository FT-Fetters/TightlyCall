package xyz.ldqc.tightcall.protocol;


import xyz.ldqc.tightcall.buffer.SimpleByteData;
import xyz.ldqc.tightcall.util.ByteUtil;
import xyz.ldqc.tightcall.util.SnowflakeUtil;

import java.util.Random;

/**
 * 协议数据构造工厂
 *
 * @author Fetters
 */
public class ProtocolDataFactory {

    /**
     * 将要构造的字节数组转化为符合协议的字节数组
     */
    public static byte[] create(byte[] data) {
        int serialNum = (int) SnowflakeUtil.getInstance().nextId();
        return create(data, serialNum);
    }

    /**
     * 将要构造的字节数组转化为符合协议的字节数组，并指定指定的序列号
     */
    public static byte[] create(byte[] data, int serialNum) {
        SimpleByteData byteData = new SimpleByteData();
        // 写入魔数
        byteData.writeBytes(ProtocolConstant.MAGIC_NUMBER);
        // 写入协议版本号
        byteData.writeByte(((byte) 1));
        // 写入请求序列号
        byteData.writeBytes(ByteUtil.int2ByteArray(serialNum));
        // 写入正文长度
        byteData.writeBytes(ByteUtil.int2ByteArray(data.length));
        // 写入正文
        byteData.writeBytes(data);
        return byteData.readBytes();
    }

}
