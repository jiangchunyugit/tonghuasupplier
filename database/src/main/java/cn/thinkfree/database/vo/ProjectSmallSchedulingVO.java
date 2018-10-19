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
     * 大排期序号
     */
    private Integer parentSort;
    /**
     * 施工项ID
     */
    private Long constructId;
    /**
     * 施工项类别
     */
    private String constructCategory;
    /**
     * 施工项分类
     */
    private String constructItem;
    /**
     * 施工项编码
     */
    private String constructCode;
    /**
     * 施工项名称
     */
    private String constructName;
    /**
     * 计量单位
     */
    private String unitCode;
    /**
     * 工艺材料简介
     */
    private String desc;
    /**
     * 辅料名称规格
     */
    private String assitSpec;
    /**
     * 验收标准
     */
    private String standard;
    /**
     * 标准出处
     */
    private String sourceOfStandard;
    /**
     * 施工项状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
    /**
     * 装饰公司
     */
    private String decorateCompany;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateBy;

}
