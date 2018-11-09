package cn.thinkfree.database.vo;

/**
 * @Auther: jiang
 * @Date: 2018/9/30 14:29
 * @Description:
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目详情
 */
@ApiModel("项目施工节点")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBigSchedulingVO {

    @ApiModelProperty("公司编号")
    private String companyId;
    @ApiModelProperty("序号")
    private Integer sort;
    @ApiModelProperty("施工阶段")
    private String name;
    @ApiModelProperty("施工阶段说明")
    private String description;
//    @ApiModelProperty("前置施工阶段")
//    private String predecessor;
    @ApiModelProperty("新旧")
    private Integer isNew;
    @ApiModelProperty("下限平米")
    private Integer squareMetreStart;
    @ApiModelProperty("上限平米")
    private Integer squareMetreEnd;
    @ApiModelProperty("户型")
    private Integer roomNum;
    @ApiModelProperty("工程量")
    private Integer workload;
    @ApiModelProperty("是否需要验收")
    private Integer check;
}
