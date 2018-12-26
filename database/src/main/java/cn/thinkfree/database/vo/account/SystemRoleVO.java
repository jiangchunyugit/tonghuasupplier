package cn.thinkfree.database.vo.account;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.database.constants.RoleScope;
import cn.thinkfree.database.model.SystemRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("系统角色")
public class SystemRoleVO extends SystemRole {

    /**
     * 创建者姓名
     */
    @ApiModelProperty("创建人姓名")
    private String creatorName;

    /**
     * 拥有的权限
     */
    @ApiModelProperty("拥有的权限")
    private String permissions;



    /**
     * 总部
     */
    @ApiModelProperty("总部适用")
    private Integer root = 0;
    /**
     * 省份
     */
    @ApiModelProperty("省级适用")
    private Integer province= 0;
    /**
     * 市
     */
    @ApiModelProperty("市级适用")
    private Integer city = 0;

    /**
     * 是否授权
     */
    @ApiModelProperty("是否授权")
    private String isGrant;

    public String getIsGrant() {
        return isGrant;
    }

    public void setIsGrant(String isGrant) {
        this.isGrant = isGrant;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }



    public Integer getRoot() {
        return root;
    }
    @ApiModelProperty("是否选中总部")
    public Integer getSelectedRoot(){


        return (getScope() == null || getScope() == 0) ? 0 : (RoleScope.ROOT.code.intValue() <= getScope()) ? 1 : 0  ;
    }

    public void setRoot(Integer root) {
        this.root = root;
    }

    public Integer getProvince() {
        return province;
    }
    @ApiModelProperty("是否选中省级")
    public Integer getSelectedProvince(){
        if(getScope() == null || getScope() == 0){
            return Integer.valueOf(SysConstants.YesOrNo.NO.val.toString());
        }
        if(RoleScope.COMMON.code.equals(getScope()) || RoleScope.PROVINCE.code.equals(getScope())){
            return Integer.valueOf(SysConstants.YesOrNo.YES.val.toString());
        }
        if(getScope() == 3 || getScope() == 6){
            return Integer.valueOf(SysConstants.YesOrNo.YES.val.toString());
        }
        return Integer.valueOf(SysConstants.YesOrNo.NO.val.toString());
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }
    @ApiModelProperty("是否选中市级")
    public Integer getSelectedCity(){
        if(getScope() == null || getScope() == 0){
            return Integer.valueOf(SysConstants.YesOrNo.NO.val.toString());
        }
        if(RoleScope.COMMON.code.equals(getScope()) || RoleScope.CITY.code.equals(getScope())){
            return Integer.valueOf(SysConstants.YesOrNo.YES.val.toString());
        }
        if(getScope() == 3 || getScope() == 5){
            return Integer.valueOf(SysConstants.YesOrNo.YES.val.toString());
        }
        return Integer.valueOf(SysConstants.YesOrNo.NO.val.toString());
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
