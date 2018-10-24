package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 审批流审批信息
 *
 * @author song
 * @version 1.0
 * @date 2018/10/18 16:34
 */
@Data
@ApiModel("审批流审批信息")
public class ApprovalFlowApprovalVO {

    @ApiModelProperty("用户Id")
    private String userId;
    @ApiModelProperty("项目编号")
    private String projectNo;
    @ApiModelProperty("公司编号")
    private String companyNo;
    @ApiModelProperty("审批流配置记录编号")
    private String configLogNum;
    @ApiModelProperty("审批流实例编号")
    private String instanceNum;
    @ApiModelProperty("项目排期编号")
    private Integer scheduleSort;
    @ApiModelProperty("项目排期版本号")
    private Integer scheduleVersion;
    @ApiModelProperty("审批数据")
    private String data;
    @ApiModelProperty("审批备注信息")
    private String remark;
    @ApiModelProperty("审批节点编号")
    private String nodeNum;
    @ApiModelProperty("审批操作项编号")
    private String optionNum;
}
