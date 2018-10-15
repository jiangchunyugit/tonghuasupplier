package cn.thinkfree.database.vo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel("合同搜索条件")
public class ContractSEO extends AbsPageSearchCriteria {

    /**
     * 省
     */
    @ApiModelProperty("省份编码")
    @NotBlank(message = "")
    @Min(0)
    @Range(max = 10,min = 1,message = "",groups ={Severitys.Insert.class})
    private String province;
    /**
     * 市
     */
    @ApiModelProperty("市编码")

    private String city;
    /**
     * 县
     */
    @ApiModelProperty("县编码")
    private String area;
    
    
    @ApiModelProperty("合同编码")
    private String contractNumber;
    
    @ApiModelProperty("客户姓名")
    private String customName;
    
    @ApiModelProperty("公司类型")
    private String companyType;
    
    @ApiModelProperty("合同状态")
    private String contractStatus;
    
    @ApiModelProperty("公司编码/公司名称")
    private String companyInfo;
    
    
    @ApiModelProperty("合同有效期开始时间")
    private String starTime;
    
    @ApiModelProperty("合同有效期结束时间")
    private String endTime;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(String companyInfo) {
		this.companyInfo = companyInfo;
	}

	public String getStarTime() {
		return starTime;
	}

	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
    



}
