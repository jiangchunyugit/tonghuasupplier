package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.StoreInfo;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 城市分站下门店信息
 */
public class StoreInfoVO extends StoreInfo {

    @ApiModelProperty("经营主体名称")
    private String businessEntityNm;

    @ApiModelProperty("门店编码")
    private String storeCode;

    @ApiModelProperty("门店地址")
    private String storeAddress;

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getBusinessEntityNm() {
        return businessEntityNm;
    }

    public void setBusinessEntityNm(String businessEntityNm) {
        this.businessEntityNm = businessEntityNm;
    }
}
