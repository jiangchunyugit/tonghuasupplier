package cn.thinkfree.database.vo;

import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

public class MyContractTemplateDetails {

    @ApiModelProperty("保证金金额")
	private String contractDeposit;//保证金金额
    
    @ApiModelProperty("付款模式")
	private String paymentType;//付款模式
	
    @ApiModelProperty("入住年限")
	private String ChecInDeadlines;//入住年限
	
    @ApiModelProperty("扣款比例key和value")
	private Map<String,String>  withholdList = new HashMap<>();//扣款比例
    
    @ApiModelProperty("扣款比例key和value")
   	private Map<String,String>  agingList = new HashMap<>();//分期 key 和vaule

	public String getContractDeposit() {
		return contractDeposit;
	}

	public void setContractDeposit(String contractDeposit) {
		this.contractDeposit = contractDeposit;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getChecInDeadlines() {
		return ChecInDeadlines;
	}

	public void setChecInDeadlines(String checInDeadlines) {
		ChecInDeadlines = checInDeadlines;
	}

	
}
