package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.BranchCompany;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 分公司信息
 */
@ApiModel("分公司信息")
public class BranchCompanyVO extends BranchCompany {

    /**
     * 省份名称
     */
    @ApiModelProperty("省份名称")
    private String provinceNm;

    public String getProvinceNm() {
        return provinceNm;
    }

    public void setProvinceNm(String provinceNm) {
        this.provinceNm = provinceNm;
    }
}
