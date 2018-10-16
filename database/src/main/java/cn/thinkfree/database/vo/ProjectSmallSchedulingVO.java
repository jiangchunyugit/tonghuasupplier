package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @author gejiaming
 */
@ApiModel(value = "ProjectSmallSchedulingVO,基础小排期详情")
@Data
public class ProjectSmallSchedulingVO {

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 施工项类别
     */
    private Integer category;

    /**
     * 施工项分类
     */
    private Integer classification;

    /**
     * 施工项编码
     */
    private String code;

    /**
     * 施工项名称
     */
    private String name;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 大排期序号
     */
    private Integer parentSort;

    /**
     * 工艺材料介绍
     */
    private String technologyIntroduce;

    /**
     * 辅料
     */
    private String accessories;

    /**
     * 验收标准
     */
    private String checkStandard;

    /**
     * 标准出处
     */
    private String standardSource;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 状态(1,正常  2,失效)
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

}
