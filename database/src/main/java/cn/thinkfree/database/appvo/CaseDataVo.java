package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@ApiModel(value = "案例信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseDataVo {
    @ApiModelProperty(value = "项目编号")
    private String projectNo;
    @ApiModelProperty(value = "用户编号")
    private String userId;
    @ApiModelProperty(value = "案例名称")
    private String caseName;
    @ApiModelProperty(value = "案例资料名称")
    private String caseDataName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "案例上传时间")
    private String caseUploadTime;
    @ApiModelProperty(value = "案例ID")
    private String caseId;
    @ApiModelProperty(value = "设计案例ID")
    private String hsDesignId;
    @ApiModelProperty(value = "资料类型(量房资料 1 , 设计资料 2 ,施工资料 3) -- 上传时选择的")
    private Integer category;
    @ApiModelProperty(value = "资料类型(1,设计资料 2,施工资料) ")
    private Integer type;
    @ApiModelProperty(name = "parlourList",value = "资料集合")
    private List<UrlDetailVo> dataList;
}
