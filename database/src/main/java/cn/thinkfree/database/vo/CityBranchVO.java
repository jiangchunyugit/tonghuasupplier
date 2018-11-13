package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.CityBranch;
import cn.thinkfree.database.model.PcUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author jiangchunyu 城市分站信息
 */
@ApiModel("城市分站信息")
public class CityBranchVO extends CityBranch {

    /**
     * 所属分公司名称
     */
    @ApiModelProperty("分公司名称")
    private String branchCompanyNm;

    @ApiModelProperty("城市名称")
    private String cityNm;

    public String getCityNm() {
        return cityNm;
    }

    public void setCityNm(String cityNm) {
        this.cityNm = cityNm;
    }
    //    @ApiModelProperty("选择城市站点")
//    private String ebsCityBranchNm;

//    public String getEbsCityBranchNm() {
//        return ebsCityBranchNm;
//    }
//
//    public void setEbsCityBranchNm(String ebsCityBranchNm) {
//        this.ebsCityBranchNm = ebsCityBranchNm;
//    }

//    /**
//
//     * 店面list
//     */
//    @ApiModelProperty("店面信息")
//    private List<StoreInfoVO> storeInfoVOList;

//    @ApiModelProperty("所属城市分站账号信息")
//    private List<PcUserInfo> pcUserInfoList;

//    public List<PcUserInfo> getPcUserInfoList() {
//        return pcUserInfoList;
//    }
//
//    public void setPcUserInfoList(List<PcUserInfo> pcUserInfoList) {
//        this.pcUserInfoList = pcUserInfoList;
//    }

    public String getBranchCompanyNm() {
        return branchCompanyNm;
    }

    public void setBranchCompanyNm(String branchCompanyNm) {
        this.branchCompanyNm = branchCompanyNm;
    }

//    public List<StoreInfoVO> getStoreInfoVOList() {
//        return storeInfoVOList;
//    }
//
//    public void setStoreInfoVOList(List<StoreInfoVO> storeInfoVOList) {
//        this.storeInfoVOList = storeInfoVOList;
//    }
}
