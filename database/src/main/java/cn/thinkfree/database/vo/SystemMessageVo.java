package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.SystemMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("公告列表信息")
public class SystemMessageVo extends SystemMessage {
    @ApiModelProperty("对象名称 多个对象逗号相隔")
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
