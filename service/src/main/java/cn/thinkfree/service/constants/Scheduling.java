package cn.thinkfree.service.constants;

import lombok.Getter;

@Getter
public enum Scheduling {
    INSERT_FAILD(0,"操作失败"),
    BASE_STATUS(1,"正常状态"),
    INVALID_STATUS(2,"失效状态"),
    INSERT_SUCCESS(1,"操作成功");

    private Integer value;
    private String description;

    Scheduling(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
