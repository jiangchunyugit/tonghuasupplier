package cn.thinkfree.service.construction.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author xusonghui
 * 项目信息
 */
public class OfferProjectVo {
    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("装修地址")
    private String address;
    @ApiModelProperty("业主姓名")
    private String ownerName;
    @ApiModelProperty("业主手机号")
    private String phone;
    @ApiModelProperty("房屋类型")
    private String houseType;
    @ApiModelProperty("户型")
    private String huxing;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否完成设计")
    private String isDesign;
    @ApiModelProperty("设计公司名称")
    private String designCompanyName;
    @ApiModelProperty("设计师姓名")
    private String designerName;
    @ApiModelProperty("效果图")
    private String effectPhoto;
    @ApiModelProperty("施工图")
    private String constructionPhoto;
    @ApiModelProperty("3D漫游")
    private String phtoto3D;

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getHuxing() {
        return huxing;
    }

    public void setHuxing(String huxing) {
        this.huxing = huxing;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsDesign() {
        return isDesign;
    }

    public void setIsDesign(String isDesign) {
        this.isDesign = isDesign;
    }

    public String getDesignCompanyName() {
        return designCompanyName;
    }

    public void setDesignCompanyName(String designCompanyName) {
        this.designCompanyName = designCompanyName;
    }

    public String getDesignerName() {
        return designerName;
    }

    public void setDesignerName(String designerName) {
        this.designerName = designerName;
    }

    public String getEffectPhoto() {
        return effectPhoto;
    }

    public void setEffectPhoto(String effectPhoto) {
        this.effectPhoto = effectPhoto;
    }

    public String getConstructionPhoto() {
        return constructionPhoto;
    }

    public void setConstructionPhoto(String constructionPhoto) {
        this.constructionPhoto = constructionPhoto;
    }

    public String getPhtoto3D() {
        return phtoto3D;
    }

    public void setPhtoto3D(String phtoto3D) {
        this.phtoto3D = phtoto3D;
    }
}
