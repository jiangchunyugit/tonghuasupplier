package cn.thinkfree.database.vo.settle;


import cn.thinkfree.database.vo.AbsPageSearchCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 计算规则 分页查询
 * @author jiangchunyu
 *
 */
@ApiModel(description = "计算规则分页查询")
public class SettlementRuleSEO extends AbsPageSearchCriteria  {

    @ApiModelProperty("结算规则编号")
    private String ruleNumber;

    @ApiModelProperty("规则状态 0生效 1失效 2作废 3未生效 ")
    private String ruleStatus;

    @ApiModelProperty("结算规则名称")
    private String ruleName;

    @ApiModelProperty("创建人")
    private String createUser;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("审核状态 1 待审核 2审核通过 3审核不通过")
    private String auditStatus;

    public String getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(String ruleNumber) {
        this.ruleNumber = ruleNumber;
    }

    public String getRuleStatus() {
        return ruleStatus;
    }

    public void setRuleStatus(String ruleStatus) {
        this.ruleStatus = ruleStatus;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
}
