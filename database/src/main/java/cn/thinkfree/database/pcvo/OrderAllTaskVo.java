package cn.thinkfree.database.pcvo;

import cn.thinkfree.database.appvo.OrderTaskSortVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@ApiModel("施工端所有项目阶段信息")
@Data
public class OrderAllTaskVo {
    @ApiModelProperty("阶段实体")
    private List<OrderTaskSortVo> allOrderTask;
    @ApiModelProperty("当前阶段")
    private Integer currentSort;
}
