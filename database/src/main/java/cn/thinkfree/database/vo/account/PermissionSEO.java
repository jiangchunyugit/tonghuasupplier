package cn.thinkfree.database.vo.account;

import cn.thinkfree.database.vo.AbsPageSearchCriteria;

public class PermissionSEO extends AbsPageSearchCriteria {

    /**
     * 权限名称或者编号
     */
    private String name;
    /**
     * 状态
     */
    private String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
