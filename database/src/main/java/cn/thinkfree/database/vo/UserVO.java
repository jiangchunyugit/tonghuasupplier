package cn.thinkfree.database.vo;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.model.SecurityUser;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.SystemResource;
import cn.thinkfree.database.model.UserRegister;
import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserVO extends SecurityUser {


    /**
     * 用户注册信息
     */
    private UserRegister userRegister;
    /**
     * 公司信息
     */
    private CompanyInfo companyInfo;

    /**
     * 用户详情
     */
    private PcUserInfo pcUserInfo;

    private List<SystemResource> resources;
    /**
     * 公司关系图
     */
    private List<String> relationMap;

    /**
     * 是否根公司
     */
    private Boolean isRoot = Boolean.FALSE;

    public Boolean isRoot() {
        return isRoot;
    }

    public void setIsRoot(Boolean root) {
        isRoot = root;
    }

    public List<String> getRelationMap() {
        return isRoot ? relationMap : Lists.newArrayList(getCompanyID());
    }

    public void setRelationMap(List<String> relationMap) {
        this.relationMap = relationMap;
    }

    public UserRegister getUserRegister() {
        return userRegister;
    }

    public void setUserRegister(UserRegister userRegister) {
        this.userRegister = userRegister;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    public PcUserInfo getPcUserInfo() {
        return pcUserInfo;
    }

    public void setPcUserInfo(PcUserInfo pcUserInfo) {
        this.pcUserInfo = pcUserInfo;
    }

    public List<SystemResource> getResources() {
        return resources;
    }

    public void setResources(List<SystemResource> resources) {
        this.resources = resources;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return resources;
    }

    @Override
    public String getPassword() {
        return userRegister.getPassword();
    }

    @Override
    public String getUsername() {
        return userRegister.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return pcUserInfo == null ? false : SysConstants.YesOrNo.YES.shortVal().equals(pcUserInfo.getEnabled());
    }

    public String getCompanyID(){
        // TODO 公司编码
        if(companyInfo == null && pcUserInfo != null){
//            return pcUserInfo.getCompanyId();
            return  null;
        }else{
            return companyInfo.getCompanyId();

        }
    }


    @Override
    public String getName() {
        return pcUserInfo.getName();
    }

    @Override
    public String getPhone() {
        return pcUserInfo.getPhone();
    }

    @Override
    public String getCompanyName() {
        return companyInfo.getCompanyName();
    }

    @Override
    public Date getCreateTime() {
        return pcUserInfo.getCreateTime();
    }
}
