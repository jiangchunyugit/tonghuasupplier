package cn.thinkfree.database.Param;

import io.swagger.annotations.ApiParam;

import java.util.List;


public class SettlementRuleParam {

	
	
	@ApiParam("审核状态 1 审核通过 2 审核拒绝") 
	private String auditStatus;
	
	
	@ApiParam("审核不通过原因")
	private String auditCase;
	
	@ApiParam("结算规则编号")
	private List<String> ruleNumbers;
	
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

	public List<String> getRuleNumbers() {
		return ruleNumbers;
	}

	public void setRuleNumbers(List<String> ruleNumbers) {
		this.ruleNumbers = ruleNumbers;
	}
}
