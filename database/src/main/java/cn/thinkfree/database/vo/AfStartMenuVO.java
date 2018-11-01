package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/31 18:36
 */
@Data
@ApiModel("审批流发起菜单")
public class AfStartMenuVO {
    @ApiModelProperty("审批流配置编号")
    private String configNo;
    @ApiModelProperty("审批流配置名称")
    private String configName;
}
