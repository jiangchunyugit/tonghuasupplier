package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ApprovalFlowFormElement;
import cn.thinkfree.database.model.ApprovalFlowFormElementType;
import io.swagger.annotations.ApiModelProperty;

public class ApprovalFlowFormElementVo extends ApprovalFlowFormElement {

    @ApiModelProperty("表单元素类型")
    private ApprovalFlowFormElementType formElementType;

    public ApprovalFlowFormElementType getFormElementType() {
        return formElementType;
    }

    public void setFormElementType(ApprovalFlowFormElementType formElementType) {
        this.formElementType = formElementType;
    }
}
