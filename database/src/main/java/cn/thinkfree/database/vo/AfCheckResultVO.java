package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 验收记录
 *
 * @author song
 * @version 1.0
 * @date 2018/12/21 15:43
 */
@ApiModel("验收记录")
@Data
public class AfCheckResultVO {
    @ApiModelProperty("实例编号")
    private String instanceNo;
    @ApiModelProperty("验收名称")
    private String checkName;
    @ApiModelProperty("验收时间")
    private Date checkTime;
    @ApiModelProperty("是否验收，1：是；0：否；")
    private Integer isCheck;
    @ApiModelProperty("验收结果，1：通过；0：不通过；")
    private Integer checkResult;
    @ApiModelProperty("支付状态，1：已支付；0：未支付；")
    private Integer payResult;
}
