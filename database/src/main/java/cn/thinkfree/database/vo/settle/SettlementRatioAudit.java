package cn.thinkfree.database.vo.settle;

import java.util.List;

import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.SettlementRatioInfo;
import io.swagger.annotations.ApiModelProperty;

public class SettlementRatioAudit {

	@ApiModelProperty("结算比例信息")
	private SettlementRatioInfo info;
	
	
	@ApiModelProperty("审核信息")
	private List<PcAuditInfo> auditInfo;


	public SettlementRatioInfo getInfo() {
		return info;
	}


	public void setInfo(SettlementRatioInfo info) {
		this.info = info;
	}


	public List<PcAuditInfo> getAuditInfo() {
		return auditInfo;
	}


	public void setAuditInfo(List<PcAuditInfo> auditInfo) {
		this.auditInfo = auditInfo;
	}
	
	
}
