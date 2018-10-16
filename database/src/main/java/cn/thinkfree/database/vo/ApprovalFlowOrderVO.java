package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ApprovalFlowConfig;
import cn.thinkfree.database.model.ApprovalFlowConfigSuper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/16 15:09
 */
@ApiModel("审批流顺序")
@Data
public class ApprovalFlowOrderVO {
    /**
     * 审批流配置
     */
    @ApiModelProperty(name = "审批流配置")
    private ApprovalFlowConfig config;
    @ApiModelProperty(name = "审批流顺序")
    private List<ApprovalFlowConfigSuper> configSupers;
}
