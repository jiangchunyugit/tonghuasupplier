package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.ApprovalFlowConfigSuper;
import cn.thinkfree.database.vo.ApprovalFlowOrderVO;

import java.util.List;

/**
 *  审批流依赖关系
 * @author song
 * @date 2018/10/16 10:23
 * @version 1.0
 */
public interface ApprovalFlowConfigSuperService {
    /**
     * 根据审批流简称查询审批流依赖关系
     * @param configAlias 审批流简称
     * @return 审批流依赖关系
     */
    List<ApprovalFlowConfigSuper> findByConfigAlias(String configAlias);

    /**
     * 创建审批流依赖关系
     * @param configAlias 审批流简称
     * @param configSupers 审批流依赖关系
     */
    void create(String configAlias, List<ApprovalFlowConfigSuper> configSupers);

    /**
     * 创建审批流依赖关系
     * @param orderVOs 审批流依赖关系
     */
    void create(List<ApprovalFlowOrderVO> orderVOs);

    /**
     * 获取所有可用的配置
     * @return 审批流依赖关系
     */
    List<ApprovalFlowConfigSuper> findAllUsable();
}
