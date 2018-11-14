package cn.thinkfree.service.construction.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 项目派单列表-反参实体
 * 装饰公司
 */
@Getter
@Setter
@ApiModel(value = "项目派单列表-反参实体（装饰公司）--迎接专用")
public class DecorationOrderListVo {


    @ApiModelProperty("项目编号1")
    private String projectNo;

    @ApiModelProperty("预约日期2")
    private Date appointmentTime;

    @ApiModelProperty("项目地址3")
    private String addressDetail;

    @ApiModelProperty("业主4")
    private String owner;

    @ApiModelProperty("手机号码5")
    private String phone;

    @ApiModelProperty("预约报价6")
    private String appointmentPrice;

    @ApiModelProperty("订单状态7")
    private String orderStage;

    @ApiModelProperty("设计师8")
    private String designerName;

    @ApiModelProperty("项目经理9")
    private String projectManager;

    @ApiModelProperty("工长10")
    private String headWork;

    @ApiModelProperty("管家11")
    private String housekeeper;

    @ApiModelProperty("项目编号12")
    private String orderNo;

}
