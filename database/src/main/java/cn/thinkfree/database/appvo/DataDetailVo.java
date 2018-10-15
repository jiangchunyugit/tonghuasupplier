package cn.thinkfree.database.appvo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author gejiaming
 */
@Data
@AllArgsConstructor
public class DataDetailVo {
    /**
     * 图片地址
     */
    private List<String> urlList;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 类型
     */
    private Integer type;
}
