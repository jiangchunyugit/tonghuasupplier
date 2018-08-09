package cn.thinkfree.core.convert;


import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.bundle.MyRequBundle;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.core.utils.LogUtil;
import cn.thinkfree.core.utils.VersionUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * 消息转换类
 * 统一出口入口
 *
 * AbstractGenericHttpMessageConverter
 * AbstractHttpMessageConverter
 */
public class MySpringConvert extends AbstractGenericHttpMessageConverter<Object> implements HttpMessageConverter<Object> {

    MyLogger logger = LogUtil.getLogger(getClass());

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
       return  clazz.isAssignableFrom(MyRequBundle.class);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return clazz.isAssignableFrom(MyRespBundle.class);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.APPLICATION_JSON);
    }


    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    public Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return readMap(inputMessage);
    }

    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return   hasAct(type)? readMap((Class)((ParameterizedType) type).getActualTypeArguments()[0],inputMessage):readMap(inputMessage);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        logger.debug("出口数据转换{}:{}",String.valueOf(Instant.now().toEpochMilli()),o);
        MyRespBundle myRespBundle = makeComplete((MyRespBundle) o);
        String result = JSONUtil.bean2JsonStr(myRespBundle);
        logger.debug("出口数据转换结束{}:{},",String.valueOf(Instant.now().toEpochMilli()),result);
        httpOutputMessage.getBody().write(result.getBytes());
    }

    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        writeInternal(o,httpOutputMessage);
    }

    private MyRespBundle makeComplete(MyRespBundle obj) {
        obj.setVersion(VersionUtil.getVersion());
        obj.setTimestamp(Instant.now().toEpochMilli());
        return obj;
    }

    private Object readMap(HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        MyRequBundle requestData = initBundle(inputMessage);

        logger.debug("入口数据转换结束:{},时间戳:{}",requestData,String.valueOf(Instant.now().toEpochMilli()));
        return requestData;
    }

    private Object readMap(Class clz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        MyRequBundle requestData = initBundle(inputMessage);
        requestData.setModel(JSONUtil.BeanParser(requestData.getModel(), clz));

        logger.debug("入口数据转换结束:{},时间戳:{}",requestData,String.valueOf(Instant.now().toEpochMilli()));
        return requestData;
    }

    private MyRequBundle initBundle(HttpInputMessage inputMessage) throws IOException {
        logger.info("入口数据转换,时间戳:{}", Instant.now().toEpochMilli());
        InputStream inputStream = inputMessage.getBody();
        HttpHeaders header = inputMessage.getHeaders();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        MyRequBundle requestData = JSONUtil.reader2Bean(reader,  MyRequBundle.class);
        return requestData;
    }





    /**
     * 是否存在泛型
     * @param type
     * @return
     */
    private boolean hasAct(Type type){
        return (type instanceof ParameterizedType);
    }
}
