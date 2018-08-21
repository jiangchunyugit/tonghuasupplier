package cn.thinkfree.service.remote;

import java.util.List;

public interface CloudService {

    /**
     * 项目上线通知
     * @param projectNo 项目编码
     * @param status   项目状态
     * @return
     */
      RemoteResult<String> projectUpOnline(String projectNo, Short status);

    /**
     * 发送短信 激活码
     * @param phone   手机号
     * @param activeCode  激活码
     * @return
     */
      RemoteResult<String> sendSms(String phone,String activeCode);

    /**
     * 发送公告
     * @param noticeNo 公告主键
     * @param receive  接收人
     * @return
     */
      RemoteResult<String> sendNotice(String noticeNo, List<String> receive);
}
