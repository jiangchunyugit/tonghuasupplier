package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.core.utils.UniqueCodeGenerator;
import cn.thinkfree.database.mapper.ApprovalFlowNodeMapper;
import cn.thinkfree.database.model.ApprovalFlowNode;
import cn.thinkfree.database.model.ApprovalFlowNodeExample;
import cn.thinkfree.database.model.ApprovalFlowNodeRole;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.vo.ApprovalFlowNodeVO;
import cn.thinkfree.database.vo.NodeRoleSequenceVo;
import cn.thinkfree.service.approvalflow.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 审批流节点服务层
 */
@Service
@Transactional
public class ApprovalFlowNodeServiceImpl implements ApprovalFlowNodeService {

    @Resource
    private ApprovalFlowNodeMapper nodeMapper;
    @Resource
    private ApprovalFlowNoticeUrlService noticeUrlService;
    @Resource
    private ApprovalFlowOptionService optionService;
    @Resource
    private ApprovalFlowNodeRoleService nodeRoleService;
    @Resource
    private ApprovalFlowTimeoutNoticeService timeoutNoticeService;
    @Resource
    private ApprovalFlowConfigLogService configLogService;
    @Resource
    private RoleService roleService;

    /**
     * 根据节点编号查询节点信息
     * @param configLogNum 节点编号
     * @return 审批流节点信息
     */
    @Override
    public List<ApprovalFlowNodeVO> findVoByConfigLogNum(String configLogNum) {
        return nodeMapper.findByConfigLogNum(configLogNum);
    }
    /**
     * 根据节点编号查询节点信息
     * @param configLogNum 节点编号
     * @return 审批流节点信息
     */
    @Override
    public List<ApprovalFlowNode> findByConfigLogNum(String configLogNum) {
        ApprovalFlowNodeExample nodeExample = new ApprovalFlowNodeExample();
        nodeExample.createCriteria().andNumEqualTo(configLogNum);
        nodeExample.setOrderByClause("sort asc");
        return nodeMapper.selectByExample(nodeExample);
    }

    /**
     * 创建审批流节点
     * @param configLogNum 审批流配置记录编号
     * @param nodeVos 审批流节点信息
     */
    @Override
    public void create(String configLogNum, List<ApprovalFlowNodeVO> nodeVos) {
        if (nodeVos != null){
            for (ApprovalFlowNodeVO nodeVo : nodeVos){
                nodeVo.setId(null);
                nodeVo.setConfigLogNum(configLogNum);
                nodeVo.setNum(UniqueCodeGenerator.AF_NODE.getCode());

                noticeUrlService.create(nodeVo.getNum(),  nodeVo.getNoticeUrls());
                optionService.create(nodeVo.getNum(), nodeVo.getOptions());
                nodeRoleService.create(nodeVo.getNum(), nodeVo.getNodeRoles());
                timeoutNoticeService.create(nodeVo.getNum(), nodeVo.getTimeoutNotices());

                nodeMapper.insertSelective(nodeVo);
            }
        }
    }

    /**
     * 根据审批流配置记录编号删除审批流节点
     * @param configLogNums 审批流配置记录编号
     */
    @Override
    public void deleteByConfigLogNums(List<String> configLogNums) {
        if (configLogNums != null && configLogNums.size() > 0) {
            ApprovalFlowNodeExample nodeExample = new ApprovalFlowNodeExample();
            nodeExample.createCriteria().andConfigLogNumIn(configLogNums);

            List<ApprovalFlowNode> nodes = nodeMapper.selectByExample(nodeExample);

            if (nodes != null && nodes.size() > 0) {
                List<String> nodeNums = new ArrayList<>(nodes.size());
                for (ApprovalFlowNode node : nodes) {
                    nodeNums.add(node.getNum());
                }
                noticeUrlService.deleteByNodeNums(nodeNums);
                optionService.deleteByNodeNums(nodeNums);
                nodeRoleService.deleteByNodeNums(nodeNums);
                timeoutNoticeService.deleteByNodeNums(nodeNums);
            }

            nodeMapper.deleteByExample(nodeExample);
        }
    }
}
