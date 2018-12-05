package cn.thinkfree.database.vo.agency;

import cn.thinkfree.database.vo.AbsPageSearchCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "经销商合同列表查询参数")
public class AgencySEO extends AbsPageSearchCriteria {

    @ApiModelProperty(value = "合同编码")
    private String contractNumber;

    @ApiModelProperty(value = "经销商编码/名称")
    private String agency;



    @ApiModelProperty(value = "品牌编码/名称")
    private String brand;

    @ApiModelProperty(value = "品类编码/名称")
    private String category;

    @ApiModelProperty(value = "分公司code")
    private String filialeId;

    @ApiModelProperty(value = "城市站code")
    private String metroId;

    @ApiModelProperty(value = "状态")
    private String status;


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @ApiModelProperty(value = "开始时间")
    private String startTime;


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilialeId() {
        return filialeId;
    }

    public void setFilialeId(String filialeId) {
        this.filialeId = filialeId;
    }

    public String getMetroId() {
        return metroId;
    }

    public void setMetroId(String metroId) {
        this.metroId = metroId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;

}
