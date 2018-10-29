package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/27 15:16
 */
@Data
@ApiModel("审批流实例")
public class AfInstanceVO {
    @ApiModelProperty("审批流实例编号")
    private String instanceNo;
    @ApiModelProperty("排期编号")
    private Integer scheduleSort;
    @ApiModelProperty("审批流配置名称")
    private String configName;
    @ApiModelProperty("创建用户Id")
    private String createUserId;
    @ApiModelProperty("创建用户姓名")
    private String createUsername;
    @ApiModelProperty("角色Id")
    private String roleId;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("审批备注")
    private String remark;
    @ApiModelProperty("审批状态，1：审批中；2：审批完成，同意；3：审批完成，不同意")
    private Integer status;
    @ApiModelProperty("是否显示审批按钮")
    private Boolean isShowButton;
}
