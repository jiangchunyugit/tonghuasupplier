package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "设计信息实体")
@Data
public class DesignVo {
    @ApiModelProperty("订单详情")
    private ProjectDetailVO projectDetailVO;
    @ApiModelProperty("预交底详情")
    private PredatingVo predatingVo;
    @ApiModelProperty("量房信息实体")
    private VolumeReservationDetailsVO volumeReservationDetailsVO;
}
