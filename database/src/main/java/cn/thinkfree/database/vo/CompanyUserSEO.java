package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModelProperty;

public class CompanyUserSEO extends AbsPageSearchCriteria{

    /**
     * 员工编号
     */
    @ApiModelProperty("员工名称")
    private String empName;

    /**
     * 员工名称
     */
    @ApiModelProperty("员工编号")
    private String empNumber;


	public String getEmpName() {
		return empName;
	}


	public void setEmpName(String empName) {
		this.empName = empName;
	}


	public String getEmpNumber() {
		return empNumber;
	}


	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}


}
