package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.construction.DecorationDistributionOrder;
import cn.thinkfree.service.construction.OrderListCommonService;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.construction.vo.DecorationOrderCommonVo;
import cn.thinkfree.service.construction.vo.DecorationOrderListVo;
import cn.thinkfree.service.construction.vo.appointWorkerListVo;
import cn.thinkfree.service.platform.employee.ProjectUserService;
import cn.thinkfree.service.platform.order.OrderService;
import cn.thinkfree.service.platform.order.SendOrderNoticeService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class DecorationDistributionOrderImpl implements DecorationDistributionOrder {

    @Autowired
    ConstructionStateService constructionStateService;
    @Autowired
    CompanyInfoMapper companyInfoMapper;
    @Autowired
    CityMapper cityMapper;
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    OrderListCommonService orderListCommonService;
    @Autowired
    EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    OrderUserMapper orderUserMapper;
    @Autowired
    private SendOrderNoticeService orderNoticeService;
    @Autowired
    private ProjectUserService projectUserService;


    /**
     * 订单列表(带有员工)
     *
     * @return
     */
    @Override
    public PageInfo<DecorationOrderListVo> getOrderList(String companyNo, int pageNum, int pageSize, String projectNo, String appointmentTime,
                                                        String addressDetail, String owner, String phone, String orderStage) {
        PageInfo<DecorationOrderListVo> pageInfo = orderListCommonService.getDecorationOrderList(companyNo, pageNum, pageSize, projectNo, appointmentTime,
                addressDetail, owner, phone, orderStage);
        return pageInfo;
    }


    /**
     * 订单列表统计
     *
     * @return
     */

    @Override
    public MyRespBundle<ConstructionOrderManageVo> getOrderNum() {

        ConstructionOrderExample example = new ConstructionOrderExample();
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);

        /* 统计状态个数 */
        int waitExamine = 0, waitSign = 0;
        for (ConstructionOrder constructionOrder : list) {
            // 订单状态 统计
            int stage = constructionOrder.getOrderStage();
            if (stage == ConstructionStateEnum.STATE_540.getState()) {
                waitExamine++;
            }
            if (stage == ConstructionStateEnum.STATE_560.getState()) {
                waitSign++;
            }
        }

        ConstructionOrderManageVo constructionOrderManageVo = new ConstructionOrderManageVo();
        constructionOrderManageVo.setOrderNum(list.size());
        constructionOrderManageVo.setWaitExamine(waitExamine);
        constructionOrderManageVo.setWaitSign(waitSign);
        return RespData.success(constructionOrderManageVo);
    }

    /**
     * 指派施工人员列表
     *
     * @return
     */
    @Override
    public MyRespBundle<Map<String, List<appointWorkerListVo>>> appointWorkerList(String companyId) {

        if (StringUtils.isBlank(companyId)) {
            return RespData.error(ResultMessage.ERROR.code, "公司ID不能为空");
        }

        EmployeeMsgExample example = new EmployeeMsgExample();
        example.createCriteria().andCompanyIdEqualTo(companyId).andEmployeeStateEqualTo(1);
        List<EmployeeMsg> list = employeeMsgMapper.selectByExample(example);

        List<appointWorkerListVo> list1 = new ArrayList<>();
        List<appointWorkerListVo> list2 = new ArrayList<>();
        List<appointWorkerListVo> list3 = new ArrayList<>();
        List<appointWorkerListVo> list4 = new ArrayList<>();

        for (EmployeeMsg employeeMsg : list) {

            if (employeeMsg.getRoleCode().equals("CM")) { //CM  工长
                appointWorkerListVo appointWorkerListVo = new appointWorkerListVo();
                appointWorkerListVo.setWorkerId(employeeMsg.getUserId());
                appointWorkerListVo.setName(employeeMsg.getRealName());
                appointWorkerListVo.setRoleName("工长");
                appointWorkerListVo.setRoleType("业务人员");
                long projectNum = projectUserService.countByUserId(employeeMsg.getUserId());
                appointWorkerListVo.setProjectNum( projectNum + "");
                list1.add(appointWorkerListVo);
            }
            if (employeeMsg.getRoleCode().equals("CS")) { //CS  管家
                appointWorkerListVo appointWorkerListVo = new appointWorkerListVo();
                appointWorkerListVo.setWorkerId(employeeMsg.getUserId());
                appointWorkerListVo.setName(employeeMsg.getRealName());
                appointWorkerListVo.setRoleName("管家");
                appointWorkerListVo.setRoleType("业务人员");
                long projectNum = projectUserService.countByUserId(employeeMsg.getUserId());
                appointWorkerListVo.setProjectNum( projectNum + "");
                list2.add(appointWorkerListVo);

            }
            if (employeeMsg.getRoleCode().equals("CP")) { //CP  项目经理
                appointWorkerListVo appointWorkerListVo = new appointWorkerListVo();
                appointWorkerListVo.setWorkerId(employeeMsg.getUserId());
                appointWorkerListVo.setName(employeeMsg.getRealName());
                appointWorkerListVo.setRoleName("项目经理");
                appointWorkerListVo.setRoleType("业务人员");
                long projectNum = projectUserService.countByUserId(employeeMsg.getUserId());
                appointWorkerListVo.setProjectNum( projectNum + "");
                list3.add(appointWorkerListVo);
            }
            if (employeeMsg.getRoleCode().equals("CQ")) { //CQ  质检员
                appointWorkerListVo appointWorkerListVo = new appointWorkerListVo();
                appointWorkerListVo.setWorkerId(employeeMsg.getUserId());
                appointWorkerListVo.setName(employeeMsg.getRealName());
                appointWorkerListVo.setRoleName("质检员");
                appointWorkerListVo.setRoleType("业务人员");
                long projectNum = projectUserService.countByUserId(employeeMsg.getUserId());
                appointWorkerListVo.setProjectNum( projectNum + "");
                list4.add(appointWorkerListVo);
            }
        }

        Map<String, List<appointWorkerListVo>> map = new HashMap<>();
        map.put("CM", list1);
        map.put("CS", list2);
        map.put("CP", list3);
        map.put("CQ", list4);

        return RespData.success(map);
    }


    /**
     * 项目分配施工人员 （单选）
     *
     * @return
     */
    @Override
    public void appointWorker(List<Map<String, String>> workerInfo) {

        Set<String> setOrderNo = new LinkedHashSet<>();
        Set<String> setProjectNO = new LinkedHashSet<>();

        for (Map<String, String> map1 : workerInfo) {
            if (StringUtils.isBlank(map1.get("orderNo"))) {
                throw new RuntimeException("订单编号不能为空");
            }
            if (StringUtils.isBlank(map1.get("projectNo"))) {
                throw new RuntimeException("项目编号不能为空");
            }
            if (StringUtils.isBlank(map1.get("workerNo"))) {
                throw new RuntimeException("员工编号不能为空");
            }
            if (StringUtils.isBlank(map1.get("roleName"))) {
                throw new RuntimeException("角色名称不能为空");
            }
            setOrderNo.add(map1.get("orderNo"));
            setProjectNO.add(map1.get("projectNo"));
        }

        if (setOrderNo.size() != 1 && setProjectNO.size() != 1) {
            throw new RuntimeException("出现不同订单编号或项目编号");
        }

        // 改变订单状态
        constructionStateService.constructionState(workerInfo.get(0).get("orderNo"), 1);
        for (Map<String, String> map : workerInfo) {

            OrderUserExample example = new OrderUserExample();
            example.createCriteria().andUserIdEqualTo(map.get("workerNo")).andOrderNoEqualTo(map.get("orderNo"));
            List<OrderUser> list = orderUserMapper.selectByExample(example);
            OrderUser orderUser = new OrderUser();
            if (list.size() <= 0) {
                orderUser.setOrderNo(map.get("orderNo"));
                orderUser.setUserId(map.get("workerNo"));
                orderUser.setProjectNo(map.get("projectNo"));
                orderUser.setRoleCode(map.get("roleName"));
                orderUser.setCreateTime(new Date());
                orderUser.setIsTransfer((short) 0);
                orderUserMapper.insertSelective(orderUser);
            } else {
                orderUser.setUserId(map.get("workerNo"));
                orderUser.setRoleCode(map.get("roleName"));
                orderUser.setUpdateTime(new Date());
                orderUserMapper.updateByExampleSelective(orderUser, example);
            }

        }
        for (Map<String, String> map : workerInfo) {
            orderNoticeService.sendEmployeeReceipt(map.get("workerNo"), map.get("projectNo"));
        }
        String projectNo = workerInfo.get(0).get("projectNo");
        String ownerId = projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.OWNER_POWER);
        orderNoticeService.sendOwnerConsDispatch(ownerId, projectNo);
    }

}
