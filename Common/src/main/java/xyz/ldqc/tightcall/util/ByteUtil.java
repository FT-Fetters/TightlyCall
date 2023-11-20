package xyz.ldqc.tightcall.util;

import com.alibaba.fastjson2.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author Fetters
 */
public class ByteUtil {

    /**
     * 对比两个字节数组
     */
    public static boolean compareByteArray(byte[] b1, byte[] b2){
        if (b1.length != b2.length){
            return false;
        }
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i])
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 将对象转化为字节数组
     */
    public static byte[] obj2ByteArray(Object obj){
        if (obj instanceof String){
            return ((String) obj).getBytes();
        }
        if (obj instanceof Serializable){
            try {
                // 创建字节数组输出流
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                // 创建对象输出流
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                // 将对象写入对象输出流
                oos.writeObject(obj);
                oos.flush();
                // 获取字节数组
                byte[] bytes = bos.toByteArray();
                // 关闭流
                oos.close();
                bos.close();
                return bytes;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        JSONObject objJson = JSONObject.from(obj);
        return objJson.toJSONString().getBytes();
    }

    public static byte[] int2ByteArray(int num){
        // 创建字节数组
        byte[] byteArray = new byte[4];
        // 将整数转换为字节数组
        byteArray[0] = (byte) (num >> 24);
        byteArray[1] = (byte) (num >> 16);
        byteArray[2] = (byte) (num >> 8);
        byteArray[3] = (byte) num;
        return byteArray;
    }
}
