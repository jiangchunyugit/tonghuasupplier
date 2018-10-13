package cn.thinkfree.database.appVo;

import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "ConstructionOrderVo",description = "施工订单详情")
@Data
public class ConstructionOrderVo {
    @ApiModelProperty(name = "orderNo",value = "订单编号")
    private String orderNo;
    @ApiModelProperty(name = "type",value = "装修类型")
    private Integer type;
//    @ApiModelProperty(name = "",value = "")
//    private
    @ApiModelProperty(name = "projectBigSchedulingVOList",value = "施工任务")
    private List<ProjectBigSchedulingVO> projectBigSchedulingVOList;
//    @ApiModelProperty(name = "",value = "")

}
