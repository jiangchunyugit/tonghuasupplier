package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 变更单其他费用
 *
 * @author song
 * @version 1.0
 * @date 2018/12/5 14:53
 */
@ApiModel
@Data
public class AfOtherChange {
    @ApiModelProperty("费用名称")
    private String expenseName;
    @ApiModelProperty("0,减项;1,增项")
    private Integer changeType;
    @ApiModelProperty("费用")
    private String amount;
    @ApiModelProperty("费用说明")
    private String describe;
}
