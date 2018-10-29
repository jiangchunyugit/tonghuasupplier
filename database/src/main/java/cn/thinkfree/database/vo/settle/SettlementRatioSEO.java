package cn.thinkfree.database.vo.settle;


import cn.thinkfree.database.vo.AbsPageSearchCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 计算比例 分页查询
 * @author lvqidong
 *
 */
@ApiModel(description = "计算比例分页查询")
public class SettlementRatioSEO extends AbsPageSearchCriteria  {

    @ApiModelProperty("结算比例编号")
    private String ratioNumber;
    
    @ApiModelProperty("结算状态")
    private String ratioStatus;
    
    @ApiModelProperty("结算比例名称")
    private String ratioName;
    
    @ApiModelProperty("创建人")
    private String createUser;
    
    @ApiModelProperty("开始时间")
    private String startTime;
    
    @ApiModelProperty("结束时间")
    private String endTime;
    
    @ApiModelProperty("审核状态")
    private String auditStatus;
    
    

    
}
