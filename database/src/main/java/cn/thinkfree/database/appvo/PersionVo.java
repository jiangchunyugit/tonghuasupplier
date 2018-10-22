package cn.thinkfree.database.appvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gejiaming
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersionVo {
    /**
     * 电话
     */
    private String phone;
    /**
     * 姓名
     */
    private String name;
    /**
     * 是否展示
     */
    private Boolean display;
    /**
     * 角色
     */
    private String role;
    /**
     * 个人主页地址
     */
    private String personalHomeUrl;
}
