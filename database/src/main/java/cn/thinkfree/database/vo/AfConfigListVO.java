package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.UserRoleSet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 展示审批流配置列表
 *
 * @author song
 * @version 1.0
 * @date 2018/11/7 18:41
 */
@Data
@ApiModel("审批流配置列表与角色信息")
public class AfConfigListVO {
    @ApiModelProperty("审批流配置信息")
    private List<AfConfigVO> configs;
    @ApiModelProperty("所有角色信息")
    private List<UserRoleSet> roles;
}
