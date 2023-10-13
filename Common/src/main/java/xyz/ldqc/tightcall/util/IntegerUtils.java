package xyz.ldqc.tightcall.util;

public class IntegerUtils {


    /**
     * 将整数的所有bit位都置1
     */
    public static int setAllBitsToOne(int num) {
        int result = num | (num - 1);
        result |= result >>> 1;
        result |= result >>> 2;
        result |= result >>> 4;
        result |= result >>> 8;
        result |= result >>> 16;
        return result;
    }
}
