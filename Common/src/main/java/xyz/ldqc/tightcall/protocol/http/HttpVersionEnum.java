package xyz.ldqc.tightcall.protocol.http;

/**
 * @author Fetters
 */
public enum HttpVersionEnum {

    /**
     * HTTP/0.9
     */
    HTTP_0_9("HTTP/0.9"),
    /**
     * HTTP/1.0
     */
    HTTP_1_0("HTTP/1.0"),
    /**
     * HTTP/1.1
     */
    HTTP_1_1("HTTP/1.1");

    private final String value;

    HttpVersionEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
