package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.ApprovalFlowFormData;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ApprovalFlowFormDataVo extends ApprovalFlowFormData {

    @ApiModelProperty("该节点需要展示的表单数据")
    private List<ApprovalFlowFormElementVo> formElements;

    public List<ApprovalFlowFormElementVo> getFormElements() {
        return formElements;
    }

    public void setFormElements(List<ApprovalFlowFormElementVo> formElements) {
        this.formElements = formElements;
    }
}
