package cn.thinkfree.database.vo.settle;


import java.util.Date;

import cn.thinkfree.database.vo.AbsPageSearchCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 计算比例 分页查询
 * @author lvqidong
 *
 */
@ApiModel(description = "计算比例分页查询")
public class SettlementRatioSEO extends AbsPageSearchCriteria  {

    @ApiModelProperty("结算比例编号")
    private String ratioNumber;
    
    @ApiModelProperty("比例状态 1 待审核 2审核通过 3审核不通过 4作废 5申请作废 7生效 8失效 9未生效 ")
    private String ratioStatus;
    
    @ApiModelProperty("结算比例名称")
    private String ratioName;
    
    @ApiModelProperty("创建人")
    private String createUser;
    
    @ApiModelProperty("开始时间")
    private Date startTime;
    
    @ApiModelProperty("结束时间")
    private Date endTime;
    
    @ApiModelProperty("审核状态 1 待审核 2审核通过 3审核不通过")
    private String auditStatus;

	public String getRatioNumber() {
		return ratioNumber;
	}

	public void setRatioNumber(String ratioNumber) {
		this.ratioNumber = ratioNumber;
	}

	public String getRatioStatus() {
		return ratioStatus;
	}

	public void setRatioStatus(String ratioStatus) {
		this.ratioStatus = ratioStatus;
	}

	public String getRatioName() {
		return ratioName;
	}

	public void setRatioName(String ratioName) {
		this.ratioName = ratioName;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
    
    

    
}
