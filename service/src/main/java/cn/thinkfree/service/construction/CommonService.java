package cn.thinkfree.service.construction;


import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonService extends AbsBaseController {
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;

    /**
     * 查询操作角色 是否有变更状态的权限
     */
    public Integer queryStateCode(String projectNo) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        if (!constructionOrderList.isEmpty()){
            return constructionOrderList.get(0).getOrderStage();
        }else {
            return null;
        }
    }


    /**
     * 查询操作角色 是否有变更状态的权限
     */
    public boolean queryIsState(String projectNo, String role) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(example);
        if (constructionOrderList.isEmpty()) {
            return false;
        }
        if (ConstructionStateEnum.queryIsState(role)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 更新状态值
     */
    public boolean updateStateCode(String projectNo,int stateCode) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        ConstructionOrder constructionOrder = new ConstructionOrder();
        constructionOrder.setOrderStage(stateCode);
        int isUpdate = constructionOrderMapper.updateByExampleSelective(constructionOrder, example);
        if (isUpdate == 1){
            return true;
        }else {
            return false;
        }
    }

}
