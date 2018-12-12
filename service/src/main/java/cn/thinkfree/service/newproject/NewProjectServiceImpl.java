package cn.thinkfree.service.newproject;

import cn.thinkfree.core.base.ErrorCode;
import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.BasicsDataParentEnum;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.core.constants.RoleFunctionEnum;
import cn.thinkfree.database.appvo.*;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.service.approvalflow.AfInstanceService;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.neworder.NewOrderService;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.designer.UserCenterService;
import cn.thinkfree.service.platform.employee.ProjectUserService;
import cn.thinkfree.service.platform.vo.UserMsgVo;
import cn.thinkfree.service.remote.CloudService;
import cn.thinkfree.service.utils.BaseToVoUtils;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.MathUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
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
    OrderUserMapper orderUserMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    NewOrderService newOrderService;
    @Autowired
    ProjectDataMapper projectDataMapper;
    @Autowired
    DesignerOrderMapper designerOrderMapper;
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    OrderApplyRefundMapper orderApplyRefundMapper;
    @Autowired
    NewOrderUserService newOrderUserService;
    @Autowired
    ProjectStageLogMapper projectStageLogMapper;
    @Autowired
    DesignDispatchService designDispatchService;
    @Autowired
    CloudService cloudService;
    @Autowired
    ConstructionStateService constructionStateService;
    @Autowired
    ProjectUserService projectUserService;
    @Autowired
    UserCenterService userCenterService;
    @Autowired
    BasicsDataMapper basicsDataMapper;
    @Autowired
    AfInstanceService afInstanceService;
    @Autowired
    ProvinceMapper provinceMapper;
    @Autowired
    CityMapper cityMapper;
    @Autowired
    AreaMapper areaMapper;


    /**
     * 项目列表
     *
     * @param appProjectSEO
     * @return
     */
    @Override
    public MyRespBundle<PageInfo<ProjectVo>> getAllProject(AppProjectSEO appProjectSEO) {
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
            if (userRoleCode.equals(UserJobs.Designer.roleCode) && project.getStage().equals(DesignStateEnum.STATE_20.getState())) {
                projectVo.setAgreeButto(true);
                projectVo.setRefuseButton(true);
            } else {
                projectVo.setAgreeButto(false);
                projectVo.setRefuseButton(false);
            }
            projectVo.setAddress(getProjectAdress(project.getProjectNo()));
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
            projectVo.setOwner(owner);
            //添加进度展示
            if (project.getStage() >= ConstructionStateEnum.STATE_500.getState()) {
                projectVo.setProgressIsShow(true);
                //添加进度信息
                projectVo.setConstructionProgress(MathUtil.getPercentage(project.getPlanStartTime(), project.getPlanEndTime(), new Date()));
                projectVo.setStageConsumerName(ConstructionStateEnum.queryByState(project.getStage()).getStateName(4));
                projectVo.setStageDesignName(ConstructionStateEnum.queryByState(project.getStage()).getStateName(3));
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
        //添加进度展示
        if (project.getStage() >= ConstructionStateEnum.STATE_500.getState()) {
            projectVo.setProgressIsShow(true);
            //添加进度信息
            projectVo.setConstructionProgress(MathUtil.getPercentage(project.getPlanStartTime(), project.getPlanEndTime(), new Date()));
            projectVo.setStageDesignName(ConstructionStateEnum.queryByState(project.getStage()).getStateName(3));
            projectVo.setStageConsumerName(ConstructionStateEnum.queryByState(project.getStage()).getStateName(4));
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
        designerOrderDetailVo.setTaskStage(projects.get(0).getStage());
        designerOrderDetailVo.setPlayTask(designDispatchService.showBtn(designerOrder.getOrderNo(), userId));
        List<DesignStateEnum> allCancelState = DesignStateEnum.getAllCancelState();
        for (DesignStateEnum designStateEnum : allCancelState) {
            if (project.getStage().equals(designStateEnum.getState())) {
                designerOrderDetailVo.setCancle(true);
            } else {
                designerOrderDetailVo.setCancle(false);
            }
        }
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
            PersionVo persionVo = employeeMsgMapper.selectByUserId(designerId);
            try {
                UserMsgVo userMsgVo = userCenterService.queryUser(designerId);
                persionVo.setPhone(userMsgVo.getUserPhone());
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
            List<Map<String, Object>> maps1 = ConstructionStateEnum.allStates(ProjectDataStatus.PLAY_CONSUMER.getValue());
            for (Map<String, Object> map : maps1) {
                OrderTaskSortVo orderTaskSortVo = new OrderTaskSortVo();
                orderTaskSortVo.setSort((Integer) map.get("key"));
                orderTaskSortVo.setName(map.get("val").toString());
                orderTaskSortVoList1.add(orderTaskSortVo);
            }
            constructionOrderDetailVo.setOrderTaskSortVoList(orderTaskSortVoList1);
            constructionOrderDetailVo.setTaskStage(projects.get(0).getStage());
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
            if (constructionOrderPlayVo == null) {
                constructionOrderPlayVo = new OrderPlayVo();
            }
            constructionOrderPlayVo.setSchedule(DateUtil.daysCalculate(projects.get(0).getPlanStartTime(), projects.get(0).getPlanEndTime()));
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
        projectTitleVo.setProjectStartTime(project.getPlanStartTime());
        projectTitleVo.setProjectEndTime(project.getPlanEndTime());
        projectTitleVo.setAddress(getProjectAdress(projectNo));
        projectTitleVo.setProjectNo(projectNo);
        List<OrderPlayVo> orderPlayVos = constructionOrderMapper.selectByProjectNoAndStatus(projectNo, ProjectDataStatus.BASE_STATUS.getValue());
        if (orderPlayVos.size() != 0) {
            OrderPlayVo orderPlayVo = orderPlayVos.get(0);
            if (orderPlayVo != null) {
                projectTitleVo.setCost(orderPlayVo.getCost());
                projectTitleVo.setDelay(orderPlayVo.getDelay());
                projectTitleVo.setSchedule(DateUtil.differentHoursByMillisecond(project.getPlanStartTime(), project.getPlanEndTime()));
                projectTitleVo.setTaskNum(orderPlayVo.getTaskNum());
            }
            //添加进度展示
            projectTitleVo.setConstructionProgress(MathUtil.getPercentage(project.getPlanStartTime(), project.getPlanEndTime(), new Date()));
        }
        int confirm;
        try {
            confirm = afInstanceService.getProjectCheckResult(projectNo);
        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error("获取排期编辑状态失败");
        }
        projectTitleVo.setIsConfirm(confirm);
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
        if (dataVo.getUserId().isEmpty()) {
            return RespData.error("请给userid赋值");
        }
        if (dataVo.getHsDesignId() == null || dataVo.getHsDesignId().trim().isEmpty()) {
            return RespData.error("hsDesignId 不可为空");
        }
        EmployeeMsgExample example = new EmployeeMsgExample();
        EmployeeMsgExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(dataVo.getUserId());
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(example);
        if (employeeMsgs.size() == 0) {
            return RespData.error("查无此设计师!");
        }
        if (dataVo.getType() == null) {
            return RespData.error("请选择资料类型");
        }
        for (UrlDetailVo urlDetailVo : dataVo.getDataList()) {
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
                return RespData.error("确认失败!");
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
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> confirmVolumeRoomDataUser(String projectNo, Integer category) {
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
        return RespData.success();
    }

    /**
     * 更具设计师ID获取设计信息
     *
     * @param designerId
     * @return
     */
    @Override
    public MyRespBundle<List<DesignOrderVo>> getDesignOrderData(String designerId) {
        if (designerId == null || designerId.trim().isEmpty()) {
            return RespData.error("设计师Id不可为空");
        }
        List<DesignOrderVo> designOrderVos = designerOrderMapper.selectByDesignerId(designerId, ProjectDataStatus.BASE_STATUS.getValue());
        for (DesignOrderVo designOrderVo : designOrderVos) {
            designOrderVo.setProjectStage(DesignStateEnum.queryByState(designOrderVo.getStage()).getStateName(3));
        }
        return RespData.success(designOrderVos);
    }

    /**
     * 根据项目编号获取地址信息
     *
     * @param projectNo
     * @return
     */
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

}
