package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PreProjectConstruction;
import cn.thinkfree.database.model.PreProjectConstructionInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("报价单条目")
public class ProjectQuotationItemVO  extends PreProjectConstruction {

    @ApiModelProperty("报价单条目信息")
    private PreProjectConstructionInfo preProjectConstructionInfo;

    public PreProjectConstructionInfo getPreProjectConstructionInfo() {
        return preProjectConstructionInfo;
    }

    public void setPreProjectConstructionInfo(PreProjectConstructionInfo preProjectConstructionInfo) {
        this.preProjectConstructionInfo = preProjectConstructionInfo;
    }
}
