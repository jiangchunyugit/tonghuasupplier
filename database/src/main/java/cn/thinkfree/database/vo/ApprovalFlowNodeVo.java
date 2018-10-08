package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.*;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ApprovalFlowNodeVo extends ApprovalFlowNode {

    @ApiModelProperty("节点操作项集合")
    private List<ApprovalFlowOptions> optionsList;

    @ApiModelProperty("订阅通知消息地址集合")
    private List<ApprovalFlowNoticeUrls> noticeUrlsList;

    @ApiModelProperty("审批流角色信息表")
    private List<ApprovalFlowRoles> roles;

    @ApiModelProperty("审批超时通知")
    private List<ApprovalFlowTimeoutNotice> timeoutNotices;

    @ApiModelProperty("该节点需要展示的表单数据")
    private List<ApprovalFlowFormDataVo> formDatas;

    public List<ApprovalFlowOptions> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<ApprovalFlowOptions> optionsList) {
        this.optionsList = optionsList;
    }

    public List<ApprovalFlowNoticeUrls> getNoticeUrlsList() {
        return noticeUrlsList;
    }

    public void setNoticeUrlsList(List<ApprovalFlowNoticeUrls> noticeUrlsList) {
        this.noticeUrlsList = noticeUrlsList;
    }

    public List<ApprovalFlowRoles> getRoles() {
        return roles;
    }

    public void setRoles(List<ApprovalFlowRoles> roles) {
        this.roles = roles;
    }

    public List<ApprovalFlowTimeoutNotice> getTimeoutNotices() {
        return timeoutNotices;
    }

    public void setTimeoutNotices(List<ApprovalFlowTimeoutNotice> timeoutNotices) {
        this.timeoutNotices = timeoutNotices;
    }

    public List<ApprovalFlowFormDataVo> getFormDatas() {
        return formDatas;
    }

    public void setFormDatas(List<ApprovalFlowFormDataVo> formDatas) {
        this.formDatas = formDatas;
    }
}
