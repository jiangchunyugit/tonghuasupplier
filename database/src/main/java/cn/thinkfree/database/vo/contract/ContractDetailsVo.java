package cn.thinkfree.database.vo.contract;

import java.util.ArrayList;
import java.util.List;

import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.vo.ContractVo;
import io.swagger.annotations.ApiModelProperty;

public class ContractDetailsVo {

	@ApiModelProperty("合同信息")
	private ContractVo vo;
	
	@ApiModelProperty("审批信息")
	private List<PcAuditInfo> list =new ArrayList<>();

	public ContractVo getVo() {
		return vo;
	}

	public void setVo(ContractVo vo) {
		this.vo = vo;
	}

	public List<PcAuditInfo> getList() {
		return list;
	}

	public void setList(List<PcAuditInfo> list) {
		this.list = list;
	}
}
