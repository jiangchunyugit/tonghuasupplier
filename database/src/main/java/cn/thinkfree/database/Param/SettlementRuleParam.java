package cn.thinkfree.database.Param;

import io.swagger.annotations.ApiParam;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 规则审批参数
 */
public class SettlementRuleParam {

	@ApiParam("审核状态 2 审核通过 3 审核拒绝")
	@NotBlank(message = "审核状态不可为空")
	private String auditStatus;

	@ApiParam("审核不通过原因")
	private String auditCase;
	
	@ApiParam("结算规则编号，规则编码不可为空")
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
