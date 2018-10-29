package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/27 16:20
 */
@Data
@ApiModel("审批流配置方案")
public class AfConfigPlanVO {
    @ApiModelProperty("审批流配置编号")
    private String configNo;
    @ApiModelProperty("审批流配置名称")
    private String configName;
    @ApiModelProperty("审批顺序方案编号")
    private String planNo;
}
