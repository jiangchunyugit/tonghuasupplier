package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @author gejiaming
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "ProjectVo--aa",description = "项目详情")
public class ProjectVo {
    @ApiModelProperty(name = "projectNo",value = "项目编号")
    private String projectNo;
    @ApiModelProperty(name = "designProgress",value = "设计进度")
    private Integer designProgress;
    @ApiModelProperty(name = "constructionProgress",value = "施工进度")
    private Integer constructionProgress;
    @ApiModelProperty(name = "address",value = "项目地址")
    private String address;
    @ApiModelProperty(name = "releaseTime",value = "发布时间")
    private Date releaseTime;
    @ApiModelProperty(name = "imgUrl",value = "图片地址")
    private String imgUrl;
    @ApiModelProperty(name = "view3d",value = "是否是3D")
    private Boolean view3d;
    @ApiModelProperty(name = "projectDynamic",value = "项目动态")
    private Integer projectDynamic;
    @ApiModelProperty(name = "projectOrder",value = "项目订单")
    private Integer projectOrder;
    @ApiModelProperty(name = "projectData",value = "项目资料")
    private Integer projectData;
    @ApiModelProperty(name = "owner",value = "业主实体")
    private PersionVo owner;
    @ApiModelProperty(name = "projectOrderDetailVoList",value = "订单模块集合")
    private List<ProjectOrderDetailVo> projectOrderDetailVoList;


}
