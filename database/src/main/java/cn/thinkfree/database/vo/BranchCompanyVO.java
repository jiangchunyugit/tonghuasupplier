package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.BranchCompany;
import cn.thinkfree.database.model.Province;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 分公司信息
 */
@ApiModel("分公司信息")
public class BranchCompanyVO extends BranchCompany {

    @ApiModelProperty("所属分公司名称（埃森哲）")
    private String ebsBranchCompanyNm;

    @ApiModelProperty("省份编码信息")
    @NotEmpty(message = "省份不可为空",groups = {Severitys.Insert.class,Severitys.Update.class})
    private List<Province> provinceList;

    public String getEbsBranchCompanyNm() {
        return ebsBranchCompanyNm;
    }

    public void setEbsBranchCompanyNm(String ebsBranchCompanyNm) {
        this.ebsBranchCompanyNm = ebsBranchCompanyNm;
    }

    public List<Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<Province> provinceList) {
        this.provinceList = provinceList;
    }
}
