package cn.thinkfree.database.vo.agency;

import cn.thinkfree.database.vo.Severitys;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经销商合同审批条件
 */
@ApiModel(description = "经销商合同审批条件")
public class AgencyAuditContractSEO {

    @ApiModelProperty("合同编号")
    @NotBlank(message = "合同编号不可以为空",groups = {Severitys.Insert.class,Severitys.Update.class})
    private String contractNumber;

    @ApiModelProperty("合同原状态")
    @NotBlank(message = "合同原状态不可以为空",groups = {Severitys.Insert.class,Severitys.Update.class})
    private String status;

    @ApiModelProperty("审批状态 1 通过 0 拒绝")
    @NotBlank(message = "审批状态不可以为空",groups = {Severitys.Insert.class})
    private String auditStatus;

    @ApiModelProperty("审核通过或者拒绝的原因")
    private String cause;

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
