package cn.thinkfree.database.vo;

import lombok.Data;

/**
 * 用户信息
 *
 * @author song
 * @version 1.0
 * @date 2018/11/1 18:19
 */
@Data
public class AfUserDTO {
    /**
     * 用户编号
     */
    private String userId;
    /**
     * 用户姓名
     */
    private String username;
    /**
     * 用户头像
     */
    private String headPortrait;
    /**
     * 用户手机号
     */
    private String phone;

}
