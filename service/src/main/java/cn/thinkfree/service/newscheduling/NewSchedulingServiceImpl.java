package cn.thinkfree.service.newscheduling;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ProjectBigSchedulingDetailsVO;
import cn.thinkfree.service.approvalflow.AfInstanceService;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.utils.BaseToVoUtils;
import cn.thinkfree.service.utils.DateUtil;
import cn.thinkfree.service.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 正常排期操作
 *
 * @author gejiaming
 * @Date 2018-09-30
 */
@Service(value = "schedulingService")
public class NewSchedulingServiceImpl implements NewSchedulingService {
    @Autowired
    ProjectBigSchedulingDetailsMapper projectBigSchedulingDetailsMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;
    @Autowired
    ProjectSchedulingMapper projectSchedulingMapper;
    @Autowired
    NewOrderUserService newOrderUserService;
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    private AfInstanceService instanceService;
    @Autowired
    OrderContractMapper orderContractMapper;


    /**
     * 根据项目编号生成排期
     *
     * @param orderNo
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public MyRespBundle createScheduling(String orderNo) {
        if (orderNo == null || orderNo.isEmpty()) {
            return RespData.error("请检查入参");
        }
        ConstructionOrderExample constructionExample = new ConstructionOrderExample();
        ConstructionOrderExample.Criteria constructionCriteria = constructionExample.createCriteria();
        constructionCriteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        constructionCriteria.andOrderNoEqualTo(orderNo);
        List<ConstructionOrder> constructionOrders = constructionOrderMapper.selectByExample(constructionExample);
        if (constructionOrders.size() == ProjectDataStatus.INSERT_FAILD.getValue()) {
            return RespData.error("此项目还未生成施工订单");
        }
        ConstructionOrder constructionOrder = constructionOrders.get(0);
        if (constructionOrder.getProjectNo() == null || constructionOrder.getProjectNo().isEmpty()) {
            return RespData.error("此施工订单无关联的项目");
        }
        String projectNo = constructionOrder.getProjectNo();
        //获取合同开始结束时间
        OrderContractExample contractExample = new OrderContractExample();
        OrderContractExample.Criteria contractCriteria = contractExample.createCriteria();
        contractCriteria.andOrderNumberEqualTo(constructionOrder.getOrderNo());
        List<OrderContract> orderContracts = orderContractMapper.selectByExample(contractExample);
        if (orderContracts.size() == 0) {
            return RespData.error("合同暂无信息");
//            throw new RuntimeException("合同暂无信息");
        }
        if (orderContracts.get(0).getStartTime() == null || orderContracts.get(0).getEndTime() == null) {
            return RespData.error("合同没有开始/结束时间");
        }
        //获取项目信息
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<Project> projects = projectMapper.selectByExample(example);
        if (projects.size() == ProjectDataStatus.INSERT_FAILD.getValue()) {
            return RespData.error("项目不存在");
        }
        Project project = projects.get(0);
        if (project.getHouseType() == null || project.getHouseRoom() == null || project.getArea() == null || orderContracts.get(0).getStartTime() == null || orderContracts.get(0).getEndTime() == null) {
            return RespData.error("请检查参数:project.houseType=" + project.getHouseType() + ";project.houseRoom=" + project.getHouseRoom() + ";project.area=" + project.getArea() + ";orderContracts.getStartTime()=" + orderContracts.get(0).getStartTime() + ";orderContract.getEndTime()=" + orderContracts.get(0).getEndTime());
        }
        //获取排期基础数据
        if (constructionOrder.getSchemeNo() == null || constructionOrder.getSchemeNo().isEmpty()) {
            return RespData.error("施工订单的schemeNo 字段值为null/空字符串");
        }
        ProjectBigSchedulingExample example1 = new ProjectBigSchedulingExample();
        ProjectBigSchedulingExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria1.andSchemeNoEqualTo(constructionOrder.getSchemeNo());
        //暂时不用房间+面积+新旧做匹配
//        criteria1.andIsNewEqualTo(project.getHouseType());
//        criteria1.andRoomNumEqualTo(project.getHouseRoom());
//        criteria1.andSquareMetreStartLessThanOrEqualTo(project.getArea());
//        criteria1.andSquareMetreEndGreaterThanOrEqualTo(project.getArea());
        List<ProjectBigScheduling> projectBigSchedulings = projectBigSchedulingMapper.selectByExample(example1);
        if (projectBigSchedulings.size() == ProjectDataStatus.INSERT_FAILD.getValue()) {
            return RespData.error("此方案尚未添加施工阶段信息!");
        }
        Date bigStartTime = orderContracts.get(0).getStartTime();
        Collections.sort(projectBigSchedulings);
        //生成总排期信息
        ProjectScheduling projectScheduling = new ProjectScheduling();
        projectScheduling.setDelay(0);
        projectScheduling.setChangeNum(0);
        projectScheduling.setIsConfirm(0);
        projectScheduling.setTaskNum(5);
        projectScheduling.setStatus(Scheduling.BASE_STATUS.getValue());
        projectScheduling.setRate(projectBigSchedulings.get(0).getSort());
        projectScheduling.setProjectNo(projectNo);
        projectScheduling.setStartTime(orderContracts.get(0).getStartTime());
        projectScheduling.setEndTime(orderContracts.get(0).getEndTime());
        projectScheduling.setCompanyId(constructionOrder.getCompanyId());
        int bigResult = projectSchedulingMapper.insertSelective(projectScheduling);
        if (bigResult != Scheduling.INSERT_SUCCESS.getValue()) {
            return RespData.error("生成排期失败!!");
        }
        //生成排期
        for (int i = 0; i < projectBigSchedulings.size(); i++) {
            ProjectBigScheduling bigScheduling = projectBigSchedulings.get(i);
            ProjectBigSchedulingDetails details = new ProjectBigSchedulingDetails();
            ProjectBigSchedulingDetailsExample bigExample = new ProjectBigSchedulingDetailsExample();
            ProjectBigSchedulingDetailsExample.Criteria bigCriteria = bigExample.createCriteria();
            bigCriteria.andSchemeNoEqualTo(bigScheduling.getSchemeNo());
            bigCriteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
            bigCriteria.andProjectNoEqualTo(projectNo);
            bigCriteria.andBigSortEqualTo(bigScheduling.getSort());
            List<ProjectBigSchedulingDetails> projectBigSchedulingDetails = projectBigSchedulingDetailsMapper.selectByExample(bigExample);
            if (projectBigSchedulingDetails.size() != Scheduling.INSERT_FAILD.getValue()) {
                return RespData.error("排期详情已存在!");
            }
            details.setProjectNo(projectNo);
            details.setBigSort(bigScheduling.getSort());
            details.setBigName(bigScheduling.getName());
            details.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
            details.setCreateTime(new Date());
            details.setIsCompleted(Scheduling.COMPLETED_NO.getValue());
            details.setIsAdd(Scheduling.ADD_NO.getValue());
            details.setIsChange(Scheduling.CHANGE_NUM.getValue());
            details.setSubmitNum(Scheduling.SUBMIT_NUM.getValue());
            details.setIsNeedCheck(bigScheduling.getIsNeedCheck());
            details.setIsAdopt(Scheduling.CHECK_NO.getValue());
            details.setVersion(bigScheduling.getVersion());
            details.setRenameBig(bigScheduling.getRename());
            details.setSchemeNo(bigScheduling.getSchemeNo());
            details.setPlanStartTime(bigStartTime);
            Date planEndTime = DateUtil.getDate(bigStartTime, bigScheduling.getWorkload());
            if (orderContracts.get(0).getEndTime().getTime() < planEndTime.getTime()) {
                details.setPlanEndTime(orderContracts.get(0).getEndTime());
                bigStartTime = orderContracts.get(0).getEndTime();
            } else {
                if (i == projectBigSchedulings.size() - 1) {
                    details.setPlanEndTime(orderContracts.get(0).getEndTime());
                } else {
                    details.setPlanEndTime(planEndTime);
                    bigStartTime = planEndTime;
                }
            }
            details.setIsMatching(Scheduling.MATHCHING_NO.getValue());
            int result = 0;
            try {
                result = projectBigSchedulingDetailsMapper.insertSelective(details);
            } catch (Exception e) {
                e.printStackTrace();
                return RespData.error("插入失败!");
            }
            if (result != Scheduling.INSERT_SUCCESS.getValue()) {
                return RespData.error("生成排期失败!!");
            }
        }
        return RespData.success();
    }


    /**
     * 获取排期信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getScheduling(String projectNo) {
        List<ProjectBigSchedulingDetails> bigList = projectBigSchedulingDetailsMapper.selectByProjectNo(projectNo, Scheduling.BASE_STATUS.getValue());
        List<ProjectBigSchedulingDetailsVO> playBigList = BaseToVoUtils.getListVo(bigList, ProjectBigSchedulingDetailsVO.class);
        //组合延期天数
        for (ProjectBigSchedulingDetailsVO bigSchedulingVO : playBigList) {
            bigSchedulingVO.setDelay(DateUtil.differentHoursByMillisecond(bigSchedulingVO.getPlanEndTime(), bigSchedulingVO.getActualEndTime()));
            //添加进度信息
            if (bigSchedulingVO.getIsCompleted() == 1) {
                bigSchedulingVO.setPercentage(100);
            } else {
                bigSchedulingVO.setPercentage(MathUtil.getPercentage(bigSchedulingVO.getPlanStartTime(), bigSchedulingVO.getPlanEndTime(), new Date()));
                if (bigSchedulingVO.getPercentage() == 100) {
                    bigSchedulingVO.setPercentage(99);
                }
            }
            if (bigSchedulingVO.getActualStartTime()==null){
                bigSchedulingVO.setPercentage(0);
            }
        }
        return RespData.success(playBigList);
    }

    /**
     * 添加公司施工节点
     *
     * @param projectBigSchedulingDetailsVO
     * @return
     */
    @Override
    public String saveProjectScheduling(ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO) {
        ProjectBigSchedulingDetails projectBigSchedulingDetails = BaseToVoUtils.getVo(projectBigSchedulingDetailsVO, ProjectBigSchedulingDetails.class);
        projectBigSchedulingDetails.setStatus(Scheduling.BASE_STATUS.getValue());
        projectBigSchedulingDetails.setCreateTime(new Date());
        int result = projectBigSchedulingDetailsMapper.insertSelective(projectBigSchedulingDetails);
        if (result != Scheduling.INSERT_SUCCESS.getValue()) {
            return Scheduling.INSERT_FAILD.getDescription();
        }
        return Scheduling.INSERT_SUCCESS.getDescription();
    }

    /**
     * 删除公司施工节点
     *
     * @param projectBigSchedulingDetailsVO
     * @return
     */
    @Override
    public String deleteProjectScheduling(ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO) {
        ProjectBigSchedulingDetails projectBigSchedulingDetails = BaseToVoUtils.getVo(projectBigSchedulingDetailsVO, ProjectBigSchedulingDetails.class);
        projectBigSchedulingDetails.setStatus(Scheduling.INVALID_STATUS.getValue());
        ProjectBigSchedulingDetailsExample example = new ProjectBigSchedulingDetailsExample();
        ProjectBigSchedulingDetailsExample.Criteria criteria = example.createCriteria();
        criteria.andBigSortEqualTo(projectBigSchedulingDetailsVO.getBigSort());
        criteria.andProjectNoEqualTo(projectBigSchedulingDetailsVO.getProjectNo());
        int result = projectBigSchedulingDetailsMapper.updateByExampleSelective(projectBigSchedulingDetails, example);
        if (result != Scheduling.INSERT_SUCCESS.getValue()) {
            return Scheduling.INSERT_FAILD.getDescription();
        }
        return Scheduling.INSERT_SUCCESS.getDescription();
    }

    /**
     * 获取验收阶段
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getCheckStage(String projectNo) {
        ProjectBigSchedulingDetailsExample example = new ProjectBigSchedulingDetailsExample();
        example.setOrderByClause("big_sort asc");
        ProjectBigSchedulingDetailsExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        List<ProjectBigSchedulingDetails> bigList = projectBigSchedulingDetailsMapper.selectByExample(example);
        if (bigList.size() == 0) {
            return RespData.error("此项目下无排期信息");
        }
        List<ProjectBigSchedulingDetailsVO> playBigList = BaseToVoUtils.getListVo(bigList, ProjectBigSchedulingDetailsVO.class);
        ProjectBigSchedulingDetailsVO otherVo = createStartReportSchedulingDetail(projectNo);
        playBigList.add(otherVo);
        Collections.sort(playBigList);
        return RespData.success(playBigList);
    }

    private ProjectBigSchedulingDetailsVO createStartReportSchedulingDetail(String projectNo) {
        ProjectBigSchedulingDetailsVO schedulingDetailsVO = new ProjectBigSchedulingDetailsVO();
        schedulingDetailsVO.setBigName("开工报告");
        schedulingDetailsVO.setBigSort(0);
        boolean succeed = instanceService.getStartReportSucceed(projectNo);
        if (succeed) {
            schedulingDetailsVO.setIsCompleted(1);
        } else {
            schedulingDetailsVO.setIsCompleted(0);
        }
        schedulingDetailsVO.setPlanEndTime(new Date(0));
        return schedulingDetailsVO;
    }

    /**
     * 确认排期
     *
     * @param bigList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle confirmProjectScheduling(List<ProjectBigSchedulingDetailsVO> bigList) {
        if (bigList.isEmpty()) {
            return RespData.success("暂无修改");
        }
        String projectNo = bigList.get(0).getProjectNo();
        if (!newOrderUserService.checkJurisdiction(projectNo, bigList.get(0).getUserId(), UserJobs.Foreman.roleCode)) {
            return RespData.error("此操作者没有此项目编辑排期的权限!");
        }
        ProjectBigSchedulingDetailsExample bigExample = new ProjectBigSchedulingDetailsExample();
        ProjectBigSchedulingDetailsExample.Criteria bigCriteria = bigExample.createCriteria();
        bigCriteria.andProjectNoEqualTo(projectNo);
        bigCriteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        List<ProjectBigSchedulingDetails> projectBigSchedulingDetailsList = projectBigSchedulingDetailsMapper.selectByExample(bigExample);
        if (projectBigSchedulingDetailsList.size() == 0) {
            return RespData.error("此项目不存在有效排期");
        }
        String schemeNo = projectBigSchedulingDetailsList.get(0).getSchemeNo();
        if (schemeNo == null || schemeNo.isEmpty()) {
            return RespData.error("此排期不存在方案编号");
        }
        //将原数据置为失效
        Integer i = projectBigSchedulingDetailsMapper.updateByProjectNo(projectNo, Scheduling.INVALID_STATUS.getValue());
        if (i == 0) {
            return RespData.error("确认排期失败,原因:原数据失效失败!");
        }


        for (ProjectBigSchedulingDetailsVO detailsVO : bigList) {
            ProjectBigSchedulingDetails projectBigSchedulingDetails = BaseToVoUtils.getVo(detailsVO, ProjectBigSchedulingDetails.class);
            projectBigSchedulingDetails.setStatus(Scheduling.BASE_STATUS.getValue());
            projectBigSchedulingDetails.setCreateTime(new Date());
            projectBigSchedulingDetails.setSchemeNo(schemeNo);
            int result = projectBigSchedulingDetailsMapper.insertSelective(projectBigSchedulingDetails);
            if (result != Scheduling.INSERT_SUCCESS.getValue()) {
                return RespData.error("插入失败!!");
            }
        }
        return RespData.success();
    }

    /**
     * 大阶段完成,添加大阶段完成时间
     *
     * @param projectNo
     * @param bigSort
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String completeBigScheduling(String projectNo, Integer bigSort) {
        ProjectBigSchedulingDetails bigSchedulingDetail = null;
        ProjectBigSchedulingDetails bigSchedulingDetails = new ProjectBigSchedulingDetails();
        bigSchedulingDetails.setIsCompleted(Scheduling.COMPLETED_YES.getValue());
        bigSchedulingDetails.setActualEndTime(new Date());
        ProjectBigSchedulingDetailsExample detailsExample = new ProjectBigSchedulingDetailsExample();
        ProjectBigSchedulingDetailsExample.Criteria detailsCriteria = detailsExample.createCriteria();
        detailsCriteria.andProjectNoEqualTo(projectNo);
        detailsCriteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        detailsExample.setOrderByClause("big_sort asc");
        List<ProjectBigSchedulingDetails> allBigDetails = projectBigSchedulingDetailsMapper.selectByExample(detailsExample);
        for (ProjectBigSchedulingDetails schedulingDetails : allBigDetails) {
            if (schedulingDetails.getBigSort().equals(bigSort)) {
                bigSchedulingDetail = schedulingDetails;
            }
            if (schedulingDetails.getBigSort() > bigSort) {
                ProjectBigSchedulingDetails nextBig = new ProjectBigSchedulingDetails();
                nextBig.setActualStartTime(new Date());
                detailsCriteria.andBigSortEqualTo(schedulingDetails.getBigSort());
                int nextResult = projectBigSchedulingDetailsMapper.updateByExampleSelective(nextBig, detailsExample);
                if (nextResult == 0) {
                    throw new RuntimeException("修改排期下个阶段开始时间失败!");
                }
                break;
            }
        }
        ProjectBigSchedulingDetailsExample detailsExampleTwo = new ProjectBigSchedulingDetailsExample();
        ProjectBigSchedulingDetailsExample.Criteria detailsCriteriaTwo = detailsExampleTwo.createCriteria();
        detailsCriteriaTwo.andProjectNoEqualTo(projectNo);
        detailsCriteriaTwo.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        detailsCriteriaTwo.andBigSortEqualTo(bigSort);
        int i = projectBigSchedulingDetailsMapper.updateByExampleSelective(bigSchedulingDetails, detailsExampleTwo);
        if (i == 0) {
            throw new RuntimeException("修改排期完成时间失败!");
        }

        ProjectScheduling projectScheduling = new ProjectScheduling();
        ProjectSchedulingExample schedulingExample = new ProjectSchedulingExample();
        ProjectSchedulingExample.Criteria schedulingCriteria = schedulingExample.createCriteria();
        schedulingCriteria.andProjectNoEqualTo(projectNo);
        schedulingCriteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        if (System.currentTimeMillis() > bigSchedulingDetail.getPlanEndTime().getTime()) {
            //修改延期时间
            projectScheduling.setDelay(DateUtil.differentHoursByMillisecond(bigSchedulingDetail.getPlanEndTime(), new Date()));
        }
        projectScheduling.setRate(bigSchedulingDetail.getBigSort() + 1);
        int result = projectSchedulingMapper.updateByExampleSelective(projectScheduling, schedulingExample);
        if (result == 0) {
            throw new RuntimeException("修改延期时间失败!");
        }
        return "执行成功!!";
    }

    /**
     * 开工申请
     *
     * @param projectNo
     * @param bigSort
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String projectStart(String projectNo, Integer bigSort) {
        ProjectBigSchedulingDetails bigSchedulingDetails = new ProjectBigSchedulingDetails();
        bigSchedulingDetails.setActualStartTime(new Date());
        ProjectBigSchedulingDetailsExample detailsExample = new ProjectBigSchedulingDetailsExample();
        ProjectBigSchedulingDetailsExample.Criteria detailsCriteria = detailsExample.createCriteria();
        detailsCriteria.andProjectNoEqualTo(projectNo);
        detailsCriteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        detailsCriteria.andBigSortEqualTo(bigSort);
        int i = projectBigSchedulingDetailsMapper.updateByExampleSelective(bigSchedulingDetails, detailsExample);
        if (i == 0) {
            return "修改排期实际开始时间失败!";
        }
        ProjectScheduling projectScheduling = new ProjectScheduling();
        projectScheduling.setIsConfirm(Scheduling.COMPLETED_YES.getValue());
        ProjectSchedulingExample schedulingExample = new ProjectSchedulingExample();
        ProjectSchedulingExample.Criteria schedulingCriteria = schedulingExample.createCriteria();
        schedulingCriteria.andProjectNoEqualTo(projectNo);
        schedulingCriteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        schedulingCriteria.andRateEqualTo(1);
        int result = projectSchedulingMapper.updateByExampleSelective(projectScheduling, schedulingExample);
        if (result == 0) {
            return "修改排期实际开始时间失败!";
        }
        return "修改成功";
    }

    /**
     * 提供PC合同处获取验收阶段
     *
     * @param orderNo
     * @param type
     * @return
     */
    @Override
    public MyRespBundle<List<String>> getPcCheckStage(String orderNo, Integer type) {
        List<String> stageList = new ArrayList<>();
        if (type.equals(Scheduling.INVALID_STATUS.getValue())) {
            stageList = projectBigSchedulingDetailsMapper.selectConstructionByOrderNo(orderNo);
        }
        if (type.equals(Scheduling.BASE_STATUS.getValue())) {
            stageList = projectBigSchedulingDetailsMapper.selectDesignerByOrderNo(orderNo);
        }
        if (stageList.size() == Scheduling.MATHCHING_NO.getValue()) {
            return RespData.error("此订单号未有与之匹配的验收信息!");
        }
        return RespData.success(stageList);
    }

    @Override
    public ProjectBigScheduling findBySchemeNoAndScheduleSort(String schemeNo, Integer scheduleSort) {
        ProjectBigSchedulingExample example = new ProjectBigSchedulingExample();
        example.createCriteria().andSchemeNoEqualTo(schemeNo).andSortEqualTo(scheduleSort);
        List<ProjectBigScheduling> projectBigSchedulings = projectBigSchedulingMapper.selectByExample(example);
        return projectBigSchedulings != null && projectBigSchedulings.size() > 0 ? projectBigSchedulings.get(0) : null;
    }

    /**
     * 获取项目总排期信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ProjectScheduling> getProjectScheduling(String projectNo) {
        if (projectNo == null || projectNo.trim().isEmpty()) {
            return RespData.error("请检查参数:projectNo=" + projectNo);
        }
        ProjectSchedulingExample example = new ProjectSchedulingExample();
        ProjectSchedulingExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        List<ProjectScheduling> projectSchedulings = projectSchedulingMapper.selectByExample(example);
        if (projectSchedulings.size() == 0) {
            return RespData.error("项目不存在");
        }
        return RespData.success(projectSchedulings.get(0));
    }

    /**
     * 修改延期天数(延期单审批通过后调用)
     *
     * @param projectNo
     * @param delay
     * @return
     */
    @Override
    public MyRespBundle editProjectDelay(String projectNo, Integer delay) {
        ProjectSchedulingExample example = new ProjectSchedulingExample();
        ProjectSchedulingExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        List<ProjectScheduling> projectSchedulings = projectSchedulingMapper.selectByExample(example);
        if (projectSchedulings.size() == 0) {
            return RespData.error("项目不存在");
        }
        ProjectScheduling projectScheduling = new ProjectScheduling();
        if (projectSchedulings.get(0).getDelay() != null) {
            projectScheduling.setDelay(projectSchedulings.get(0).getDelay() - delay);
        }
        int i = projectSchedulingMapper.updateByExampleSelective(projectScheduling, example);
        if (i == 0) {
            return RespData.error("修改失败");
        }
        return RespData.success();
    }
}
