package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: jiang
 * @Date: 2018/11/28 14:44
 * @Description: 预约量房详情
 */
@Data
public class VolumeReservationDetailsVO {

    @ApiModelProperty("订单编号")
    private String designOrderNo;
    @ApiModelProperty("业主姓名")
    private String ownerName;
    @ApiModelProperty("业主手机号")
    private String ownerPhone;
    @ApiModelProperty("所在地")
    private String address;
    @ApiModelProperty("订单来源")
    private String orderSource;
    @ApiModelProperty("户型")
    private String houseType;
    @ApiModelProperty("常住人口")
    private Integer permanentResidents;
    @ApiModelProperty("建筑面积")
    private String area;
    @ApiModelProperty("招揽公司")
    private String companyName;
    @ApiModelProperty("设计师")
    private String designerName;
    @ApiModelProperty("房屋类型")
    private Integer propertyType;
    @ApiModelProperty("小区名称")
    private String communityName;
    @ApiModelProperty("装饰地点")
    private String decorationLocation;
    @ApiModelProperty("量房地点")
    private String measuringRoomLocation;


}
