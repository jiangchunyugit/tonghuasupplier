package cn.thinkfree.service.neworder;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.Role;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.config.HttpLinks;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.construction.OrderListCommonService;
import cn.thinkfree.service.construction.vo.ConstructionOrderCommonVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.construction.vo.SiteDetailsVo;
import cn.thinkfree.service.platform.vo.PageVo;
import cn.thinkfree.service.utils.AfUtils;
import cn.thinkfree.service.utils.HttpUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 项目用户关系服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/18 11:37
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class NewOrderUserServiceImpl implements NewOrderUserService {

    private static final MyLogger LOGGER = new MyLogger(NewOrderUserService.class);

    @Autowired
    private OrderUserMapper orderUserMapper;
    @Autowired(required = false)
    private PreProjectGuideMapper preProjectGuideMapper;
    @Autowired(required = false)
    private DesignerOrderMapper designerOrderMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    private ProjectBigSchedulingDetailsMapper projectBigSchedulingDetailsMapper;
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    private HttpLinks httpLinks;
    @Autowired
    private ProjectSchedulingMapper projectSchedulingMapper;
    @Autowired
    private  OrderContractMapper orderContractMapper;
    @Autowired
    private FundsOrderMapper fundsOrderMapper;
    @Autowired
    private OrderListCommonService orderListCommonService;

    @Override
    public List<OrderUser> findByProjectNo(String projectNo) {
        OrderUserExample example = new OrderUserExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        return orderUserMapper.selectByExample(example);
    }

    @Override
    public String findUserIdByProjectNoAndRoleId(String projectNo, String roleId) {
        OrderUser orderUser = findByProjectNoAndRoleId(projectNo, roleId);
        return orderUser != null ? orderUser.getUserId() : null;
    }

    @Override
    public String findRoleIdByProjectNoAndUserId(String projectNo, String userId) {
        OrderUser orderUser = findByProjectNoAndUserId(projectNo, userId);
        return orderUser != null ? orderUser.getRoleCode() : null;
    }

    @Override
    public OrderUser findByProjectNoAndUserId(String projectNo, String userId) {
        OrderUserExample example = new OrderUserExample();
        example.createCriteria().andProjectNoEqualTo(projectNo).andUserIdEqualTo(userId);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(example);
        return orderUsers != null && orderUsers.size() > 0 ? orderUsers.get(0) : null;
    }

    /**
     * @return
     * @Author jiang
     * @Description 分页查询项目派单
     * @Date
     * @Param
     **/
    @Override
    public List<ProjectOrderVO> queryProjectOrderByPage(ProjectOrderVO projectOrderVO, Integer pageNum, Integer pageSize) {
        projectOrderVO.setStatus(1);
        //获取业主  项目经理 设计师
        EmployeeInfoVO employeeInfoVO = new EmployeeInfoVO();
        OrderUserExample orderUserExample = new OrderUserExample();
        orderUserExample.createCriteria().andProjectNoEqualTo(projectOrderVO.getProjectNo());
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(orderUserExample);
        List<EmployeeInfoVO> list = new ArrayList<>();
        orderUsers.forEach((user) ->
                {
                    EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
                    employeeMsgExample.createCriteria().andUserIdEqualTo(user.getUserId());
                    List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(employeeMsgExample);
                    if(employeeMsgs != null){
                        employeeMsgs.forEach(employeeMsg -> {
                            if (employeeMsg.getRoleCode().equals("CP")) {
                                employeeInfoVO.setProjectManager(employeeMsg.getRealName());
                            } else if (employeeMsg.getRoleCode().equals("CM")) {
                                employeeInfoVO.setForeman(employeeMsg.getRealName());
                            } else if (employeeMsg.getRoleCode().equals("CS")) {
                                employeeInfoVO.setHousekeeper(employeeMsg.getRealName());
                            } else if (employeeMsg.getRoleCode().equals("CQ")) {
                                employeeInfoVO.setQualityInspection(employeeMsg.getRealName());
                            } else if (employeeMsg.getRoleCode().equals("CD")) {
                                employeeInfoVO.setDesigner(employeeMsg.getRealName());
                            }
                        });
                    }else {
                        throw  new RuntimeException();
                    }

                }

        );
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andProjectNoEqualTo(projectOrderVO.getProjectNo());
        List<Project> projects = projectMapper.selectByExample(projectExample);
      /*  Map result = getUserName(projects.get(0).getOwnerId(), "CC");
        //昵称先用着
        String nickName = (String) result.get("nickName");
        String phone = (String) result.get("phone");
        */
        List<ProjectOrderVO> projectOrderList = designerOrderMapper.selectProjectOrderByPage(projectOrderVO, pageNum, pageSize);
        projectOrderList.forEach((projectOrder) -> {
            projectOrder.setProjectManager(employeeInfoVO.getProjectManager());
            projectOrder.setDesignerName(employeeInfoVO.getDesigner());
           /* projectOrder.setOwner(nickName);
           projectOrder.setPhone(phone);*/
        });

        return projectOrderList;
    }

    /**
     * @return
     * @Author jiang
     * @Description 查询项目派单总条数
     * @Date
     * @Param
     **/
    @Override
    public Integer queryProjectOrderCount(ProjectOrderVO projectOrderVO) {
        projectOrderVO.setStatus(1);
        return designerOrderMapper.selectProjectOrderCount(projectOrderVO);
    }

    /**
     * @return
     * @Author jiang
     * @Description 订单确认接口
     * @Date
     * @Param orderConfirmationVO
     **/
    @Override
    public Integer updateorderConfirmation(OrderConfirmationVO orderConfirmationVO) {
        DesignerOrder DesignerOrder = new DesignerOrder();
        DesignerOrder.setCompanyId(orderConfirmationVO.getCompanyId());
        DesignerOrder.setOrderStage(orderConfirmationVO.getOrderStage().intValue());

        DesignerOrderExample example = new DesignerOrderExample();
        example.createCriteria().andProjectNoEqualTo(orderConfirmationVO.getProjectNo());
        return designerOrderMapper.updateByExampleSelective(DesignerOrder, example);
    }

    /**
     * @return
     * @Author jiang
     * @Description 查看订单详情
     * @Date
     * @Param
     **/
    @Override
    public OrderDetailsVO selectOrderDetails(String projectNo) {
  /*      ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(projectExample);
        Map result = getUserName(projects.get(0).getOwnerId(), "CC");
        String phone = (String) result.get("phone");
        //昵称先用着
        String nickName = (String) result.get("nickName");*/
        OrderDetailsVO orderDetailsVO = projectMapper.selectOrderDetails(projectNo, 1).get(0);
    /*  orderDetailsVO.setPhone(phone);
        orderDetailsVO.setConsumerName(nickName);
        orderDetailsVO.setUserName(nickName);*/
        orderDetailsVO.setProjectNo(projectNo);
        return orderDetailsVO;
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    public Map getUserName(String userId, String roleId) {
        Map<String, String> requestMap = new HashMap<>(2);
        requestMap.put("userId", userId);
        requestMap.put("roleId", roleId);
        HttpUtils.HttpRespMsg httpRespMsg = HttpUtils.post(httpLinks.getUserCenterGetUserMsg(), requestMap);
        Map responseMap = JSONUtil.json2Bean(httpRespMsg.getContent(), Map.class);
        return (Map) responseMap.get("data");
    }

    /**
     * @return
     * @Author jiang
     * @Description 阶段展示
     * @Date
     * @Param
     **/
    @Override
    public List<StageDetailsVO> selectStageDetailsList(String projectNo) {
        StageDetailsVO stageDetailsVO = new StageDetailsVO();
        stageDetailsVO.setType(3);//查询的是项目阶段 (1,设计订单 2,施工订单 3,项目)

        return constructionOrderMapper.selectStageDetailsList(projectNo, stageDetailsVO.getType());
    }

    @Override
    public OrderUser findByProjectNoAndRoleId(String projectNo, String roleId) {
        OrderUserExample example = new OrderUserExample();
        example.createCriteria().andProjectNoEqualTo(projectNo).andRoleCodeEqualTo(roleId);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(example);
        return orderUsers != null && orderUsers.size() > 0 ? orderUsers.get(0) : null;
    }

    /**
     * @return
     * @Author jiang
     * @Description 修改订单状态
     * @Date
     * @Param
     **/
    @Override
    public Integer modifyOrder(OrderConfirmationVO orderConfirmationVO) {
        DesignerOrder DesignerOrder = new DesignerOrder();
        DesignerOrder.setOrderStage(orderConfirmationVO.getOrderStage().intValue());
        DesignerOrderExample example = new DesignerOrderExample();
        example.createCriteria().andProjectNoEqualTo(orderConfirmationVO.getProjectNo());
        return designerOrderMapper.updateByExampleSelective(DesignerOrder, example);
    }

    /**
     * @return
     * @Author jiang
     * @Description 分页查询施工工地
     * @Date
     * @Param
     **/
    @Override
    public List<ConstructionSiteVO> querySiteDetailsByPage(ConstructionSiteVO constructionSiteVO, Integer pageNum, Integer pageSize) {
        constructionSiteVO.setStatus(1);
        //获取业主  项目经理 设计师
        EmployeeInfoVO employeeInfoVO = new EmployeeInfoVO();
        OrderUserExample orderUserExample = new OrderUserExample();
        orderUserExample.createCriteria().andProjectNoEqualTo(constructionSiteVO.getProjectNo());
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(orderUserExample);
        List<EmployeeInfoVO> list = new ArrayList<>();
        orderUsers.forEach((user) ->
                {
                    EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
                    employeeMsgExample.createCriteria().andUserIdEqualTo(user.getUserId());
                    List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(employeeMsgExample);
                    employeeMsgs.forEach(employeeMsg -> {
                        if (employeeMsg.getRoleCode().equals("CP")) {
                            employeeInfoVO.setProjectManager(employeeMsg.getRealName());
                        } else if (employeeMsg.getRoleCode().equals("CM")) {
                            employeeInfoVO.setForeman(employeeMsg.getRealName());
                        } else if (employeeMsg.getRoleCode().equals("CS")) {
                            employeeInfoVO.setHousekeeper(employeeMsg.getRealName());
                        } else if (employeeMsg.getRoleCode().equals("CQ")) {
                            employeeInfoVO.setQualityInspection(employeeMsg.getRealName());
                        } else if (employeeMsg.getRoleCode().equals("CD")) {
                            employeeInfoVO.setDesigner(employeeMsg.getRealName());
                        }
                    });
                }

        );
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andProjectNoEqualTo(constructionSiteVO.getProjectNo());
        List<Project> projects = projectMapper.selectByExample(projectExample);
        AfUserDTO customerInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), projects.get(0).getOwnerId(), Role.CC.id);
        List<ConstructionSiteVO> constructionSiteList = projectMapper.selectSiteDetailsByPage(constructionSiteVO, pageNum, pageSize);
        constructionSiteList.forEach((projectOrder) -> {
            projectOrder.setProjectManager(employeeInfoVO.getProjectManager());
            projectOrder.setDesignerName(employeeInfoVO.getDesigner());
           /* projectOrder.setOwner(customerInfo.getUsername());
           projectOrder.setPhone(customerInfo.getPhone());*/
        });

        return constructionSiteList;
    }

    /**
     * @return
     * @Author jiang
     * @Description 查询施工工地总条数
     * @Date
     * @Param
     **/
    @Override
    public Integer querySiteDetailsCount(ConstructionSiteVO constructionSiteVO) {
        constructionSiteVO.setStatus(1);
        return projectMapper.selectSiteDetailsCount(constructionSiteVO);
    }

    /**
     * @return
     * @Author jiang
     * @Description 分页查询工地详情
     * @Date
     * @Param
     **/
    @Override
    public List<SiteDetailsVO> querySiteByPage(SiteDetailsVO siteDetailsVO, Integer pageNum, Integer pageSize) {
        siteDetailsVO.setStage(1);
        return projectMapper.selectSiteByPage(siteDetailsVO, pageNum, pageSize);
    }

    /**
     * @return
     * @Author jiang
     * @Description 查询工地详情总条数
     * @Date
     * @Param
     **/
    @Override
    public Integer querySiteCount(SiteDetailsVO siteDetailsVO) {
        siteDetailsVO.setStage(1);
        return projectMapper.selectSiteCount(siteDetailsVO);
    }

    /**
     * @return
     * @Author jiang
     * @Description 分页查询施工计划
     * @Date
     * @Param
     **/
    @Override
    public List<ConstructionPlanVO> queryConstructionPlanByPage(ConstructionPlanVO constructionPlanVO, Integer pageNum, Integer pageSize) {
        constructionPlanVO.setStatus(1);
        return projectBigSchedulingDetailsMapper.selectConstructionPlanByPage(constructionPlanVO, pageNum, pageSize);
    }

    /**
     * @return
     * @Author jiang
     * @Description 查询施工计划总条数
     * @Date
     * @Param
     **/
    @Override
    public Integer queryConstructionPlanCount(ConstructionPlanVO constructionPlanVO) {
        constructionPlanVO.setStatus(1);
        return projectBigSchedulingDetailsMapper.selectConstructionPlanCount(constructionPlanVO);
    }

    /**
     * @return
     * @Author jiang
     * @Description 查询员工详情
     * @Date
     * @Param
     **/
    @Override
    public EmployeeInfoVO selectemployeeInfoList(String projectNo) {
        EmployeeInfoVO employeeInfoVO = new EmployeeInfoVO();
        OrderUserExample orderUserExample = new OrderUserExample();
        orderUserExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(orderUserExample);
        List<EmployeeInfoVO> list = new ArrayList<>();
        orderUsers.forEach((user) ->
                {
                    EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
                    employeeMsgExample.createCriteria().andUserIdEqualTo(user.getUserId());
                    List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(employeeMsgExample);
                    employeeMsgs.forEach(employeeMsg -> {
                        if (employeeMsg.getRoleCode().equals("CP")) {
                            employeeInfoVO.setProjectManager(employeeMsg.getRealName());
                        } else if (employeeMsg.getRoleCode().equals("CM")) {
                            employeeInfoVO.setForeman(employeeMsg.getRealName());
                        } else if (employeeMsg.getRoleCode().equals("CS")) {
                            employeeInfoVO.setHousekeeper(employeeMsg.getRealName());
                        } else if (employeeMsg.getRoleCode().equals("CQ")) {
                            employeeInfoVO.setQualityInspection(employeeMsg.getRealName());
                        }
                    });
                }

        );

        return employeeInfoVO;
    }

    /**
     * @return
     * @Author jiang
     * @Description 分页查询验收结果
     * @Date
     * @Param
     **/
    @Override
    public List<AcceptanceResultsVO> queryAcceptanceResultsByPage(String projectNo, Integer pageNum, Integer pageSize) {

        return projectBigSchedulingDetailsMapper.selectAcceptanceResultsByPage(projectNo, pageNum, pageSize);
    }

    /**
     * @return
     * @Author jiang
     * @Description 查询验收结果总条数
     * @Date
     * @Param
     **/
    @Override
    public Integer queryAcceptanceResultsCount(String projectNo) {
        return projectBigSchedulingDetailsMapper.selectAcceptanceResultsCount(projectNo);
    }

    /**
     * @return
     * @Author jiang
     * @Description 更改延期天数
     * @Date
     * @Param
     **/
    @Override
    public void modifyDelayDay(String projectNo, Integer newDelay) {
        ProjectSchedulingExample projectSchedulingExample = new ProjectSchedulingExample();
        projectSchedulingExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<ProjectScheduling> projectSchedulings = projectSchedulingMapper.selectByExample(projectSchedulingExample);
        if (projectSchedulings.size() == 1) {
            ProjectScheduling projectScheduling = projectSchedulings.get(0);
            Integer delay = projectScheduling.getDelay();
            ProjectScheduling projectScheduling1 = new ProjectScheduling();
            projectScheduling1.setDelay(newDelay + delay);
            projectSchedulingMapper.updateByExampleSelective(projectScheduling1, projectSchedulingExample);
        }
        if (projectSchedulings.size() <= 0) {
            LOGGER.error("未查询到项目编号为{}的项目！", projectNo);
            throw new RuntimeException();
        }
        if (projectSchedulings.size() > 1) {
            LOGGER.error("查询到项目编号为{}的项目不止一个！", projectNo);
            throw new RuntimeException();
        }


    }

    /**
     * 根据项目编号,员工编号,员工
     * 角色查看是否符合实际权限
     *
     * @param projectNo
     * @param userId
     * @param roleCode
     * @return
     */
    @Override
    public Boolean checkJurisdiction(String projectNo, String userId, String roleCode) {
        OrderUserExample userExample = new OrderUserExample();
        OrderUserExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andProjectNoEqualTo(projectNo);
        userCriteria.andUserIdEqualTo(userId);
        userCriteria.andRoleCodeEqualTo(UserJobs.Foreman.roleCode);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(userExample);
        if (orderUsers.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * @Author jiang
     * @Description 设计合同列表
     * @Date
     * @Param
     * @return
     **/

    @Override
    public PageVo<List<DesignContractVO>> getDesignContractListss(Integer pageNum, Integer pageSize, String companyId) {
        List<DesignContractVO> designContractVOs = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            DesignContractVO vo = new DesignContractVO();
            vo.setContractStatus(1);
            vo.setContractAmount(11);
            vo.setSigningTime(new Date());
            vo.setContractNo("测试");
            vo.setOrderSource(1);
            vo.setOrderAddress("北京");
            vo.setOrderNo("11111");
            vo.setOwnerName("张三");
            vo.setOwnerPhone("18223364014");
            vo.setProjectNo("11111");
            vo.setSort(1);
            vo.setSunOrderNo("11212");
            designContractVOs.add(vo);

        }
        PageVo<List<DesignContractVO>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(10);
        pageVo.setPageIndex(pageNum);
        pageVo.setData(designContractVOs);
        return pageVo;
    }

    /**
     * @Author jiang
     * @Description 设计合同列表
     * @Date
     * @Param
     * @return
     **/
    @Override
    public PageVo<List<DesignContractVO>> queryContractByPage(DesignContractVO designContractVO, Integer pageNum, Integer pageSize) {
        List<DesignContractVO> voList = designerOrderMapper.selectContractByPage( designContractVO,  pageNum,  pageSize);
        //装业主模糊的list
        List<DesignContractVO> newList = new ArrayList();
        if(voList.size()>0){
            for (DesignContractVO vo :voList){
                //业主
                ProjectExample projectExample = new ProjectExample();
                projectExample.createCriteria().andProjectNoEqualTo(vo.getProjectNo());
                List<Project> projects = projectMapper.selectByExample(projectExample);
                if(projects.size()>0){
                    for(Project pr:projects){
                        //遍历查询业主
                        AfUserDTO customerInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), pr.getOwnerId(), Role.CC.id);
                        //业主
                        vo.setOwnerName(customerInfo.getUsername());
                        //手机号码
                        vo.setOwnerPhone(customerInfo.getPhone());
                    }
                }
                if(vo.getAuditType() != null){
                    if(vo.getAuditType() ==1 && vo.getSigningTime().after(new Date())){
                        vo.setContractStatus(1);//生效
                    }else {
                        vo.setContractStatus(0);//不生效

                    }
                }
            }
        }
        //模糊业主
        if(designContractVO.getOwnerName() != null){
            for(int i=0;i<voList.size();i++ ){
                if(voList.get(i).getOwnerName().contains(designContractVO.getOwnerName())){
                    newList.add(voList.get(i));
                }
            }
            PageVo<List<DesignContractVO>> pageVo = new PageVo<>();
            pageVo.setPageSize(pageSize);
            pageVo.setTotal(designerOrderMapper.selectContractCount(designContractVO));
            pageVo.setPageIndex(pageNum);
            pageVo.setData(newList);
            return pageVo;
        }
        PageVo<List<DesignContractVO>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(designerOrderMapper.selectContractCount(designContractVO));
        pageVo.setPageIndex(pageNum);
        pageVo.setData(voList);
        return pageVo;
    }

    @Override
    public Integer queryContractCount(DesignContractVO designContractVO) {
        return designerOrderMapper.selectContractCount(designContractVO);
    }
    /**
     * @Author jiang
     * @Description 施工合同列表
     * @Date
     * @Param
     * @return
     **/
    @Override
    public PageVo<List<ConstructionContractVO>> queryConstructionContractByPage(ConstructionContractVO constructionContractVO, int pageNum, int pageSize) {
        List<ConstructionContractVO> voList = constructionOrderMapper.selectConstructionContractByPage( constructionContractVO,  pageNum,  pageSize);
        List<ConstructionContractVO> newList = new ArrayList();
        if(voList.size()>0){
           for (ConstructionContractVO vo :voList){
               //业主
               ProjectExample projectExample = new ProjectExample();
               projectExample.createCriteria().andProjectNoEqualTo(vo.getProjectNo());
               List<Project> projects = projectMapper.selectByExample(projectExample);
               if(projects.size()>0){
                   for(Project pr:projects){
                       AfUserDTO customerInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), pr.getOwnerId(), Role.CC.id);
                       //业主
                       vo.setOwnerName(customerInfo.getUsername());
                       //手机号码
                       vo.setOwnerPhone(customerInfo.getPhone());
                   }
               }
               if(vo.getAuditType() != null){
                   if(vo.getAuditType() ==1 && vo.getSigningTime().after(new Date())){
                       vo.setContractStatus(1);//生效
                   }else {
                       vo.setContractStatus(0);//不生效

                   }
               }
           }
       }
        //模糊业主
        if(constructionContractVO.getOwnerName() != null){
            for(int i=0;i<voList.size();i++ ){
                if(voList.get(i).getOwnerName().contains(constructionContractVO.getOwnerName())){
                    newList.add(voList.get(i));

                }
            }
            PageVo<List<ConstructionContractVO>> pageVo = new PageVo<>();
            pageVo.setPageSize(pageSize);
            pageVo.setTotal(constructionOrderMapper.selectconstructionContractVOCount(constructionContractVO));
            pageVo.setPageIndex(pageNum);
            pageVo.setData(newList);
            return pageVo;
        }
        PageVo<List<ConstructionContractVO>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setTotal(constructionOrderMapper.selectconstructionContractVOCount(constructionContractVO));
        pageVo.setPageIndex(pageNum);
        pageVo.setData(voList);
        return pageVo;
    }

    /**
     * @Author jiang
     * @Description 工地详情信息
     * @Date
     * @Param
     * @return
     **/
    @Override
    public MyRespBundle<SiteDetailsVo> getSiteDetails(String projectNo) {
        SiteDetailsVo siteDetailsVo = new SiteDetailsVo();
        ProjectSchedulingExample projectSchedulingExample = new ProjectSchedulingExample();
        projectSchedulingExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<ProjectScheduling> projectSchedulings = projectSchedulingMapper.selectByExample(projectSchedulingExample);
        if(projectSchedulings.size() == 1){
            ProjectScheduling projectScheduling = projectSchedulings.get(0);
            //项目编号
            siteDetailsVo.setProjectNo(projectNo);
            //开工时间
            siteDetailsVo.setStartDates(projectScheduling.getStartTime());
            //竣工时间
            siteDetailsVo.setCompletionDays(projectScheduling.getEndTime());
            //工期
            Long day=(projectScheduling.getEndTime().getTime()-projectScheduling.getStartTime().getTime())/(24*60*60*1000);
            siteDetailsVo.setDuration(day.intValue());
            //施工进度
            siteDetailsVo.setConstructionSchedule(projectScheduling.getRate().intValue());
            //延期天数
            siteDetailsVo.setDeferredDays(projectScheduling.getDelay());
        }
        DesignerOrderExample designerOrderExample = new DesignerOrderExample();
        designerOrderExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<DesignerOrder> designerOrders = designerOrderMapper.selectByExample(designerOrderExample);
        if(designerOrders.size()==1){
            DesignerOrder designerOrder = designerOrders.get(0);
            //订单编号
            siteDetailsVo.setOrderNo(designerOrder.getOrderNo());
            //订单类型
            siteDetailsVo.setOrderType(designerOrder.getStyleType());
        }


        //业主
        ProjectExample projectExample = new ProjectExample();
        projectExample.createCriteria().andProjectNoEqualTo(siteDetailsVo.getProjectNo());
        List<Project> projects = projectMapper.selectByExample(projectExample);
       AfUserDTO customerInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), projects.get(0).getOwnerId(), Role.CC.id);
        //业主
        siteDetailsVo.setOwner(customerInfo.getUsername());
        //手机号码
        siteDetailsVo.setPhone(customerInfo.getPhone());
        //项目地址
        siteDetailsVo.setProjectAddress(projects.get(0).getAddressDetail());

        //合同
        ConstructionOrderExample constructionOrderExample = new ConstructionOrderExample();
        constructionOrderExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(constructionOrderExample);
        if(constructionOrders.size()==1){
            ConstructionOrder constructionOrder = constructionOrders.get(0);
            //合同款
            siteDetailsVo.setContractFunds(constructionOrder.getMoney());
        }
        FundsOrderExample example = new FundsOrderExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        List<FundsOrder> list = fundsOrderMapper.selectByExample(example);
        if(list.size() ==1){
            FundsOrder fundsOrder = list.get(0);
            //已付款
            siteDetailsVo.setPaid(fundsOrder.getPaidAmount());
            //待付款
            siteDetailsVo.setPendingPayment(fundsOrder.getActualAmount());
        }

        //EmployeeInfoVO employeeInfoVO = new EmployeeInfoVO();
        OrderUserExample orderUserExample = new OrderUserExample();
        orderUserExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(orderUserExample);
        List<EmployeeInfoVO> lists = new ArrayList<>();
        orderUsers.forEach((user) ->
                {
                    EmployeeMsgExample employeeMsgExample = new EmployeeMsgExample();
                    employeeMsgExample.createCriteria().andUserIdEqualTo(user.getUserId());
                    List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(employeeMsgExample);
                    employeeMsgs.forEach(employeeMsg -> {
                        String roleCode = employeeMsg.getRoleCode();
                        if (roleCode != null) {
                            if ("CP".equals(roleCode)) {
                                //项目经理
                                siteDetailsVo.setProjectManager(employeeMsg.getRealName());
                            } else if ("CM".equals(roleCode)) {
                                //工长
                                siteDetailsVo.setForeman(employeeMsg.getRealName());
                            } else if ("CS".equals(roleCode)) {
                                //管家
                                siteDetailsVo.setHousekeeper(employeeMsg.getRealName());
                            }else if ("CD".equals(roleCode)) {
                                //设计师
                                siteDetailsVo.setDesignerName(employeeMsg.getRealName());
                            }
                        }
                    });
                }

        );


        return RespData.success(siteDetailsVo);
    }
    /**
     * @Author jiang
     * @Description 工地管理列表
     * @Date
     * @Param
     * @return
     **/
    @Override
    public MyRespBundle<ConstructionOrderCommonVo> getConstructionSiteList(int pageNum, int pageSize, String cityName) {
        PageInfo<ConstructionOrderListVo> pageInfo = orderListCommonService.getConstructionOrderList(pageNum, pageSize, cityName);
        ConstructionOrderCommonVo constructionOrderCommonVo = new ConstructionOrderCommonVo();
        constructionOrderCommonVo.setCountPageNum(pageInfo.getSize());
        constructionOrderCommonVo.setOrderList(pageInfo.getList());
        return RespData.success(constructionOrderCommonVo);
    }
    /**
     * @Author jiang
     * @Description 施工阶段数量
     * @Date
     * @Param
     * @return
     **/
    @Override
    public MyRespBundle<ConstructionStageNunVO> getScheduleNum() {
        ConstructionOrderExample example = new ConstructionOrderExample();
        List<ConstructionOrder> list = constructionOrderMapper.selectByExample(example);
        /* 统计状态个数 */
        int waitStart = 0;//待开工
        int underConstruction = 0;//施工中
        int completed = 0;//已完工
        // 订单状态 统计
        for (ConstructionOrder constructionOrder : list) {
            int stage = constructionOrder.getOrderStage();
            if (stage == ConstructionStateEnum.STATE_600.getState()) {
                waitStart++;
            }
            if (stage > ConstructionStateEnum.STATE_600.getState() && stage < ConstructionStateEnum.STATE_700.getState()) {
                underConstruction++;
            }
            if (stage == ConstructionStateEnum.STATE_700.getState()) {
                completed++;
            }
        }

        ConstructionStageNunVO constructionStageNunVO = new ConstructionStageNunVO();
        constructionStageNunVO.setWaitStart(waitStart);
        constructionStageNunVO.setUnderConstruction(underConstruction);
        constructionStageNunVO.setCompleted(completed);
        return RespData.success(constructionStageNunVO);
    }


    @Override
    public MyRespBundle<List<DesignContractVO>> getDesignContractList(Integer pageNum, Integer pageSize, String companyId) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<DesignContractVO> pageInfo = new PageInfo<>();
        DesignerOrderExample designerOrderExample = new DesignerOrderExample();
        List<DesignContractVO> voList = new ArrayList<>();
        designerOrderExample.createCriteria().andCompanyIdEqualTo(companyId).andStatusEqualTo(1);
        List<DesignerOrder> designerOrders = designerOrderMapper.selectByExample(designerOrderExample);
        if(designerOrders.size()>0){
            for (DesignerOrder or:designerOrders){
                DesignContractVO designContractVO = new DesignContractVO();
                //订单编号
                designContractVO.setOrderNo(or.getOrderNo());
                //项目编号
                designContractVO.setProjectNo(or.getProjectNo());
                ProjectExample projectExample = new ProjectExample();
                projectExample.createCriteria().andProjectNoEqualTo(or.getProjectNo());
                List<Project> projects = projectMapper.selectByExample(projectExample);
               if(projects.size()>0){
                   for (Project pr:projects){

                       designContractVO.setOrderAddress(pr.getProvince());
                       designContractVO.setOrderSource(pr.getOrderSource());
                       AfUserDTO customerInfo = AfUtils.getUserInfo(httpLinks.getUserCenterGetUserMsg(), projects.get(0).getOwnerId(), Role.CC.id);
                       //业主
                       designContractVO.setOwnerName(customerInfo.getUsername());
                       //手机号码
                       designContractVO.setOwnerPhone(customerInfo.getPhone());
                   }
               }
                OrderContractExample orderContractExample = new OrderContractExample();
                orderContractExample.createCriteria().andOrderNumberEqualTo(designContractVO.getOrderNo());
                List<OrderContract> orderContracts = orderContractMapper.selectByExample(orderContractExample);
                if(orderContracts.size()>0){
                    for(OrderContract ord:orderContracts){
                        designContractVO.setContractNo(ord.getContractNumber());
                        designContractVO.setSigningTime(ord.getSignTime());
                       /* if(ord.getSignTime().after(new Date())&&ord.getAuditType()){

                        }*/
                        designContractVO.setContractAmount(11);
                        designContractVO.setContractStatus(1);
                    }
                }
                voList.add(designContractVO);
            }
        }

        return RespData.success(voList);
    }
}
