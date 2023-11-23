package xyz.ldqc.tightcall.registry.serializer;

/**
 * @author Fetters
 */
public interface Serializer {

    /**
     * 序列化
     * @param o 需要序列化的对象
     * @return 序列化后的字节数组
     */
    byte[] serialize(Object o);


    /**
     * 反序列化
     * @param bytes 已序列化后的字节数组
     * @return 反序列化后的对象
     */
    <T> T deserialize(byte[] bytes);

}
