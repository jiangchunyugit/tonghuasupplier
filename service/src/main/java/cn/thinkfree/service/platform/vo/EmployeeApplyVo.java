package cn.thinkfree.service.platform.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 *
 */
public class EmployeeApplyVo {
    @ApiModelProperty("申请人姓名")
    private String realName;
    @ApiModelProperty("证件号码")
    private String cardNo;
    @ApiModelProperty("申请时间")
    private long applyTime;
    @ApiModelProperty("处理时间")
    private long dealTime;
    @ApiModelProperty("处理状态，1已处理，2未处理，3已过期")
    private int dealState;
    @ApiModelProperty("处理人名称")
    private String dealUserName;
    @ApiModelProperty("用户ID")
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }

    public int getDealState() {
        return dealState;
    }

    public void setDealState(int dealState) {
        this.dealState = dealState;
    }

    public String getDealUserName() {
        return dealUserName;
    }

    public void setDealUserName(String dealUserName) {
        this.dealUserName = dealUserName;
    }
}
