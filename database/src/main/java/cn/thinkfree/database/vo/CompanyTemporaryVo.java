package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcAuditTemporaryInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("公司信息")
public class CompanyTemporaryVo extends PcAuditTemporaryInfo {
    /**
     * 公司资质相关文件
     */
    @ApiModelProperty(value = "公司资质相关文件")
    private CompanySubmitFileVo companySubmitFileVo;

    /**
     * 公司类型  比如：有限公司，集体公司
     */
    @ApiModelProperty(value = "公司类型  比如：有限公司，集体公司")
    private String companyTypeName;

    public CompanySubmitFileVo getCompanySubmitFileVo() {
        return companySubmitFileVo;
    }

    public void setCompanySubmitFileVo(CompanySubmitFileVo companySubmitFileVo) {
        this.companySubmitFileVo = companySubmitFileVo;
    }

    public String getCompanyTypeName() {
        return companyTypeName;
    }

    public void setCompanyTypeName(String companyTypeName) {
        this.companyTypeName = companyTypeName;
    }
}
