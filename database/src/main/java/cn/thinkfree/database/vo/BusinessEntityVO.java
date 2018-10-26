package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.BusinessEntity;

import java.util.List;

public class BusinessEntityVO extends BusinessEntity {

    private List<String> StoreNm;

    public List<String> getStoreNm() {
        return StoreNm;
    }

    public void setStoreNm(List<String> storeNm) {
        StoreNm = storeNm;
    }
}
