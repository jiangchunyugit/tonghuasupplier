package cn.thinkfree.database.vo.account;

import cn.thinkfree.database.constants.RoleScope;
import cn.thinkfree.database.model.SystemRole;

public class SystemRoleVO extends SystemRole {

    /**
     * 创建者姓名
     */
    private String creatorName;

    /**
     * 拥有的权限
     */
    private String permissions;



    /**
     * 总部
     */
    private Integer root = 0;
    /**
     * 省份
     */
    private Integer province= 0;
    /**
     * 市
     */
    private Integer city = 0;


    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }



    public Integer getRoot() {
        return root;
    }
    public Integer getSelectedRoot(){
        return (getScope() == null || getScope() == 0) ? 0 :
                RoleScope.COMMON.code.equals(getScope()) ? 1:
                        RoleScope.ROOT.code.equals(getScope()) ? 1:0;
    }

    public void setRoot(Integer root) {
        this.root = root;
    }

    public Integer getProvince() {
        return province;
    }
    public Integer getSelectedProvince(){
        return (getScope() == null || getScope() == 0) ? 0 :
                RoleScope.COMMON.code.equals(getScope()) ? 1:
                        RoleScope.PROVINCE.code.equals(getScope()) ? 1:0;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }
    public Integer getSelectedCity(){
        return (getScope() == null || getScope() == 0) ? 0 :
                RoleScope.COMMON.code.equals(getScope()) ? 1:
                        RoleScope.CITY.code.equals(getScope()) ? 1:0;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
