package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PreProjectConstruction;
import cn.thinkfree.database.model.PreProjectConstructionInfo;

public class ProjectQuotationItemVO  extends PreProjectConstruction {

    private PreProjectConstructionInfo preProjectConstructionInfo;

    public PreProjectConstructionInfo getPreProjectConstructionInfo() {
        return preProjectConstructionInfo;
    }

    public void setPreProjectConstructionInfo(PreProjectConstructionInfo preProjectConstructionInfo) {
        this.preProjectConstructionInfo = preProjectConstructionInfo;
    }
}
