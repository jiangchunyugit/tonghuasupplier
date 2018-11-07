package cn.thinkfree.database.vo.settle;

import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.SettlementMethodInfo;
import cn.thinkfree.database.model.SettlementRuleInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author jiangchunyu 结算规则信息
 */
@ApiModel(description = "结算规则信息")
public class SettlementRuleVO extends SettlementRuleInfo {

    @ApiModelProperty("代收款结算办法")
    private List<SettlementMethodInfo> settlementMethodInfos;

    @ApiModelProperty("审核信息")
    private List<PcAuditInfo> auditInfo;

    public List<PcAuditInfo> getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(List<PcAuditInfo> auditInfo) {
        this.auditInfo = auditInfo;
    }

    public List<SettlementMethodInfo> getSettlementMethodInfos() {
        return settlementMethodInfos;
    }

    public void setSettlementMethodInfos(List<SettlementMethodInfo> settlementMethodInfos) {
        this.settlementMethodInfos = settlementMethodInfos;
    }
}
