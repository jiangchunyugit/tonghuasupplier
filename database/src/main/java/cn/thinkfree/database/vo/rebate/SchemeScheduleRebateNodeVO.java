package cn.thinkfree.database.vo.rebate;

import cn.thinkfree.database.model.RebateNode;
import cn.thinkfree.database.model.SchemeScheduleRebateNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 排期与返款节点编号关系
 *
 * @author song
 * @version 1.0
 * @date 2018/12/19 11:10
 */
@ApiModel("排期与返款节点编号关系")
@Data
public class SchemeScheduleRebateNodeVO {

    @ApiModelProperty("创建用户")
    private String createUserId;
    @ApiModelProperty("方案编号")
    private String schemeNo;
    @ApiModelProperty("返款节点编号")
    private List<RebateNode> rebateNodes;
    @ApiModelProperty("排期与返款节点编号关系")
    private List<SchemeScheduleRebateNode> schemeScheduleRebateNodes;
}
