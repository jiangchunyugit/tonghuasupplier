package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcAuditTemporaryInfo;

public class CompanyTemporaryVo extends PcAuditTemporaryInfo {
    /**
     * 公司资质相关文件
     */
    private CompanySubmitFileVo companySubmitFileVo;

    /**
     * 公司类型  比如：有限公司，集体公司
     */
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
