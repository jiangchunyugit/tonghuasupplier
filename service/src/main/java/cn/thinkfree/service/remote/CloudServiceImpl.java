package cn.thinkfree.service.remote;

import cn.thinkfree.database.model.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Value("${custom.cloud.sendNotice}")
    String sendNotice;

    @Value("${custom.cloud.noticeShowUrl}")
    String noticeShowUrl;

    Integer SuccessCode = 1000;
    Integer ProjectUpFailCode = 2005;



    private RemoteResult buildFailResult(){
        RemoteResult remoteResult = new RemoteResult();
        remoteResult.setIsComplete(Boolean.FALSE);
        return remoteResult;
    }

    @Transactional
    @Override
    public RemoteResult<String> projectUpOnline(String projectNo, Short status) {

        MultiValueMap<String, Object> param = initParam();
        param.add("projectNo", projectNo);
        param.add("state",status);


        RemoteResult<String> result = null;

        try {
            result = restTemplate.postForObject(projectUpOnlineUrl, param, RemoteResult.class);
            System.out.println(result);
            result.setIsComplete(SuccessCode.equals(result.getCode()));

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

    private RemoteResult<String> invokeRemoteMethod(String url, MultiValueMap<String, Object> param) {
        RemoteResult<String> result  = restTemplate.postForObject(url, param, RemoteResult.class);
        result.setIsComplete(SuccessCode.equals(result.getCode()) ? Boolean.TRUE : Boolean.FALSE);
        return result;
    }

    private RemoteResult<String> invokeRemoteMethod(String url, HttpEntity<MultiValueMap> param) {
        RemoteResult<String> result  = restTemplate.postForObject(url, param, RemoteResult.class);
        result.setIsComplete(SuccessCode.equals(result.getCode()) ? Boolean.TRUE : Boolean.FALSE);
        return result;
    }

    private MultiValueMap<String, Object> initParam() {
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        return param;
    }

    /**
     * 发送公告
     * @param systemMessage 公告主键
     * @param receive  接收人
     * @return
     */
    @Override
    public RemoteResult<String> sendNotice(SystemMessage systemMessage, List<String> receive) {
        MultiValueMap<String, Object> param = initParam();
        param.add("content", systemMessage.getContent());
        param.add("skipUrl",noticeShowUrl+systemMessage.getId());
        param.add("title",systemMessage.getTitle());
        param.add("companyNo",systemMessage.getCompanyId());
        param.add("senderNo",systemMessage.getSendUserId());
        param.add("userNo",receive);
        RemoteResult<String> result = null;
        try {
            result = invokeRemoteMethod(sendNotice,param);
        }catch (Exception e){
            e.printStackTrace();
            return buildFailResult();
        }
        return result;
    }


}
