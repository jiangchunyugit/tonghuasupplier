package cn.thinkfree.database.vo.construct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 延期信息
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 11:46
 */
@ApiModel("延期信息")
@Data
public class DelayDetailVO {
    @ApiModelProperty("延期天数")
    private Integer delayDays;
    @ApiModelProperty("业主确认延期天数")
    private Integer affirmDelayDays;
}
