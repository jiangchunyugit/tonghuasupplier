package cn.thinkfree.service.approvalFlow.impl;

import cn.thinkfree.core.constants.AFType;
import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.dto.ApprovalFlowConfigLogDTO;
import cn.thinkfree.database.mapper.ApprovalFlowConfigMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ApprovalFlowFormVo;
import cn.thinkfree.database.vo.ScheduleApprovalFlowConfigVo;
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
    private ApprovalFlowConfigMapper configMapper;
    @Resource
    private ApprovalFlowConfigLogService configLogService;

    /**
     * 查询所有审批流，并以sort正序
     * @return 所有审批流
     */
    @Override
    public List<ApprovalFlowConfig> list() {
        ApprovalFlowConfigExample configExample = new ApprovalFlowConfigExample();
        configExample.setOrderByClause("sort asc");
        return configMapper.selectByExample(configExample);
    }

    /**
     * 根据审批流编号查询审批流
     * @param approvalFlowNum 审批流编号
     * @return 审批流
     */
    @Override
    public ApprovalFlowConfig findByNum(String approvalFlowNum){
        ApprovalFlowConfigExample configExample = new ApprovalFlowConfigExample();
        configExample.createCriteria().andApprovalFlowNumEqualTo(approvalFlowNum);
        List<ApprovalFlowConfig> configs = configMapper.selectByExample(configExample);
        return configs != null && configs.size() > 0 ? configs.get(0) : null;
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
        ApprovalFlowConfig config = findByNum(configLogDTO.getApprovalFlowNum());
        if (config == null) {
            throw new RuntimeException("数据无效");
        }
        config.setVersion(config.getVersion() + 1);
        config.setUpdateUserId(configLogDTO.getCreateUserId());
        config.setUpdateTime(new Date());
        config.setApprovalFlowName(configLogDTO.getApprovalFlowName());
        config.setCompanyNum(configLogDTO.getCompanyNum());
        config.setH5Link(configLogDTO.getH5Link());
        config.setH5Resume(configLogDTO.getH5Resume());
        config.setType(configLogDTO.getType());
        save(config);
        configLogService.create(config, configLogDTO.getApprovalFlowNodeVos());
    }

    /**
     * 保存审批流
     * @param config 审批流
     */
    private void save(ApprovalFlowConfig config){
        configMapper.updateByPrimaryKey(config);
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
        ApprovalFlowConfig config = findByNum(configLogDTO.getApprovalFlowNum());
        if (config == null) {
            throw new RuntimeException("数据无效");
        }
        config.setId(0);
        config.setVersion(config.getVersion() + 1);
        config.setApprovalFlowNum(UniqueCodeGenerator.AF_CONFIG.getCode());
        config.setUpdateUserId(configLogDTO.getCreateUserId());
        config.setUpdateTime(new Date());
        config.setApprovalFlowName(configLogDTO.getApprovalFlowName());
        config.setCompanyNum(configLogDTO.getCompanyNum());
        config.setH5Link(configLogDTO.getH5Link());
        config.setH5Resume(configLogDTO.getH5Resume());
        config.setType(configLogDTO.getType());
        insert(config);
        configLogService.create(config, configLogDTO.getApprovalFlowNodeVos());
    }

    /**
     * 插入单个配置
     * @param config 审批流
     */
    private void insert(ApprovalFlowConfig config) {
        configMapper.insert(config);
    }

    /**
     * 根据审批流编号删除审批流
     * @param approvalFlowNum 审批流编号
     */
    @Override
    public void delete(String approvalFlowNum) {
        ApprovalFlowConfigExample configExample = new ApprovalFlowConfigExample();
        configExample.createCriteria().andApprovalFlowNumEqualTo(approvalFlowNum);
        configMapper.deleteByExample(configExample);

        configLogService.deleteByApprovalFlowNum(approvalFlowNum);
    }


    /**
     * 根据公司编号与项目节点序号查询审批配置信息
     * @param companyNum 公司编号
     * @param ProjectBigScheduleSort 项目节点序号
     * @return 审批配置信息
     */
    @Override
    public ScheduleApprovalFlowConfigVo findScheduleApprovalFlowConfigVo(String companyNum, Integer ProjectBigScheduleSort) {
        return null;
    }

    /**
     * 根据公司编号与项目节点序号查询审批角色顺序
     * @param companyNum 公司编号
     * @param ProjectBigScheduleSort 项目节点序号
     * @return 审批角色顺序
     */
    public List<List<UserRoleSet>> findNodeRoleSequence(String companyNum, Integer ProjectBigScheduleSort) {
        ApprovalFlowConfig config = findByNumAndCompanyNum(AFType.CHECK_APPLICATION.num, companyNum);



        return null;
    }

    private ApprovalFlowConfig findByNumAndCompanyNum(String approvalFlowNum, String companyNum){
        ApprovalFlowConfigExample configExample = new ApprovalFlowConfigExample();
        configExample.createCriteria().andApprovalFlowNumEqualTo(approvalFlowNum).andCompanyNumEqualTo(companyNum);
        List<ApprovalFlowConfig> configs = configMapper.selectByExample(configExample);
        return configs != null && configs.size() > 0 ? configs.get(0) : null;
    }

    public ApprovalFlowFormVo findApprovalFlowForm(String companyNum, Integer ProjectBigScheduleSort) {
        ApprovalFlowConfig config = findByNumAndCompanyNum(AFType.CHECK_APPLICATION.num, companyNum);

        return null;
    }

    @Override
    public void saveScheduleApprovalFlowConfigVo(String companyNum, Integer ProjectBigScheduleSort, ScheduleApprovalFlowConfigVo scheduleApprovalFlowConfigVo) {

    }
}
