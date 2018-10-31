package cn.thinkfree.service.construction.impl;


import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.construction.vo.ConstructionStateListVo;
import cn.thinkfree.service.construction.vo.ConstructionStateVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 施工状态
 */
@Service
public class ConstructionStateServiceImpl extends AbsBaseController implements ConstructionStateService {

    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    CommonService commonService;

    /**
     * 施工状态查询 -根据项目编号和操作角色返回相应 状态
     *
     * @param projectNo
     * @param role
     * @return
     */
    @Override
    public MyRespBundle<ConstructionStateVo> getConstructionState(String projectNo, String role) {
        if (StringUtils.isBlank(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号不能为空");
        }
        if (StringUtils.isBlank(role)) {
            return sendJsonData(ResultMessage.ERROR, "操作角色不能为空");
        }
        Integer stateCode = commonService.queryStateCode(projectNo);
        if (stateCode == null) {
            return sendJsonData(ResultMessage.ERROR, "暂无项目信息");
        }

        /* 当前状态说明 */
        String stateInfo = ConstructionStateEnum.queryStateByRole(stateCode, role);
        if (StringUtils.isBlank(stateInfo)) {
            return sendJsonData(ResultMessage.ERROR, "暂无状态信息");
        }

        /* 查询下一个状态码 */
        ConstructionStateVo constructionStateVo = new ConstructionStateVo();
        List<Integer> nextStateList = ConstructionStateEnum.getNextStates(stateCode);
        for (Integer nextCode : nextStateList){
            ConstructionStateListVo constructionStateListVo = new ConstructionStateListVo();
            constructionStateListVo.setStateCode(nextCode);
            constructionStateListVo.setStateInfo(ConstructionStateEnum.queryStateByRole(nextCode, role));
            // 组装数据 下一个状态码&状态说明
            List<ConstructionStateListVo> list = new ArrayList<>();
            list.add(constructionStateListVo);
            constructionStateVo.setNextState(list);
        }
        /* 是否有变更权限 */
        boolean isState = commonService.queryIsState(projectNo,role);
        constructionStateVo.setStateInfo(stateInfo);
        constructionStateVo.setState(isState);

        return sendJsonData(ResultMessage.SUCCESS, constructionStateVo);
    }

    /**
     * 查询操作角色 是否有变更状态的权限
     *
     * @param projectNo
     * @param role
    * @return
     */
    @Override
    public MyRespBundle<Object> queryIsState(String projectNo, String role) {
        if (StringUtils.isBlank(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号不能为空");
        }
        if (StringUtils.isBlank(role)) {
            return sendJsonData(ResultMessage.ERROR, "操作角色不能为空");
        }

        if (commonService.queryIsState(projectNo,role)) {
            return sendJsonData(ResultMessage.SUCCESS,"有变更状态权限");
        } else {
            return sendJsonData(ResultMessage.ERROR, "无变更状态权限");
        }
    }

    /**
     * 施工状态修改
     *  (自动可取消)
     *
     * @param projectNo
     * @param role
     * @return
     */
    @Override
    public MyRespBundle<String> updateConstructionState(String projectNo,String role,int stateCode) {
        if (StringUtils.isBlank(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号不能为空");
        }
        if (StringUtils.isBlank(role)) {
            return sendJsonData(ResultMessage.ERROR, "操作角色不能为空");
        }
        /* 查询操作角色 是否有变更状态的权限 */
        if (!commonService.queryIsState(projectNo,role)) {
            return sendJsonData(ResultMessage.ERROR, "无变更状态权限");
        }

        /* 校验传入状态值-是否在下一步中存在 */
        List<Integer> listCode = ConstructionStateEnum.getNextStates(commonService.queryStateCode(projectNo));
        if (!listCode.contains(stateCode)){
            return sendJsonData(ResultMessage.ERROR, "传入状态值不符");
        }

        /* 更新状态 */
        if (commonService.updateStateCode(projectNo,stateCode)){
            return sendJsonData(ResultMessage.SUCCESS,"成功");
        }else {
            return sendJsonData(ResultMessage.ERROR,"失败");
        }

    }

}
