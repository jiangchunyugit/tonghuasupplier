package cn.thinkfree.database.vo.contract;

import java.util.ArrayList;
import java.util.List;

import cn.thinkfree.database.model.ContractTermsChild;
import io.swagger.annotations.ApiModelProperty;

public class ContractCostVo {

	
    @ApiModelProperty("费用code")
	private String costCode;
    
    @ApiModelProperty("费用名称")
	private String costName;
	
    @ApiModelProperty("费用比例值")
	private String  costValue;
    
    @ApiModelProperty("阶段列表")
    private List<ContractTermsChild> list = new ArrayList<>();
	
    public String getCostCode() {
		return costCode;
	}

	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}

	public String getCostName() {
		return costName;
	}

	public void setCostName(String costName) {
		this.costName = costName;
	}

	public String getCostValue() {
		return costValue;
	}

	public void setCostValue(String costValue) {
		this.costValue = costValue;
	}

	public List<ContractTermsChild> getList() {
		return list;
	}

	public void setList(List<ContractTermsChild> list) {
		this.list = list;
	}

	
	
	
	
}
