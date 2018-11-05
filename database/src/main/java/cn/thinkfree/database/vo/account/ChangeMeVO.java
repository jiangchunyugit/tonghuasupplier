package cn.thinkfree.database.vo.account;

import cn.thinkfree.core.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 更新个人信息
 */
@ApiModel("更新个人信息主体")
public class ChangeMeVO extends BaseModel {

    @ApiModelProperty("头像")
    private String face;
    @ApiModelProperty("昵称")
    private String name;
    @ApiModelProperty("旧密码")
    private String oldPwd;
    @ApiModelProperty("新密码")
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
