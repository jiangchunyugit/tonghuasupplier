package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.model.AfConfig;
import cn.thinkfree.database.vo.AfConfigEditVO;
import cn.thinkfree.database.vo.AfConfigListVO;
import cn.thinkfree.database.vo.AfConfigVO;

import java.util.List;

/**
 * 审批流配置服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 16:14
 */
public interface AfConfigService {
    /**
     * 列出所有审批流配置
     * @param schemeNo 方案编号
     * @return 所有审批流配置
     */
    AfConfigListVO list(String schemeNo);

    /**
     * 审批流配置详情
     * @param configNo 审批流配置编号
     * @param schemeNo 方案编号
     * @return 审批流配置详情
     */
    AfConfigVO detail(String configNo, String schemeNo);

    /**
     * 修改审批流
     * @param configEditVO 审批流配置
     */
    void edit(AfConfigEditVO configEditVO);

    /**
     * 根据审批流配置查询审批流
     * @param configNo 审批流配置编号
     * @return 审批流配置
     */
    AfConfig findByNo(String configNo);

    /**
     * 根据审批类型获取审批配置编号
     * @param approvalType 审批类型
     * @return 审批配置编号
     */
    List<String> getConfigNosByApprovalType(String approvalType);
}
