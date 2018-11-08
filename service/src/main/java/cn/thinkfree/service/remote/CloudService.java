package cn.thinkfree.service.remote;

import cn.thinkfree.database.model.SystemMessage;

import java.util.List;

public interface CloudService {

    /**
     * 项目上线通知
     *
     * @param projectNo 项目编码
     * @param status    项目状态
     * @return
     */
    RemoteResult<String> projectUpOnline(String projectNo, Short status);

    /**
     * 发送短信 激活码
     *
     * @param phone      手机号
     * @param activeCode 激活码
     * @return
     */
    RemoteResult<String> sendSms(String phone, String activeCode);

    /**
     * 发送公告
     *
     * @param systemMessage 公告
     * @param receive       接收人
     * @return
     */
    RemoteResult<String> sendNotice(SystemMessage systemMessage, List<String> receive);

    /**
     * 与上海同步小排期
     * @param status
     * @param limit
     * @return
     */
    String getBaseScheduling(Integer status,Integer limit);

    /**
     * 给用户发消息
     * @param userNo
     * @param projectNo
     * @param content
     * @param senderId
     * @param type
     * @return
     */
    String remindConsumer(String[] userNo, String projectNo,String content,String senderId,Integer dynamicId,Integer type);


}
