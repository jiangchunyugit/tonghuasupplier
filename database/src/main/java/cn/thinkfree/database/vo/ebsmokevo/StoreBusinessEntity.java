package cn.thinkfree.database.vo.ebsmokevo;

import cn.thinkfree.database.model.BusinessEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author jiangchunyu 埃森哲店面经营主体
 */
@ApiModel("店面经营主体")
public class StoreBusinessEntity {

    /**
     * 分店名称
     */
    @ApiModelProperty("店面名称")
    private String storeNm;

    /**
     * 经营主体
     */
    @ApiModelProperty("经营主体list")
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
