package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcCompanyFinancial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="公司开户信息")
public class CompanyFinancialVO extends PcCompanyFinancial {
    @ApiModelProperty(value="省名称")
    private String provinceName;

    @ApiModelProperty(value="市名称")
    private String cityName;

    @ApiModelProperty(value="区名称")
    private String areaName;

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
