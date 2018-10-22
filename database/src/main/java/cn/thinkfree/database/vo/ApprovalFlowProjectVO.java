package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 项目信息
 *
 * @author song
 * @version 1.0
 * @date 2018/10/17 11:55
 */
@Data
@ApiModel("项目信息")
public class ApprovalFlowProjectVO {
    /**
     * 项目编号
     */
    @ApiModelProperty("项目编号")
    private String projectNo;
    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;
    /**
     * 户型描述
     */
    @ApiModelProperty("户型描述")
    private String houseType;
    /**
     * 业主Id
     */
    @ApiModelProperty("业主Id")
    private String ownerId;
}
