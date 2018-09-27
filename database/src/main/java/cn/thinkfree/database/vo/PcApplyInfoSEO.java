package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcApplyInfo;

/**
 * @author ying007
 * 添加账号需要的参数
 */
public class PcApplyInfoSEO extends PcApplyInfo {
    private String password;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
                '}';
    }
}
