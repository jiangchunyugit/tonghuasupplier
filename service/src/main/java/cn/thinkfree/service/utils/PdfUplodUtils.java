package cn.thinkfree.service.utils;

import java.io.File;
import java.io.IOException;

import cn.thinkfree.core.logger.AbsLogPrinter;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class PdfUplodUtils  {

	private static Logger LOGGER = LoggerFactory.getLogger(PdfUplodUtils.class);


    /**
     * 上传pdf返回 pdf url
     * @param filename httpurl
     * @param http
     * @return
     */
    public static String upload(String filename,String httpurl) {
    	
    	String url = "";
    	
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(httpurl);

            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
            httppost.setConfig(requestConfig);

            FileBody bin = new FileBody(new File(filename));
            StringBody comment = new StringBody("This is comment", ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("files", bin).addPart("comment", comment).build();

            httppost.setEntity(reqEntity);

            LOGGER.info("executing request " , httppost.getRequestLine());
            
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
//                	{"code":1000,"msg":"","timestamp":"2018-11-08 10:10:10.828",
//                		"data":{"userId":"","file":["http://47.94.212.199:81/1541643009842.pdf"]},"versionMsg":{"updateState":0,"updateMsg":"","appAddress":""}}
                    String responseEntityStr = EntityUtils.toString(response.getEntity());
                    //json 转换为字符串
                    JSONObject jsStr = JSONObject.parseObject(responseEntityStr);
                    LOGGER.info("Response content length: " + jsStr);
                    JSONArray arr = JSONArray.parseArray(JSONObject.toJSONString(JSONObject.parseObject(jsStr.get("data").toString()).get("file")));

                    url = String.valueOf(arr.get(0));
                    LOGGER.info("Response url: " + url);
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
            }
        }
        return url;
    }


}
