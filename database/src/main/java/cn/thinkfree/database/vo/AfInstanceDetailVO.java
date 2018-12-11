package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 审批流实例
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 17:43
 */
@Data
@ApiModel("审批流实例")
public class AfInstanceDetailVO {

    @ApiModelProperty("项目信息")
    private String projectNo;
    @ApiModelProperty("业主ID")
    private String customerId;
    @ApiModelProperty("业主姓名")
    private String customerName;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("审批流配置编号")
    private String configNo;
    @ApiModelProperty("排期编号")
    private Integer scheduleSort;
    @ApiModelProperty("审批流配置名称")
    private String configName;
    @ApiModelProperty("审批流实例编号")
    private String instanceNo;
    @ApiModelProperty("审批人列表")
    private List<AfApprovalLogVO> approvalLogs;
    @ApiModelProperty("是否显示审批按钮")
    private Boolean isShowButton;
    @ApiModelProperty("审批状态：1：正在进行；2：结束，审批通过；3结束，审批不通过")
    private Integer status;
    @ApiModelProperty("审批数据")
    private String data;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("拒绝原因")
    private String refusalReason;
    @ApiModelProperty("验收项集合")
    private List<AfCheckItemVO> checkItems;
    @ApiModelProperty("关联实例数据")
    private String relevancyDate;
    @ApiModelProperty("最大延期天数")
    private Integer maxDelayDays;
}
