package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 *@author gejiaming
 */
@Data
@ApiModel(value = "UserVo,用户详情")
public class UserVo {
    @ApiModelProperty(value = "用户编码")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    private String realName;

    @ApiModelProperty(value = "角色")
    private String roleCode;

    @ApiModelProperty(value = "角色中文名")
    private String roleName;
}
