package xyz.ldqc.tightcall.util;

/**
 * @author Fetters
 */
public class StringUtil {

    public static boolean isBlank(CharSequence charSequence){
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isNotBlank(CharSequence charSequence){
        return charSequence != null && charSequence.length() != 0;
    }

    public static boolean isAnyBlank(CharSequence... charSequences){
        for (CharSequence charSequence : charSequences) {
            if (isBlank(charSequence)) {
                return true;
            }
        }
        return false;
    }
}
