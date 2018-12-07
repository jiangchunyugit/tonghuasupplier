package cn.thinkfree.database.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 变更单变更项
 *
 * @author song
 * @version 1.0
 * @date 2018/12/5 14:52
 */
@ApiModel
@Data
public class AfChangeOrder {
    @ApiModelProperty("施工项编号")
    private String constructionNo;
    @ApiModelProperty("施工项名称")
    private String constructionName;
    @ApiModelProperty("0,减项;1,增项")
    private Integer changeType;
    @ApiModelProperty("单价")
    private String unitPrice;
    @ApiModelProperty("数量")
    private Integer count;
}
