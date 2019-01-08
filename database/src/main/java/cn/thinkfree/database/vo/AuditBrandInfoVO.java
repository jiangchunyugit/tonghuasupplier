package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.DealerBrandInfo;
import cn.thinkfree.database.model.DealerCategory;
import cn.thinkfree.database.model.PcAuditInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

@ApiModel(value = "品牌和审批信息")
public class AuditBrandInfoVO extends DealerBrandInfo {

    @ApiModelProperty(value="品类名称")
    private String categoryName;

    @ApiModelProperty(value="品类编码")
    private String categoryNo;

    @ApiModelProperty(value="auditType审核类型0入驻 1合同 2变更 3续签4结算比例 5结算规则6:创建账号(录入）7经销商合同 8：经销商品牌审核")
    private String auditType;

    @ApiModelProperty(value="auditLevel审核级别0初次审核1二级审核")
    private String auditLevel;

    @ApiModelProperty(value="auditPersion审核人")
    private String auditPersion;

    @ApiModelProperty(value="atStatus 审批表里的审批类型 0不通过1通过")
    private String atStatus;
    @ApiModelProperty(value="auditTime审核时间")
    private Date auditTime;

    @ApiModelProperty(value="auditCase审核成功或者失败的原因")
    private String auditCase;

    @ApiModelProperty(value="auditAccount审批人账号")
    private String auditAccount;

    @ApiModelProperty(value="公司名称")
    private String companyName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getAuditLevel() {
        return auditLevel;
    }

    public void setAuditLevel(String auditLevel) {
        this.auditLevel = auditLevel;
    }

    public String getAuditPersion() {
        return auditPersion;
    }

    public void setAuditPersion(String auditPersion) {
        this.auditPersion = auditPersion;
    }

    public String getAtStatus() {
        return atStatus;
    }

    public void setAtStatus(String atStatus) {
        this.atStatus = atStatus;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditCase() {
        return auditCase;
    }

    public void setAuditCase(String auditCase) {
        this.auditCase = auditCase;
    }

    public String getAuditAccount() {
        return auditAccount;
    }

    public void setAuditAccount(String auditAccount) {
        this.auditAccount = auditAccount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
