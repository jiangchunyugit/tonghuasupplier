package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 审批流审批用户信息
 *
 * @author song
 * @version 1.0
 * @date 2018/10/17 13:56
 */
@Data
@ApiModel("审批流审批用户信息")
public class ApprovalFlowUserVO {
    @ApiModelProperty("审批流节点编号")
    private String nodeNum;
    @ApiModelProperty("用户Id")
    private String userId;
    @ApiModelProperty("用户姓名")
    private String userName;
    @ApiModelProperty("用户角色Id")
    private String roleId;
    @ApiModelProperty("用户角色名称")
    private String roleName;
    @ApiModelProperty("审批时间")
    private Date approvalTime;
    @ApiModelProperty("是否审批过")
    private Boolean isOperated;
}
