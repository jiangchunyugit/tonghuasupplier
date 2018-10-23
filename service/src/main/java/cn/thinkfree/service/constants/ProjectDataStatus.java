package cn.thinkfree.service.constants;

import lombok.Getter;

@Getter
public enum ProjectDataStatus {
    INSERT_FAILD(0,"操作失败"),
    BASE_STATUS(1,"正常状态"),
    INVALID_STATUS(2,"失效状态"),
    VERSION(1,"版本号"),
    INSERT_SUCCESS(1,"操作成功"),
    DESIGN_STATUS(1,"设计订单type"),
    CONSTRUCTION_STATUS(2,"施工订单type"),
    QUOTATION_STATUS(3,"报价单type"),
    FILE_PNG(1,"png 文件"),
    FILE_PDF(2,"PDF 文件"),
    FILE_THIRD(3,"3D云 文件");

    private Integer value;
    private String description;

    ProjectDataStatus(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
}
