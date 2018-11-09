package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.StoreInfo;


public class StoreInfoVO extends StoreInfo {

    /**
     * 经营主体名称
     */
    private String businessEntityNm;

    public String getBusinessEntityNm() {
        return businessEntityNm;
    }

    public void setBusinessEntityNm(String businessEntityNm) {
        this.businessEntityNm = businessEntityNm;
    }
}
