package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfConfigSchemeMapper;
import cn.thinkfree.database.model.AfConfigScheme;
import cn.thinkfree.database.model.AfConfigSchemeExample;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.vo.AfConfigVO;
import cn.thinkfree.service.approvalflow.AfConfigSchemeService;
import cn.thinkfree.service.neworder.NewOrderService;
import cn.thinkfree.service.neworder.NewOrderUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 审批流配置方案关系服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/30 17:02
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfConfigSchemeServiceImpl implements AfConfigSchemeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AfConfigSchemeServiceImpl.class);

    @Autowired
    private AfConfigSchemeMapper configSchemeMapper;
    @Autowired
    private NewOrderUserService orderUserService;
    @Autowired
    private NewOrderService orderService;


    @Override
    public List<AfConfigScheme> findBySchemeNo(String schemeNo) {
        AfConfigSchemeExample example = new AfConfigSchemeExample();
        example.createCriteria().andSchemeNoEqualTo(schemeNo).andUsableEqualTo(1);
        return configSchemeMapper.selectByExample(example);
    }

    @Override
    public AfConfigScheme findByConfigNoAndSchemeNo(String configNo, String schemeNo) {
        AfConfigSchemeExample example = new AfConfigSchemeExample();
        example.createCriteria().andConfigNoEqualTo(configNo).andSchemeNoEqualTo(schemeNo).andUsableEqualTo(1);
        List<AfConfigScheme> configSchemes = configSchemeMapper.selectByExample(example);
        return configSchemes != null && configSchemes.size() > 0 ? configSchemes.get(0) : null;
    }

    @Override
    public void create(String schemeNo, String userId, String configSchemeNo, AfConfigVO configVO) {
        AfConfigScheme configScheme = new AfConfigScheme();
        configScheme.setConfigNo(configVO.getConfigNo());
        configScheme.setConfigSchemeNo(configSchemeNo);
        configScheme.setCreateTime(new Date());
        configScheme.setCreateUserId(userId);
        configScheme.setSchemeNo(schemeNo);
        configScheme.setUsable(1);
        configScheme.setDescribe(configVO.getDescribe());

        List<UserRoleSet> approvalRoles = configVO.getApprovalRoles();
        configScheme.setFirstRoleId(approvalRoles.get(0).getRoleCode());

        delete(configVO.getConfigNo(), schemeNo);

        insert(configScheme);
    }

    /**
     * 旧数据标志位usable置为0：不可用
     * @param configNo 审批流配置编号
     * @param schemeNo 方案编号
     */
    private void delete(String configNo, String schemeNo) {
        AfConfigScheme configScheme = new AfConfigScheme();
        configScheme.setUsable(0);

        AfConfigSchemeExample example = new AfConfigSchemeExample();
        example.createCriteria().andConfigNoEqualTo(configNo).andSchemeNoEqualTo(schemeNo);

        configSchemeMapper.updateByExampleSelective(configScheme, example);
    }

    @Override
    public String findByProjectNoAndConfigNoAndUserId(String projectNo, String configNo, String userId) {
        ConstructionOrder constructionOrder = orderService.getConstructionOrder(projectNo);
        if (constructionOrder == null) {
            LOGGER.error("未查询到订单，projectNo：{}", projectNo);
            throw new RuntimeException();
        }
        String schemeNo = constructionOrder.getSchemeNo();
        if (StringUtils.isEmpty(schemeNo)) {
            LOGGER.error("项目未配置审批方案，projectNo:{}", projectNo);
            throw new RuntimeException();
        }
        String roleId = orderUserService.findRoleIdByProjectNoAndUserId(projectNo, userId);
        AfConfigScheme configScheme = findByConfigNoAndSchemeNoAndRoleId(configNo, schemeNo, roleId);
        return configScheme != null ? configScheme.getConfigSchemeNo() : null;
    }

    @Override
    public AfConfigScheme findByConfigNoAndSchemeNoAndRoleId(String configNo, String schemeNo, String roleId) {
        AfConfigSchemeExample example = new AfConfigSchemeExample();
        example.createCriteria().andConfigNoEqualTo(configNo).andSchemeNoEqualTo(schemeNo).andFirstRoleIdEqualTo(roleId).andUsableEqualTo(1);
        List<AfConfigScheme> configSchemes = configSchemeMapper.selectByExample(example);
        return configSchemes != null && configSchemes.size() > 0 ? configSchemes.get(0) : null;
    }

    private void insert(AfConfigScheme configScheme) {
        configSchemeMapper.insertSelective(configScheme);
    }
}
