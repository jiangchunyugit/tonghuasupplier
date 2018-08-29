package cn.thinkfree.core.config;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){

        RestTemplate restTemplate = new RestTemplate(factory);
        List<HttpMessageConverter<?>> mcs = restTemplate.getMessageConverters();

        Iterator<HttpMessageConverter<?>> it = mcs.iterator();
        while(it.hasNext()){
            HttpMessageConverter<?> hmc = it.next();
            if(hmc instanceof MappingJackson2HttpMessageConverter){
                it.remove();
            }
        }

//        Gson gson = new GsonBuilder()
//                .serializeNulls()
//                .enableComplexMapKeySerialization()
//                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
//                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//                        return new Date(json.getAsJsonPrimitive().getAsLong());
//                    }
//                })
//                .create();
        mcs.add(mcs.size(),new GsonHttpMessageConverter());
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(10000);//单位为ms
        factory.setConnectTimeout(10000);//单位为ms
        factory.setConnectionRequestTimeout(10000);
        return factory;
    }
}
