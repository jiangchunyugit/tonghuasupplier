package cn.thinkfree.database.vo.account;

import cn.thinkfree.database.vo.AbsPageSearchCriteria;

public class SystemRoleSEO extends AbsPageSearchCriteria {

    private String name;
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
