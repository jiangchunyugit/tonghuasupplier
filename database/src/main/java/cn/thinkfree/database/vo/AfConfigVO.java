package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.UserRoleSet;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 审批流配置
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:32
 */
@Data
@ApiModel(value = "审批流配置")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AfConfigVO {
    @ApiModelProperty(value = "审批流配置编号")
    private String configNo;
    @ApiModelProperty(value = "审批流配置名称")
    private String name;
    @ApiModelProperty(value = "审批流配置描述")
    private String describe;
    @ApiModelProperty(name = "审批角色顺序")
    private List<UserRoleSet> approvalRoles;
    @ApiModelProperty(name = "订阅消息角色")
    private List<UserRoleSet> subRoles;
}
