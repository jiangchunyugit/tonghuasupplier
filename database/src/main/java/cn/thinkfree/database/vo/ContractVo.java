package cn.thinkfree.database.vo;

import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 合同信息 列表vo
 * @author lvqidong
 *
 */
@ApiModel(description = "合同列表")
public class ContractVo {
	
    @ApiModelProperty("主键")
    private Integer id;
    
    @ApiModelProperty("合同编号")
    private String contractNumber;
    
    @ApiModelProperty("签约时间")
    private String signedTime;
    
    @ApiModelProperty("合同签约开始时间")
    private String startTime;
    
    @ApiModelProperty("合同签约结束时间时间")
    private String endTime;
    
    @ApiModelProperty("公司编号")
    private String companyId;
    
    @ApiModelProperty("公司名称")
    private String companyName;
    
    @ApiModelProperty("公司类型")
    private String companyType;
    
    @ApiModelProperty("公司所在地区")
    private String companyLocation;
    
    @ApiModelProperty("保证金金额")
    private String depositMoney;
    
    @ApiModelProperty(hidden = true)
    private String contractStatus;
    
    
    @ApiModelProperty(hidden = true)
    private String contractUrl;
    
    @ApiModelProperty("合同条款")
    private Map<String,String> contractClause = new HashMap<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getSignedTime() {
		return signedTime;
	}

	public void setSignedTime(String signedTime) {
		this.signedTime = signedTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}

	
	public Map<String, String> getContractClause() {
		return contractClause;
	}

	public void setContractClause(Map<String, String> contractClause) {
		this.contractClause = contractClause;
	}

	public String getDepositMoney() {
		return depositMoney;
	}

	public void setDepositMoney(String depositMoney) {
		this.depositMoney = depositMoney;
	}

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getContractUrl() {
		return contractUrl;
	}

	public void setContractUrl(String contractUrl) {
		this.contractUrl = contractUrl;
	}
}
