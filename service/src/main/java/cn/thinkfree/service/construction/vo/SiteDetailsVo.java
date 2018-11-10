package cn.thinkfree.service.construction.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Auther: jiang
 * @Date: 2018/11/9 10:52
 * @Description:
 */
@Getter
@Setter
@ApiModel("工地信息详情")
public class SiteDetailsVo {
    @ApiModelProperty("项目编号")
    private  String projectNo;
    @ApiModelProperty("订单编号")
    private  String orderNo;
    @ApiModelProperty("订单类型")
    private  String orderType;

    @ApiModelProperty("业主")
    private  String owner;
    @ApiModelProperty("手机号码")
    private  String phone;
    @ApiModelProperty("项目地址")
    private  String projectAddress;

    @ApiModelProperty("开工日期")
    private Date StartDates;
    @ApiModelProperty("竣工日期")
    private Date completionDays;
    @ApiModelProperty("工期")
    private Integer duration;
    @ApiModelProperty("施工进度 1:准备阶段 2:拆除工程  3: 门窗工程 4:水电工程 5:泥瓦工程 6:木工施工工程 7:涂料喷涂工程 8:安装工程 9:竣工阶段")
    private Integer constructionSchedule;
    @ApiModelProperty("延期天数")
    private Integer deferredDays;

    @ApiModelProperty("项目经理")
    private String projectManager;
    @ApiModelProperty("工长")
    private String foreman;
    @ApiModelProperty("管家")
    private String housekeeper;
    @ApiModelProperty("设计师")
    private String designerName;

    @ApiModelProperty("合同款")
    private Integer contractFunds;
    @ApiModelProperty("已付款")
    private String paid;
    @ApiModelProperty("待付款")
    private String pendingPayment;



}
