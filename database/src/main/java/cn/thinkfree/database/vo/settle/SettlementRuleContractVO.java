package cn.thinkfree.database.vo.settle;

import cn.thinkfree.database.model.SettlementMethodInfo;
import cn.thinkfree.database.model.SettlementRuleInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author jiangchunyu 结算规则信息
 */
@ApiModel(description = "结算规则信息")
public class SettlementRuleContractVO extends SettlementRuleInfo {


    @ApiModelProperty("方案信息")
    private List<SettlementMethodInfo> settlementMethod;

    public List<SettlementMethodInfo> getSettlementMethod() {
        return settlementMethod;
    }

    public void setSettlementMethod(List<SettlementMethodInfo> settlementMethod) {
        this.settlementMethod = settlementMethod;
    }
}
