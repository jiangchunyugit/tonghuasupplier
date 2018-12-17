package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcAuditTemporaryInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "资质变更信息")
public class AuditTemporaryInfoVO extends PcAuditTemporaryInfo{

    @ApiModelProperty("省名称")
    private String provinceName;

    @ApiModelProperty("市名称")
    private String cityName;

    @ApiModelProperty("区名称")
    private String areaName;

    @ApiModelProperty("省名称")
    private String registerProvinceName;

    @ApiModelProperty("市名称")
    private String registerCityName;

    @ApiModelProperty("区名称")
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

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
