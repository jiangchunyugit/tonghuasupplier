package cn.thinkfree.service.newproject;

import cn.thinkfree.core.base.ErrorCode;
import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.*;
import cn.thinkfree.core.model.OrderStatusDTO;
import cn.thinkfree.database.appvo.*;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ProjectBigSchedulingDetailsVO;
import cn.thinkfree.service.approvalflow.AfInstanceService;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.construction.ConstructOrderService;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.neworder.NewOrderService;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.platform.basics.BasicsService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.employee.ProjectUserService;
import cn.thinkfree.service.platform.vo.PageVo;
import cn.thinkfree.service.platform.vo.UserMsgVo;
import cn.thinkfree.service.remote.CloudService;
import cn.thinkfree.service.utils.BaseToVoUtils;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.MathUtil;
import cn.thinkfree.service.utils.ReflectUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 项目相关
 *
 * @author gejiaming
 */
@Slf4j
@Service
public class NewProjectServiceImpl implements NewProjectService {
    @Autowired
    private OrderUserMapper orderUserMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private NewOrderService newOrderService;
    @Autowired
    private ProjectDataMapper projectDataMapper;
    @Autowired
    private DesignerOrderMapper designerOrderMapper;
    @Autowired
    private ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    private EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    OrderApplyRefundMapper orderApplyRefundMapper;
    @Autowired
    private NewOrderUserService newOrderUserService;
    @Autowired
    private ProjectStageLogMapper projectStageLogMapper;
    @Autowired
    private DesignDispatchService designDispatchService;
    @Autowired
    private CloudService cloudService;
    @Autowired
    private ConstructionStateService constructionStateService;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private UserCenterService userCenterService;
    @Autowired
    private BasicsDataMapper basicsDataMapper;
    @Autowired
    private AfInstanceService afInstanceService;
    @Autowired
    private ConstructOrderService constructOrderService;
    @Autowired
    ProvinceMapper provinceMapper;
    @Autowired
    CityMapper cityMapper;
    @Autowired
    AreaMapper areaMapper;
    @Autowired
    ProjectQuotationMapper projectQuotationMapper;
    @Autowired
    ProjectMessageMapper projectMessageMapper;
    @Autowired
    ProjectBigSchedulingDetailsMapper projectBigSchedulingDetailsMapper;
    @Autowired
    ProjectSchedulingMapper projectSchedulingMapper;
    @Autowired
    private BuildSchemeCompanyRelMapper companyRelMapper;
    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;
    @Autowired
    OrderContractMapper orderContractMapper;
    @Autowired
    ContractTermsMapper contractTermsMapper;
    @Autowired
    private BasicsService basicsService;


    /**
     * 项目列表
     *
     * @param appProjectSEO
     * @return
     */
    @Override
    public MyRespBundle<PageInfo<ProjectVo>> getAllProject(AppProjectSEO appProjectSEO) {
        if (appProjectSEO.getUserId() == null || appProjectSEO.getUserId().trim().isEmpty() || appProjectSEO.getWhichEnd() == null) {
            return RespData.error("请检查userId=" + appProjectSEO.getUserId() + ";whichEnd=" + appProjectSEO.getWhichEnd());
        }
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        EmployeeMsgExample.Criteria msgCriteria = msgExample.createCriteria();
        msgCriteria.andUserIdEqualTo(appProjectSEO.getUserId());
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        if (appProjectSEO.getWhichEnd() != 1 && (employeeMsgs.size() == 0 || employeeMsgs.get(0).getEmployeeState() == 2)) {
            return RespData.success(new PageInfo<>());
        }
        OrderUserExample example1 = new OrderUserExample();
        OrderUserExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andUserIdEqualTo(appProjectSEO.getUserId());
        List<OrderUser> allOrder = orderUserMapper.selectByExample(example1);
        PageHelper.startPage(appProjectSEO.getPage(), appProjectSEO.getRows());
        //查询此人名下所有项目
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(example1);
        if (orderUsers.size() == 0) {
            return RespData.success(new PageInfo<>(), "此用户尚未分配项目");
        }
        String userRoleCode = orderUsers.get(0).getRoleCode();
        List<String> list = new ArrayList<>();
        for (OrderUser orderUser : orderUsers) {
            list.add(orderUser.getProjectNo());
        }
        //根据项目编号查询项目信息
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoIn(list);
        List<Project> projects = projectMapper.selectByExample(example);
        if (projects.size() == 0) {
            return RespData.error("暂无此项目");
        }
        List<ProjectVo> projectVoList = new ArrayList<>();
        for (Project project : projects) {
            ProjectVo projectVo = BaseToVoUtils.getVo(project, ProjectVo.class);
            //添加业主信息
            PersionVo owner = new PersionVo();
            try {
                Map userName = newOrderUserService.getUserName(project.getOwnerId(), ProjectDataStatus.OWNER.getDescription());
                owner.setPhone(userName.get("phone").toString());
                owner.setName(userName.get("nickName").toString());
            } catch (Exception e) {
                e.printStackTrace();
                log.info("调取人员信息失败!");
            }

            if (userRoleCode.equals(UserJobs.Designer.roleCode) && project.getStage().equals(DesignStateEnum.STATE_20.getState())) {
                projectVo.setAgreeButto(true);
                projectVo.setRefuseButton(true);
                owner.setPhone(null);
            } else {
                projectVo.setAgreeButto(false);
                projectVo.setRefuseButton(false);
            }
            projectVo.setOwner(owner);
            projectVo.setAddress(getProjectAdress(project.getProjectNo()));

            //添加进度展示
            if (project.getStage() >= ConstructionStateEnum.STATE_500.getState()) {
                projectVo.setProgressIsShow(true);
                //添加进度信息
                if (project.getStage() == ConstructionStateEnum.STATE_700.getState()) {
                    projectVo.setConstructionProgress(100);
                } else {
                    projectVo.setConstructionProgress(MathUtil.getPercentage(project.getPlanStartTime(), project.getPlanEndTime(), new Date()));
                }
                projectVo.setStageConsumerName(ConstructionStateEnum.queryByState(project.getStage()).getStateName(ConstructOrderConstants.APP_TYPE_CUSTOMER));
                projectVo.setStageDesignName(ConstructionStateEnum.queryByState(project.getStage()).getStateName(ConstructOrderConstants.APP_TYPE_DESIGN));
            } else {
                projectVo.setStageDesignName(DesignStateEnum.queryByState(project.getStage()).getStateName(3));
                projectVo.setStageConsumerName(DesignStateEnum.queryByState(project.getStage()).getStateName(4));
                projectVo.setProgressIsShow(false);
            }
            String projectMessageStatus = cloudService.getProjectMessageStatus(project.getProjectNo(), project.getOwnerId());
            if (projectMessageStatus.trim().isEmpty()) {
                return RespData.error("获取动态消息信息失败!");
            }
            JSONObject messageJson = JSONObject.parseObject(projectMessageStatus);
            JSONObject data = messageJson.getJSONObject("data");
            if (!messageJson.getInteger("code").equals(ErrorCode.OK.getCode())) {
                return RespData.error("获取动态消息信息失败!");
            }
            String dataString = JSONObject.toJSONString(data);
            OperationVo operationVo = JSONObject.parseObject(dataString, OperationVo.class);
            projectVo.setProjectDynamic(Integer.parseInt(operationVo.getProjectDynamic()) > 0 ? 1 : 0);
            projectVo.setProjectOrder(Integer.parseInt(operationVo.getProjectOrder()) > 0 ? 1 : 0);
            projectVo.setProjectData(Integer.parseInt(operationVo.getProjectData()) > 0 ? 1 : 0);
            projectVo.setProjectInvoice(Integer.parseInt(operationVo.getInvoice()) > 0 ? 1 : 0);
            projectVo.setStageNameColor("#50ABD2");
            projectVoList.add(projectVo);
        }
        PageInfo<ProjectVo> pageInfo = new PageInfo<>(projectVoList);
        int pages = (int) Math.ceil(((double) allOrder.size()) / appProjectSEO.getRows());
        pageInfo.setPages(pages);
        if (appProjectSEO.getRows() == projects.size()) {
            if (allOrder.size() > appProjectSEO.getRows() * appProjectSEO.getPage()) {
                pageInfo.setHasNextPage(true);
            }
        }
        return RespData.success(pageInfo);
    }

    /**
     * C/B-项目列表--施工端
     *
     * @param pageSize
     * @param pageNum
     * @param userId
     * @param inputData
     * @return
     */
    @Override
    public MyRespBundle<PageVo<List<ConstructionProjectVo>>> getConstructionAllProject(int pageSize, int pageNum, String userId, String inputData, Integer projectType) {
        if (userId == null || userId.trim().isEmpty() || projectType == null || projectType == 0) {
            return RespData.error("参数userId 和 projectType 不可为空");
        }
        List<ConstructionProjectVo> constructionProjectVos = getAllConstructionProject(userId);
        PageVo<List<ConstructionProjectVo>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setPageIndex(pageNum);
        Map<String, Object> playMap = getPlayList(constructionProjectVos, pageSize, pageNum, inputData, projectType);
        List<ConstructionProjectVo> playProjects = (List<ConstructionProjectVo>) playMap.get("data");
        pageVo.setData(playProjects);
        pageVo.setTotal((Integer) playMap.get("total"));
        return RespData.success(pageVo);
    }

    /**
     * 施工端项目列表项目筛选
     *
     * @param list
     * @param pageSize
     * @param pageNum
     * @param inputData
     * @param projectType
     * @return
     */
    public Map<String, Object> getPlayList(List<ConstructionProjectVo> list, int pageSize, int pageNum, String inputData, Integer projectType) {
        Map<String, Object> map = new HashMap<>();
        if (inputData != null && !inputData.trim().isEmpty()) {
            List<ConstructionProjectVo> playProjects = new ArrayList<>();
            List<ConstructionProjectVo> allProjects = new ArrayList<>();
            Integer count = 0;
            for (ConstructionProjectVo projectVo : list) {
                if (projectVo.getAddress().contains(inputData) || projectVo.getProjectNo().contains(inputData) || projectVo.getOrderNo().contains(inputData) || projectVo.getOwner().contains(inputData)) {
                    if ((pageNum - 1) * pageSize <= count && count < pageNum * pageSize && constructionStateService.getConstructState(projectVo.getStage(), projectVo.getComplaintState(), projectType)) {
                        playProjects.add(projectVo);
                    }
                    allProjects.add(projectVo);
                    count++;
                }
            }
            map.put("data", playProjects);
            map.put("total", allProjects.size());
        } else {
            List<ConstructionProjectVo> playProjects = new ArrayList<>();
            List<ConstructionProjectVo> allProjects = new ArrayList<>();
            Integer count = 0;
            for (ConstructionProjectVo projectVo : list) {
                //传让提供接口后把他放到逻辑与条件中
                boolean result = constructionStateService.getConstructState(projectVo.getStage(), projectVo.getComplaintState(), projectType);
                if ((pageNum - 1) * pageSize <= count && count < pageNum * pageSize && result) {
                    playProjects.add(projectVo);
                }
                allProjects.add(projectVo);
                count++;
            }
            map.put("data", playProjects);
            map.put("total", allProjects.size());
        }
        return map;
    }

    /**
     * 获取施工端项目搜索项(进度阶段+验收阶段)
     *
     * @param userId
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ProjectScreenVo> getProjectScreen(String userId, String projectNo) {
        if (userId == null || userId.trim().isEmpty()) {
            return RespData.error("请上传参数:userId=" + userId);
        }
        ProjectScreenVo projectScreenVo = new ProjectScreenVo();
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        EmployeeMsgExample.Criteria msgCriteria = msgExample.createCriteria();
        msgCriteria.andUserIdEqualTo(userId);
        msgCriteria.andEmployeeStateEqualTo(1);
        List list = new ArrayList();
        list.add(3);
        msgCriteria.andEmployeeApplyStateIn(list);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        if (employeeMsgs.size() == 0) {
            return RespData.error("此用户当前状态无查看项目资格(已离职或被解约)");
        }
        BuildSchemeCompanyRelExample relExample = new BuildSchemeCompanyRelExample();
        BuildSchemeCompanyRelExample.Criteria relCriteria = relExample.createCriteria();
        relCriteria.andCompanyIdEqualTo(employeeMsgs.get(0).getCompanyId());
        relCriteria.andDelStateEqualTo(2);
        relCriteria.andIsEableEqualTo(1);
        List<BuildSchemeCompanyRel> buildSchemeCompanyRels = companyRelMapper.selectByExample(relExample);
        if (buildSchemeCompanyRels.size() == 0) {
            return RespData.error("此公司尚无在用施工方案");
        }
        ProjectBigSchedulingExample bigSchedulingExample = new ProjectBigSchedulingExample();
        bigSchedulingExample.setOrderByClause("sort asc");
        ProjectBigSchedulingExample.Criteria bigCriteria = bigSchedulingExample.createCriteria();
        bigCriteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        bigCriteria.andSchemeNoEqualTo(buildSchemeCompanyRels.get(0).getBuildSchemeNo());
        List<ProjectBigScheduling> bigList = projectBigSchedulingMapper.selectByExample(bigSchedulingExample);
        bigCriteria.andIsNeedCheckEqualTo(Scheduling.CHECK_YES.getValue());
        List<ProjectBigScheduling> checkBigList = projectBigSchedulingMapper.selectByExample(bigSchedulingExample);
        if (bigList.size() == 0) {
            return RespData.error("此项目下无排期信息");
        }
        List<ProjectBigSchedulingDetailsVO> playBigList = new ArrayList<>();
        for (ProjectBigScheduling details : bigList) {
            ProjectBigSchedulingDetailsVO detailsVO = new ProjectBigSchedulingDetailsVO();
            detailsVO.setBigSort(details.getSort());
            detailsVO.setBigName(details.getName());
            playBigList.add(detailsVO);
        }
        List<ProjectBigSchedulingDetailsVO> playCheckList = new ArrayList<>();
        for (ProjectBigScheduling details : checkBigList) {
            ProjectBigSchedulingDetailsVO detailsVO = new ProjectBigSchedulingDetailsVO();
            detailsVO.setBigSort(details.getSort());
            detailsVO.setRenameBig(details.getName());
            playCheckList.add(detailsVO);
        }
        Collections.sort(playBigList);
        Collections.sort(playCheckList);
        projectScreenVo.setCheckList(playCheckList);
        projectScreenVo.setSchedulingList(playBigList);
        return RespData.success(projectScreenVo);
    }

    /**
     * 施工端项目列表--筛选
     *
     * @param pageSize       每页条数
     * @param pageNum        页码
     * @param userId         用户id
     * @param delayBegin     延期开始时间
     * @param delayEnd       延期结束时间
     * @param schedulingSort 进度阶段sort值
     * @param checkSort      验收阶段sort值
     * @param checkComplete  验收是否完成
     * @param projectNo      项目编号
     * @return
     */
    @Override
    public MyRespBundle<PageVo<List<ConstructionProjectVo>>> getProjectByScreen(
            int pageSize, int pageNum, String userId,
            Integer delayBegin, Integer delayEnd,
            Integer schedulingSort, Integer checkSort,
            Integer checkComplete, String projectNo) {
        if (userId == null || userId.trim().isEmpty()) {
            return RespData.error("请检查入参:userId=" + userId);
        }
        List<ConstructionProjectVo> constructionProjectVos = getAllConstructionProject(userId);
        if (constructionProjectVos.size() == 0) {
            PageVo<List<ConstructionProjectVo>> pageVo = new PageVo<>();
            pageVo.setPageSize(pageSize);
            pageVo.setPageIndex(pageNum);
            pageVo.setTotal(0);
            return RespData.success(pageVo);
        }
        List<ConstructionProjectVo> playProjects = new ArrayList<>();
        List<ConstructionProjectVo> allProjects = new ArrayList<>();
        Integer count = 0;
        Boolean result = false;
        for (ConstructionProjectVo projectVo : constructionProjectVos) {
            boolean checkResult = false;
            if (projectVo.getSort() != null) {
                checkResult = afInstanceService.getCheckSuccess(projectVo.getProjectNo(), projectVo.getSort());
//                System.out.println("projectNo=" + projectVo.getProjectNo() + ";sort=" + projectVo.getSort() + "; result=" + checkResult);
            }
            if (checkComplete != null && checkComplete == 2) {
                if (checkResult) {
                    checkResult = false;
                } else {
                    checkResult = true;
                }
            }
            if (delayBegin != null && delayEnd != null && schedulingSort == null && checkSort == null && checkComplete == null && projectVo.getDelay() != null && delayBegin <= projectVo.getDelay() && projectVo.getDelay() <= delayEnd) {
                if ((pageNum - 1) * pageSize <= count && count < pageNum * pageSize) {
                    playProjects.add(projectVo);
                }
                count++;
                allProjects.add(projectVo);
                result = true;
            } else if (delayBegin != null && delayEnd != null && schedulingSort != null && checkSort != null && checkComplete != null && projectVo.getSort() != null && projectVo.getDelay() != null && delayBegin <= projectVo.getDelay() && projectVo.getDelay() <= delayEnd && schedulingSort.equals(projectVo.getSort()) && checkSort.equals(projectVo.getSort())) {
                if ((pageNum - 1) * pageSize <= count && count < pageNum * pageSize) {
                    playProjects.add(projectVo);
                }
                count++;
                allProjects.add(projectVo);
                result = true;
            } else if (delayBegin != null && delayEnd != null && schedulingSort != null && checkSort != null && checkComplete != null && projectVo.getSort() != null && projectVo.getDelay() != null && delayBegin <= projectVo.getDelay() && projectVo.getDelay() <= delayEnd && schedulingSort.equals(projectVo.getSort()) && checkSort.equals(projectVo.getSort()) && checkResult) {
                if ((pageNum - 1) * pageSize <= count && count < pageNum * pageSize) {
                    playProjects.add(projectVo);
                }
                count++;
                allProjects.add(projectVo);
                result = true;
            } else if (delayBegin == null && delayEnd == null && schedulingSort != null && checkSort != null && checkComplete == null && schedulingSort.equals(projectVo.getSort()) && checkSort.equals(projectVo.getSort())) {
                if ((pageNum - 1) * pageSize <= count && count < pageNum * pageSize) {
                    playProjects.add(projectVo);
                }
                count++;
                allProjects.add(projectVo);
                result = true;
            } else if (delayBegin == null && delayEnd == null && schedulingSort != null && checkSort != null && checkComplete != null && projectVo.getSort() != null && projectVo.getDelay() != null && schedulingSort.equals(projectVo.getSort()) && checkSort.equals(projectVo.getSort()) && checkResult) {
                if ((pageNum - 1) * pageSize <= count && count < pageNum * pageSize) {
                    playProjects.add(projectVo);
                }
                count++;
                allProjects.add(projectVo);
                result = true;
            } else if (delayBegin == null && delayEnd == null && schedulingSort == null && checkSort == null && checkComplete == null) {
                if ((pageNum - 1) * pageSize <= count && count < pageNum * pageSize) {
                    playProjects.add(projectVo);
                }
                count++;
                allProjects.add(projectVo);
                result = true;
            }
        }
        if (playProjects.size() == 0) {
            return RespData.success(new PageVo<>());
        }
        if (!result) {
            return RespData.error("请检查入参:userId=" + userId + ";delayBegin=" + delayBegin + ";delayEnd=" + delayEnd + ";=schedulingSort=" + schedulingSort + ";checkSort=" + checkSort + ";checkComplete=" + checkComplete);
        }
        PageVo<List<ConstructionProjectVo>> pageVo = new PageVo<>();
        pageVo.setPageSize(pageSize);
        pageVo.setPageIndex(pageNum);
        pageVo.setData(playProjects);
        pageVo.setTotal(allProjects.size());
        return RespData.success(pageVo);
    }

    /**
     * 获取施工端所有项目
     *
     * @return
     */
    public List<ConstructionProjectVo> getAllConstructionProject(String userId) {
        List<ConstructionProjectVo> constructionProjectVos = new ArrayList<>();
        OrderUserExample userExample = new OrderUserExample();
        OrderUserExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andUserIdEqualTo(userId);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(userExample);
        if (orderUsers.size() == 0) {
            return constructionProjectVos;
        }
        List<String> list = new ArrayList<>();
        for (OrderUser orderUser : orderUsers) {
            list.add(orderUser.getProjectNo());
        }
        ConstructionOrderExample orderExample = new ConstructionOrderExample();
        ConstructionOrderExample.Criteria orderCriteria = orderExample.createCriteria();
        orderCriteria.andProjectNoIn(list);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(orderExample);
        if (constructionOrders.size() == 0) {
            return constructionProjectVos;
        }
        for (ConstructionOrder constructionOrder : constructionOrders) {
            ConstructionProjectVo constructionProjectVo = new ConstructionProjectVo();
            constructionProjectVo.setProjectNo(constructionOrder.getProjectNo());
            constructionProjectVo.setOrderNo(constructionOrder.getOrderNo());
            constructionProjectVo.setAppointmentTime(DateUtil.formartDate(constructionOrder.getCreateTime(), "yyyy-MM-dd HH:mm;ss"));
            constructionProjectVo.setType(constructionOrder.getType());
            constructionProjectVo.setStageName(ConstructionStateEnum.queryByState(constructionOrder.getOrderStage()).getStateName(3));
            constructionProjectVo.setStage(constructionOrder.getOrderStage());
            constructionProjectVo.setComplaintState(constructionOrder.getComplaintState());
            //装修地址
            ProjectExample projectExample = new ProjectExample();
            ProjectExample.Criteria projectCriteria = projectExample.createCriteria();
            projectCriteria.andProjectNoEqualTo(constructionOrder.getProjectNo());
            projectCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
            List<Project> projects = projectMapper.selectByExample(projectExample);
            if (projects.size() != 0) {
                constructionProjectVo.setAddress(projects.get(0).getAddressDetail());
                //业主信息
                Map user = new HashMap();
                try {
                    user = newOrderUserService.getUserName(projects.get(0).getOwnerId(), ProjectDataStatus.OWNER.getDescription());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("调取人员信息失败!");
                }
                constructionProjectVo.setOwner(user.get("nickName") == null ? "" : user.get("nickName").toString());
                constructionProjectVo.setPhone(user.get("phone") == null ? "" : user.get("phone").toString());
            }
            //3d图片地址
//            ProjectDataExample dataExample = new ProjectDataExample();
//            ProjectDataExample.Criteria dataCriteria = dataExample.createCriteria();
//            dataCriteria.andProjectNoEqualTo(constructionOrder.getProjectNo());
//            dataCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
//            dataCriteria.andTypeEqualTo(ProjectDataStatus.DESIGN_DATA.getValue());
//            List<ProjectData> projectDatas = projectDataMapper.selectByExample(dataExample);
//            if (projectDatas.size() != 0) {
//                constructionProjectVo.setThirdUrl(projectDatas.get(0).getPhotoPanoramaUrl());
//            }
            //延期天数
            ProjectSchedulingExample schedulingExample = new ProjectSchedulingExample();
            ProjectSchedulingExample.Criteria schedulingCriteria = schedulingExample.createCriteria();
            schedulingCriteria.andProjectNoEqualTo(constructionOrder.getProjectNo());
            schedulingCriteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
            List<ProjectScheduling> projectSchedulings = projectSchedulingMapper.selectByExample(schedulingExample);
            if (projectSchedulings.size() > 0) {
                constructionProjectVo.setDelay(projectSchedulings.get(0).getDelay());
                constructionProjectVo.setSort(projectSchedulings.get(0).getRate());
            }
            constructionProjectVos.add(constructionProjectVo);
        }
        return constructionProjectVos;
    }

    /**
     * C/B-项目个数
     *
     * @param userId
     * @return
     */
    @Override
    public MyRespBundle<Integer> getProjectNum(String userId) {
        OrderUserExample example1 = new OrderUserExample();
        OrderUserExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andUserIdEqualTo(userId);
        //查询此人名下所有项目
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(example1);
        Set<String> set = new HashSet<>();
        for (OrderUser orderUser : orderUsers) {
            set.add(orderUser.getProjectNo());
        }
        return RespData.success(set.size());
    }

    /**
     * 获取项目详情接口
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ProjectVo> getAppProjectDetail(String userId, String projectNo) {
        List<ProjectOrderDetailVo> projectOrderDetailVoList = new ArrayList<>();
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(example);
        if (projects.size() == 0) {
            return RespData.error("项目不存在!!");
        }
        Project project = projects.get(0);
        ProjectVo projectVo = BaseToVoUtils.getVo(project, ProjectVo.class, BaseToVoUtils.getProjectMap());
        projectVo.setAddress(getProjectAdress(projectNo));
        //存放跑马灯消息
        projectVo.setMessage(getMessage(project.getProjectNo(), project.getStage(), userId));
        //添加进度展示
        if (project.getStage() >= ConstructionStateEnum.STATE_500.getState()) {
            projectVo.setProgressIsShow(true);
            //添加进度信息
            if (project.getStage() == ConstructionStateEnum.STATE_700.getState()) {
                projectVo.setConstructionProgress(100);
            } else {
                projectVo.setConstructionProgress(MathUtil.getPercentage(project.getPlanStartTime(), project.getPlanEndTime(), new Date()));
            }
            projectVo.setStageDesignName(ConstructionStateEnum.queryByState(project.getStage()).getStateName(ConstructOrderConstants.APP_TYPE_DESIGN));
            projectVo.setStageConsumerName(ConstructionStateEnum.queryByState(project.getStage()).getStateName(ConstructOrderConstants.APP_TYPE_CUSTOMER));
        } else {
            projectVo.setStageDesignName(DesignStateEnum.queryByState(project.getStage()).getStateName(3));
            projectVo.setStageConsumerName(DesignStateEnum.queryByState(project.getStage()).getStateName(4));
            projectVo.setProgressIsShow(false);
        }
        String projectMessageStatus = cloudService.getProjectMessageStatus(projectNo, project.getOwnerId());
        if (projectMessageStatus.trim().isEmpty()) {
            return RespData.error("获取动态消息信息失败!");
        }
        JSONObject messageJson = JSONObject.parseObject(projectMessageStatus);
        JSONObject data = messageJson.getJSONObject("data");
        if (!messageJson.getInteger("code").equals(ErrorCode.OK.getCode())) {
            return RespData.error("获取动态消息信息失败!");
        }
        String dataString = JSONObject.toJSONString(data);
        OperationVo operationVo = JSONObject.parseObject(dataString, OperationVo.class);
        projectVo.setProjectDynamic(Integer.valueOf(operationVo.getProjectDynamic()) > 0 ? 1 : 0);
        projectVo.setProjectOrder(Integer.valueOf(operationVo.getProjectOrder()) > 0 ? 1 : 0);
        projectVo.setProjectData(Integer.valueOf(operationVo.getProjectData()) > 0 ? 1 : 0);
        projectVo.setProjectInvoice(Integer.valueOf(operationVo.getInvoice()) > 0 ? 1 : 0);
        projectVo.setStageNameColor("#50ABD2");
        //添加业主信息
        PersionVo owner = new PersionVo();
        try {
            Map userName1 = newOrderUserService.getUserName(project.getOwnerId(), ProjectDataStatus.OWNER.getDescription());
            owner.setPhone(userName1.get("phone").toString());
            owner.setName(userName1.get("nickName").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error("调取人员信息失败!");
        }
        projectVo.setOwner(owner);

        //组合设计订单
        DesignerOrderExample designerOrderExample = new DesignerOrderExample();
        DesignerOrderExample.Criteria designCriteria = designerOrderExample.createCriteria();
        designCriteria.andProjectNoEqualTo(projectNo);
        designCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<DesignerOrder> designerOrders = designerOrderMapper.selectByExample(designerOrderExample);
        if (designerOrders.size() == ProjectDataStatus.INSERT_FAILD.getValue()) {
            return RespData.error("查无此设计订单");
        }
        DesignerOrder designerOrder = designerOrders.get(0);
        ProjectOrderDetailVo designerOrderDetailVo = BaseToVoUtils.getVo(designerOrder, ProjectOrderDetailVo.class);
        //存放阶段信息
        List<OrderTaskSortVo> orderTaskSortVoList = new ArrayList<>();
        List<Map<String, Object>> maps = DesignStateEnum.allState(designerOrder.getOrderStage());
        for (Map<String, Object> map : maps) {
            OrderTaskSortVo orderTaskSortVo = new OrderTaskSortVo();
            orderTaskSortVo.setName(map.get("val").toString());
            orderTaskSortVo.setSort((Integer) map.get("key"));
            orderTaskSortVoList.add(orderTaskSortVo);
        }
        designerOrderDetailVo.setOrderTaskSortVoList(orderTaskSortVoList);
        designerOrderDetailVo.setTaskStage(designerOrder.getOrderStage());//
        designerOrderDetailVo.setPlayTask(designDispatchService.showBtnByUserId(designerOrder.getProjectNo(), designerOrder.getOrderNo(), userId));
        List<DesignStateEnum> allCancelState = DesignStateEnum.getAllCancelState();
        designerOrderDetailVo.setCancle(allCancelState.contains(DesignStateEnum.queryByState(designerOrder.getOrderStage())));
        //存放订单类型
        designerOrderDetailVo.setOrderType(ProjectDataStatus.EFFECT_STATUS.getValue());
        //存放展示信息
        OrderPlayVo designOrderPlayVo = designerOrderMapper.selectByProjectNoAndStatus(projectNo, ProjectDataStatus.BASE_STATUS.getValue());
        if (designOrderPlayVo == null) {
            return RespData.error("设计订单的公司不存在!");
        }
        List<PersionVo> persionList = new ArrayList<>();
        String designerId = projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.DESIGN_POWER);
        if (designerId != null && !designerId.equals(userId)) {
            List<PersionVo> persionVos = employeeMsgMapper.selectByUserId(designerId);
            PersionVo persionVo = persionVos.get(0);
            try {
                UserMsgVo userMsgVo = userCenterService.queryUser(designerId);
                if (persionVos.size() > 0 && persionVos.get(0) != null) {
                    persionVo.setPhone(userMsgVo.getUserPhone());
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.info("调取人员信息失败!");
            }
            persionVo.setRole(UserJobs.Designer.mes);
            persionList.add(persionVo);
        }
        designOrderPlayVo.setPersionList(persionList);
        designerOrderDetailVo.setOrderPlayVo(designOrderPlayVo);
        BasicsDataExample basicsDataExample = new BasicsDataExample();
        BasicsDataExample.Criteria dataCriteria = basicsDataExample.createCriteria();
        dataCriteria.andBasicsCodeEqualTo(designerOrderDetailVo.getStyleType());
        dataCriteria.andBasicsGroupEqualTo(BasicsDataParentEnum.DESIGN_STYLE.getCode());
        List<BasicsData> basicsData = basicsDataMapper.selectByExample(basicsDataExample);
        if (basicsData.size() == 0) {
            designerOrderDetailVo.setStyleType("");
        } else {
            designerOrderDetailVo.setStyleType(basicsData.get(0).getBasicsName());
        }
        projectOrderDetailVoList.add(designerOrderDetailVo);
        //组合施工订单
        ProjectOrderDetailVo constructionOrderDetailVo = constructionOrderMapper.selectByProjectNo(projectNo);
        List<OrderTaskSortVo> orderTaskSortVoList1 = new ArrayList<>();
        if (constructionOrderDetailVo != null) {
            ConstructionOrder constructionOrder = constructOrderService.findByProjectNo(projectNo);
            List<OrderStatusDTO> states = constructionStateService.getStates(ConstructOrderConstants.APP_TYPE_CUSTOMER, constructionOrder.getOrderStage(), constructionOrder.getSchemeNo());
            for (OrderStatusDTO orderStatus : states) {
                OrderTaskSortVo orderTaskSortVo = new OrderTaskSortVo();
                orderTaskSortVo.setSort(orderStatus.getStatus());
                orderTaskSortVo.setName(orderStatus.getName());
                orderTaskSortVoList1.add(orderTaskSortVo);
            }
            constructionOrderDetailVo.setOrderTaskSortVoList(orderTaskSortVoList1);
//            constructionOrderDetailVo.setTaskStage(projects.get(0).getStage());
            Boolean aBoolean = constructionStateService.customerCancelOrderState(project.getOwnerId(), constructionOrderDetailVo.getOrderNo());
            constructionOrderDetailVo.setCancle(aBoolean);
            //存放订单类型
            constructionOrderDetailVo.setOrderType(ProjectDataStatus.CONSTRUCTION_STATUS.getValue());
            //存放展示信息
            List<OrderPlayVo> constructionOrderPlays = constructionOrderMapper.selectByProjectNoAndStatus(projectNo, ProjectDataStatus.BASE_STATUS.getValue());
            if (constructionOrderPlays.size() == 0) {
                return RespData.error("获取公司信息失败");
            }
            OrderPlayVo constructionOrderPlayVo = constructionOrderPlays.get(0);
            //查询合同费用

            String totalMoney = "";
            OrderContractExample contractExample = new OrderContractExample();
            OrderContractExample.Criteria contractCriteria = contractExample.createCriteria();
            contractCriteria.andOrderNumberEqualTo(constructionOrder.getOrderNo());
            contractCriteria.andAuditTypeEqualTo("1");
            List<OrderContract> orderContracts = orderContractMapper.selectByExample(contractExample);
            if (orderContracts.size() > 0) {
                ContractTermsExample termsExample = new ContractTermsExample();
                ContractTermsExample.Criteria termsCriteria = termsExample.createCriteria();
                termsCriteria.andContractNumberEqualTo(orderContracts.get(0).getContractNumber());
                termsCriteria.andContractDictCodeEqualTo("c17");
                List<ContractTerms> contractTerms = contractTermsMapper.selectByExample(termsExample);
                if (contractTerms.size() > 0) {
                    totalMoney = contractTerms.get(0).getContractValue();
                }
            }


//            String totalMoney = "";
//            OrderContractExample contractExample = new OrderContractExample();
//            OrderContractExample.Criteria contractCriteria = contractExample.createCriteria();
//            contractCriteria.andOrderNumberEqualTo(constructionOrder.getOrderNo());
//            contractCriteria.andAuditTypeEqualTo( "1");
//            List<OrderContract> orderContracts = orderContractMapper.selectByExample(contractExample);
//            if (orderContracts.size() > 0) {
//                ContractTermsExample termsExample = new ContractTermsExample();
//                ContractTermsExample.Criteria termsCriteria = termsExample.createCriteria();
//                termsCriteria.andContractNumberNotEqualTo(orderContracts.get(0).getContractNumber());
//                termsCriteria.andContractDictCodeEqualTo("c17");
//                List<ContractTerms> contractTerms = contractTermsMapper.selectByExample(termsExample);
//                if (contractTerms.size() > 0) {
//                    totalMoney = contractTerms.get(0).getContractValue();
//                }
//            }
            if (constructionOrderPlayVo == null) {
                constructionOrderPlayVo = new OrderPlayVo();
                constructionOrderPlayVo.setCost(totalMoney);
            }else {
                constructionOrderPlayVo.setCost(totalMoney);
            }
            constructionOrderPlayVo.setSchedule(DateUtil.daysCalculate(project.getPlanStartTime(), project.getPlanEndTime()));
            //存放人员信息
            List<PersionVo> constructionPersionList = employeeMsgMapper.selectAllByUserId(designerOrder.getUserId());
            for (PersionVo persionVo1 : constructionPersionList) {
                try {
                    Map persionDetail = newOrderUserService.getUserName(persionVo1.getUserId(), persionVo1.getRole());
                    persionVo1.setPhone(persionDetail.get("phone").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("调取人员信息失败!");
                }
            }
            constructionOrderPlayVo.setPersionList(constructionPersionList);
            constructionOrderDetailVo.setOrderPlayVo(constructionOrderPlayVo);
            projectOrderDetailVoList.add(constructionOrderDetailVo);
        }
        projectVo.setProjectOrderDetailVoList(projectOrderDetailVoList);
        return RespData.success(projectVo);
    }

    /**
     * APP-获取项目详情头接口
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ProjectTitleVo> getAppProjectTitleDetail(String projectNo) {
        ProjectTitleVo projectTitleVo = new ProjectTitleVo();
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(example);
        if (projects.size() == 0) {
            return RespData.error("项目不存在!!");
        }
        Project project = projects.get(0);
        //添加进度展示
        if (project.getStage() >= ConstructionStateEnum.STATE_500.getState()) {
            //添加进度信息
            projectTitleVo.setStageDesignName(ConstructionStateEnum.queryByState(project.getStage()).getStateName(3));
        } else {
            projectTitleVo.setStageDesignName(DesignStateEnum.queryByState(project.getStage()).getStateName(3));
        }
        projectTitleVo.setProjectStartTime(project.getPlanStartTime());
        projectTitleVo.setProjectEndTime(project.getPlanEndTime());
        projectTitleVo.setAddress(getProjectAdress(projectNo));
        projectTitleVo.setProjectNo(projectNo);
        List<OrderPlayVo> orderPlayVos = constructionOrderMapper.selectByProjectNoAndStatus(projectNo, ProjectDataStatus.BASE_STATUS.getValue());
        if (orderPlayVos.size() != 0) {
            OrderPlayVo orderPlayVo = orderPlayVos.get(0);
            String totalMoney = "";
            ConstructionOrder constructionOrder = constructOrderService.findByProjectNo(projectNo);
            OrderContractExample contractExample = new OrderContractExample();
            OrderContractExample.Criteria contractCriteria = contractExample.createCriteria();
            contractCriteria.andOrderNumberEqualTo(constructionOrder.getOrderNo());
            contractCriteria.andAuditTypeEqualTo("1");
            List<OrderContract> orderContracts = orderContractMapper.selectByExample(contractExample);
            if (orderContracts.size() > 0) {
                projectTitleVo.setContractStartTime(orderContracts.get(0).getStartTime());
                projectTitleVo.setContractEndTime(orderContracts.get(0).getEndTime());
                ContractTermsExample termsExample = new ContractTermsExample();
                ContractTermsExample.Criteria termsCriteria = termsExample.createCriteria();
                termsCriteria.andContractNumberEqualTo(orderContracts.get(0).getContractNumber());
                termsCriteria.andContractDictCodeEqualTo("c17");
                List<ContractTerms> contractTerms = contractTermsMapper.selectByExample(termsExample);
                if (contractTerms.size() > 0) {
                    totalMoney = contractTerms.get(0).getContractValue();
                }
            }
            if (orderPlayVo != null) {
                projectTitleVo.setDelay(orderPlayVo.getDelay());
                projectTitleVo.setSchedule(DateUtil.differentHoursByMillisecond(project.getPlanStartTime(), project.getPlanEndTime()));
                projectTitleVo.setTaskNum(orderPlayVo.getTaskNum());
                projectTitleVo.setCost(totalMoney);
            }
            //添加进度展示
            if (project.getStage() == ConstructionStateEnum.STATE_700.getState()) {
                projectTitleVo.setConstructionProgress(100);
            } else {
                projectTitleVo.setConstructionProgress(MathUtil.getPercentage(project.getPlanStartTime(), project.getPlanEndTime(), new Date()));
            }
        }
        int confirm;
        try {
            confirm = afInstanceService.getProjectCheckResult(projectNo);
        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error("获取排期编辑状态失败");
        }
        ProjectSchedulingExample schedulingExample = new ProjectSchedulingExample();
        ProjectSchedulingExample.Criteria schedulingCriteria = schedulingExample.createCriteria();
        schedulingCriteria.andStatusEqualTo(1);
        schedulingCriteria.andProjectNoEqualTo(projectNo);
        List<ProjectScheduling> projectSchedulings = projectSchedulingMapper.selectByExample(schedulingExample);
        projectTitleVo.setIsConfirm(projectSchedulings.get(0).getIsConfirm());
        projectTitleVo.setGanttChartUrl("https://www.baidu.com");
        return RespData.success(projectTitleVo);
    }

    /**
     * 获取设计资料
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<DataVo> getDesignData(String projectNo) {
        DataVo dataVo = new DataVo();
        ProjectDataExample example = new ProjectDataExample();
        ProjectDataExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<Integer> categoryList = new ArrayList<>();
        categoryList.add(ProjectDataStatus.EFFECT_STATUS.getValue());
        categoryList.add(ProjectDataStatus.CONSTRUCTION_STATUS.getValue());
        criteria.andCategoryIn(categoryList);
        List<ProjectData> projectDataList = projectDataMapper.selectByExample(example);
        if (projectDataList.size() == ProjectDataStatus.INSERT_FAILD.getValue()) {
            return RespData.error("暂无设计资料");
        }
        List<DataDetailVo> dataDetailVoList = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (ProjectData projectData : projectDataList) {
            set.add(projectData.getCategory());
        }
        for (Integer integer : set) {
            DataDetailVo detailVo = new DataDetailVo();
            List<UrlDetailVo> urlList = new ArrayList<>();
            List<String> urlStringList = new ArrayList<>();
            for (ProjectData projectData : projectDataList) {
                if (projectData.getCategory().equals(integer)) {
                    if (projectData.getFileType().equals(ProjectDataStatus.FILE_PNG.getValue())) {
                        UrlDetailVo urlDetailVo = new UrlDetailVo();
                        detailVo.setConfirm(projectData.getIsConfirm());
                        detailVo.setUploadTime(projectData.getUploadTime());
                        detailVo.setCategory(projectData.getCategory());
                        urlDetailVo.setImgUrl(projectData.getUrl());
                        urlDetailVo.setPhoto360Url(projectData.getPhotoPanoramaUrl());
                        urlDetailVo.setUploadTime(projectData.getUploadTime());
                        //TODO 设计资料后期优化
                        if (urlList.size() < 4) {
                            urlList.add(urlDetailVo);
                            urlStringList.add(projectData.getUrl());
                        }
                    }
                    if (projectData.getPhotoPanoramaUrl() != null) {
                        detailVo.setThirdUrl(projectData.getPhotoPanoramaUrl());
                    }
                    if (projectData.getFileType().equals(ProjectDataStatus.FILE_PDF.getValue())) {
                        detailVo.setPdfUrl(projectData.getUrl());
                    }
                }
            }
            detailVo.setUrlList(urlList);
            detailVo.setUrlStringList(urlStringList);
            dataDetailVoList.add(detailVo);
        }
        dataVo.setDataList(dataDetailVoList);
        return RespData.success(dataVo);
    }

    /**
     * 获取施工资料
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ConstructionDataVo> getConstructionData(String projectNo) {
        ConstructionDataVo constructionDataVo = new ConstructionDataVo();
        List<UrlDetailVo> urlList = new ArrayList<>();
        Integer confirm = 0;
        ProjectDataExample example = new ProjectDataExample();
        ProjectDataExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria.andTypeEqualTo(ProjectDataStatus.QUOTATION_STATUS.getValue());
        List<ProjectData> projectDataList = projectDataMapper.selectByExample(example);
        if (projectDataList.size() == ProjectDataStatus.INSERT_FAILD.getValue()) {
            return RespData.error("暂无施工资料");
        }
        for (ProjectData projectData : projectDataList) {
            if (projectData.getIsConfirm() != null) {
                confirm = projectData.getIsConfirm();
            }
            UrlDetailVo urlDetailVo = new UrlDetailVo();
            urlDetailVo.setImgUrl(projectData.getUrl());
            urlDetailVo.setName(projectData.getFileName());
            urlDetailVo.setUploadTime(projectData.getUploadTime());
            urlList.add(urlDetailVo);
        }
        constructionDataVo.setUrlList(urlList);
        constructionDataVo.setConfirm(confirm);
        return RespData.success(constructionDataVo);
    }

    /**
     * 获取报价单资料
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<List<UrlDetailVo>> getQuotationData(String projectNo) {
        List<UrlDetailVo> urlList = new ArrayList<>();
        ProjectDataExample example = new ProjectDataExample();
        ProjectDataExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria.andTypeEqualTo(ProjectDataStatus.QUOTATION_STATUS.getValue());
        List<ProjectData> projectDataList = projectDataMapper.selectByExample(example);
        for (ProjectData projectData : projectDataList) {
            UrlDetailVo urlDetailVo = new UrlDetailVo();
            urlDetailVo.setImgUrl(projectData.getUrl());
            urlDetailVo.setName(projectData.getFileName());
            urlList.add(urlDetailVo);
        }
        return RespData.success(urlList);
    }

    /**
     * 确认资料
     *
     * @param dataVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> confirmVolumeRoomData(CaseDataVo dataVo) {
        if (StringUtils.isBlank(dataVo.getUserId())) {
            return RespData.error("请给userid赋值");
        }
        if (dataVo.getHsDesignId() == null || dataVo.getHsDesignId().trim().isEmpty()) {
            return RespData.error("hsDesignId 不可为空");
        }
        if (dataVo.getProjectNo() == null || dataVo.getProjectNo().trim().isEmpty()) {
            return RespData.error("projectNo 不可为空");
        }
        if (dataVo.getType() == null) {
            return RespData.error("type=" + dataVo.getType());
        }

        EmployeeMsgExample example = new EmployeeMsgExample();
        EmployeeMsgExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(dataVo.getUserId());
        criteria.andEmployeeStateEqualTo(1);
        criteria.andRoleCodeEqualTo("CD");
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(example);
        if (employeeMsgs.size() == 0) {
            return RespData.error("查无此设计师!");
        }

        OrderUserExample userExample = new OrderUserExample();
        OrderUserExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andRoleCodeEqualTo("CD");
        userCriteria.andProjectNoEqualTo(dataVo.getProjectNo());
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(userExample);
        if (orderUsers.size() == 0 || orderUsers.size() > 1) {
            return RespData.error("您不符合接单条件/设计师身份有误，请核实后再操作！");
        }

        ProjectDataExample dataExample = new ProjectDataExample();
        ProjectDataExample.Criteria dataCriteria = dataExample.createCriteria();
        dataCriteria.andHsDesignidEqualTo(dataVo.getHsDesignId());
        dataCriteria.andProjectNoNotEqualTo(dataVo.getProjectNo());
        List<ProjectData> projectOldDatas = projectDataMapper.selectByExample(dataExample);
        if (projectOldDatas.size() > 0) {
            return RespData.error("该案例已使用，请创作新的设计案例为消费者提供交付");
        }
        if (orderUsers.get(0).getTransferUserId() == null || orderUsers.get(0).getTransferUserId().isEmpty()) {
            //未更换过设计师
            if (dataVo.getType() == 2 || dataVo.getType() == 3) {
                ProjectDataExample oneDataExample = new ProjectDataExample();
                ProjectDataExample.Criteria oneDataCriteria = oneDataExample.createCriteria();
                oneDataCriteria.andProjectNoEqualTo(dataVo.getProjectNo());
                oneDataCriteria.andTypeEqualTo(1);
                List<ProjectData> projectDataList = projectDataMapper.selectByExample(oneDataExample);
                if (projectDataList.size() == 0) {
                    return RespData.error("此项目尚未上传量房资料");
                }
                if (!dataVo.getHsDesignId().equals(projectDataList.get(0).getHsDesignid())) {
                    return RespData.error("请选择本项目案例");
                }
            }
        } else {
            //已更换过设计师
            if (dataVo.getType() == 3) {
                ProjectDataExample oneDataExample = new ProjectDataExample();
                ProjectDataExample.Criteria oneDataCriteria = oneDataExample.createCriteria();
                oneDataCriteria.andProjectNoEqualTo(dataVo.getProjectNo());
                oneDataCriteria.andTypeEqualTo(2);
                List<ProjectData> projectDataList = projectDataMapper.selectByExample(oneDataExample);
                if (projectDataList.size() == 0) {
                    return RespData.error("此项目尚未上传量房资料");
                }
                if (!dataVo.getHsDesignId().equals(projectDataList.get(0).getHsDesignid())) {
                    return RespData.error("请选择本项目案例");
                }
            }
        }
        if (dataVo.getType() == null) {
            return RespData.error("请选择资料类型");
        }
        for (UrlDetailVo urlDetailVo : dataVo.getDataList()) {
            if (dataVo.getType() == 3) {
                break;
            }
            ProjectData projectData = new ProjectData();
            projectData.setFileType(ProjectDataStatus.FILE_PNG.getValue());
            projectData.setCompanyId(employeeMsgs.get(0).getCompanyId());
            projectData.setType(dataVo.getType());
            projectData.setCategory(dataVo.getType());
            projectData.setProjectNo(dataVo.getProjectNo());
            projectData.setCaseId(dataVo.getCaseId());
            projectData.setHsDesignid(dataVo.getHsDesignId());
            projectData.setFileName(urlDetailVo.getName());
            projectData.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
            if (dataVo.getType().equals(ProjectDataStatus.DESIGN_DATA.getValue())) {
                if (urlDetailVo.getPhoto360Url() == null || urlDetailVo.getPhoto360Url().trim().isEmpty()) {
                    return RespData.error("3D全景度为空");
                }
                projectData.setPhotoPanoramaUrl(urlDetailVo.getPhoto360Url());
            } else {
                if (urlDetailVo.getImgUrl() == null || urlDetailVo.getImgUrl().trim().isEmpty()) {
                    return RespData.error("图片地址为空");
                }
                projectData.setUrl(urlDetailVo.getImgUrl());
            }

            if (DateUtil.getNewDate(dataVo.getCaseUploadTime()) != null) {
                projectData.setUploadTime(DateUtil.getNewDate(dataVo.getCaseUploadTime()));
            }
            int i = projectDataMapper.insertSelective(projectData);
            if (i == ProjectDataStatus.INSERT_FAILD.getValue()) {
                throw new RuntimeException("确认失败!");
            }
        }
        if (dataVo.getType() == 3) {
            String result = cloudService.getShangHaiPriceDetail(dataVo.getHsDesignId());
            if (result.trim().isEmpty()) {
                throw new RuntimeException("获取上海报价信息失败!");
            }
            JSONObject jsonObject = JSON.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            Integer code = jsonObject.getJSONObject("status").getInteger("code");
            if (code != 0) {
                throw new RuntimeException("未提交报价，不能提交施工资料");
            }
            if (data == null) {
                throw new RuntimeException(jsonObject.getJSONObject("status").getString("message"));
            }
            String quoteId = data.getString("quoteId");
            String baseUrl = "https://aly-uat-api.homestyler.com/quote-system/api/v1/quote/" + quoteId + "/export?exportCode=";
            ProjectData projectData = new ProjectData();
            projectData.setFileType(ProjectDataStatus.FILE_PNG.getValue());
            projectData.setCompanyId(employeeMsgs.get(0).getCompanyId());
            projectData.setType(dataVo.getType());
            projectData.setCategory(dataVo.getType());
            projectData.setProjectNo(dataVo.getProjectNo());
            projectData.setCaseId(dataVo.getCaseId());
            projectData.setHsDesignid(dataVo.getHsDesignId());
            projectData.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
            projectData.setUploadTime(new Date());
            for (int i = 1; i <= 5; i++) {
                switch (i) {
                    case 1:
                        projectData.setFileName("全屋报价");
                        projectData.setUrl(baseUrl + i);
                        break;
                    case 2:
                        projectData.setFileName("施工报价");
                        projectData.setUrl(baseUrl + i);
                        break;
                    case 3:
                        projectData.setFileName("材料报价");
                        projectData.setUrl(baseUrl + i);
                        break;
                    case 4:
                        projectData.setFileName("硬装报价");
                        projectData.setUrl(baseUrl + i);
                        break;
                    default:
                        projectData.setFileName("软装报价");
                        projectData.setUrl(baseUrl + i);
                        break;
                }
                int a = projectDataMapper.insertSelective(projectData);
                if (a == ProjectDataStatus.INSERT_FAILD.getValue()) {
                    throw new RuntimeException("确认失败!");
                }
            }
        }
        if (dataVo.getType().equals(ProjectDataStatus.VOLUME_DATA.getValue())) {
            designDispatchService.updateOrderState(dataVo.getProjectNo(), DesignStateEnum.STATE_60.getState(), "system", "system");
        }
        if (dataVo.getType().equals(ProjectDataStatus.DESIGN_DATA.getValue())) {
            DesignStateEnum stateEnum = DesignStateEnum.STATE_240;
            //1全款合同，2分期合同
            if (designDispatchService.queryDesignerOrder(dataVo.getProjectNo()).getContractType() == 2) {
                stateEnum = DesignStateEnum.STATE_160;
            }
            designDispatchService.updateOrderState(dataVo.getProjectNo(), stateEnum.getState(), "system", "system");
        }
        if (dataVo.getType().equals(ProjectDataStatus.CONSTRUCTION_DATA.getValue())) {
            DesignStateEnum stateEnum = DesignStateEnum.STATE_260;
            //1全款合同，2分期合同
            if (designDispatchService.queryDesignerOrder(dataVo.getProjectNo()).getContractType() == 2) {
                stateEnum = DesignStateEnum.STATE_190;
            }
            designDispatchService.updateOrderState(dataVo.getProjectNo(), stateEnum.getState(), "system", "system");
        }
        return RespData.success();
    }

    /**
     * 获取项目阶段
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<Integer> getProjectStatus(String projectNo) {
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<Project> projects = projectMapper.selectByExample(example);
        if (projects.size() == 0) {
            return RespData.error("此项目不存在");
        }
        return RespData.success(projects.get(0).getStage());
    }

    /**
     * 退款
     *
     * @param orderNo
     * @param payOrderNo
     * @param otherReason
     * @param money
     * @param moneyName
     * @param userId
     * @param cancelReason
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> applyRefund(String orderNo, String payOrderNo, String otherReason, Integer money, String moneyName, String userId, String cancelReason) {
        OrderApplyRefund orderApplyRefund = new OrderApplyRefund();
        orderApplyRefund.setLaunchTime(new Date());
        orderApplyRefund.setType(ProjectDataStatus.REFUND_ONE.getValue());
        orderApplyRefund.setOrderNo(orderNo);
        orderApplyRefund.setPayOrderNo(payOrderNo);
        orderApplyRefund.setMoney(money);
        orderApplyRefund.setCancleReason(cancelReason);
        orderApplyRefund.setOtherReason(otherReason);
        orderApplyRefund.setMoneyName(moneyName);
        orderApplyRefund.setInitiatorId(userId);
        int i = orderApplyRefundMapper.insertSelective(orderApplyRefund);
        if (i != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
            return RespData.error("退款申请失败!!");
        }
        return RespData.success();
    }

    /**
     * 取消订单
     *
     * @param orderNo
     * @param projectNo
     * @param userId
     * @param cancelReason
     * @return
     */
    @Override
    public MyRespBundle cancleOrder(String orderNo, String projectNo, String userId, String cancelReason) {
        DesignerOrderExample designerOrderExample = new DesignerOrderExample();
        DesignerOrderExample.Criteria designerCriteria = designerOrderExample.createCriteria();
        designerCriteria.andOrderNoEqualTo(orderNo);
        designerCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<DesignerOrder> designerOrders = designerOrderMapper.selectByExample(designerOrderExample);
        if (designerOrders.size() == 0) {
            return RespData.error("查无此项目");
        }
        if (designerOrders.get(0).getOrderStage().equals(DesignStateEnum.STATE_270.getState()) || designerOrders.get(0).getOrderStage().equals(DesignStateEnum.STATE_210.getState())) {
            //如果设计订单完成,则请求施工订单更改状态
            constructionStateService.customerCancelOrder(userId, orderNo, cancelReason);
        } else {
            designDispatchService.endOrder(projectNo, userId, cancelReason);
        }
        return RespData.success();
    }

    /**
     * C端确认资料
     *
     * @param projectNo
     * @param category
     * @param result
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> confirmVolumeRoomDataUser(String projectNo, Integer category, Integer result) {
        if (projectNo == null || projectNo.trim().isEmpty() || category == null) {
            return RespData.error("请检查入参:projectNo=" + projectNo + ";category=" + category);
        }
        if (result == null || (result != 1 && result != 2)) {
            return RespData.error("请检查入参:result=" + result);
        }
        if (result == 1) {
            ProjectData projectData = new ProjectData();
            projectData.setIsConfirm(ProjectDataStatus.CONFIRM.getValue());
            projectData.setConfirmTime(new Date());
            ProjectDataExample example = new ProjectDataExample();
            ProjectDataExample.Criteria criteria = example.createCriteria();
            criteria.andCategoryEqualTo(category);
            criteria.andProjectNoEqualTo(projectNo);
            criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
            int i = projectDataMapper.updateByExampleSelective(projectData, example);
            if (i == ProjectDataStatus.INSERT_FAILD.getValue()) {
                return RespData.error("确认失败");
            }
            String ownerId = projectUserService.queryUserIdOne(projectNo, RoleFunctionEnum.OWNER_POWER);
            if (category == 1) {
                designDispatchService.confirmedDeliveries(projectNo, ownerId);
            } else if (category == 2) {
                DesignStateEnum stateEnum = DesignStateEnum.STATE_250;
                //1全款合同，2分期合同
                if (designDispatchService.queryDesignerOrder(projectNo).getContractType() == 2) {
                    stateEnum = DesignStateEnum.STATE_170;
                }
                designDispatchService.updateOrderState(projectNo, stateEnum.getState(), "system", "system");
            } else if (category == 3) {
                DesignStateEnum stateEnum = DesignStateEnum.STATE_270;
                //1全款合同，2分期合同
                if (designDispatchService.queryDesignerOrder(projectNo).getContractType() == 2) {
                    stateEnum = DesignStateEnum.STATE_200;
                }
                designDispatchService.updateOrderState(projectNo, stateEnum.getState(), "system", "system");
            }
        } else {
            DesignStateEnum stateEnum = null;
            if (category == 1) {
                stateEnum = DesignStateEnum.STATE_50;
            } else if (category == 2) {
                stateEnum = DesignStateEnum.STATE_230;
                //1全款合同，2分期合同
                if (designDispatchService.queryDesignerOrder(projectNo).getContractType() == 2) {
                    stateEnum = DesignStateEnum.STATE_150;
                }
            } else if (category == 3) {
                stateEnum = DesignStateEnum.STATE_250;
                //1全款合同，2分期合同
                if (designDispatchService.queryDesignerOrder(projectNo).getContractType() == 2) {
                    stateEnum = DesignStateEnum.STATE_180;
                }
            }
            ProjectData data = new ProjectData();
            data.setStatus(2);
            ProjectDataExample dataExample = new ProjectDataExample();
            ProjectDataExample.Criteria dataCriteria = dataExample.createCriteria();
            dataCriteria.andProjectNoEqualTo(projectNo);
            dataCriteria.andStatusEqualTo(1);
            dataCriteria.andTypeEqualTo(category);
            int i = projectDataMapper.updateByExampleSelective(data, dataExample);
            if (i == 0) {
                throw new RuntimeException("拒绝失败");
            }
            designDispatchService.updateOrderState(projectNo, stateEnum.getState(), "system", "system");
        }
        return RespData.success();
    }

    /**
     * 更具设计师ID获取设计信息
     *
     * @param designerId
     * @return
     */
    @Override
    public PageVo<List<DesignOrderVo>> getDesignOrderData(String designerId, String ownerMsg, String projectNo, List<Integer> states, int pageIndex, int pageSize) {
        if(pageIndex <= 0){
            pageIndex = 1;
        }
        if (designerId == null || designerId.trim().isEmpty()) {
            return PageVo.def(new ArrayList<>());
        }
        List<String> projectNos = null;
        if(StringUtils.isNotBlank(ownerMsg)){
            List<UserMsgVo> userMsgVos = userCenterService.queryUserMsg(ownerMsg);
            if(userMsgVos == null || userMsgVos.isEmpty()){
                return PageVo.def(new ArrayList<>());
            }
            List<String> ownerIds = ReflectUtils.getList(userMsgVos,"consumerId");
            if(ownerIds == null || ownerIds.isEmpty()){
                return PageVo.def(new ArrayList<>());
            }
            OrderUserExample orderUserExample = new OrderUserExample();
            orderUserExample.createCriteria().andUserIdIn(ownerIds).andRoleCodeEqualTo("CC");
            List<OrderUser> orderUsers = orderUserMapper.selectByExample(orderUserExample);
            projectNos = ReflectUtils.getList(orderUsers,"projectNo");
        }
        if(StringUtils.isNotBlank(projectNo)){
            if(projectNos == null){
                projectNos = new ArrayList<>();
            }
            projectNos.add(projectNo);
        }
        if(states != null && states.isEmpty()){
            states = null;
        }
        long total = designerOrderMapper.countByDesignerId(designerId, ProjectDataStatus.BASE_STATUS.getValue(), projectNos, states);
        List<DesignOrderVo> designOrderVos = designerOrderMapper.selectByDesignerId(designerId, ProjectDataStatus.BASE_STATUS.getValue(), projectNos, states, pageIndex - 1, pageSize);
        if(designOrderVos == null || designOrderVos.isEmpty()){
            return PageVo.def(new ArrayList<>());
        }
        Map<String,String> provinceMap = basicsService.getProvince(ReflectUtils.getList(designOrderVos,"province").toArray(new String[]{}));
        Map<String,String> cityMap = basicsService.getCity(ReflectUtils.getList(designOrderVos,"city").toArray(new String[]{}));
        Map<String,String> areaMap = basicsService.getArea(ReflectUtils.getList(designOrderVos,"region").toArray(new String[]{}));
        projectNos = ReflectUtils.getList(designOrderVos,"projectNo");
        Map<String, UserMsgVo> userMsgVoMap = getStringUserMsgVoMap(projectNos);
        for (DesignOrderVo designOrderVo : designOrderVos) {
            designOrderVo.setProjectStage(DesignStateEnum.queryByState(designOrderVo.getStage()).getStateName(3));
            designOrderVo.setProvinceName(provinceMap.get(designOrderVo.getProvince()));
            designOrderVo.setCityName(cityMap.get(designOrderVo.getCity()));
            designOrderVo.setAreaName(areaMap.get(designOrderVo.getRegion()));
            UserMsgVo userMsgVo = userMsgVoMap.get(designOrderVo.getProjectNo());
            if(userMsgVo != null){
                designOrderVo.setOwnerId(userMsgVo.getConsumerId());
                designOrderVo.setOwnerName(userMsgVo.getUserName());
                designOrderVo.setOwnerPhone(userMsgVo.getUserPhone());
            }
        }
        PageVo<List<DesignOrderVo>> pageVo = PageVo.def(designOrderVos);
        pageVo.setTotal(total);
        pageVo.setPageIndex(pageIndex);
        pageVo.setPageSize(pageSize);
        return pageVo;
    }

    private Map<String, UserMsgVo> getStringUserMsgVoMap(List<String> projectNos) {
        OrderUserExample orderUserExample = new OrderUserExample();
        orderUserExample.createCriteria().andProjectNoIn(projectNos).andRoleCodeEqualTo("CC");
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(orderUserExample);
        List<String> ownerIds = ReflectUtils.getList(orderUsers,"userId");
        List<UserMsgVo> userMsgVos = userCenterService.queryUsers(ownerIds);
        Map<String, UserMsgVo> userMsgVoMap = ReflectUtils.listToMap(userMsgVos,"consumerId");
        Map<String, UserMsgVo> proAndUserMsgMap = new HashMap<>();
        for(OrderUser orderUser : orderUsers){
            UserMsgVo userMsgVo = userMsgVoMap.get(orderUser.getUserId());
            proAndUserMsgMap.put(orderUser.getProjectNo(), userMsgVo);
        }
        return proAndUserMsgMap;
    }

    /**
     * 根据项目编号获取地址信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public String getProjectAdress(String projectNo) {
        StringBuilder builder = new StringBuilder();
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria.andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(example);
        if (projects.size() == 0) {
            return "";
        }
        Project project = projects.get(0);
        //查询省份
        if (project.getProvince() != null && !project.getProvince().trim().isEmpty()) {
            ProvinceExample provinceExample = new ProvinceExample();
            ProvinceExample.Criteria provinceCriteria = provinceExample.createCriteria();
            provinceCriteria.andProvinceCodeEqualTo(project.getProvince());
            List<Province> provinces = provinceMapper.selectByExample(provinceExample);
            if (provinces.size() > 0) {
                builder.append(provinces.get(0).getProvinceName());
            }
        }
        //查询城市
        if (project.getCity() != null && !project.getCity().trim().isEmpty()) {
            CityExample ciytExample = new CityExample();
            CityExample.Criteria cityCriteria = ciytExample.createCriteria();
            cityCriteria.andCityCodeEqualTo(project.getCity());
            List<City> cities = cityMapper.selectByExample(ciytExample);
            if (cities.size() > 0) {
                builder.append(cities.get(0).getCityName());
            }
        }
        //查询区域
        if (project.getRegion() != null && !project.getRegion().trim().isEmpty()) {
            AreaExample areaExample = new AreaExample();
            AreaExample.Criteria areaCriteria = areaExample.createCriteria();
            areaCriteria.andAreaCodeEqualTo(project.getRegion());
            List<Area> areas = areaMapper.selectByExample(areaExample);
            if (areas.size() > 0) {
                builder.append(areas.get(0).getAreaName());
            }
        }
        builder.append(project.getAddressDetail());
        return builder.toString();

    }

    /**
     * 获取跑马灯信息
     *
     * @param projectNo
     * @param orderStage
     * @param userId
     * @return
     */
    public String getMessage(String projectNo, Integer orderStage, String userId) {
        if (projectNo == null || projectNo.trim().isEmpty()) {
            return "";
        }
        if (orderStage == null) {
            return "";
        }
        if (userId == null || userId.trim().isEmpty()) {
            return "";
        }
        OrderUserExample userExample = new OrderUserExample();
        OrderUserExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andUserIdEqualTo(userId);
        userCriteria.andProjectNoEqualTo(projectNo);
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(userExample);
        if (orderUsers.size() == 0) {
            return "";
        }
        ProjectMessageExample example = new ProjectMessageExample();
        ProjectMessageExample.Criteria criteria = example.createCriteria();
        criteria.andProjectStageEqualTo(orderStage);
        criteria.andRoleCodeEqualTo(orderUsers.get(0).getRoleCode());
        criteria.andIsSendEqualTo(1);
        List<ProjectMessage> projectMessages = projectMessageMapper.selectByExample(example);
        if (projectMessages.size() == 0) {
            return "";
        }
        return projectMessages.get(0).getContent();
    }

}
