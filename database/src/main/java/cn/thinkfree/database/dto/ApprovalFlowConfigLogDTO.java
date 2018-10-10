package cn.thinkfree.database.dto;

import cn.thinkfree.database.vo.ApprovalFlowNodeVo;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 编辑审批流输入对象
 * @author xusonghui
 */
public class ApprovalFlowConfigLogDTO {

    /**
     * 审批流编号
     */
    @ApiModelProperty("审批流编号")
    private String approvalFlowNum;
    /**
     * 修改人或者创建人ID
     */
    @ApiModelProperty("修改人或者创建人ID")
    private String createUserId;
    /**
     * 审批流名称
     */
    @ApiModelProperty("审批流名称")
    private String approvalFlowName;
    // 公司编号
    @ApiModelProperty("公司编号")
    private String companyNum;
    /**
     * 生效时间
     */
    @ApiModelProperty("生效时间")
    private String effectTime;
    /**
     * h5链接地址
     */
    @ApiModelProperty("h5链接地址")
    private String h5Link;
    /**
     * h5链接描述
     */
    @ApiModelProperty("h5链接描述")
    private String h5Resume;

    @ApiModelProperty("审批流类型，1h5，2原生")
    private int type;
    /**
     * 审批流节点信息
     */
    private List<ApprovalFlowNodeVo> approvalFlowNodeVos;

    public String getApprovalFlowNum() {
        return approvalFlowNum;
    }

    public void setApprovalFlowNum(String approvalFlowNum) {
        this.approvalFlowNum = approvalFlowNum;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getApprovalFlowName() {
        return approvalFlowName;
    }

    public void setApprovalFlowName(String approvalFlowName) {
        this.approvalFlowName = approvalFlowName;
    }

    public String getCompanyNum() {
        return companyNum;
    }

    public void setCompanyNum(String companyNum) {
        this.companyNum = companyNum;
    }

    public String getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(String effectTime) {
        this.effectTime = effectTime;
    }

    public String getH5Link() {
        return h5Link;
    }

    public void setH5Link(String h5Link) {
        this.h5Link = h5Link;
    }

    public String getH5Resume() {
        return h5Resume;
    }

    public void setH5Resume(String h5Resume) {
        this.h5Resume = h5Resume;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ApprovalFlowNodeVo> getApprovalFlowNodeVos() {
        return approvalFlowNodeVos;
    }

    public void setApprovalFlowNodeVos(List<ApprovalFlowNodeVo> approvalFlowNodeVos) {
        this.approvalFlowNodeVos = approvalFlowNodeVos;
    }
}
