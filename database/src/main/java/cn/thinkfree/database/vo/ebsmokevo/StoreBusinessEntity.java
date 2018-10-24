package cn.thinkfree.database.vo.ebsmokevo;

import cn.thinkfree.database.model.BusinessEntity;

import java.util.List;

public class StoreBusinessEntity {

    /**
     * 分店名称
     */
    private String storeNm;

    /**
     * 经营主体
     */
    private List<BusinessEntity> businessEntityList;

    public String getStoreNm() {
        return storeNm;
    }

    public void setStoreNm(String storeNm) {
        this.storeNm = storeNm;
    }

    public List<BusinessEntity> getBusinessEntityList() {
        return businessEntityList;
    }

    public void setBusinessEntityList(List<BusinessEntity> businessEntityList) {
        this.businessEntityList = businessEntityList;
    }
}
