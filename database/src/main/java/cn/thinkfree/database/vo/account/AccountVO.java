package cn.thinkfree.database.vo.account;

import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.SystemRole;

import java.util.List;

public class AccountVO  {
    /**
     * 权限范围
     */
    private String scope;

    /**
     * 角色集
     */
    private List<SystemRole> roles;

    /**
     * 埃森哲账号信息
     */
    private ThirdAccountVO thirdAccount;

    /**
     * 第三方主键
     */
    private String thirdId;

    private PcUserInfo pcUserInfo;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<SystemRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SystemRole> roles) {
        this.roles = roles;
    }

    public ThirdAccountVO getThirdAccount() {
        return thirdAccount;
    }

    public void setThirdAccount(ThirdAccountVO thirdAccount) {
        this.thirdAccount = thirdAccount;
    }

    public String getThirdId() {
        return thirdId;
    }

    public void setThirdId(String thirdId) {
        this.thirdId = thirdId;
    }

    public PcUserInfo getPcUserInfo() {
        return pcUserInfo;
    }

    public void setPcUserInfo(PcUserInfo pcUserInfo) {
        this.pcUserInfo = pcUserInfo;
    }
}
