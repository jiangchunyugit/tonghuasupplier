package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CityBranch;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class CityBranchVO extends CityBranch {

    /**
     * 所属分公司名称
     */
    @ApiModelProperty("所属分公司名称")
    private String branchCompanyNm;

    /**
     * 店面list
     */
    @ApiModelProperty("店面信息")
    private List<StoreInfoVO> storeInfoVOList;

    public String getBranchCompanyNm() {
        return branchCompanyNm;
    }

    public void setBranchCompanyNm(String branchCompanyNm) {
        this.branchCompanyNm = branchCompanyNm;
    }

    public List<StoreInfoVO> getStoreInfoVOList() {
        return storeInfoVOList;
    }

    public void setStoreInfoVOList(List<StoreInfoVO> storeInfoVOList) {
        this.storeInfoVOList = storeInfoVOList;
    }
}
