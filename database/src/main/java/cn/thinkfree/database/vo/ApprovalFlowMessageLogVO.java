package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ApprovalFlowMessageLog;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 审批消息
 *
 * @author song
 * @version 1.0
 * @date 2018/10/23 10:43
 */
@ApiModel("审批消息")
@Data
public class ApprovalFlowMessageLogVO extends ApprovalFlowMessageLog {

    @ApiModelProperty("审批流名称")
    private String configName;
    @ApiModelProperty("消息发送者姓名")
    private String sendUserName;
    @ApiModelProperty("消息发送者角色名称")
    private String sendRoleName;
    @ApiModelProperty("头像")
    private String headPortrait;

}
