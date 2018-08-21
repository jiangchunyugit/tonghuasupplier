package cn.thinkfree.service.remote;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CloudServiceImpl implements CloudService {

    @Autowired
    RestTemplate restTemplate;


    @Override
    public RemoteResult<String> projectUpOnline(String projectNo, Short status) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("projectNo", projectNo);
        param.add("state",status);
        ResponseEntity<RemoteResult> rs = restTemplate.postForEntity("http://10.240.10.93:8789/project/status", param, RemoteResult.class);
        System.out.println(rs);
        if(rs.getStatusCode().value() == 1000){

        }else{

        }
        return null;
    }

    /**
     * 发送短信 激活码
     *
     * @param phone
     * @param activeCode
     * @return
     */
    @Override
    public RemoteResult<String> sendSms(String phone, String activeCode) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("phone", phone);
        param.add("activationCode",activeCode);
        ResponseEntity<RemoteResult> rs = restTemplate.postForEntity("http://10.240.10.78:5000/userapi/userRegister/smsAndSaveCode", param, RemoteResult.class);
        System.out.println(rs);
        return null;
    }

    /**
     * 发送公告
     * TODO 等待前端定制业务后再做
     * @param noticeNo 公告主键
     * @param receive  接收人
     * @return
     */
    @Override
    public RemoteResult<String> sendNotice(String noticeNo, List<String> receive) {
        return null;
    }
}
