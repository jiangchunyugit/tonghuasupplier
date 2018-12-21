package cn.thinkfree.core.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

public class GsonSerializer<T> implements RedisSerializer<T>  {

    private Gson gson;


    private Class<T> clazz;

    public GsonSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
        gson = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
    }

    public GsonSerializer(){
        gson = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
    }


    @Override
    public byte[] serialize(T o) throws SerializationException {
        if(o == null){
            return null;
        }

        return gson.toJson(o).getBytes( Charset.forName("UTF-8"));
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if(bytes == null || bytes.length == 0 ){
            return null;
        }
        return gson.fromJson(new String(bytes, Charset.forName("UTF-8")),clazz);
    }
}
