package cn.thinkfree.service.cache;

import cn.thinkfree.database.event.SendValidateCode;
import cn.thinkfree.service.event.EventService;
import cn.thinkfree.service.utils.AccountHelper;
import cn.thinkfree.service.utils.ActivationCodeHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    EventService eventService;

    /**
     * 验证是否存在
     *
     * @param key
     * @param val
     * @return
     */
    @Override
    public String validate(String key, String val) {
        String value = redisTemplate.opsForValue().get(key);
        if(StringUtils.equals(val,value)){
            return "验证成功!";
        }
        return "验证失败!";
    }

    /**
     * 存储验证码
     * @param key
     * @return
     */
    @Override
    public String saveVerificationCode(String key) {
        String code = ActivationCodeHelper.ActivationCode.Mix.code.get();
        redisTemplate.opsForValue().set(key,code,30, TimeUnit.MINUTES);

        SendValidateCode event = new SendValidateCode(key,key,code);
        eventService.publish(event);
        return code;
    }
}
