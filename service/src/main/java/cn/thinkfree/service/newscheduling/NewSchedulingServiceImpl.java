package cn.thinkfree.service.newscheduling;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.appvo.ProjectOrderDetailVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ProjectBigSchedulingDetailsVO;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.utils.BaseToVoUtils;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.thinkfree.service.utils.DateUtil;

import java.util.*;

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
        //获取报价基础信息
//        Set<Integer> bigSortSet = projectQuotationLogMapper.selectByProjectNo(projectNo);
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
        if (project.getHouseType() == null || project.getHouseRoom() == null || project.getArea() == null || project.getPlanStartTime() == null || project.getPlanEndTime() == null) {
            return RespData.error("请检查参数:project.houseType=" + project.getHouseType() + ";project.houseRoom=" + project.getHouseRoom() + ";project.area=" + project.getArea() + ";project.planStartTime=" + project.getPlanStartTime() + ";project.planEndTime=" + project.getPlanEndTime());
        }

        //获取排期基础数据
        if (constructionOrder.getSchemeNo() == null || constructionOrder.getSchemeNo().isEmpty()) {
            return RespData.error("施工订单的schemeNo 字段值为null/空字符串");
        }
        ProjectBigSchedulingExample example1 = new ProjectBigSchedulingExample();
        ProjectBigSchedulingExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria1.andSchemeNoEqualTo(constructionOrder.getSchemeNo());
        criteria1.andIsNewEqualTo(project.getHouseType());
        criteria1.andRoomNumEqualTo(project.getHouseRoom());
        criteria1.andSquareMetreStartLessThanOrEqualTo(project.getArea());
        criteria1.andSquareMetreEndGreaterThanOrEqualTo(project.getArea());
        List<ProjectBigScheduling> projectBigSchedulings = projectBigSchedulingMapper.selectByExample(example1);
        if (projectBigSchedulings.size() == ProjectDataStatus.INSERT_FAILD.getValue()) {
            return RespData.error("此方案尚未添加施工阶段信息!");
        }
        Date bigStartTime = project.getPlanStartTime();
        Collections.sort(projectBigSchedulings);
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
            if (project.getPlanEndTime().getTime() < planEndTime.getTime()) {
                details.setPlanEndTime(project.getPlanEndTime());
                bigStartTime = project.getPlanEndTime();
            } else {
                if (i == projectBigSchedulings.size() - 1) {
                    details.setPlanEndTime(project.getPlanEndTime());
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
        List<ProjectBigSchedulingDetails> bigList = projectBigSchedulingDetailsMapper.selectByExample(example);
        List<ProjectBigSchedulingDetailsVO> playBigList = BaseToVoUtils.getListVo(bigList, ProjectBigSchedulingDetailsVO.class);
        return RespData.success(playBigList);
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
        //将原数据置为失效
        Integer i = projectBigSchedulingDetailsMapper.updateByProjectNo(projectNo, Scheduling.INVALID_STATUS.getValue());
        if (i == 0) {
            return RespData.error("确认排期失败,原因:原数据失效失败!");
        }
        for (ProjectBigSchedulingDetailsVO detailsVO : bigList) {
            ProjectBigSchedulingDetails projectBigSchedulingDetails = BaseToVoUtils.getVo(detailsVO, ProjectBigSchedulingDetails.class);
            projectBigSchedulingDetails.setStatus(Scheduling.BASE_STATUS.getValue());
            projectBigSchedulingDetails.setCreateTime(new Date());
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
                    return "修改排期下个阶段开始时间失败!";
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
            return "修改排期完成时间失败!";
        }
        if (System.currentTimeMillis() > bigSchedulingDetail.getPlanEndTime().getTime()) {
            ProjectScheduling projectScheduling = new ProjectScheduling();
            ProjectSchedulingExample schedulingExample = new ProjectSchedulingExample();
            ProjectSchedulingExample.Criteria schedulingCriteria = schedulingExample.createCriteria();
            schedulingCriteria.andProjectNoEqualTo(projectNo);
            schedulingCriteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
            //修改延期时间
            projectScheduling.setDelay(DateUtil.differentHoursByMillisecond(bigSchedulingDetail.getPlanEndTime(), new Date()));
            int result = projectSchedulingMapper.updateByExampleSelective(projectScheduling, schedulingExample);
            if (result == 0) {
                return "修改延期时间失败!";
            }
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
}
