package cn.thinkfree.service.cache;

public interface RedisService {
    /**
     * 验证是否存在
     * @param key
     * @param val
     * @return
     */
    String validate(String key, String val);

    /**
     * 发送验证码
     * @param key
     * @return
     */
    String saveVerificationCode(String key);


}
