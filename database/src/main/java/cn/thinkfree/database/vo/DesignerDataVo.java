package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "设计师个人信息")
@Data
public class DesignerDataVo {
    @ApiModelProperty("用户编号")
    private String userId;
    @ApiModelProperty("性别 1男，2女")
    private Integer sex;
    @ApiModelProperty("生日 yyyy-MM-dd")
    private String birthday;
    @ApiModelProperty("省份编码")
    private String province;
    @ApiModelProperty("城市编码")
    private String city;
    @ApiModelProperty("地区编码")
    private String area;
    @ApiModelProperty("从业年限")
    private Integer years;
    @ApiModelProperty("量房费")
    private String volumeRoomMoney;
    @ApiModelProperty("设计费用最低(单位：元/m2)")
    private String moneyLow;
    @ApiModelProperty("设计费用最高(单位：元/m2)")
    private String moneyHigh;
    @ApiModelProperty("风格编码")
    private List<String> styleCodes;
    @ApiModelProperty("个人简介")
    private String personalProfile;
    @ApiModelProperty("证书与奖项")
    private String certificatePrize;
}
