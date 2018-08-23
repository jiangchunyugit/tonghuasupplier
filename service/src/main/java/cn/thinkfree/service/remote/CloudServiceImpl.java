package cn.thinkfree.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CloudServiceImpl implements CloudService {

    @Autowired
    RestTemplate restTemplate;
    @Value("${custom.cloud.projectUpOnlineUrl}")
    String projectUpOnlineUrl;
    @Value("${custom.cloud.sendSmsUrl}")
    String sendSmsUrl;

    static Integer SuccessCode = 1000;

    private RemoteResult buildFailResult(){
        RemoteResult remoteResult = new RemoteResult();
        remoteResult.setIsComplete(Boolean.FALSE);
        return remoteResult;
    }

    @Override
    public RemoteResult<String> projectUpOnline(String projectNo, Short status) {

        MultiValueMap<String, Object> param = initParam();
        param.add("projectNo", projectNo);
        param.add("state",status);
        RemoteResult<String> result = null;
        try {
           result = invokeRemoteMethod(projectUpOnlineUrl,param);

        }catch (Exception e){
            e.printStackTrace();
            return buildFailResult();
        }
        return result;
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

        MultiValueMap<String, Object> param = initParam();
        param.add("phone", phone);
        param.add("activationCode",activeCode);
        RemoteResult<String> result = null;
        try {
            result = invokeRemoteMethod(sendSmsUrl,param);

        }catch (Exception e){
            e.printStackTrace();
            return buildFailResult();
        }
        return result;
    }

    private RemoteResult<String> invokeRemoteMethod(String sendSmsUrl, MultiValueMap<String, Object> param) {
        RemoteResult<String> result  = restTemplate.postForObject(sendSmsUrl, param, RemoteResult.class);
        result.setIsComplete(SuccessCode.equals(result.getCode()) ? Boolean.TRUE : Boolean.FALSE);
        return result;
    }

    private MultiValueMap<String, Object> initParam() {

//        HttpHeaders headers = new HttpHeaders();
//        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//        headers.setContentType(type);
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        return param;
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
