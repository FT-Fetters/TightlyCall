package xyz.ldqc.tightcall.protocol.http;

/**
 * 响应体头部键枚举
 * @author Fetters
 */

public enum ResponseHeaderEnum {

    /**
     * 内容长度
     */
    CONTENT_LENGTH("Content-Length"),
    /**
     * 内容类型
     */
    CONTENT_TYPE("Content-Type"),
    /**
     * 内容分发
     */
    CONTENT_DISPOSITION("Content-Disposition");


    private final String key;

    ResponseHeaderEnum(String key) {
        this.key = key;
    }

    public String getKey(){
        return this.key.toUpperCase();
    }

    @Override
    public String toString() {
        return this.key;
    }
}
