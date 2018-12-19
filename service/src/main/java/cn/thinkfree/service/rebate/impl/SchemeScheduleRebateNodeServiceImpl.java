package cn.thinkfree.service.rebate.impl;

import cn.thinkfree.core.constants.RebateNodeConstants;
import cn.thinkfree.database.mapper.SchemeScheduleRebateNodeMapper;
import cn.thinkfree.database.model.RebateNode;
import cn.thinkfree.database.model.SchemeScheduleRebateNode;
import cn.thinkfree.database.model.SchemeScheduleRebateNodeExample;
import cn.thinkfree.database.vo.rebate.SchemeScheduleRebateNodeVO;
import cn.thinkfree.service.approvalflow.RebateNodeService;
import cn.thinkfree.service.rebate.SchemeScheduleRebateNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * 方案返款节点服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/19 10:37
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class SchemeScheduleRebateNodeServiceImpl implements SchemeScheduleRebateNodeService {

    @Autowired
    private SchemeScheduleRebateNodeMapper schemeScheduleRebateNodeMapper;
    @Autowired
    private RebateNodeService rebateNodeService;

    @Override
    public SchemeScheduleRebateNodeVO list(String schemeNo) {
        SchemeScheduleRebateNodeVO schemeScheduleRebateNodeVO = new SchemeScheduleRebateNodeVO();

        List<SchemeScheduleRebateNode> schemeScheduleRebateNodes = findBySchemeNo(schemeNo);
        schemeScheduleRebateNodeVO.setSchemeScheduleRebateNodes(schemeScheduleRebateNodes);

        List<RebateNode> rebateNodes = rebateNodeService.findByType(RebateNodeConstants.node_type_construct);
        schemeScheduleRebateNodeVO.setRebateNodes(rebateNodes);

        schemeScheduleRebateNodeVO.setSchemeNo(schemeNo);

        return schemeScheduleRebateNodeVO;
    }

    private List<SchemeScheduleRebateNode> findBySchemeNo(String schemeNo) {
        SchemeScheduleRebateNodeExample example = new SchemeScheduleRebateNodeExample();
        example.createCriteria().andSchemeNoEqualTo(schemeNo).andUsableEqualTo(1);
        return schemeScheduleRebateNodeMapper.selectByExample(example);
    }

    @Override
    public void edit(SchemeScheduleRebateNodeVO schemeScheduleRebateNodeVO) {
        String schemeNo = schemeScheduleRebateNodeVO.getSchemeNo();
        deleteBySchemeNo(schemeNo);

        List<SchemeScheduleRebateNode> schemeScheduleRebateNodes = schemeScheduleRebateNodeVO.getSchemeScheduleRebateNodes();

        Date createTime = new Date();
        for (SchemeScheduleRebateNode schemeScheduleRebateNode : schemeScheduleRebateNodes) {
            schemeScheduleRebateNode.setSchemeNo(schemeNo);
            schemeScheduleRebateNode.setId(null);
            schemeScheduleRebateNode.setUsable(1);
            schemeScheduleRebateNode.setCreateUserId(schemeScheduleRebateNodeVO.getCreateUserId());
            schemeScheduleRebateNode.setCreateTime(createTime);
            insert(schemeScheduleRebateNode);
        }
    }

    private void insert(SchemeScheduleRebateNode schemeScheduleRebateNode) {
        schemeScheduleRebateNodeMapper.insertSelective(schemeScheduleRebateNode);
    }

    private void deleteBySchemeNo(String schemeNo) {
        SchemeScheduleRebateNodeExample example = new SchemeScheduleRebateNodeExample();
        example.createCriteria().andSchemeNoEqualTo(schemeNo);

        SchemeScheduleRebateNode schemeScheduleRebateNode = new SchemeScheduleRebateNode();
        schemeScheduleRebateNode.setUsable(0);

        schemeScheduleRebateNodeMapper.updateByExampleSelective(schemeScheduleRebateNode, example);
    }

    @Override
    public SchemeScheduleRebateNode findBySchemeNoAndScheduleSort(String schemeNo, Integer scheduleSort) {
        SchemeScheduleRebateNodeExample example = new SchemeScheduleRebateNodeExample();
        example.createCriteria().andSchemeNoEqualTo(schemeNo).andScheduleSortEqualTo(scheduleSort).andUsableEqualTo(1);
        List<SchemeScheduleRebateNode> schemeScheduleRebateNodes = schemeScheduleRebateNodeMapper.selectByExample(example);
        return schemeScheduleRebateNodes != null && schemeScheduleRebateNodes.size() > 0 ? schemeScheduleRebateNodes.get(0) : null;
    }
}
