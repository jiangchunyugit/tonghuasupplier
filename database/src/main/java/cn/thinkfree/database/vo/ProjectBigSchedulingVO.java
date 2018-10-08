package cn.thinkfree.database.vo;

/**
 * @Auther: jiang
 * @Date: 2018/9/30 14:29
 * @Description:
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目详情
 */
@ApiModel("项目施工节点")
@Data
public class ProjectBigSchedulingVO {

    @ApiModelProperty("公司编号")
    private String companyId;
    @ApiModelProperty("序号")
    private Integer sort;
    @ApiModelProperty("节点名称")
    private String rename;
    @ApiModelProperty("节点阶段")
    private String name;
    @ApiModelProperty("节点描述")
    private String description;
    @ApiModelProperty("是否需要验收")
    private boolean check;
    @ApiModelProperty("是否需要支付")
    private boolean pay;
    @ApiModelProperty("对应支付节点")
    private Integer payNode;
    @ApiModelProperty("未支付预警额度")
    private Integer noPayQuota;
    @ApiModelProperty("是否需要上传附件")
    private boolean uploadEnclosure;
    @ApiModelProperty("附件类型")
    private String enclosureType;

}
