package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.service.construction.vo.ConstructionStateVo;

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
    MyRespBundle<ConstructionStateVo> getConstructionState(String projectNo, String role);

    /**
     * 查询操作角色 是否有变更状态的权限
     *
     * @param projectNo
     * @param role
     * @return
     */
    MyRespBundle<Object> queryIsState(String projectNo, String role);


    /**
     * 施工状态修改 -根据项目编号和操作角色返回相应 状态
     * @param projectNo
     * @param role
     * @return
     */
    MyRespBundle<String> updateConstructionState(String projectNo,String role,int stateCode);
}
