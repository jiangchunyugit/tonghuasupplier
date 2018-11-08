package cn.thinkfree.database.vo.account;

import cn.thinkfree.core.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

 @ApiModel("用于用户列表展示")
public class AccountListVO extends BaseModel {

    private String id;
    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String account;
    /**
     * 名称
     */
    private String name;
    /**
     * 部门
     */
    @ApiModelProperty("部门")
    private String dept;
    /**
     * 分公司
     */
    @ApiModelProperty("分公司")
    private String branchCompanyName;
    /**
     * 分站
     */
    @ApiModelProperty("分站")
    private String cityBranchCompanyName;
    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private String state;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

     /**
      * 角色
      */
     @ApiModelProperty("角色")
    private String roles;

     public String getRoles() {
         return roles;
     }

     public void setRoles(String roles) {
         this.roles = roles;
     }

     public String getId() {
         return id;
     }

     public void setId(String id) {
         this.id = id;
     }

     public String getAccount() {
         return account;
     }

     public void setAccount(String account) {
         this.account = account;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public String getDept() {
         return dept;
     }

     public void setDept(String dept) {
         this.dept = dept;
     }

     public String getBranchCompanyName() {
         return branchCompanyName;
     }

     public void setBranchCompanyName(String branchCompanyName) {
         this.branchCompanyName = branchCompanyName;
     }

     public String getCityBranchCompanyName() {
         return cityBranchCompanyName;
     }

     public void setCityBranchCompanyName(String cityBranchCompanyName) {
         this.cityBranchCompanyName = cityBranchCompanyName;
     }

     public String getState() {
         return state;
     }

     public void setState(String state) {
         this.state = state;
     }

     public Date getCreateTime() {
         return createTime;
     }

     public void setCreateTime(Date createTime) {
         this.createTime = createTime;
     }

     public String getPhone() {
         return phone;
     }

     public void setPhone(String phone) {
         this.phone = phone;
     }

     public String getEmail() {
         return email;
     }

     public void setEmail(String email) {
         this.email = email;
     }
 }
