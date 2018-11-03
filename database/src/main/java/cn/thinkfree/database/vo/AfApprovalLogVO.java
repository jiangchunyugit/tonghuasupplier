package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 审批流审批信息
 *
 * @author song
 * @version 1.0
 * @date 2018/10/17 13:56
 */
@Data
@ApiModel("审批流审批信息")
public class AfApprovalLogVO {
    @ApiModelProperty("用户Id")
    private String userId;
    @ApiModelProperty("用户姓名")
    private String userName;
    @ApiModelProperty("用户角色Id")
    private String roleId;
    @ApiModelProperty("用户角色名称")
    private String roleName;
    @ApiModelProperty("用户头像")
    private String headPortrait;
    @ApiModelProperty("审批时间")
    private Date approvalTime;
    @ApiModelProperty("审批状态，1：发起；2：同意；3：拒绝；4：未审批")
    private Integer status;
    @ApiModelProperty("等待审批提示")
    private String waitTip;
}
