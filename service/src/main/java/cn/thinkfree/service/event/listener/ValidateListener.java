package cn.thinkfree.service.event.listener;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.database.event.SendValidateCode;
import cn.thinkfree.service.event.EventService;
import cn.thinkfree.service.remote.CloudService;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码相关
 */
@Component
public class ValidateListener extends AbsLogPrinter {

    @Autowired
    CloudService cloudService;
    /**
     * 发送验证码
     * @param sendValidateCode
     */
//    @TransactionalEventListener()
//    @Async
    @EventListener
    public void sendValidateCode(SendValidateCode sendValidateCode){
        if(sendValidateCode.getIsPhone()){
            cloudService.sendSms(sendValidateCode.getTarget(),sendValidateCode.getCode());
        }else {
            Map<String,Object> para = new HashMap<>();
            para.put("code",sendValidateCode.getCode());
            cloudService.sendEmail(sendValidateCode.getTarget(), SysConstants.EmailTemplate.register.code,
                    new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create().toJson(para));
        }

        return;
    }

}
