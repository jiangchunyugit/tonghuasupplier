package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.AfConfig;
import cn.thinkfree.database.model.UserRoleSet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:32
 */
@Data
@ApiModel(value = "审批流配置")
public class AfConfigVO {
    @ApiModelProperty(value = "审批流配置")
    private AfConfig config;
    @ApiModelProperty(name = "审批顺序方案")
    private List<AfPlanVO> plans;
    @ApiModelProperty(name = "订阅消息角色")
    private List<UserRoleSet> subRoles;
}
