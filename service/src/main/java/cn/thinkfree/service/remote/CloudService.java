package cn.thinkfree.service.remote;

import java.util.List;

import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.vo.remote.SyncContractVO;
import cn.thinkfree.database.vo.remote.SyncTransactionVO;

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
       * 合同上传
       *  @param filepath
       */
      public String uploadFile(String fileName);

    /**
     * 同步公司信息
     * @param syncTransactionVO
     * @return
     */
      RemoteResult<String> syncTransaction(SyncTransactionVO syncTransactionVO);


    /**
     * 发送邮件
     * @param email
     * @param templateCode
     * @param para
     * @return
     */
      RemoteResult<String> sendEmail(String email,String templateCode,String para);
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


    /**
     * 同步合同信息
     * @param syncContractVO
     * @return
     */
    RemoteResult<String> syncContract(SyncContractVO syncContractVO);
}
