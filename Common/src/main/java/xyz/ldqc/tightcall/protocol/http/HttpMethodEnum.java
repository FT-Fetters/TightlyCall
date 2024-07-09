package xyz.ldqc.tightcall.protocol.http;

/**
 * @author Fetters
 */

public enum HttpMethodEnum {
    /**
     * GET
     */
    GET("GET"),
    /**
     * POST
     */
    POST("POST"),
    /**
     * PUT
     */
    PUT("PUT"),
    /**
     * DELETE
     */
    DELETE("DELETE"),
    /**
     * HEAD
     */
    HEAD("HEAD"),
    /**
     * OPTIONS
     */
    OPTIONS("OPTIONS"),
    /**
     * TRACE
     */
    TRACE("TRACE"),
    /**
     * CONNECT
     */
    CONNECT("CONNECT");

    final String value;
    HttpMethodEnum(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }

    public static HttpMethodEnum parse(String value){
        return HttpMethodEnum.valueOf(value);
    }

}
