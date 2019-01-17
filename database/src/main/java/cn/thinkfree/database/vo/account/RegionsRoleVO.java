package cn.thinkfree.database.vo.account;

import cn.thinkfree.core.model.BaseModel;

public class RegionsRoleVO extends BaseModel {

    private String userID;
    private String userStoreID;
    private Integer level;

    public Integer[] getRoles() {
        return roles;
    }

    public void setRoles(Integer[] roles) {
        this.roles = roles;
    }

    private Integer[] roles;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserStoreID() {
        return userStoreID;
    }

    public void setUserStoreID(String userStoreID) {
        this.userStoreID = userStoreID;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


}
