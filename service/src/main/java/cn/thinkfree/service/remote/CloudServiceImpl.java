package cn.thinkfree.service.remote;

import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.vo.MarginContractVO;
import cn.thinkfree.database.vo.remote.SyncContractVO;
import cn.thinkfree.database.vo.remote.SyncOrderVO;
import cn.thinkfree.database.vo.remote.SyncTransactionVO;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    @Value("${custom.cloud.fileUpload}")
    private String fileUploadUrl;

    @Value("${custom.cloud.senEmail}")
    String sendEMail;
    @Value("${shanghai.smallSchedulingUrl}")
    String smallSchedulingUrl;
    @Value("${shanghai.priceUrl}")
    String priceUrl;
    @Value("${shanghai.priceUrl}")
    String remindConsumerUrl;

    @Value("${custom.cloud.syncMerchantUrl}")
    String syncMerchantUrl;
    @Value("${custom.cloud.syncContractUrl}")
    String syncContractUrl;
    @Value("${custom.cloud.syncOrderUrl}")
    String syncOrderUrl;
    @Value("${message.messageStatusUrl}")
    String messageStatusUrl;

    @Value("${custom.cloud.sendCreateAccountNotice}")
    String sendCreateAccountNotice;

    Integer SuccessCode = 1000;



    private RemoteResult buildFailResult() {
        RemoteResult remoteResult = new RemoteResult();
        remoteResult.setIsComplete(Boolean.FALSE);
        return remoteResult;
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
//        param.add("phone", phone);
//        param.add("activationCode", activeCode);
        param.add("telephone", phone);
        param.add("code", activeCode);
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

    private String invokeRemoteJuRanMethod(String url, Integer status, Integer limit, String decorateCompany) {
        String result = restTemplate.getForObject(url, String.class, status, limit, decorateCompany);
        return result;
    }

    private String invokeRemoteShangHaiPriceMethod(String url) {
        String result = restTemplate.getForObject(url, String.class);
        return result;
    }

    private String invokeRemoteMessageMethod(String url, MultiValueMap<String, Object> param) {
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
            result = invokeRemoteMethod(syncMerchantUrl, param);
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
        MultiValueMap<String, Object> param = initParam();
        param.add("recipientAddress",email);
        param.add("templateNo",templateCode);
        param.add("tplContent",para);

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
    public String getBaseScheduling(Integer status, Integer limit, String decorateCompany) {
        String result = null;
        try {
            result = invokeRemoteJuRanMethod(smallSchedulingUrl, status, limit, decorateCompany);
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
    public String remindConsumer(String[] userNo, String projectNo, String content, String senderId, Integer dynamicId, Integer type) {
        MultiValueMap<String, Object> param = initParam();
        param.add("userNo", userNo);
        param.add("projectNo", projectNo);
        param.add("content", content);
        param.add("senderId", senderId);
        param.add("dynamicId", dynamicId);
        param.add("type", type);
        String result = null;
        try {
            result = invokeRemoteMessageMethod(remindConsumerUrl, param);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }

    @Override
    public String uploadFile(String fileName) {
//      HttpHeaders headers = new HttpHeaders();
//      headers.add("Authorization", "token");
//      MediaType type = MediaType.parseMediaType("multipart/form-data");
//      headers.setContentType(type);
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
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
    }

    /**
     * 同步合同信息
     *
     * @param syncContractVO
     * @return
     */
    @Override
    public RemoteResult<String> syncContract(SyncContractVO syncContractVO) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        String body = new GsonBuilder().serializeNulls().create().toJson(syncContractVO);
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        RemoteResult<String> result = null;
        try {
            result = invokeRemoteMethodForJson(syncContractUrl, requestEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return buildFailResult();
        }
        return result;
    }

    /**
     * 同步订单信息
     *
     * @param syncOrderVO
     */
    @Override
    public RemoteResult<String> syncOrder(List<SyncOrderVO> syncOrderVO) {
//        MultiValueMap<String, Object> param = initParam();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        Gson gson = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
        String body = gson.toJson(syncOrderVO);
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);

        RemoteResult<String> result = null;
        try {
            result = invokeRemoteMethodForJson(syncOrderUrl, requestEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return buildFailResult();
        }
        return result;

    }

    private RemoteResult<String> invokeRemoteMethodForJson(String url, HttpEntity<String> param) {
        RemoteResult<String> result = restTemplate.postForObject(url, param, RemoteResult.class);
        result.setIsComplete(SuccessCode.equals(result.getCode()) ? Boolean.TRUE : Boolean.FALSE);
        return result;
    }

    @Override
    public RemoteResult<String> marginContractTransaction(MarginContractVO marginContractVO) {

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        String body = new Gson().toJson(marginContractVO);
        System.out.println(body);
        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        RemoteResult<String> result = null;
        try {
            //todo jiangchunyu  更换路径
//            result = invokeRemoteMethodForJson(syncMerchantUrl,requestEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return buildFailResult();
        }
        return result;
    }
/**
     * 获取上海报价信息
     *
     * @param designId
     * @return
     */
    @Override
    public String getShangHaiPriceDetail(String designId) {
        String result = null;
        try {
            result = invokeRemoteShangHaiPriceMethod(priceUrl + designId);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }

    /**
     * 获取徐洋按钮红点信息
     *
     * @param projectNo
     * @param userNo
     * @return
     */
    @Override
    public String getProjectMessageStatus(String projectNo, String userNo) {
        String result = null;
        MultiValueMap<String, Object> param = initParam();
        param.add("userNo", userNo);
        param.add("projectNo", projectNo);
        try {
            result = invokeRemoteMessageMethod(messageStatusUrl, param);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }
    /**
     * 发送账号创建短信
     *
     * @param phone
     * @param para
     * @return
     */
    @Override
    public RemoteResult<String> sendCreateAccountNotice(String phone, String para) {
        MultiValueMap<String, Object> param = initParam();
        param.add("telephone", phone);
        param.add("code", para);
        param.add("status","succeed");

        RemoteResult<String> result = null;
        try {
            result = invokeRemoteMethod(sendCreateAccountNotice, param);
        } catch (Exception e) {
            e.printStackTrace();
            return buildFailResult();
        }
        return result;
    }}
