package cn.thinkfree.service.constants;

import lombok.Getter;

@Getter
public enum UserStatus {
    ON_JOB(1,"在职"),
    QUIT_JOB(2,"离职"),
    TRANSFER(1,"项目已移交"),
    NO_TRANSFER(0,"项目未移交");

    private Integer value;
    private String description;

    UserStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
