package cn.thinkfree.database.vo.construct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业主信息
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 11:27
 */
@ApiModel("业主信息")
@Data
public class ConsumerDetailVO {
    @ApiModelProperty("业主姓名")
    private String name;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("项目地址")
    private String projectAddress;
}
