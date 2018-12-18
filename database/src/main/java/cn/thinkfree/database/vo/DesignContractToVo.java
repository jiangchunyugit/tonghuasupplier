package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesignContractToVo {

    //业主名称
    @ApiModelProperty("业主名称")
    private String ownerName;

    //设计公司名称
    @ApiModelProperty("设计公司名称")
    private String designName;

    @ApiModelProperty("设计师名称")
    private String designerName;

    @ApiModelProperty("设计师电话")
    private String designerTel;

   //业主电话
    @ApiModelProperty("业主电话")
    private String ownerTel;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("省")
    private String province;


    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String region;


    @ApiModelProperty("详细地址")
    private String addressDetail;

    @ApiModelProperty("建筑面积")
    private String area;

    @ApiModelProperty("量房费")
    private String volumeRoomMoney;


    @ApiModelProperty("合同类型")
    private String contractType;

}
