package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author gejiaming
 */
@ApiModel(value = "ProjectSmallSchedulingVO,基础小排期详情")
@Data
public class ProjectSmallSchedulingVO {
    @ApiModelProperty(value = "排序号")
    private Integer sort;
    @ApiModelProperty(value = "大排期序号")
    private Integer parentSort;
    @ApiModelProperty(value = "施工项ID")
    private Integer constructId;
    @ApiModelProperty(value = "施工项类别")
    private String constructCategory;
    @ApiModelProperty(value = "施工项分类")
    private String constructItem;
    @ApiModelProperty(value = "施工项编码")
    private String constructCode;
    @ApiModelProperty(value = "施工项名称")
    private String constructName;
    @ApiModelProperty(value = "计量单位")
    private String unitCode;
    @ApiModelProperty(value = "工艺材料简介")
    private String desc;
    @ApiModelProperty(value = "辅料名称规格")
    private String assitSpec;
    @ApiModelProperty(value = "验收标准")
    private String standard;
    @ApiModelProperty(value = "标准出处")
    private String sourceOfStandard;
    @ApiModelProperty(value = "施工项状态")
    private Integer status;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "装饰公司")
    private String decorateCompany;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "更新人")
    private String updateBy;

}
