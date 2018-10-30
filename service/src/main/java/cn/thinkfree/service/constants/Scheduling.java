package cn.thinkfree.service.constants;

import lombok.Getter;

@Getter
public enum Scheduling {
    /**
     * 操作失败
     */
    INSERT_FAILD(0,"操作失败"),
    /**
     * 正常状态
     */
    BASE_STATUS(1,"正常状态"),
    /**
     * 失效状态
     */
    INVALID_STATUS(2,"失效状态"),
    /**
     * 版本号
     */
    VERSION(1,"版本号"),
    /**
     * 上海小排期基础数据默认条数
     */
    LIMIT(10000,"上海小排期基础数据默认条数"),
    /**
     * 操作成功
     */
    INSERT_SUCCESS(1,"操作成功"),
    /**
     * 需要验收
     */
    CHECK_YES(1,"需要验收"),
    /**
     * 不需要验收
     */
    CHECK_NO(0,"不需要验收"),
    /**
     * 匹配
     */
    MATCHING_YES(1,"匹配"),
    /**
     * 未匹配
     */
    MATHCHING_NO(0,"未匹配"),
    /**
     * 未完成
     */
    COMPLETED_YES(1,"未完成"),
    /**
     * 已完成
     */
    COMPLETED_NO(0,"已完成"),
    /**
     * 新增
     */
    ADD_YES(1,"新增"),
    /**
     * 未新增
     */
    ADD_NO(0,"未新增"),
    /**
     * 变更次数
     */
    CHANGE_NUM(0,"变更次数"),
    /**
     * 提交次数
     */
    SUBMIT_NUM(0,"提交次数");

    private Integer value;
    private String description;

    Scheduling(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
