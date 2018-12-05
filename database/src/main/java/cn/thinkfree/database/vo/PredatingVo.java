package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gejiaming
 */
@Data
@ApiModel("预交底详情")
public class PredatingVo {
    @ApiModelProperty("业主姓名")
    private String ownerName;
    @ApiModelProperty("业主手机号")
    private String ownerPhone;
    @ApiModelProperty("户型")
    private String houseType;
    @ApiModelProperty("建筑面积")
    private Integer area;
    @ApiModelProperty("房屋类型")
    private String propertyType;
    @ApiModelProperty("装饰地点")
    private String decorationLocation;
    @ApiModelProperty("预约时间")
    private Long predatingTime;
    @ApiModelProperty("备注")
    private String remark;
}
