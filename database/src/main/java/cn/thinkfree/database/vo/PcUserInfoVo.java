package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Api
public class PcUserInfoVo extends PcUserInfo {

    @ApiModelProperty("最后登陆时间")
    private Date lastLogin;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("账号")
    private String regPhone;

    public String getRegPhone() {
        return regPhone;
    }

    public void setRegPhone(String regPhone) {
        this.regPhone = regPhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}
