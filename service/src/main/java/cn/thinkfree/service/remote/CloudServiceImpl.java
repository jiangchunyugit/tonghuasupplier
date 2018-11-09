package cn.thinkfree.service.remote;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.thinkfree.database.vo.remote.SyncTransactionVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import cn.thinkfree.database.model.SystemMessage;

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
    @Value("${custom.cloud.fileUpload}")
    private String fileUploadUrl;

    @Value("${custom.cloud.senEmail}")
    String sendEMail;
    @Value("${shanghai.smallSchedulingUrl}")
    String smallSchedulingUrl;
	@Value("${message.remindConsumerUrl}")
    String remindConsumerUrl;

    Integer SuccessCode = 1000;    Integer ProjectUpFailCode = 2005;


    private RemoteResult buildFailResult() {
        RemoteResult remoteResult = new RemoteResult();
        remoteResult.setIsComplete(Boolean.FALSE);
        return remoteResult;
    }

    @Transactional
    @Override
    public RemoteResult<String> projectUpOnline(String projectNo, Short status) {

        MultiValueMap<String, Object> param = initParam();
        param.add("projectNo", projectNo);
        param.add("state", status);


        RemoteResult<String> result = null;

        try {
            result = restTemplate.postForObject(projectUpOnlineUrl, param, RemoteResult.class);
            System.out.println(result);
            result.setIsComplete(SuccessCode.equals(result.getCode()));

        } catch (Exception e) {
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
        param.add("activationCode", activeCode);

        RemoteResult<String> result = null;
        try {
            result = invokeRemoteMethod(sendSmsUrl, param);

        } catch (Exception e) {
            e.printStackTrace();
            return buildFailResult();
        }
        return result;
    }

    private RemoteResult<String> invokeRemoteMethod(String url, MultiValueMap<String, Object> param) {
        RemoteResult<String> result = restTemplate.postForObject(url, param, RemoteResult.class);
        result.setIsComplete(SuccessCode.equals(result.getCode()) ? Boolean.TRUE : Boolean.FALSE);
        return result;
    }

    private String invokeRemoteJuRanMethod(String url, Integer status, Integer limit) {
        String result = restTemplate.getForObject(url, String.class, status, limit);
        return result;
    }

    private String invokeRemoteMessageMethod(String url,MultiValueMap<String, Object> param){
        String result = restTemplate.postForObject(url, param, String.class);
        return result;
    }

    private RemoteResult<String> invokeRemoteMethod(String url, HttpEntity<MultiValueMap> param) {
        RemoteResult<String> result = restTemplate.postForObject(url, param, RemoteResult.class);
        result.setIsComplete(SuccessCode.equals(result.getCode()) ? Boolean.TRUE : Boolean.FALSE);
        return result;
    }

    private MultiValueMap<String, Object> initParam() {
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        return param;
    }

    /**
     * 发送公告
     *
     * @param systemMessage 公告主键
     * @param receive       接收人
     * @return
     */
    @Override
    public RemoteResult<String> sendNotice(SystemMessage systemMessage, List<String> receive) {
        MultiValueMap<String, Object> param = initParam();
        param.add("content", systemMessage.getContent());
        param.add("skipUrl", noticeShowUrl + systemMessage.getId());
        param.add("title", systemMessage.getTitle());
        param.add("companyNo", systemMessage.getCompanyId());
        param.add("senderNo", systemMessage.getSendUserId());
        param.add("userNo", receive);
        RemoteResult<String> result = null;
        try {
            result = invokeRemoteMethod(sendNotice, param);
        } catch (Exception e) {
            e.printStackTrace();
            return buildFailResult();
        }
        return result;
    }

    /**
     * 同步公司信息
     *
     * @param syncTransactionVO
     * @return
     */
    @Override
    public RemoteResult<String> syncTransaction(SyncTransactionVO syncTransactionVO) {
        MultiValueMap<String, Object> param = initParam();

        param.add("address", syncTransactionVO.getAddress());
        param.add("code", syncTransactionVO.getCode());
        param.add("cwgsdm", syncTransactionVO.getCwgsdm());
        param.add("gssh", syncTransactionVO.getGssh());
        param.add("jc", syncTransactionVO.getJc());
        param.add("name", syncTransactionVO.getName());
        param.add("vendorCode", syncTransactionVO.getVendorCode());
        RemoteResult<String> result = null;
        try {
            result = invokeRemoteMethod(sendNotice, param);
        } catch (Exception e) {
            e.printStackTrace();
            return buildFailResult();
        }
        return result;
    }

    /**
     * 发送邮件
     *
     * @param email
     * @param templateCode
     * @param para
     * @return
     */
    @Override
    public RemoteResult<String> sendEmail(String email, String templateCode, String para) {
        MultiValueMap param = initParam();

        RemoteResult<String> result = null;
        try {
            result = invokeRemoteMethod(sendEMail, param);
        } catch (Exception e) {
            e.printStackTrace();
            return buildFailResult();
        }
        return result;
    }


    /**
     * 与上海同步小排期
     *
     * @param status
     * @param limit
     * @return
     */
    @Override
    public String getBaseScheduling(Integer status, Integer limit) {
        String result = null;
        try {
            result = invokeRemoteJuRanMethod(smallSchedulingUrl, status, limit);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }

/**
     * 给用户发消息
     *
     * @param userNo
     * @param projectNo
     * @param content
     * @param senderId
     * @param type
     * @return
     */
    @Override
    public String remindConsumer(String[] userNo, String projectNo, String content, String senderId,Integer dynamicId, Integer type) {
        MultiValueMap<String, Object> param = initParam();
        param.add("userNo", userNo);
        param.add("projectNo",projectNo );
        param.add("content",content );
        param.add("senderId", senderId);
        param.add("dynamicId",dynamicId );
        param.add("type",type );
        String result = null;
        try {
            result = invokeRemoteMessageMethod(remindConsumerUrl, param);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }	@Override
    public String uploadFile(String fileName) {
        // TODO Auto-generated method stub
//      HttpHeaders headers = new HttpHeaders();
//      headers.add("Authorization", "token");
//      MediaType type = MediaType.parseMediaType("multipart/form-data");
//      headers.setContentType(type);
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        List<File> files = new ArrayList<>();
        files.add(file);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("files", null);
//      HttpEntity<MultiValueMap > requestEnullntity = new HttpEntity<>(form);
        RemoteResult<String> result = invokeRemoteMethod(fileUploadUrl, form);
        System.out.println("返回结果。。。" + result);
        file.delete();
        return null;
    }}
