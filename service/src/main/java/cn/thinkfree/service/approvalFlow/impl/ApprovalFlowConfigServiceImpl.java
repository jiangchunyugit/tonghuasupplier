package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.dto.ApprovalFlowConfigLogDTO;
import cn.thinkfree.database.mapper.ApprovalFlowMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigLogService;
import cn.thinkfree.service.approvalFlow.ApprovalFlowConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 审批流配置服务层
 */
@Service
@Transactional
public class ApprovalFlowConfigServiceImpl implements ApprovalFlowConfigService {

    @Resource
    private ApprovalFlowMapper approvalFlowMapper;
    @Resource
    private ApprovalFlowConfigLogService configLogService;

    /**
     * 查询所有审批流，并以sort正序
     * @return 所有审批流
     */
    @Override
    public List<ApprovalFlow> list() {
        ApprovalFlowExample approvalFlowExample = new ApprovalFlowExample();
        approvalFlowExample.setOrderByClause("sort asc");
        return approvalFlowMapper.selectByExample(approvalFlowExample);
    }

    /**
     * 根据审批流编号查询审批流
     * @param approvalFlowNum 审批流编号
     * @return 审批流
     */
    @Override
    public ApprovalFlow findByNum(String approvalFlowNum){
        ApprovalFlowExample approvalFlowExample = new ApprovalFlowExample();
        approvalFlowExample.createCriteria().andApprovalFlowNameEqualTo(approvalFlowNum);
        List<ApprovalFlow> approvalFlows = approvalFlowMapper.selectByExample(approvalFlowExample);
        return approvalFlows != null && approvalFlows.size() > 0 ? approvalFlows.get(0) : null;
    }

    /**
     * 编辑审批流
     * @param configLogDTO 审批流信息
     */
    @Override
    public void edit(ApprovalFlowConfigLogDTO configLogDTO) {
        if (StringUtils.isEmpty(configLogDTO.getApprovalFlowNum())) {
            throw new RuntimeException("审批流编号不能为空");
        }
        ApprovalFlow approvalFlow = findByNum(configLogDTO.getApprovalFlowNum());
        if (approvalFlow == null) {
            throw new RuntimeException("数据无效");
        }
        approvalFlow.setVersion(approvalFlow.getVersion() + 1);
        approvalFlow.setUpdateUserId(configLogDTO.getCreateUserId());
        approvalFlow.setUpdateTime(new Date());
        approvalFlow.setApprovalFlowName(configLogDTO.getApprovalFlowName());
        approvalFlow.setCompanyNum(configLogDTO.getCompanyNum());
        approvalFlow.setH5Link(configLogDTO.getH5Link());
        approvalFlow.setH5Resume(configLogDTO.getH5Resume());
        approvalFlow.setType(configLogDTO.getType());
        save(approvalFlow);
        configLogService.create(approvalFlow, configLogDTO.getApprovalFlowNodeVos());
    }

    /**
     * 保存审批流
     * @param approvalFlow 审批流
     */
    private void save(ApprovalFlow approvalFlow){
        approvalFlowMapper.updateByPrimaryKey(approvalFlow);
    }

    /**
     * 添加审批流
     * @param configLogDTO 审批流信息
     */
    @Override
    public void add(ApprovalFlowConfigLogDTO configLogDTO) {
        if (StringUtils.isEmpty(configLogDTO.getApprovalFlowNum())) {
            throw new RuntimeException("审批流编号不能为空");
        }
        ApprovalFlow approvalFlow = findByNum(configLogDTO.getApprovalFlowNum());
        if (approvalFlow == null) {
            throw new RuntimeException("数据无效");
        }
        approvalFlow.setId(0);
        approvalFlow.setVersion(approvalFlow.getVersion() + 1);
        approvalFlow.setApprovalFlowNum(UniqueCodeGenerator.AF.getCode());
        approvalFlow.setUpdateUserId(configLogDTO.getCreateUserId());
        approvalFlow.setUpdateTime(new Date());
        approvalFlow.setApprovalFlowName(configLogDTO.getApprovalFlowName());
        approvalFlow.setCompanyNum(configLogDTO.getCompanyNum());
        approvalFlow.setH5Link(configLogDTO.getH5Link());
        approvalFlow.setH5Resume(configLogDTO.getH5Resume());
        approvalFlow.setType(configLogDTO.getType());
        insert(approvalFlow);
        configLogService.create(approvalFlow, configLogDTO.getApprovalFlowNodeVos());
    }

    /**
     * 插入单个配置
     * @param approvalFlow 审批流
     */
    private void insert(ApprovalFlow approvalFlow) {
        approvalFlowMapper.insert(approvalFlow);
    }

    /**
     * 根据审批流编号删除审批流
     * @param approvalFlowNum 审批流编号
     */
    @Override
    public void delete(String approvalFlowNum) {
        ApprovalFlowExample approvalFlowExample = new ApprovalFlowExample();
        approvalFlowExample.createCriteria().andApprovalFlowNumEqualTo(approvalFlowNum);
        approvalFlowMapper.deleteByExample(approvalFlowExample);

        configLogService.deleteByApprovalFlowNum(approvalFlowNum);
    }
}
