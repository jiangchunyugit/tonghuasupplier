package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.model.ConstructionOrder;

/**
 *  施工状态
 */
public interface ConstructionStateService {

    /**
     * 施工状态查询 -根据项目编号和操作角色返回相应 状态
     * @param projectNo
     * @param role
     * @return
     */
    MyRespBundle<String> getConstructionState(String projectNo,String role);
}
