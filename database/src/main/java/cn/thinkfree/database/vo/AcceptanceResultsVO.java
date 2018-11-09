package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Auther: jiang
 * @Date: 2018/10/25 14:02
 * @Description:验收结果
 */
@ApiModel(description = "验收结果")
public class AcceptanceResultsVO {
    @ApiModelProperty("验收阶段")
    private String acceptancePhase;
    @ApiModelProperty("验收时间")
    private Date acceptanceTime;
    @ApiModelProperty("是否验收")
    private Integer isNeedCheck;
    @ApiModelProperty("验收结果")
    private Integer isAdopt;
    @ApiModelProperty("支付状态")
    private Integer payStatus;

    public String getAcceptancePhase() {
        return acceptancePhase;
    }

    public void setAcceptancePhase(String acceptancePhase) {
        this.acceptancePhase = acceptancePhase;
    }

    public Date getAcceptanceTime() {
        return acceptanceTime;
    }

    public void setAcceptanceTime(Date acceptanceTime) {
        this.acceptanceTime = acceptanceTime;
    }

    public Integer getIsNeedCheck() {
        return isNeedCheck;
    }

    public void setIsNeedCheck(Integer isNeedCheck) {
        this.isNeedCheck = isNeedCheck;
    }

    public Integer getIsAdopt() {
        return isAdopt;
    }

    public void setIsAdopt(Integer isAdopt) {
        this.isAdopt = isAdopt;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }
}
