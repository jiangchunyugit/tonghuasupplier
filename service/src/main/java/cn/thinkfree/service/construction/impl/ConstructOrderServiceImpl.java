package cn.thinkfree.service.construction.impl;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.*;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.appvo.ConstructionProjectVo;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.AfUserDTO;
import cn.thinkfree.database.vo.ConstructCountVO;
import cn.thinkfree.database.vo.construct.*;
import cn.thinkfree.service.approvalflow.AfConfigService;
import cn.thinkfree.service.approvalflow.AfInstanceService;
import cn.thinkfree.service.config.HttpLinks;
import cn.thinkfree.service.construction.CommonService;
import cn.thinkfree.service.construction.ConstructOrderService;
import cn.thinkfree.service.construction.OrderListCommonService;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.newscheduling.NewSchedulingBaseService;
import cn.thinkfree.service.newscheduling.NewSchedulingService;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.project.AddressService;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.utils.AfUtils;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.HttpUtils;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ConstructOrderServiceImpl implements ConstructOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstructOrderServiceImpl.class);

    @Autowired
    private OrderListCommonService orderListCommonService;
    @Autowired
    private ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AfConfigService configService;
    @Autowired
    private BasicsService basicsService;
    @Autowired
    private NewOrderUserService orderUserService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private HttpLinks httpLinks;
    @Autowired
    private NewSchedulingService schedulingService;
    @Autowired
    private NewSchedulingBaseService schedulingBaseService;
    @Autowired
    private AfInstanceService instanceService;

    /**
     * 订单列表
     *
     * @param pageNum
     * @param pageSize
     * @param cityName
     * @param orderType
     * @return
     */
    @Override
    public PageInfo<ConstructionOrderListVo> getOrderList(int pageNum, int pageSize, String cityName, int orderType) {
        PageInfo<ConstructionOrderListVo> pageInfo = orderListCommonService.getConstructionOrderList(pageNum, pageSize, cityName, orderType);
        return pageInfo;
    }

    /**
     * 施工订单列表统计
     * @return
     */

    @Override
    public MyRespBundle<ConstructionOrderManageVo> getOrderNum() {

        ConstructionOrderExample example = new ConstructionOrderExample();
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);

        /* 统计状态个数 */
        int waitExamine = 0, waitSign = 0, waitPay = 0;
        for (ConstructionOrder constructionOrder : list) {
            // 订单状态 统计
            int stage = constructionOrder.getOrderStage();
            if (stage == ConstructionStateEnum.STATE_530.getState()) {
                waitExamine++;
            }
            if (stage == ConstructionStateEnum.STATE_540.getState()) {
                waitSign++;
            }
            if ((stage >= ConstructionStateEnum.STATE_600.getState() && stage <= ConstructionStateEnum.STATE_700.getState())) {
                waitPay++;
            }
        }

        ConstructionOrderManageVo constructionOrderManageVo = new ConstructionOrderManageVo();
        constructionOrderManageVo.setCityList(commonService.getCityList());
        constructionOrderManageVo.setOrderNum(list.size());
        constructionOrderManageVo.setWaitExamine(waitExamine);
        constructionOrderManageVo.setWaitSign(waitSign);
        constructionOrderManageVo.setWaitPay(waitPay);
        return RespData.success(constructionOrderManageVo);
    }

    @Override
    public ConstructCountVO count(String userId, String approvalType, Integer pageNum, Integer pageSize) {
        ConstructCountVO constructCountVO = new ConstructCountVO();

        int total = constructionOrderMapper.countByUserId(userId);
        constructCountVO.setTotal(total);

        List<String> configNos = configService.getConfigNosByApprovalType(AfConstants.APPROVAL_TYPE_SCHEDULE_APPROVAL);
        int count = constructionOrderMapper.countByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState());
        constructCountVO.setCheckCount(count);

        configNos = configService.getConfigNosByApprovalType(AfConstants.APPROVAL_TYPE_CONSTRUCTION_CHANGE);
        count = constructionOrderMapper.countByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState());
        constructCountVO.setChangeCount(count);

        configNos = configService.getConfigNosByApprovalType(AfConstants.APPROVAL_TYPE_PROBLEM_RECTIFICATION);
        count = constructionOrderMapper.countByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState());
        constructCountVO.setProblemCount(count);

        configNos = configService.getConfigNosByApprovalType(AfConstants.APPROVAL_TYPE_DELAY_VERIFY);
        count = constructionOrderMapper.countByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState());
        constructCountVO.setDelayCount(count);

        configNos = configService.getConfigNosByApprovalType(approvalType);
        List<ConstructionProjectVo> constructionProjectVos = constructionOrderMapper.selectByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState(), (pageNum - 1) * pageSize, pageSize);
        PageInfo<ConstructionProjectVo> pageInfo = new PageInfo<>(constructionProjectVos);
        count = constructionOrderMapper.countByApproval(userId, configNos, ConstructionStateEnum.STATE_700.getState());
        pageInfo.setTotal(count);
        pageInfo.setPages((count % pageSize == 0) ? (count / pageSize) : (count / pageSize + 1));
        for (ConstructionProjectVo constructionProjectVo : constructionProjectVos) {
            constructionProjectVo.setStageName(ConstructionStateEnum.queryByState(constructionProjectVo.getStage()).getStateName(1));
        }
        constructCountVO.setPageInfo(pageInfo);

        return constructCountVO;
    }

    @Override
    public ConstructionOrder findByProjectNo(String projectNo) {
        ConstructionOrderExample example = new ConstructionOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(example);
        return constructionOrders != null && constructionOrders.size() > 0 ? constructionOrders.get(0) : null;
    }

    private String getOrderType(String type) {
        BasicsData basicsData = basicsService.queryDataOne(BasicsDataParentEnum.DESIGN_STYLE.getCode(), type);
        if (basicsData == null) {
            LOGGER.error("未查询到装饰风格:type:{}", type);
            throw new RuntimeException();
        }
        return basicsData.getBasicsName();
    }

    @Override
    public ConstructOrderDetailVO detail(String projectNo) {
        ConstructOrderDetailVO constructOrderDetailVO = new ConstructOrderDetailVO();

        ConstructionOrder constructionOrder = findByProjectNo(projectNo);
        Project project = projectService.findByProjectNo(projectNo);
        // 订单信息
        OrderDetailVO orderDetailVO = getOrderDetail(projectNo, constructionOrder.getOrderNo(), project.getStyle());
        constructOrderDetailVO.setOrderDetailVO(orderDetailVO);
        // 业主信息
        ConsumerDetailVO consumerDetailVO = getCustomerDetail(constructionOrder);
        String address = addressService.getAddress(project);
        consumerDetailVO.setProjectAddress(address);
        constructOrderDetailVO.setConsumerDetailVO(consumerDetailVO);

        ProjectScheduling projectScheduling = schedulingService.getProjectScheduling(projectNo).getData();
        // 施工信息
        String schemeNo = constructionOrder.getSchemeNo();
        ConstructDetailVO constructDetailVO = getConstructDetail(projectScheduling, schemeNo);
        constructOrderDetailVO.setConstructDetailVO(constructDetailVO);
        // 服务人员信息
        ServiceStaffsVO serviceStaffsVO = getServiceStaffs(projectNo);
        constructOrderDetailVO.setServiceStaffsVO(serviceStaffsVO);
        // 支付信息
        PayDetailVO payDetailVO = getPayDetail(projectNo);
        constructOrderDetailVO.setPayDetailVO(payDetailVO);
        // 延期信息
        DelayDetailVO delayDetailVO = getDelayDetail(projectScheduling, projectNo);
        constructOrderDetailVO.setDelayDetailVO(delayDetailVO);
        return constructOrderDetailVO;
    }

    private DelayDetailVO getDelayDetail(ProjectScheduling projectScheduling, String projectNo) {
        DelayDetailVO delayDetailVO = new DelayDetailVO();
        int delayDays = instanceService.getDelayDaysByProjectNo(projectNo);
        delayDetailVO.setAffirmDelayDays(delayDays);
        delayDetailVO.setDelayDays(projectScheduling.getDelay() + delayDays);
        return delayDetailVO;
    }

    private PayDetailVO getPayDetail(String projectNo) {
        PayDetailVO payDetailVO = new PayDetailVO();

        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(httpLinks.getProjectPayInfo(), Collections.singletonMap("projectNo", projectNo));
        String content = httpRespMsg.getContent();
        Map map = JSONUtil.json2Bean(content, Map.class);
        Map<String, String> data = (Map<String, String>) map.get("data");

        payDetailVO.setCompactMoney(data.get(ProjectPayInfoKey.CONSTRUCTION_TOTAL));
        payDetailVO.setPayedMoney(data.get(ProjectPayInfoKey.CONSTRUCTION_TOTAL_PAID));

        payDetailVO.setWaitPayMoney(new BigDecimal(data.get(ProjectPayInfoKey.CONSTRUCTION_TOTAL)).subtract(new BigDecimal(data.get(ProjectPayInfoKey.CONSTRUCTION_TOTAL_PAID))).toString());
        return payDetailVO;
    }

    private ServiceStaffsVO getServiceStaffs(String projectNo) {
        ServiceStaffsVO serviceStaffsVO = new ServiceStaffsVO();

        List<OrderUser> orderUsers = orderUserService.findByProjectNo(projectNo);
        if (orderUsers == null || orderUsers.isEmpty()) {
            LOGGER.error("未查询到项目用户关系信息，projectNo:{}", projectNo);
            throw new RuntimeException();
        }

        for (OrderUser orderUser : orderUsers) {
            switch (orderUser.getRoleCode()) {
                case "CP":
                    AfUserDTO userInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), orderUser.getUserId(), orderUser.getRoleCode());
                    if (userInfo == null) {
                        LOGGER.error("未查询到项目经理信息，userId:{}", orderUser.getUserId());
                        throw new RuntimeException();
                    }
                    serviceStaffsVO.setProjectManager(userInfo.getUsername());
                    break;
                case "CM":
                    userInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), orderUser.getUserId(), orderUser.getRoleCode());
                    if (userInfo == null) {
                        LOGGER.error("未查询到工长信息，userId:{}", orderUser.getUserId());
                        throw new RuntimeException();
                    }
                    serviceStaffsVO.setForeman(userInfo.getUsername());
                    break;
                case "CS":
                    userInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), orderUser.getUserId(), orderUser.getRoleCode());
                    if (userInfo == null) {
                        LOGGER.error("未查询到管家信息，userId:{}", orderUser.getUserId());
                        throw new RuntimeException();
                    }
                    serviceStaffsVO.setSteward(userInfo.getUsername());
                    break;
                case "CD":
                    userInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), orderUser.getUserId(), orderUser.getRoleCode());
                    if (userInfo == null) {
                        LOGGER.error("未查询到设计师信息，userId:{}", orderUser.getUserId());
                        throw new RuntimeException();
                    }
                    serviceStaffsVO.setDesigner(userInfo.getUsername());
                    break;
                default:
                    break;
            }
        }

        return serviceStaffsVO;
    }

    private ConstructDetailVO getConstructDetail(ProjectScheduling projectScheduling, String schemeNo) {
        ConstructDetailVO constructDetailVO = new ConstructDetailVO();
        constructDetailVO.setStartDate(projectScheduling.getStartTime());
        constructDetailVO.setCompleteDate(projectScheduling.getEndTime());
        constructDetailVO.setDelayDays(projectScheduling.getDelay());
        constructDetailVO.setLimitDays(DateUtil.differentDaysByMillisecond(projectScheduling.getStartTime(), projectScheduling.getEndTime()));

        ProjectBigScheduling projectBigScheduling = schedulingBaseService.findBySchemeNoAndSort(schemeNo, projectScheduling.getRate());
        if (projectBigScheduling == null) {
            LOGGER.error("未查询到排期信息，schemeNo:{}, sort:{}", schemeNo, projectScheduling.getRate());
            throw new RuntimeException();
        }
        constructDetailVO.setConstructProgress(projectBigScheduling.getName());
        return constructDetailVO;
    }

    private ConsumerDetailVO getCustomerDetail(ConstructionOrder constructionOrder) {
        ConsumerDetailVO consumerDetailVO = new ConsumerDetailVO();

        OrderUser orderUser = orderUserService.findByProjectNoAndRoleId(constructionOrder.getProjectNo(), Role.CC.id);
        if (orderUser == null) {
            LOGGER.error("项目未配置业主，projectNo:{}", constructionOrder.getProjectNo());
            throw new RuntimeException();
        }
        AfUserDTO userInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), orderUser.getUserId(), Role.CC.id);
        if (userInfo == null) {
            LOGGER.error("未查询到业主信息，userId:{}", orderUser.getUserId());
            throw new RuntimeException();
        }
        consumerDetailVO.setName(userInfo.getUsername());
        consumerDetailVO.setPhone(userInfo.getPhone());

        return consumerDetailVO;
    }

    private OrderDetailVO getOrderDetail(String projectNo, String orderNo, String orderType) {
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setProjectNo(projectNo);
        orderDetailVO.setOrderNo(orderNo);
        orderDetailVO.setOrderType(orderType);
        return orderDetailVO;
    }
}