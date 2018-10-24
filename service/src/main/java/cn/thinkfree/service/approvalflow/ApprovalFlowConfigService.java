package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.vo.ApprovalFlowConfigVO;
import cn.thinkfree.database.model.ApprovalFlowConfig;
import cn.thinkfree.database.vo.ApprovalFlowConfigLogVO;
import cn.thinkfree.database.vo.ApprovalFlowOrderVO;
import cn.thinkfree.database.vo.ScheduleApprovalFlowConfigVo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface ApprovalFlowConfigService {

    ApprovalFlowConfigLogVO detail(String num);

    ApprovalFlowConfig findByNum(String approvalFlowNum);

    List<ApprovalFlowConfig> list();

    void edit(ApprovalFlowConfigVO configLogDTO);

    void add(ApprovalFlowConfigVO configLogDTO);

    void delete(String approvalFlowNum);

    /**
     * 获取审批顺序
     * @return 审批顺序
     */
    List<ApprovalFlowOrderVO> order();

    /**
     * 修改获取审批顺序
     * @param orderVOs 审批顺序
     */
    void editOrder(List<ApprovalFlowOrderVO> orderVOs);

    /**
     * 根据公司id与节点编号获取审批流配置
     * @param companyNum 公司id
     * @param projectBigScheduleSort 节点编号
     * @return 审批流配置
     */
    ScheduleApprovalFlowConfigVo findScheduleApprovalFlowConfigVo(String companyNum, Integer projectBigScheduleSort);

    /**
     * 保存公司id、项目节点、审批流配置
     * @param companyNum 公司id
     * @param projectBigScheduleSort 项目节点
     * @param scheduleApprovalFlowConfigVo 审批流配置
     */
    void saveScheduleApprovalFlowConfigVo(String companyNum, Integer projectBigScheduleSort, Integer version, ScheduleApprovalFlowConfigVo scheduleApprovalFlowConfigVo);

    /**
     * 清除多余配置（测试用）
     */
    void clearConfig();
}
