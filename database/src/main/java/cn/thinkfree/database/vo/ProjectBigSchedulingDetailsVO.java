package cn.thinkfree.database.vo;

import io.swagger.annotations.ApiModel;

import java.util.Date;

/**
 * @author gejiaming
 */
@ApiModel(value = "ProjectBigSchedulingDetailsVO,大排期详情实体")
public class ProjectBigSchedulingDetailsVO {

    /**
     *   大排期序号
     */
    private Integer bigSort;

    /**
     *   大排期名字
     */
    private String bigName;

    /**
     *   自定义大排期名字
     */
    private String renameBig;

    /**
     *   计划开始时间
     */
    private Date planSatrtTime;

    /**
     *   计划结束时间
     */
    private Date planEndTime;

    /**
     *   状态(1-正常，2-失效，3-冻结)
     */
    private Integer status;

    /**
     *   创建时间
     */
    private Date createTime;

    /**
     *   是否与报价单材料匹配 (0,未匹配 1, 匹配)
     */
    private Integer isMatching;

    /**
     *   是否完成(0,否 1,是)
     */
    private Integer isCompleted;

    /**
     *   是否新增(0,否 1,是)
     */
    private Integer isAdd;

    /**
     *   是否变更
     */
    private Integer isChange;

    /**
     *   备注
     */
    private String remark;

    /**
     *   提交次数
     *
     * @mbg.generated
     */
    private Integer submitNum;

    /**
     *   是否需要验收(0-不需要验收，1-需要验收)
     *
     * @mbg.generated
     */
    private Integer isNeedCheck;

    /**
     *   是否验收通过（0-未验收过，1-验收通过）
     */
    private Integer isAdopt;

    /**
     *   版本号
     */
    private Integer version;





}
