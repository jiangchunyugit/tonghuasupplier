package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcApplyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
