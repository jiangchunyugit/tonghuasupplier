package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.print.attribute.standard.Severity;
import javax.validation.constraints.*;

@ApiModel("员工查询参数")
public class StaffSEO extends AbsPageSearchCriteria{

    @ApiModelProperty("省编号")
    private String provinceCode;

    @ApiModelProperty("市编号")
    private String cityCode;

    @ApiModelProperty("县编号")
    private String areaCode;

    /**
     * 公司编号
     */
    @ApiModelProperty("公司编号")
    private String companyId;

    @ApiModelProperty("员工名称")
    private String name;

    @ApiModelProperty("员工手机号")
    @Max(value = 11, message = "员工手机号不能超过11位")
    private String phone;

    @ApiModelProperty("状态-->1:正常 2：待激活 3：移除")
    private Integer status;

    private Integer isBind;

    private Integer isJob;

    public Integer getIsJob() {
        if(getStatus() != null && ("3".equals(getStatus()) || getStatus() == 3)){
            isJob = 2;
        }else{
            isJob = 1;
        }
        return isJob;
    }

    public void setIsJob(Integer isJob) {
        this.isJob = isJob;
    }

    public Integer getIsBind() {
        if(getStatus() != null && ("1".equals(getStatus()) || getStatus() == 1)){
            isBind = 1;
        }else if(getStatus() != null && ("2".equals(getStatus()) || getStatus() == 2)){
            isBind = 2;
        }
        return isBind;
    }

    public void setIsBind(Integer isBind) {
        this.isBind = isBind;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
