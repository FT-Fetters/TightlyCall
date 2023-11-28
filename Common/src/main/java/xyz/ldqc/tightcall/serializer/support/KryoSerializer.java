package xyz.ldqc.tightcall.serializer.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import xyz.ldqc.tightcall.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Kryo序列化器
 * @author Fetters
 */
public class KryoSerializer implements Serializer {

    private static final ThreadLocal<Kryo> KRYO_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);//检测循环依赖，默认值为true,避免版本变化显式设置
        kryo.setRegistrationRequired(false);//默认值为true，避免版本变化显式设置
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());//设定默认的实例化器
        return kryo;
    });

    private KryoSerializer(){

    }

    private static KryoSerializer serializer;

    public static synchronized KryoSerializer serializer(){
        if (serializer == null){
            serializer = new KryoSerializer();
        }
        return serializer;
    }

    @Override
    public byte[] serialize(Object o) {
        Kryo kryo = getKryo();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeClassAndObject(output, o);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }



    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] bytes) {
        Kryo kryo = getKryo();
        Input input = new Input(new ByteArrayInputStream(bytes));
        return (T) kryo.readClassAndObject(input);
    }

    private Kryo getKryo() {
        return KRYO_LOCAL.get();
    }
}
