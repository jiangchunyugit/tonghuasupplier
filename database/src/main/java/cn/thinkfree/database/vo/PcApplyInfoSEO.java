package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcApplyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.checkerframework.checker.units.qual.A;

/**
 * @author ying007
 * 添加账号需要的参数
 */
@ApiModel(description = "入驻申请")
public class PcApplyInfoSEO extends PcApplyInfo {
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    private String verifyCode;

    @ApiModelProperty("站点公司id，门店id")
    private String siteCompanyId;

    @ApiModelProperty(value = "分公司编号")
    private String branchCompanyId;

    @ApiModelProperty(value = "城市分站编号")
    private String cityBranchCompanyId;

    public String getBranchCompanyId() {
        return branchCompanyId;
    }

    public void setBranchCompanyId(String branchCompanyId) {
        this.branchCompanyId = branchCompanyId;
    }

    public String getCityBranchCompanyId() {
        return cityBranchCompanyId;
    }

    public void setCityBranchCompanyId(String cityBranchCompanyId) {
        this.cityBranchCompanyId = cityBranchCompanyId;
    }

    public String getSiteCompanyId() {
        return siteCompanyId;
    }

    public void setSiteCompanyId(String siteCompanyId) {
        this.siteCompanyId = siteCompanyId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "PcApplyInfoSEO{" +
                "password='" + password + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }
}
