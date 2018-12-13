package cn.thinkfree.service.customercomplaint.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 客诉工单信息VO
 */
@Getter
@Setter
@ApiModel(value = "客诉工单信息VO")
public class CreateComplaintVO {

    @ApiModelProperty("客诉工单编号")
    private String workOrderNo;

    @ApiModelProperty("处理状态 1待处理 2处理中 3已关闭 4平台介入")
    private Short processState;

    @ApiModelProperty("投诉类型 1设计 2施工 3材料 4其他")
    private Short complaintType;

    @ApiModelProperty("处理优先级 1紧急 2一般")
    private Short priority;

    @ApiModelProperty("被投诉的项目编号")
    private String projectNo;

    @ApiModelProperty("投诉内容")
    private String complaintInfo;

    @ApiModelProperty("投诉时间")
    private Date complaintTime;

    @ApiModelProperty("处理人(创建人ID)")
    private String handleUserId;

    @ApiModelProperty("设计公司订单编号")
    private String designerOrderNo;

    @ApiModelProperty("装饰公司订单编号")
    private String constructionOrderNo;

    @ApiModelProperty("图片集合")
    private String imgList;

    @ApiModelProperty("业主姓名")
    private String name;

    @ApiModelProperty("业主电话")
    private String phone;

    @ApiModelProperty("设计师姓名")
    private String designerName;

    @ApiModelProperty("所属设计公司")
    private String companyName;

    @ApiModelProperty("项目详细地址")
    private String address;

    @ApiModelProperty("完成时间")
    private String endTime;

}