package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.*;
import io.swagger.annotations.ApiParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ying007
 * 公司资质所有信息
 */
public class CompanySubmitVo {
    /**
     * 公司资质相关文件
     */
    private CompanySubmitFileVo companySubmitFileVo;

    /**
     * 公司表
     */
    private CompanyInfo companyInfo;

    /**
     * 公司拓展表
     */
    private CompanyInfoExpand companyInfoExpand;

    /**
     * 对公账信息
     */
    private PcCompanyFinancial pcCompanyFinancial;

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    public CompanyInfoExpand getCompanyInfoExpand() {
        return companyInfoExpand;
    }

    public void setCompanyInfoExpand(CompanyInfoExpand companyInfoExpand) {
        this.companyInfoExpand = companyInfoExpand;
    }

    public PcCompanyFinancial getPcCompanyFinancial() {
        return pcCompanyFinancial;
    }

    public void setPcCompanyFinancial(PcCompanyFinancial pcCompanyFinancial) {
        this.pcCompanyFinancial = pcCompanyFinancial;
    }

    public CompanySubmitFileVo getCompanySubmitFileVo() {
        return companySubmitFileVo;
    }

    public void setCompanySubmitFileVo(CompanySubmitFileVo companySubmitFileVo) {
        this.companySubmitFileVo = companySubmitFileVo;
    }

    @Override
    public String toString() {
        return "CompanySubmitVo{" +
                "companyInfo=" + companyInfo +
                ", companyInfoExpand=" + companyInfoExpand +
                ", pcCompanyFinancial=" + pcCompanyFinancial +
                '}';
    }
}
