package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 验收项
 *
 * @author song
 * @version 1.0
 * @date 2018/12/4 16:28
 */
@ApiModel
@Data
public class AfCheckItemVO {
    @ApiModelProperty("验收项类型")
    private Integer type;
    @ApiModelProperty("验收项名称")
    private String name;
}
