package cn.thinkfree.service.utils;


import com.alibaba.fastjson.JSONObject;
import jdk.internal.util.xml.impl.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


/**
 * @author xusonghui
 * 
 * httpServletRequest工具类
 */
public class HttpUtils {

    /**
     * 未知
     */
    private static final String UNKNOWN = "unknown";
    /**
     * ip长度
     */
    private static final int IP_MIN_LENGTH = 15;
    
    /**
     * 尝试获取当前请求的HttpServletRequest实例
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 尝试获取当前请求的HttpServletRequest实例
     *
     * @return HttpServletRequest
     */
    public static Map<String,String[]> getHttpParams() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            return request.getParameterMap();
        } catch (Exception e) {
            return null;
        }
    }


    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public static String getHeaders(String key) {
        if(key == null){
            return "";
        }
        HttpServletRequest request = getHttpServletRequest();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            if(key.equals(enumeration.nextElement())){
               return request.getHeader(key);
            }
        }
        return null;
    }

    /**
     * 获取请求客户端的真实ip地址
     *
     * @param request 请求对象
     * @return ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {

        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > IP_MIN_LENGTH) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!(UNKNOWN.equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取请求客户端的真实ip地址
     *
     * @param
     * @return ip地址
     */
    public static String getIpAddress() {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        return getIpAddress(getHttpServletRequest());
    }


    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static final String CHARSET_UTF8 = "UTF-8";

    public static final String METHOD_POST = "POST";

//    public static void main(String[] args) {
//        Map<String,String> params = new HashMap<>();
//        params.put("username","asdfsadf");
//        params.put("password","fadfasdf");
//        HttpRespMsg httpRespMsg = post("http://10.240.10.53:7181/login",params);
//        System.out.println(JSONObject.toJSONString(httpRespMsg));
//    }
    /**
     * 发送post请求
     * @param requestUrl 请求链接
     * @param requestMap 请求参数
     * @return
     */
    public static HttpRespMsg post(String requestUrl, Map<String,String> requestMap) {
        String requestMsg = mapToParams(requestMap);
        return send(requestUrl, METHOD_POST, CHARSET_UTF8, requestMsg);
    }

    /**
     * map转换为key-value格式
     * @param requestMap
     * @return
     */
    public static String mapToParams(Map<String,String> requestMap){
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String,String> entry : requestMap.entrySet()){
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return sb.toString();
    }
    /**
     * 发送post请求
     * @param requestUrl 请求链接
     * @param requestMsg 请求参数 格式 userId=111&projectNo=1111
     * @return
     */
    public static HttpRespMsg post(String requestUrl, String requestMsg) {
        return send(requestUrl, METHOD_POST, CHARSET_UTF8, requestMsg);
    }

    /**
     * 发送post请求
     * @param requestUrl 请求链接
     * @param requestMsg 请求参数 格式 userId=111&projectNo=1111
     * @return
     */
    public static HttpRespMsg postJson(String requestUrl, String requestMsg) {
        return send(requestUrl, METHOD_POST, CHARSET_UTF8, requestMsg,"application/json");
    }
    /**
     * 发送请求
     * @param requestUrl
     * @param method
     * @param charset
     * @param requestMsg
     * @return
     */
    public static HttpRespMsg send(String requestUrl, String method, String charset, String requestMsg) {
        return send(requestUrl,method,charset,requestMsg,"application/x-www-form-urlencoded");
    }

    /**
     * 发送请求
     * @param requestUrl
     * @param method
     * @param charset
     * @param requestMsg
     * @return
     */
    public static HttpRespMsg send(String requestUrl, String method, String charset, String requestMsg,String contentType) {
        int responseCode = -1;
        List<String> header = new ArrayList<String>();
        String errorMessage = null;
        String content = null;
        HttpURLConnection httpConn = null;
        try {
            URL url = new URL(requestUrl);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod(method);
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestProperty("Content-Type",contentType);
            //设置超时时间
            httpConn.setConnectTimeout(2000);
            httpConn.setReadTimeout(2000);
            httpConn.connect();
            OutputStreamWriter out = new OutputStreamWriter(httpConn.getOutputStream(), charset);
            out.write(requestMsg);
            out.flush();
            out.close();
            //响应编码
            responseCode = httpConn.getResponseCode();
            //报文头
            header.add(httpConn.getHeaderField(0));
            for(int i = 1; ; i ++) {
                if(httpConn.getHeaderField(i) == null || httpConn.getHeaderFieldKey(i) == null){
                    break;
                }
                header.add(httpConn.getHeaderFieldKey(i) + ": " + httpConn.getHeaderField(i));
            }
            InputStream inputStream = null;
            if(responseCode == 200){
                inputStream = httpConn.getInputStream();
            }else{
                inputStream = httpConn.getErrorStream();
            }
            //响应报文
            Reader reader = new InputStreamReader(inputStream, charset);
            StringBuilder contentSb = new StringBuilder();
            int c;
            while((c = reader.read()) != -1) {
                contentSb.append((char)c);
            }
            content = contentSb.toString();
        } catch (Exception e) {
            logger.error(null, e);
        } finally {
            try {
                httpConn.disconnect();
            } catch (Exception e) {}
        }

        return new HttpRespMsg(responseCode, header, content, errorMessage);
    }

    public static class HttpRespMsg {
        private int responseCode;
        private List<String> headerList;
        private String content;
        private String errorMessage;

        public HttpRespMsg(int responseCode, List<String> headerList, String content, String errorMessage) {
            this.responseCode = responseCode;
            this.headerList = headerList;
            this.content = content;
            this.errorMessage = errorMessage;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public List<String> getHeader() {
            return headerList;
        }

        public String getContent() {
            return content;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

    }
}
