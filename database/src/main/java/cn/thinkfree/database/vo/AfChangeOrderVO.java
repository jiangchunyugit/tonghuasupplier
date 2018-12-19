package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 变更单变更费用
 *
 * @author song
 * @version 1.0
 * @date 2018/12/5 14:51
 */
@ApiModel
@Data
public class AfChangeOrderVO {
    @ApiModelProperty("变更项")
    private List<AfChangeOrder> changeOrders;
    @ApiModelProperty("其他费用变更")
    private List<AfOtherChange> otherChanges;
    @ApiModelProperty("变更原因")
    private String changeCause;
}
