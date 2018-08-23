package cn.thinkfree.database.vo;

import cn.thinkfree.core.model.BaseModel;


public class StaffsVO extends BaseModel {



    /**
     * 用户主键
     */
    private String userId;
    /**
     * 手机号
     */
    private String phone;

    private String roleId;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
