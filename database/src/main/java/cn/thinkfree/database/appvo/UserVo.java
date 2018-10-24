package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
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
}
