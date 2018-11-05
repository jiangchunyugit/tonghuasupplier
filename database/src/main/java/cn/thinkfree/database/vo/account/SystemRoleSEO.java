package cn.thinkfree.database.vo.account;

import cn.thinkfree.database.vo.AbsPageSearchCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("角色查询条件")
public class SystemRoleSEO extends AbsPageSearchCriteria {

    @ApiModelProperty("名称或编号")
    private String name;
    @ApiModelProperty("状态")
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
