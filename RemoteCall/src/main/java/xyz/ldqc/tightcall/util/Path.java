package xyz.ldqc.tightcall.util;

/**
 * @author LENOVO
 */
public class Path {

    private static final String CHECK_EXPRESSION = "^[a-zA-Z/]+$";

    private static final String RM_DUP_EXPRESSION = "/+";

    private static final char DELIMITER = '/';

    private String path;

    public Path(String path){
        this.path = "";
        append(path);
    }

    public void append(String path){
        if (!path.isEmpty() && !check(path)){
            throw new RuntimeException("illegal path");
        }
        this.path = fix(this.path + fix(path));
    }

    public String getPath() {
        return path;
    }

    private String fix(String path){
        String p = String.valueOf(path);
        if (path == null || path.isEmpty()){
            return String.valueOf(DELIMITER);
        }
        if (path.length() == 1 && path.charAt(0) == DELIMITER){
            return String.valueOf(DELIMITER);
        }
        char firstChar = p.charAt(0);
        if (firstChar != DELIMITER){
            p = DELIMITER + p;
        }
        p = p.replaceAll(RM_DUP_EXPRESSION, String.valueOf(DELIMITER));
        char lastChar = p.charAt(p.length() - 1);
        if (lastChar == DELIMITER){
            p = p.substring(0, p.length() - 1);
        }
        return p;
    }

    public static boolean check(String path){
        return path.matches(CHECK_EXPRESSION);
    }
}
