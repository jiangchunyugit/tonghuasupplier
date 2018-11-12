package cn.thinkfree.service.event.listener;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.event.account.AccountCreate;
import cn.thinkfree.database.event.account.ResetPassWord;
import cn.thinkfree.database.model.UserLoginLog;
import cn.thinkfree.database.model.UserRegister;
import cn.thinkfree.service.remote.CloudService;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 账号相关事件
 */
@Component
public class AccountListener extends AbsLogPrinter {

    @Autowired
    CloudService cloudService;

    @Value("${custom.cloud.userLoginUrl}")
    private String UserLoginUrl;

    /**
     * 账号创建后事件
     * 发送邮件
     * @param accountCreate
     */
//    @TransactionalEventListener
//    @Async
    @EventListener
    public void accountCreateAfter(AccountCreate accountCreate){
        Map<String,Object> para = new HashMap<>();
        para.put("userName",accountCreate.getUserName());
        para.put("pwd",accountCreate.getPassword());
        para.put("http", UserLoginUrl);
        if(accountCreate.isPhone()){
            // TODO 运营平台开通后需要短信提醒
//            cloudService.sendSms()
        }else{
            cloudService.sendEmail(accountCreate.getEmail(),
                    SysConstants.EmailTemplate.join.code,
                    new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create().toJson(para));
        }

    }

    /**
     * 重置密码之后
     * @param resetPassWord
     */
//    @TransactionalEventListener
//    @Async
    @EventListener
    public void resetPassWordAfter(ResetPassWord resetPassWord){
        Map<String,Object> para = new HashMap<>();

        para.put("pwd",resetPassWord.getPassword());

        cloudService.sendEmail(resetPassWord.getEmail(),
                SysConstants.EmailTemplate.resetPwd.code,
                new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create().toJson(para));
        return;
    }

}
