package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ApprovalFlowFormData;
import cn.thinkfree.database.model.ApprovalFlowFormElement;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ApprovalFlowFormDataVo extends ApprovalFlowFormData {

    @ApiModelProperty("该节点需要展示的表单数据")
    private List<ApprovalFlowFormElement> formElements;

    public List<ApprovalFlowFormElement> getFormElements() {
        return formElements;
    }

    public void setFormElements(List<ApprovalFlowFormElement> formElements) {
        this.formElements = formElements;
    }
}
