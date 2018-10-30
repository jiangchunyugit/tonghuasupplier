package cn.thinkfree.service.construction.impl;


import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.service.construction.ConstructionStateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  施工状态
 */
@Service
public class ConstructionStateServiceImpl extends AbsBaseController implements ConstructionStateService {

    @Autowired
    ConstructionOrderMapper constructionOrderMapper;

    /**
     * 施工状态查询 -根据项目编号和操作角色返回相应 状态
     * @param projectNo
     * @param role
     * @return
     */
    @Override
    public MyRespBundle<String> getConstructionState(String projectNo,String role) {
        if (StringUtils.isBlank(projectNo)){
            return sendJsonData(ResultMessage.ERROR,"项目编号不能为空");
        }
        if (StringUtils.isBlank(role)){
            return sendJsonData(ResultMessage.ERROR,"操作角色不能为空");
        }
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        if (constructionOrderList.isEmpty()){
            return sendJsonData(ResultMessage.ERROR,"暂无项目信息");
        }
        String state = ConstructionStateEnum.queryStateByRole(constructionOrderList.get(0).getOrderStage(),role);
        if (StringUtils.isBlank(state)){
            return sendJsonData(ResultMessage.ERROR,"暂无状态信息");
        }
        return sendJsonData(ResultMessage.SUCCESS,state);
    }


}
