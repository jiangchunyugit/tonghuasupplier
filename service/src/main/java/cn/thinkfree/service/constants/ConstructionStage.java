package cn.thinkfree.service.constants;

import lombok.Getter;

/**
 * @Auther: jiang
 * @Date: 2018/10/18 11:44
 * @Description: 阶段详情
 */
@Getter
public enum ConstructionStage {
    RESERVATION(1,"预约"),
    MEASURING_ROOM(2,"量房"),
    DESIGN_CONTRACT(3,"设计签约"),
    DESIGN(4,"签约"),
    PRE_SUBMISSION(5,"预交底"),
    CONSTRUCTION_CONTRACT(6,"施工签约"),
    CONSTRUCTION(7,"施工"),
    CARRY_OUT(8,"完成");

    private Integer value;
    private String description;

    ConstructionStage(Integer value, String description){
        this.value = value;
        this.description = description;
    }
}
