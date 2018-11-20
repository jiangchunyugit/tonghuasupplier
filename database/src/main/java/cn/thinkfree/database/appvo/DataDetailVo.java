package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author gejiaming
 */
@ApiModel("资料详情")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDetailVo {
    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private List<UrlDetailVo> urlList;
    /**
     * 设计资料集合(不符合开发,为了照顾APP端)
     */
    @ApiModelProperty(value = "urlStringList")
    private List<String> urlStringList;
    /**
     * pdf地址
     */
    @ApiModelProperty(value = "pdf地址")
    private String pdfUrl;
    /**
     * 上传时间
     */
    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;
    /**
     * 资料类别(客厅施工图,开工报告)
     */
    @ApiModelProperty(value = "资料类别(1,效果图资料 2,施工图资料)")
    private Integer category;
    /**
     * 是否确认
     */
    @ApiModelProperty(value = "是否确认 0,未确认 1,确认")
    private Integer confirm;
    /**
     * 3D图URL
     */
    @ApiModelProperty(value = "3D图URL")
    private String thirdUrl;
    /**
     * 项目编号
     */
    @ApiModelProperty(value = "项目编号")
    private String projectNo;
    /**
     * 点击图片更多H5地址
     */
    @ApiModelProperty(value = "点击图片更多H5地址")
    private String moreUrl;
}
