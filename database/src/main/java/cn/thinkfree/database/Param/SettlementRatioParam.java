package cn.thinkfree.database.Param;

import java.util.List;

import io.swagger.annotations.ApiParam;


public class SettlementRatioParam {

	
	
	@ApiParam("审核状态 1 审核通过 2 审核拒绝") 
	private String auditStatus;
	
	
	@ApiParam("审核不通过原因")
	private String auditCase;
	
	@ApiParam("结算比例编号")
	private List<String> ratioNumbers;
	
	public String getAuditStatus() {
		return auditStatus;
	}


	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}


	public String getAuditCase() {
		return auditCase;
	}


	public void setAuditCase(String auditCase) {
		this.auditCase = auditCase;
	}


	public List<String> getRatioNumbers() {
		return ratioNumbers;
	}


	public void setRatioNumbers(List<String> ratioNumbers) {
		this.ratioNumbers = ratioNumbers;
	}
}
