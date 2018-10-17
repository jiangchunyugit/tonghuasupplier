package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Auther: jiang
 * @Date: 2018/10/16 15:19
 * @Description:订单确定
 */
public class OrderConfirmationVO {
    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("公司编号")
    private Integer companyId;
    @ApiModelProperty("订单状态")
    private Short orderStage;

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Short getOrderStage() {
        return orderStage;
    }

    public void setOrderStage(Short orderStage) {
        this.orderStage = orderStage;
    }

    public OrderConfirmationVO(String projectNo, Integer companyId, Short orderStage) {
        this.projectNo = projectNo;
        this.companyId = companyId;
        this.orderStage = orderStage;
    }
}
