package cn.tonghua.service.remote;

import cn.tonghua.core.logger.AbsLogPrinter;
import cn.tonghua.database.model.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class CloudServiceImpl extends AbsLogPrinter implements CloudService {

    @Autowired
    RestTemplate restTemplate;

    Integer SuccessCode = 1000;
    /**
     * 发送命令
     * @return
     */
    @Override
    public RemoteResult<String> sendCommand(String command, Equipment equipment) {
        MultiValueMap<String, Object> param = initParam();
        param.add("command", command);

        RemoteResult<String> result = null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://");
        stringBuilder.append(equipment.getIp());
        stringBuilder.append(":");
        stringBuilder.append(equipment.getPort());
        stringBuilder.append("/command");
        try {
            result = invokeRemoteMethod(stringBuilder.toString(), param);
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
    private RemoteResult buildFailResult() {
        RemoteResult remoteResult = new RemoteResult();
        remoteResult.setIsComplete(Boolean.FALSE);
        return remoteResult;
    }
    private MultiValueMap<String, Object> initParam() {
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        return param;
    }
}
