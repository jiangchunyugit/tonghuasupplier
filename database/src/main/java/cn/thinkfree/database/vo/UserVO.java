package cn.thinkfree.database.vo;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.model.SecurityUser;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.constants.UserRegisterType;
import cn.thinkfree.database.model.*;
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

    /**
     * 可用资源
     */
    private List<SystemResource> resources;
    /**
     * 公司关系图
     */
    private List<String> relationMap;

    /**
     * 分公司
     */
    private BranchCompany branchCompany;

    /**
     * 市公司
     */
    private CityBranch cityBranch;

    /**
     * 企业账号
     */
    private CompanyUser companyUser;
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
         if(pcUserInfo == null || SysConstants.YesOrNo.NO.shortVal().equals(pcUserInfo.getEnabled())){
             return false;
         }
         if(UserLevel.Company_Province.shortVal().equals(pcUserInfo.getLevel())
                 && (branchCompany == null ||
                         !UserEnabled.Enabled_true.shortVal().equals(branchCompany.getIsEnable()))){
             return false;
         }
         if(UserLevel.Company_City.shortVal().equals(pcUserInfo.getLevel())
                 &&  (cityBranch == null ||
                         !UserEnabled.Enabled_true.shortVal().equals(cityBranch.getIsEnable()))){
             return false;
         }
        return true;
    }

    public String getCompanyID(){
        // TODO 分站信息待定
        return companyInfo.getCompanyId();
    }

    @Override
    public String getName() {
        if(pcUserInfo!=null){
            return pcUserInfo.getName();
        }
        if(companyUser != null){
            return companyUser.getEmpName();
        }
        return "";
    }

    @Override
    public String getPhone() {

        return userRegister.getPhone();
    }

    @Override
    public String getCompanyName() {
        return companyInfo.getCompanyName();
    }

    @Override
    public Date getCreateTime() {
        return userRegister.getRegisterTime();
    }

    @Override
    public Short getType() {
        return UserRegisterType.Platform.shortVal();
    }


    public BranchCompany getBranchCompany() {
        return branchCompany;
    }

    public void setBranchCompany(BranchCompany branchCompany) {
        this.branchCompany = branchCompany;
    }

    public CityBranch getCityBranch() {
        return cityBranch;
    }

    public void setCityBranch(CityBranch cityBranch) {
        this.cityBranch = cityBranch;
    }

    public CompanyUser getCompanyUser() {
        return companyUser;
    }

    public void setCompanyUser(CompanyUser companyUser) {
        this.companyUser = companyUser;
    }
}
