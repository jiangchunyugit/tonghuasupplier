package cn.thinkfree.database.vo;

import cn.thinkfree.database.appvo.ConstructionProjectVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 施工订单统计
 *
 * @author song
 * @version 1.0
 * @date 2018/12/11 16:34
 */
@ApiModel
@Data
public class ConstructCountVO {
    @ApiModelProperty("项目总数")
    private Integer total;
    @ApiModelProperty("进度验收总数")
    private Integer checkCount;
    @ApiModelProperty("问题整改总数")
    private Integer problemCount;
    @ApiModelProperty("延期确认总数")
    private Integer delayCount;
    @ApiModelProperty("施工变更总数")
    private Integer changeCount;
    @ApiModelProperty("项目列表")
    private List<ConstructionProjectVo> projects;
}
