package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("(权限)运营平台---站点信息")
public class SiteInfo {

    @ApiModelProperty("分站名称")
    private String siteNm;

    @ApiModelProperty("所属分公司")
    private String ebsBranchCompany;

    @ApiModelProperty("站点")
    private String ebsCityBranch;

    @ApiModelProperty(value="负责人姓名")
    private String legalName;

    @ApiModelProperty(value="负责人手机号")
    private String legalPhone;

    @ApiModelProperty(value="mail邮箱地址")
    private String mail;

    public String getSiteNm() {
        return siteNm;
    }

    public void setSiteNm(String siteNm) {
        this.siteNm = siteNm;
    }

    public String getEbsBranchCompany() {
        return ebsBranchCompany;
    }

    public void setEbsBranchCompany(String ebsBranchCompany) {
        this.ebsBranchCompany = ebsBranchCompany;
    }

    public String getEbsCityBranch() {
        return ebsCityBranch;
    }

    public void setEbsCityBranch(String ebsCityBranch) {
        this.ebsCityBranch = ebsCityBranch;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalPhone() {
        return legalPhone;
    }

    public void setLegalPhone(String legalPhone) {
        this.legalPhone = legalPhone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @ApiModelProperty(value="mark备注")
    private String mark;
}
