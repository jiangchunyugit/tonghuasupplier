package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author gejiaming
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDetailVo {
    /**
     * 图片地址
     */
    private List<UrlDetailVo> urlList;
    /**
     * 设计资料集合(不符合开发,为了照顾APP端)
     */
    private List<String> urlStringList;
    /**
     * pdf地址
     */
    private String pdfUrl;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 资料类别(客厅施工图,开工报告)
     */
    private Integer category;
    /**
     * 是否确认
     */
    private Integer confirm;
    /**
     * 3D图URL
     */
    private String thirdUrl;
}
