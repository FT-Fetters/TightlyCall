package xyz.ldqc.tightcall.util;

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
}
