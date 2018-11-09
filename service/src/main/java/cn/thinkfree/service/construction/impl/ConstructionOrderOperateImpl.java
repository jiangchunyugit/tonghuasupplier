package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.appvo.PersionVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.approvalflow.AfInstanceService;
import cn.thinkfree.service.config.HttpLinks;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructionOrderOperate;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.HttpUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ConstructionOrderOperateImpl implements ConstructionOrderOperate {

    @Autowired
    ConstructionOrderMapper constructionOrderMapper;

    @Autowired
    CommonService commonService;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    NewOrderUserService newOrderUserService;

    @Autowired
    EmployeeMsgMapper employeeMsgMapper;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;

    @Autowired
    ProjectSchedulingMapper projectSchedulingMapper;

    @Autowired
    OrderUserMapper orderUserMapper;

    @Autowired
    AfInstanceService afInstanceService;

    @Autowired
    FundsOrderMapper fundsOrderMapper;

    @Resource
    private HttpLinks httpLinks;

    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * 施工订单管理-列表
     * 运营后台
     *
     * @return
     */
    @Override
    public MyRespBundle<ConstructionOrderManageVo> getConstructionOrderList(int pageNum, int pageSize, String cityName) {

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ConstructionOrderListVo> pageInfo = new PageInfo<>();
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        example.createCriteria().andStatusEqualTo(1);

        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        List<ConstructionOrderListVo> listVo = new ArrayList<>();

        /* 统计状态个数 */
        int waitExamine = 0, waitSign = 0, waitPay = 0;

        /* 项目编号List */
        List<String> listProjectNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listProjectNo.add(constructionOrder.getProjectNo());
        }

        /* 订单编号List */
        List<String> listOrdertNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listOrdertNo.add(constructionOrder.getOrderNo());
        }

        /* 公司编号List */
        List<String> listCompanyNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listCompanyNo.add(constructionOrder.getCompanyId());
        }

        // 所属地区 & 项目地址 & 预约日期
        List<Project> list1 = getProjectInfo(listProjectNo);
        // 项目经理
        List<Map<String, String>> list2 = getEmployeeInfo(listProjectNo, "CP");
        // 设计师
        List<Map<String, String>> list3 = getEmployeeInfo(listProjectNo, "CD");
        // 延期天数
        List<ProjectScheduling> list4 = getdelayDay(listProjectNo);
        // 确认验收
        Map<String, Integer> Map5 = getApprove(listProjectNo);
        // 合同金额/时间
        List<FundsOrder> list6 = getFundsOrder(listProjectNo);

        continueOut:
        for (ConstructionOrder constructionOrder : list) {
            ConstructionOrderListVo constructionOrderListVo = new ConstructionOrderListVo();

            // 所属地区 & 项目地址 & 预约日期
            for (Project project : list1) {
                if (constructionOrder.getProjectNo().equals(project.getProjectNo())) {
                    // 条件筛选 - 城市
                    if (!StringUtils.isBlank(cityName)) {
                        if (!cityName.equals(project.getCity())) {
                            continue continueOut;
                        }
                    }
                    constructionOrderListVo.setAddress(project.getCity());
                    constructionOrderListVo.setAddressDetail(project.getAddressDetail());
                    constructionOrderListVo.setAppointmentTime(project.getCreateTime());
                }
                // 业主 & 手机号  TODO 没做批量查询
//                PersionVo owner = getOwnerId(project.getOwnerId());
//                constructionOrderListVo.setOwner(owner.getName());
//                constructionOrderListVo.setPhone(owner.getPhone());
            }

            // 订单编号 & 项目编号
            constructionOrderListVo.setOrderNo(constructionOrder.getOrderNo());
            constructionOrderListVo.setProjectNo(constructionOrder.getProjectNo());
            // 项目经理
            for (Map<String, String> OrderUser : list2) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    constructionOrderListVo.setProjectManager(OrderUser.get("name"));
                }
            }
            // 设计师
            for (Map<String, String> OrderUser : list3) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    constructionOrderListVo.setProjectManager(OrderUser.get("name"));
                }
            }
            // 公司名称
            constructionOrderListVo.setCompanyName(getCompanyInfo(constructionOrder.getCompanyId()));

            // 施工阶段
            constructionOrderListVo.setConstructionProgress(getContstructionStage(constructionOrder.getConstructionStage()));

            // 订单状态
            constructionOrderListVo.setOrderStage(ConstructionStateEnum.getNowStateInfo(constructionOrder.getOrderStage(), 1));

            //延期天数
            for (ProjectScheduling projectScheduling : list4) {
                if (constructionOrder.getProjectNo().equals(projectScheduling.getProjectNo())) {
                    constructionOrderListVo.setDelayDays(projectScheduling.getDelay());
                }
            }
            // 签约日期 已支付 应支付金额
            for (FundsOrder fundsOrder : list6){
                if (constructionOrder.getProjectNo().equals(fundsOrder.getProjectNo())){
                    constructionOrderListVo.setSignedTime(DateUtil.formateToDate(fundsOrder.getSignedTime(),FORMAT));
                    constructionOrderListVo.setReducedContractAmount(fundsOrder.getActualAmount());
                    constructionOrderListVo.setHavePaid(fundsOrder.getPaidAmount());
                }
            }

            // 最近验收情况14
            for (Map.Entry<String, Integer> map : Map5.entrySet()) {
                if (constructionOrder.getProjectNo().equals(map.getKey())) {
                    switch (map.getKey()) {
                        case "1":
                            constructionOrderListVo.setCheckCondition("通过");
                        case "2":
                            constructionOrderListVo.setCheckCondition("未通过");
                        default:
                            constructionOrderListVo.setCheckCondition("--");
                    }
                }
            }

            // 订单状态 统计
            int stage = constructionOrder.getOrderStage();
            if (stage == ConstructionStateEnum.STATE_530.getState()) {
                waitExamine++;
            }
            if (stage == ConstructionStateEnum.STATE_550.getState()) {
                waitSign++;
            }
            if ((stage >= ConstructionStateEnum.STATE_600.getState() && stage <= ConstructionStateEnum.STATE_690.getState())) {
                waitPay++;
            }

            listVo.add(constructionOrderListVo);
        }


        pageInfo.setList(listVo);
        ConstructionOrderManageVo constructionOrderManageVo = new ConstructionOrderManageVo();

        constructionOrderManageVo.setCityList(commonService.getCityList());
        constructionOrderManageVo.setOrderList(pageInfo.getList());
        constructionOrderManageVo.setCountPageNum(pageInfo.getSize());
        constructionOrderManageVo.setOrderNum(list.size());

        constructionOrderManageVo.setWaitExamine(waitExamine);
        constructionOrderManageVo.setWaitSign(waitSign);
        constructionOrderManageVo.setWaitPay(waitPay);

        return RespData.success(constructionOrderManageVo);
    }

    /**
     * 施工工地管理-列表
     * 运营后台
     *
     * @return
     */
    @Override
    public MyRespBundle<ConstructionOrderManageVo> getConstructionSiteList(int pageNum, int pageSize, String cityName) {

        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ConstructionOrderListVo> pageInfo = new PageInfo<>();
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.setOrderByClause("create_time DESC");
        example.createCriteria().andStatusEqualTo(1);

        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        List<ConstructionOrderListVo> listVo = new ArrayList<>();

        /* 统计状态个数 */
        int waitExamine = 0, waitSign = 0, waitPay = 0;

        /* 项目编号List */
        List<String> listProjectNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listProjectNo.add(constructionOrder.getProjectNo());
        }

        /* 订单编号List */
        List<String> listOrdertNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listOrdertNo.add(constructionOrder.getOrderNo());
        }

        /* 公司编号List */
        List<String> listCompanyNo = new ArrayList<>();
        for (ConstructionOrder constructionOrder : list) {
            listCompanyNo.add(constructionOrder.getCompanyId());
        }

        // 所属地区 & 项目地址 & 预约日期
        List<Project> list1 = getProjectInfo(listProjectNo);
        // 项目经理
        List<Map<String, String>> list2 = getEmployeeInfo(listProjectNo, "CP");
        // 设计师
        List<Map<String, String>> list3 = getEmployeeInfo(listProjectNo, "CD");
        // 延期天数
        List<ProjectScheduling> list4 = getdelayDay(listProjectNo);
        // 确认验收
        Map<String, Integer> Map5 = getApprove(listProjectNo);
        // 合同金额/时间
        List<FundsOrder> list6 = getFundsOrder(listProjectNo);
        List<ProjectScheduling> list7 = getProjectScheduling(listProjectNo);
        //合同额
        List<ConstructionOrder> list8 = getMoney(listOrdertNo);
        continueOut:
        for (ConstructionOrder constructionOrder : list) {
            ConstructionOrderListVo constructionOrderListVo = new ConstructionOrderListVo();

            // 所属地区 & 项目地址 & 预约日期
            for (Project project : list1) {
                if (constructionOrder.getProjectNo().equals(project.getProjectNo())) {
                    // 条件筛选 - 城市
                    if (!StringUtils.isBlank(cityName)) {
                        if (!cityName.equals(project.getCity())) {
                            continue continueOut;
                        }
                    }
                    constructionOrderListVo.setAddress(project.getCity());
                    constructionOrderListVo.setAddressDetail(project.getAddressDetail());
                    constructionOrderListVo.setAppointmentTime(project.getCreateTime());
                }
                // 业主 & 手机号  TODO 没做批量查询
//                PersionVo owner = getOwnerId(project.getOwnerId());
//                constructionOrderListVo.setOwner(owner.getName());
//                constructionOrderListVo.setPhone(owner.getPhone());
            }

            // 订单编号 & 项目编号
            constructionOrderListVo.setOrderNo(constructionOrder.getOrderNo());
            constructionOrderListVo.setProjectNo(constructionOrder.getProjectNo());
            // 项目经理
            for (Map<String, String> OrderUser : list2) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    constructionOrderListVo.setProjectManager(OrderUser.get("name"));
                }
            }
            // 设计师
            for (Map<String, String> OrderUser : list3) {
                if (constructionOrder.getProjectNo().equals(OrderUser.get("projectNo"))) {
                    constructionOrderListVo.setProjectManager(OrderUser.get("name"));
                }
            }
            // 公司名称
            constructionOrderListVo.setCompanyName(getCompanyInfo(constructionOrder.getCompanyId()));

            // 施工阶段
            constructionOrderListVo.setConstructionProgress(getContstructionStage(constructionOrder.getConstructionStage()));

            // 订单状态
            constructionOrderListVo.setOrderStage(ConstructionStateEnum.getNowStateInfo(constructionOrder.getOrderStage(), 1));

            //延期天数
            for (ProjectScheduling projectScheduling : list4) {
                if (constructionOrder.getProjectNo().equals(projectScheduling.getProjectNo())) {
                    constructionOrderListVo.setDelayDays(projectScheduling.getDelay());
                    constructionOrderListVo.setStartDates(projectScheduling.getStartTime());
                    constructionOrderListVo.setCompletionDays(projectScheduling.getEndTime());
                }
            }
            // 签约日期 已支付 应支付金额
            for (FundsOrder fundsOrder : list6){
                if (constructionOrder.getProjectNo().equals(fundsOrder.getProjectNo())){
                    constructionOrderListVo.setSignedTime(DateUtil.formateToDate(fundsOrder.getSignedTime(),FORMAT));
                    constructionOrderListVo.setReducedContractAmount(fundsOrder.getActualAmount());
                    constructionOrderListVo.setHavePaid(fundsOrder.getPaidAmount());
                }
            }
            //合同金额
            for (ConstructionOrder co :list8){
                constructionOrderListVo.setContractAmount(co.getMoney().toString());
            }


            // 最近验收情况14
            for (Map.Entry<String, Integer> map : Map5.entrySet()) {
                if (constructionOrder.getProjectNo().equals(map.getKey())) {
                    switch (map.getKey()) {
                        case "1":
                            constructionOrderListVo.setCheckCondition("通过");
                        case "2":
                            constructionOrderListVo.setCheckCondition("未通过");
                        default:
                            constructionOrderListVo.setCheckCondition("--");
                    }
                }
            }

            // 订单状态 统计
            int stage = constructionOrder.getOrderStage();
            if (stage == ConstructionStateEnum.STATE_530.getState()) {
                waitExamine++;
            }
            if (stage == ConstructionStateEnum.STATE_550.getState()) {
                waitSign++;
            }
            if ((stage >= ConstructionStateEnum.STATE_600.getState() && stage <= ConstructionStateEnum.STATE_690.getState())) {
                waitPay++;
            }

            listVo.add(constructionOrderListVo);
        }


        pageInfo.setList(listVo);
        ConstructionOrderManageVo constructionOrderManageVo = new ConstructionOrderManageVo();

        constructionOrderManageVo.setCityList(commonService.getCityList());
        constructionOrderManageVo.setOrderList(pageInfo.getList());
        constructionOrderManageVo.setCountPageNum(pageInfo.getSize());
        constructionOrderManageVo.setOrderNum(list.size());

        constructionOrderManageVo.setWaitExamine(waitExamine);
        constructionOrderManageVo.setWaitSign(waitSign);
        constructionOrderManageVo.setWaitPay(waitPay);

        return RespData.success(constructionOrderManageVo);
    }
    //查询合同
    private List<ConstructionOrder> getMoney(List<String> listOrdertNo) {
        ConstructionOrderExample constructionOrderExample = new ConstructionOrderExample();
        constructionOrderExample.createCriteria().andOrderNoIn(listOrdertNo);
        return constructionOrderMapper.selectByExample(constructionOrderExample);
    }

    //查询竣工
    private List<ProjectScheduling> getProjectScheduling(List<String> listProjectNo) {
        ProjectSchedulingExample projectSchedulingExample = new ProjectSchedulingExample();
        projectSchedulingExample.createCriteria().andProjectNoIn(listProjectNo);
        return projectSchedulingMapper.selectByExample(projectSchedulingExample);
    }

    /**
     * 查询项目信息
     *
     * @param listProjectNo
     * @return
     */
    public List<Project> getProjectInfo(List<String> listProjectNo) {
        ProjectExample example = new ProjectExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        return projectMapper.selectByExample(example);
    }

    /**
     * 查询用户信息 -用户中心接口
     *
     * @param userId
     * @return
     */
    public PersionVo getOwnerId(String userId) {
        PersionVo owner = new PersionVo();
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("userId", userId);
        requestMap.put("roleId", "CC");
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(httpLinks.getUserCenterGetUserMsg(), requestMap);
        Map responseMap = JSONUtil.json2Bean(httpRespMsg.getContent(), Map.class);
        owner.setName(responseMap.get("nickName").toString());
        owner.setPhone(responseMap.get("phone").toString());
        return owner;
    }

    /**
     * 设计师 & 项目经理
     *
     * @param listProjectNo
     * @return
     */
    public List<Map<String, String>> getEmployeeInfo(List<String> listProjectNo, String role) {
        OrderUserExample example = new OrderUserExample();
        example.createCriteria().andProjectNoIn(listProjectNo).andRoleCodeEqualTo(role).andIsTransferEqualTo((short) 0);
        List<OrderUser> list = orderUserMapper.selectByExample(example);
        List<Map<String, String>> listName = new ArrayList<>();
        for (OrderUser orderUser : list) {
            EmployeeMsgExample example2 = new EmployeeMsgExample();
            example2.createCriteria().andUserIdEqualTo(orderUser.getUserId()).andRoleCodeEqualTo(role).andEmployeeStateEqualTo(1);
            List<EmployeeMsg> listEm = employeeMsgMapper.selectByExample(example2);
            for (EmployeeMsg employeeMsg : listEm) {
                Map<String, String> map = new HashMap<>();
                map.put("projectNo", orderUser.getProjectNo());
                map.put("name", employeeMsg.getRealName());
                listName.add(map);
            }
        }
        return listName;
    }

    /**
     * 公司名称
     * TODO 批量
     *
     * @param companyId
     * @return
     */
    public String getCompanyInfo(String companyId) {
        String companyName = constructionOrderMapper.getCompanyName(companyId);
        return companyName;
    }

    /**
     * 查询 施工阶段
     *
     * @param stage
     * @return
     */
    public String getContstructionStage(int stage) {
        ProjectBigSchedulingExample example = new ProjectBigSchedulingExample();
        example.createCriteria().andSortEqualTo(stage);
        List<ProjectBigScheduling> list = projectBigSchedulingMapper.selectByExample(example);
        return list.get(0).getName();
    }

    /**
     * 查询 延期天数
     *
     * @param listProjectNo
     * @return
     */
    public List<ProjectScheduling> getdelayDay(List<String> listProjectNo) {
        ProjectSchedulingExample example = new ProjectSchedulingExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        return projectSchedulingMapper.selectByExample(example);
    }

    /**
     * 查询 延期天数
     *
     * @param listProjectNo
     * @return
     */
    public Map<String, Integer> getApprove(List<String> listProjectNo) {
        return afInstanceService.getProjectCheckResult(listProjectNo);
    }

    /**
     * 查询 延期天数
     *
     * @param listProjectNo
     * @return
     */
    public List<FundsOrder> getFundsOrder(List<String> listProjectNo) {
        FundsOrderExample example = new FundsOrderExample();
        example.createCriteria().andProjectNoIn(listProjectNo);
        List<FundsOrder> list = fundsOrderMapper.selectByExample(example);
        return list;
    }


}
