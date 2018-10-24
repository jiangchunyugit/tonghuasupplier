package cn.thinkfree.database.appvo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 资料详情
 * @author gejiaming
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlDetailVo {
    /**
     * 地址
     */
    private String url;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 名字
     */
    private String name;
    /**
     * 文件类型
     */
    private Integer fileType;
}
