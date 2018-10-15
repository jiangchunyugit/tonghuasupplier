package cn.thinkfree.database.appvo;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * @author gejiaming
 */
@Data
@AllArgsConstructor
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
}
