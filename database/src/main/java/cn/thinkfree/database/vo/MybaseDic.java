package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "字典返回相关参数")
public class MybaseDic {

	 @ApiModelProperty(value = "返回的值")
	 private String dicName;
	 
	 @ApiModelProperty(value = "返回字典code")
	 private String dicCode;
	 
	 @ApiModelProperty(value = "返回字典备注")
	 private String remarks;
	 
	 @ApiModelProperty(value = "排序")
	 private String dicSort;

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public String getDicCode() {
		return dicCode;
	}

	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}

	public String getDicSort() {
		return dicSort;
	}

	public void setDicSort(String dicSort) {
		this.dicSort = dicSort;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
