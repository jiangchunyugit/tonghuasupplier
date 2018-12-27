package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.PcAuditInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("审批")
public class PcAuditInfoVO extends PcAuditInfo {
    @ApiModelProperty(value = "运营入驻审批时候的必填字段：角色id BD：装饰， SJ：设计  DR:经销商")
    private String roleId;

    @ApiModelProperty(value = "运营资质变更审批的必填字段：申请id")
    private String applyId;

    @ApiModelProperty(value = "运营经销商品牌品类审批的必填字段：品牌表的id字段")
    private String brandId;

    @ApiModelProperty(value = "运营经销商品牌品类审批的必填字段：品牌编号")
    private String brandNo;

    @ApiModelProperty(value = "运营经销商品牌品类审批的必填字段：品类编号")
    private String categoryNo;

    public String getBrandNo() {
        return brandNo;
    }

    public void setBrandNo(String brandNo) {
        this.brandNo = brandNo;
    }

    public String getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(String categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
