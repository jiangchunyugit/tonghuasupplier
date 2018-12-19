package cn.thinkfree.service.construction.impl;


import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.model.OrderStatusDTO;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ProjectBigSchedulingDetailsVO;
import cn.thinkfree.service.construction.*;
import cn.thinkfree.service.newscheduling.NewSchedulingService;
import cn.thinkfree.service.platform.build.BuildConfigService;
import cn.thinkfree.service.project.ProjectService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 施工状态
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConstructionStateServiceImpl implements ConstructionStateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstructionStateServiceImpl.class);

    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    CommonService commonService;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ConstructionOrderPayMapper constructionOrderPayMapper;
    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;
    @Autowired
    ConstructionAndPayStateService constructionAndPayStateService;
    @Autowired
    ProjectBigSchedulingDetailsMapper detailsMapper;
    @Autowired
    BuildPayConfigMapper buildPayConfigMapper;
    @Autowired
    private BuildConfigService buildConfigService;
    @Autowired
    private ConstructOrderService constructOrderService;
    @Autowired
    private ConstructOrderPayService constructOrderPayService;
    @Autowired
    private NewSchedulingService schedulingService;
    @Autowired
    private ProjectService projectService;


    /**
     * 查询当前状态
     *
     * @param type ，1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态
     * @return
     */
    @Override
    public MyRespBundle<String> getStateInfo(String orderNo, int type) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        String stateInfo = ConstructionStateEnum.queryStateByRole(stageCode, type);
        return RespData.success(stateInfo);
    }

    /**
     * 查询当前状态/付款施工详细阶段
     *
     * @param type ，1获取平台状态，2获取装饰公司状态，3获取施工人员状态，4获取消费者状态
     * @return
     */
    @Override
    public MyRespBundle<Map<String, String>> getStateDetailInfo(String orderNo, int type) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        String stateInfo = ConstructionStateEnum.queryStateByRole(stageCode, type);
        String stateDetailInfo = commonService.queryStateCodeDetailByOrderNo(orderNo);
        Map<String, String> map = new HashMap<>();
        map.put("bigStage", stateInfo);
        map.put("detailStage", stateDetailInfo);
        return RespData.success(map);
    }

    /**
     * 运营平台
     * 派单给装饰公司
     */
    @Override
    public MyRespBundle<String> operateDispatchToConstruction(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        if (stageCode.equals(ConstructionStateEnum.STATE_500.getState())) {
            if (commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnum.STATE_510.getState())) {
                return RespData.success();
            }
        }
        return RespData.error(ResultMessage.ERROR.code, "该订单不符合派单状态");
    }


    /**
     * 装饰公司
     * 1派单给服务人员 2施工报价完成  5确认线下签约完成（自动创建工地项目）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void constructionState(String orderNo, int type) {
        if (StringUtils.isBlank(orderNo)) {
            throw new RuntimeException("订单编号不能为空");
        }
        ConstructionStateEnum constructionState;
        ConstructionStateEnum nextConstructionState;
        switch (type) {
            case 1:
                constructionState = ConstructionStateEnum.STATE_510;
                nextConstructionState = ConstructionStateEnum.STATE_530;
                break;
            case 2:
                constructionState = ConstructionStateEnum.STATE_530;
                nextConstructionState = ConstructionStateEnum.STATE_540;
                break;
            case 4:
                constructionState = ConstructionStateEnum.STATE_550;
                nextConstructionState = ConstructionStateEnum.STATE_560;
                break;
            case 5:
                constructionState = ConstructionStateEnum.STATE_560;
                nextConstructionState = ConstructionStateEnum.STATE_600;
                break;
            default:
                LOGGER.error("错误的操作类型，orderNo:{}, type:{}", orderNo, type);
                throw new RuntimeException("操作失败-请稍后重试");
        }
        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        if (constructionState.getState() == stageCode) {
            commonService.updateStateCodeByOrderNo(orderNo, nextConstructionState.getState());
        } else {
            LOGGER.error("错误的订单状态，orderNo:{}, type:{}, state:{}", orderNo, type, stageCode);
            throw new RuntimeException("操作失败-请稍后重试");
        }
    }

    /**
     * 装饰公司
     * 3审核完成 （审核是否通过）
     */
    @Override
    public MyRespBundle<String> constructionStateOfExamine(String orderNo, int type, int isPass) {

        Integer stage = commonService.queryStateCodeByOrderNo(orderNo);

        if (ConstructionStateEnum.STATE_540.getState() == stage) {
            if (isPass == 1) {
                if (commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnum.STATE_550.getState())) {
                    return RespData.success();
                }
            } else {
                if (commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnum.STATE_530.getState())) {
                    return RespData.success();
                }
            }
        } else {
            return RespData.error(ResultMessage.ERROR.code, "该订单不符合审核状态");
        }

        return RespData.error(ResultMessage.ERROR.code, "状态更新失败状态-请稍后重试");
    }

    /**
     * 装饰公司
     * 5确认线下签约完成（自动创建工地项目）
     */
    @Override
    public void contractCompleteState(String orderNo) {
        commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnum.STATE_600.getState());
    }

    /**
     * 装饰公司
     */
    @Override
    public MyRespBundle<Boolean> firstPay(String orderNo) {
        return RespData.success(commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnum.STATE_610.getState()));
    }


    /**
     * 支付
     */
    @Override
    public MyRespBundle<String> customerPay(String orderNo, String feeName, Integer sort, String isComplete) {

        if (sort == -1) {
            firstPay(orderNo);
        }

        ConstructionOrderExample orderExample = new ConstructionOrderExample();
        ConstructionOrderExample.Criteria criteria = orderExample.createCriteria();
        criteria.andStatusEqualTo(1);
        criteria.andOrderNoEqualTo(orderNo);
        List<ConstructionOrder> constructionOrderList = constructionOrderMapper.selectByExample(orderExample);
        if (constructionOrderList.size() == 0) {
            return RespData.error("此项目不存在！");
        }
        String projectNo = constructionOrderList.get(0).getProjectNo();
        MyRespBundle<String> boo = constructionAndPayStateService.isStagePay(orderNo, sort);
        if (!boo.getCode().equals(1000)) {
            return RespData.error(ResultMessage.ERROR.code, "该状态不能进行支付");
        }

        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }

        ConstructionOrderPayExample example = new ConstructionOrderPayExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        List<ConstructionOrderPay> list = constructionOrderPayMapper.selectByExample(example);

        if (list.size() > 0) {
            ConstructionOrderPay constructionOrderPay = new ConstructionOrderPay();
            constructionOrderPay.setFeeName(commonService.getSchedulingName(orderNo, sort));
            constructionOrderPay.setSort(sort.shortValue());
            constructionOrderPay.setIsEnd("pay");
            constructionOrderPayMapper.updateByExampleSelective(constructionOrderPay, example);
        } else {
            ConstructionOrderPay constructionOrderPay = new ConstructionOrderPay();
            constructionOrderPay.setOrderNo(orderNo);
            constructionOrderPay.setFeeName(commonService.getSchedulingName(orderNo, sort));
            constructionOrderPay.setSort(sort.shortValue());
            constructionOrderPay.setIsEnd("pay");
            constructionOrderPayMapper.insertSelective(constructionOrderPay);
        }
        // sort=0 支付首款
        if (sort == 0) {
            commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnum.STATE_610.getState());
        } else {
            commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnum.STATE_640.getState());

            Map<String, Integer> map = getPayScheduling(projectNo);
            if (map.get("paySort").equals(map.get("bigSort")) && sort.equals(map.get("paySort"))) {
                //支付必须先回调再改状态
                commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnum.STATE_700.getState());
            }
        }

        return RespData.success();
    }


    /**
     * 施工阶段方案
     */
    @Override
    public MyRespBundle<String> constructionPlan(String projectNo, Integer sort) {
        if (!constructionAndPayStateService.isBeComplete(projectNo, sort)) {
            throw new RuntimeException("施工阶段状态变更异常");
        }
        if (StringUtils.isBlank(projectNo)) {
            LOGGER.error("项目编号为空");
            throw new RuntimeException();
        }
        //查询订单
        ConstructionOrder constructionOrder = constructOrderService.findByProjectNo(projectNo);
        if (constructionOrder == null) {
            LOGGER.error("未查询的施工订单，projectNo:{}", projectNo);
            throw new RuntimeException();
        }
        //更新状态
        ConstructionOrderPay constructionOrderPay = constructOrderPayService.findByOrderNo(constructionOrder.getOrderNo());
        if (constructionOrderPay == null) {
            constructionOrderPay = new ConstructionOrderPay();
            constructionOrderPay.setOrderNo(constructionOrder.getOrderNo());
            constructionOrderPay.setFeeName("开工报告");
            constructionOrderPay.setSort(sort.shortValue());
            constructionOrderPay.setIsEnd("construction");
            constructOrderPayService.insert(constructionOrderPay);
            commonService.updateStateCodeByOrderNo(constructionOrder.getOrderNo(), ConstructionStateEnum.STATE_620.getState());
        } else {
            if (sort == 0) {
                constructionOrderPay.setFeeName("开工报告");
                constructionOrderPay.setSort(sort.shortValue());
                constructionOrderPay.setIsEnd("construction");
                commonService.updateStateCodeByOrderNo(constructionOrder.getOrderNo(), ConstructionStateEnum.STATE_620.getState());
            } else {

                //查询当前施工阶段名称
                List<ProjectBigSchedulingDetailsVO> schedulingDetailsVOs = schedulingService.getScheduling(projectNo).getData();
                if (schedulingDetailsVOs == null || schedulingDetailsVOs.isEmpty()) {
                    LOGGER.error("未查询到正确的排期信息，projectNo:{}", projectNo);
                    throw new RuntimeException();
                }

                ProjectBigSchedulingDetailsVO currentProjectBigScheduling = null;
                int maxSort = 0;
                for (ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO : schedulingDetailsVOs) {
                    if (projectBigSchedulingDetailsVO.getBigSort().equals(sort)) {
                        currentProjectBigScheduling = projectBigSchedulingDetailsVO;
                    }
                    if (projectBigSchedulingDetailsVO.getBigSort() > maxSort) {
                        maxSort = projectBigSchedulingDetailsVO.getBigSort();
                    }
                }

                if (currentProjectBigScheduling == null) {
                    throw new RuntimeException("施工阶段状态变更异常");
                }
                constructionOrderPay.setFeeName(currentProjectBigScheduling.getBigName());
                constructionOrderPay.setSort(sort.shortValue());
                constructionOrderPay.setIsEnd("construction");

                BuildPayConfig buildPayConfig = buildConfigService.queryBySchemeNoAndScheduleSort(constructionOrder.getSchemeNo(), sort);
                ConstructionStateEnum nextConstructState = null;
                if (buildPayConfig != null) {
                    nextConstructState = ConstructionStateEnum.STATE_630;
                }
                if (maxSort == sort) {
                    //完工即修改状态为订单完成
                    nextConstructState = ConstructionStateEnum.STATE_650;
                }
                if (nextConstructState != null) {
                    commonService.updateStateCodeByOrderNo(constructionOrder.getOrderNo(), nextConstructState.getState());
                }

                //支付阶段通知
                constructionAndPayStateService.notifyPay(constructionOrder.getOrderNo(), sort);
            }
            constructOrderPayService.updateByOrderNo(constructionOrderPay, constructionOrder.getOrderNo());
        }

        return RespData.success();
    }

    /**
     * 根据项目编号获取支付尾期sort+排期尾期sort
     * bigsot  排期阶段最大值
     * paySort 支付方案中最大排期值
     *
     * @param projectNo
     * @return
     */
    public Map<String, Integer> getPayScheduling(String projectNo) {
        Map<String, Integer> map = new HashMap<>();
        //获取排期详情
        ProjectBigSchedulingDetailsExample detailsExample = new ProjectBigSchedulingDetailsExample();
        ProjectBigSchedulingDetailsExample.Criteria detailsCriteria = detailsExample.createCriteria();
        detailsCriteria.andStatusEqualTo(1);
        detailsCriteria.andProjectNoEqualTo(projectNo);
        List<ProjectBigSchedulingDetails> projectBigSchedulingDetailsList = detailsMapper.selectByExample(detailsExample);
        if (projectBigSchedulingDetailsList.size() == 0) {
            throw new RuntimeException("此项目不存在排期详情");
        }
        Integer bigSort = 0;
        for (ProjectBigSchedulingDetails projectBigSchedulingDetails : projectBigSchedulingDetailsList) {
            if (projectBigSchedulingDetails.getBigSort() > bigSort) {
                bigSort = projectBigSchedulingDetails.getBigSort();
            }
        }
        map.put("bigSort", bigSort);

        //获取支付方案
        BuildPayConfigExample configExample = new BuildPayConfigExample();
        BuildPayConfigExample.Criteria criteria = configExample.createCriteria();
        criteria.andSchemeNoEqualTo(projectBigSchedulingDetailsList.get(0).getSchemeNo());
        criteria.andDeleteStateIn(Arrays.asList(2, 3));
        List<BuildPayConfig> buildPayConfigList = buildPayConfigMapper.selectByExample(configExample);
        if (buildPayConfigList.size() == 0) {
            throw new RuntimeException("支付方案不存在");
        }
        Integer paySort = 0;
        for (BuildPayConfig buildPayConfig : buildPayConfigList) {
            if (!buildPayConfig.getStageCode().equals("-1") && Integer.parseInt(buildPayConfig.getStageCode()) > paySort) {
                paySort = Integer.parseInt(buildPayConfig.getStageCode());
            }
        }
        map.put("paySort", paySort);
        return map;

    }


    /**
     * 消费者
     * 取消订单
     * 签约阶段逆向
     */
    @Override
    public MyRespBundle<String> customerCancelOrder(String userId, String orderNo, String cancelReason) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }

        if (StringUtils.isBlank(userId)) {
            return RespData.error(ResultMessage.ERROR.code, "用户ID不能为空");
        }

        ProjectExample example = new ProjectExample();
        example.createCriteria().andOwnerIdEqualTo(userId).andStatusEqualTo(1);
        List<Project> list = projectMapper.selectByExample(example);
        if (list.size() <= 0) {
            return RespData.error(ResultMessage.ERROR.code, "用户不存在");
        }

        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        if (stageCode >= ConstructionStateEnum.STATE_600.getState()) {
            return RespData.error(ResultMessage.ERROR.code, "当前状态不能取消订单");
        }

        ConstructionOrderExample example2 = new ConstructionOrderExample();
        example2.createCriteria().andOrderNoEqualTo(orderNo);
        ConstructionOrder constructionOrder = new ConstructionOrder();
        constructionOrder.setOrderStage(ConstructionStateEnum.STATE_710.getState());
        constructionOrder.setRemark(cancelReason);
        int isUpdate = constructionOrderMapper.updateByExampleSelective(constructionOrder, example2);
        if (isUpdate == 1) {
            return RespData.success();
        } else {
            return RespData.error(ResultMessage.ERROR.code, "取消订单失败-请稍后重试");
        }
    }

    /**
     * 消费者
     * 取消订单
     * 签约阶段逆向
     * 查看状态
     */
    @Override
    public Boolean customerCancelOrderState(String userId, String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return false;
        }

        if (StringUtils.isBlank(userId)) {
            return false;
        }

        ProjectExample example = new ProjectExample();
        example.createCriteria().andOwnerIdEqualTo(userId).andStatusEqualTo(1);
        List<Project> list = projectMapper.selectByExample(example);
        if (list.size() <= 0) {
            return false;
        }

        Integer stageCode = commonService.queryStateCodeByOrderNo(orderNo);
        if (ConstructionStateEnum.STATE_600.getState() <= stageCode) {
            return false;
        }
        return true;
    }

    /**
     * 消费者
     * 取消订单
     * 支付未开工逆向
     * 审核通过
     */
    @Override
    public MyRespBundle<String> customerCancelOrderForPay(String orderNo, int type) {
        if (StringUtils.isBlank(orderNo)) {
            return RespData.error(ResultMessage.ERROR.code, "订单编号不能为空");
        }

        if (type == 1) {
            commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnum.STATE_720.getState());
        } else {
            commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnum.STATE_730.getState());
        }

        return RespData.success();
    }

    /**
     * 订单完成
     *
     * @param orderNo
     */
    public void constructionComplete(String orderNo) {

        commonService.updateStateCodeByOrderNo(orderNo, ConstructionStateEnum.STATE_700.getState());
    }

    @Override
    public List<OrderStatusDTO> getStates(int type, Integer currentStatus) {
        List<OrderStatusDTO> orderStatusDTOs = new ArrayList<>();
        ConstructionStateEnum[] stateEnums = ConstructionStateEnum.values();
        String preStateName = "";
        for (ConstructionStateEnum constructionState : stateEnums) {
            String stateName = constructionState.getStateName(type);
            if (StringUtils.isBlank(stateName)) {
                continue;
            }
            if (stateName.equals(preStateName)) {
                continue;
            }
            if (currentStatus > ConstructionStateEnum.STATE_700.getState()) {
                break;
            }
            if (constructionState.getState() > ConstructionStateEnum.STATE_700.getState()) {
                break;
            }
            preStateName = stateName;
            OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
            orderStatusDTO.setStatus(constructionState.getState());
            orderStatusDTO.setName(stateName);

            orderStatusDTOs.add(orderStatusDTO);
        }

        if (currentStatus > ConstructionStateEnum.STATE_700.getState()) {
            ConstructionStateEnum currentConstructState = ConstructionStateEnum.queryByState(currentStatus);
            OrderStatusDTO orderStatusDTO = new OrderStatusDTO();
            orderStatusDTO.setStatus(currentConstructState.getState());
            orderStatusDTO.setName(currentConstructState.getStateName(type));

            orderStatusDTOs.add(orderStatusDTO);
        }

        return orderStatusDTOs;
    }

    @Override
    public boolean getConstructState(int state, int complaintState, int stateRange) {

        switch (complaintState) {
            // 未投诉
            case 1:
                break;
            // 处理中
            case 2:
                break;
            // 关闭
            case 3:
                return stateRange == 1 || stateRange == 6;
            // 已取消
            case 4:
                break;
            default:
        }

        switch (stateRange) {
            // 全部
            case 1:
                return true;
            // 待签约
            case 2:
                return state >= 500 && state < 600;
            // 待开工
            case 3:
                return state == 610;
            // 施工中
            case 4:
                return state == 630 || state == 650;
            // 已竣工
            case 5:
                return state == 700;
            default:
                break;
        }

        return false;
    }
}