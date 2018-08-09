package cn.thinkfree.core.bundle;

import java.util.Map;

/**
 * 自定义入参请求
 */
public class MyRequBundle<T> {

    /**
     * 版本号
     */
    private String version;
    /**
     * 消息头
     */
    private Map<String,Object> headers;
    /**
     * 数据集
     */
    private Map<String,Object> datas;
    /**
     * 对象数据
     */
    private T model;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, Object> datas) {
        this.datas = datas;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "MyRequBundle{" +
                "version='" + version + '\'' +
                ", headers=" + headers +
                ", datas=" + datas +
                ", model=" + model +
                '}';
    }
}
