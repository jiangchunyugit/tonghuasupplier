package cn.thinkfree.database.vo.construct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 施工信息
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 11:31
 */
@ApiModel("施工信息")
@Data
public class ConstructDetailVO {
    @ApiModelProperty("开工日期")
    private Date startDate;
    @ApiModelProperty("竣工日期")
    private Date completeDate;
    @ApiModelProperty("工期")
    private Integer limitDays;
    @ApiModelProperty("施工进度")
    private String constructProgress;
    @ApiModelProperty("延期天数")
    private Integer delayDays;
}
