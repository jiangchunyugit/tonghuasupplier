package cn.thinkfree.database.vo;

import cn.thinkfree.database.model.*;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ApprovalFlowNodeVo extends ApprovalFlowNode {

    @ApiModelProperty("节点操作项集合")
    private List<ApprovalFlowOption> options;

    @ApiModelProperty("订阅通知消息地址集合")
    private List<ApprovalFlowNoticeUrl> noticeUrls;

    @ApiModelProperty("审批流角色信息表")
    private List<ApprovalFlowNodeRole> nodeRoles;

    @ApiModelProperty("审批超时通知")
    private List<ApprovalFlowTimeoutNotice> timeoutNotices;

    public List<ApprovalFlowOption> getOptions() {
        return options;
    }

    public void setOptions(List<ApprovalFlowOption> options) {
        this.options = options;
    }

    public List<ApprovalFlowNoticeUrl> getNoticeUrls() {
        return noticeUrls;
    }

    public void setNoticeUrls(List<ApprovalFlowNoticeUrl> noticeUrls) {
        this.noticeUrls = noticeUrls;
    }

    public List<ApprovalFlowNodeRole> getNodeRoles() {
        return nodeRoles;
    }

    public void setNodeRoles(List<ApprovalFlowNodeRole> nodeRoles) {
        this.nodeRoles = nodeRoles;
    }

    public List<ApprovalFlowTimeoutNotice> getTimeoutNotices() {
        return timeoutNotices;
    }

    public void setTimeoutNotices(List<ApprovalFlowTimeoutNotice> timeoutNotices) {
        this.timeoutNotices = timeoutNotices;
    }

}
