package cn.thinkfree.database.vo.account;

import cn.thinkfree.core.model.BaseModel;

/**
 * 更新个人信息
 */
public class ChangeMeVO extends BaseModel {

    private String face;
    private String name;
    private String oldPwd;
    private String newPwd;

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
