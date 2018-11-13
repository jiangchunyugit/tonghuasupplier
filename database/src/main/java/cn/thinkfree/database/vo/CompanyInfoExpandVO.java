package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CompanyInfoExpand;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="公司补充信息Vo")
public class CompanyInfoExpandVO extends CompanyInfoExpand {
    @ApiModelProperty(value="省名称")
    private String registerProvinceName;

    @ApiModelProperty(value="市名称")
    private String registerCityName;

    @ApiModelProperty(value="区名称")
    private String registerAreaName;

    public String getRegisterProvinceName() {
        return registerProvinceName;
    }

    public void setRegisterProvinceName(String registerProvinceName) {
        this.registerProvinceName = registerProvinceName;
    }

    public String getRegisterCityName() {
        return registerCityName;
    }

    public void setRegisterCityName(String registerCityName) {
        this.registerCityName = registerCityName;
    }

    public String getRegisterAreaName() {
        return registerAreaName;
    }

    public void setRegisterAreaName(String registerAreaName) {
        this.registerAreaName = registerAreaName;
    }
}
