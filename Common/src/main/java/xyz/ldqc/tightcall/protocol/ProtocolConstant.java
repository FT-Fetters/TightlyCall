package xyz.ldqc.tightcall.protocol;

import java.nio.charset.StandardCharsets;

/**
 * @author Fetters
 *
 * 该类是协议常量类，定义的是TighltyCall中协议层的协议内容<br/>
 * 84 95 67 65 76 76 'T_CALL' 魔数 用来标识该协议，占6个字节<br/>
 * 01 版本号，占1个字节<br/>
 * 123 124 125 请求序号，占3个字节<br/>
 * 123 123 123 123 请求长度，占4个字节<br/>
 * 111 111 111 222 222 222 222 正文内容，字节数与请求长度相同
 */
public class ProtocolConstant {

    /**
     * 协议-魔数
     */
    public static final byte[] MAGIC_NUMBER = "T_CALL".getBytes(StandardCharsets.UTF_8);

    /**
     * 协议请求头的长度<br/>
     * 魔数6 版本号1 请求序号3 请求长度4
     */
    public static final int PROTOCOL_HEAD_LEN = 6 + 1 + 3 + 4;


}
