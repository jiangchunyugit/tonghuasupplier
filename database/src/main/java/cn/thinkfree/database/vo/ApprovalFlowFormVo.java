package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ApprovalFlowForm;
import cn.thinkfree.database.model.ApprovalFlowFormElement;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ApprovalFlowFormVo extends ApprovalFlowForm {

    @ApiModelProperty("该节点需要展示的表单数据")
    private List<ApprovalFlowFormElement> formElements;

    public List<ApprovalFlowFormElement> getFormElements() {
        return formElements;
    }

    public void setFormElements(List<ApprovalFlowFormElement> formElements) {
        this.formElements = formElements;
    }
}
