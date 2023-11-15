package xyz.ldqc.tightcall.util;

import xyz.ldqc.tightcall.exception.DigestException;

/**
 * @author Fetters
 */
public class DigestUtil {

    private static final int INT_OCCUPIES_BYTE_LEN = 4;

    public static int byte2int(byte[] bytes) {
        int len = bytes.length;
        if (len != INT_OCCUPIES_BYTE_LEN) {
            try {
                throw new DigestException("Illegal byte array length");
            } catch (DigestException e) {
                throw new RuntimeException(e);
            }
        }
        return bytes[3] & 0xFF |
                (bytes[2] & 0xFF) << 8 |
                (bytes[1] & 0xFF) << 16 |
                (bytes[0] & 0xFF) << 24;
    }
}
