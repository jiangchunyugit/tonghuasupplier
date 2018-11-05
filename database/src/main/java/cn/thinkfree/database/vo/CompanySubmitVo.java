package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author ying007
 * 公司资质所有信息
 */
@Api(value = "公司资质所有信息")
public class CompanySubmitVo {
    /**
     * 公司资质相关文件
     */
//    @ApiModelProperty("公司资质图片")
//    private CompanySubmitFileVo companySubmitFileVo;

    /**
     * 公司表
     */
    @ApiModelProperty("公司资质信息")
    private CompanyInfo companyInfo;

    /**
     * 公司拓展表
     */
    @ApiModelProperty("公司资质拓展信息")
    private CompanyInfoExpand companyInfoExpand;

    /**
     * 对公账信息
     */
    @ApiModelProperty("公司资质账户信息")
    private PcCompanyFinancial pcCompanyFinancial;

    /**
     * 公司类型  比如：有限公司，集体公司
     */
    @ApiModelProperty("公司类型：0:有限责任公司,1:股份有限公司,2:个人独资企业,3:合伙企业,4:全民所有制企业,5:集体所有制企业'")
    private String companyTypeName;

    public String getCompanyTypeName() {
        return companyTypeName;
    }

    public void setCompanyTypeName(String companyTypeName) {
        this.companyTypeName = companyTypeName;
    }

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


    @Override
    public String toString() {
        return "CompanySubmitVo{" +
                "companyInfo=" + companyInfo +
                ", companyInfoExpand=" + companyInfoExpand +
                ", pcCompanyFinancial=" + pcCompanyFinancial +
                '}';
    }
}
