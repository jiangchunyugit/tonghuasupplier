package cn.thinkfree.service.newscheduling;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ProjectBigSchedulingDetailsVO;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.utils.BaseToVoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.thinkfree.service.utils.DateUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    ProjectQuotationLogMapper projectQuotationLogMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;
    @Autowired
    ProjectSchedulingMapper projectSchedulingMapper;
    @Autowired
    NewOrderUserService newOrderUserService;


    /**
     * 根据项目编号生成排期
     *
     * @param projectNo
     */
//    @Override
//    @Transactional(rollbackFor = {Exception.class})
//    public MyRespBundle createScheduling(String projectNo, String companyId) {
//
//        //获取报价基础信息
//        Set<Integer> bigSortSet = projectQuotationLogMapper.selectByProjectNo(projectNo);
//        //获取项目信息
//        ProjectExample example = new ProjectExample();
//        ProjectExample.Criteria criteria = example.createCriteria();
//        criteria.andProjectNoEqualTo(projectNo);
//        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
//        List<Project> projects = projectMapper.selectByExample(example);
//        Project project = projects.get(0);
//        //获取排期基础数据
//        ProjectBigSchedulingExample example1 = new ProjectBigSchedulingExample();
//        ProjectBigSchedulingExample.Criteria criteria1 = example1.createCriteria();
//        criteria1.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
//        List<ProjectBigScheduling> projectBigSchedulings = projectBigSchedulingMapper.selectByExample(example1);
//        Date bigStartTime = project.getPlanStartTime();
//        Collections.sort(projectBigSchedulings);
//        //生成排期
//        for (ProjectBigScheduling bigScheduling : projectBigSchedulings) {
//            ProjectBigSchedulingDetails details = new ProjectBigSchedulingDetails();
//            details.setCompanyId(companyId);
//            details.setProjectNo(projectNo);
//            details.setBigSort(bigScheduling.getSort());
//            details.setBigName(bigScheduling.getName());
//            details.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
//            details.setCreateTime(new Date());
//            details.setIsCompleted(Scheduling.COMPLETED_NO.getValue());
//            details.setIsAdd(Scheduling.ADD_NO.getValue());
//            details.setIsChange(Scheduling.CHANGE_NUM.getValue());
//            details.setSubmitNum(Scheduling.SUBMIT_NUM.getValue());
//            details.setIsNeedCheck(bigScheduling.getIsNeedCheck());
//            details.setIsAdopt(Scheduling.CHECK_NO.getValue());
//            details.setVersion(bigScheduling.getVersion());
//            details.setRenameBig(bigScheduling.getRename());
//            if (bigSortSet.contains(bigScheduling.getSort())) {
//                details.setPlanStartTime(bigStartTime);
//                details.setPlanEndTime(DateUtil.getDate(bigStartTime, bigScheduling.getWorkload()));
//                bigStartTime = DateUtil.getDate(bigStartTime, bigScheduling.getWorkload());
//                details.setIsMatching(Scheduling.MATCHING_YES.getValue());
//            } else {
//                details.setPlanStartTime(bigStartTime);
//                details.setPlanEndTime(bigStartTime);
//                details.setIsMatching(Scheduling.MATHCHING_NO.getValue());
//            }
//            int i = projectBigSchedulingDetailsMapper.insertSelective(details);
//            if (i != Scheduling.INSERT_SUCCESS.getValue()) {
//                return RespData.error("生成排期失败!!");
//            }
//        }
//        return RespData.success();
//    }

    /**
     * 根据项目编号生成排期
     *
     * @param projectNo
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public MyRespBundle createScheduling(String projectNo, String companyId) {
        //获取项目信息
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<Project> projects = projectMapper.selectByExample(example);
        if(projects.size()==0){
            return RespData.error("无此项目");
        }
        Project project = projects.get(0);
        //获取排期基础数据
        ProjectBigSchedulingExample example1 = new ProjectBigSchedulingExample();
        ProjectBigSchedulingExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        List<ProjectBigScheduling> projectBigSchedulings = projectBigSchedulingMapper.selectByExample(example1);
        Date bigStartTime = project.getPlanStartTime();
        Collections.sort(projectBigSchedulings);
        //生成排期
        for (ProjectBigScheduling bigScheduling : projectBigSchedulings) {
            ProjectBigSchedulingDetails details = new ProjectBigSchedulingDetails();
            details.setCompanyId(companyId);
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
       /*     if (bigSortSet.contains(bigScheduling.getSort())) {
                details.setPlanStartTime(bigStartTime);
                details.setPlanEndTime(DateUtil.getDate(bigStartTime, bigScheduling.getWorkload()));
                bigStartTime = DateUtil.getDate(bigStartTime, bigScheduling.getWorkload());
                details.setIsMatching(Scheduling.MATCHING_YES.getValue());
            } else {
                details.setPlanStartTime(bigStartTime);
                details.setPlanEndTime(bigStartTime);
                details.setIsMatching(Scheduling.MATHCHING_NO.getValue());
            }*/
            int i = projectBigSchedulingDetailsMapper.insertSelective(details);
            if (i != Scheduling.INSERT_SUCCESS.getValue()) {
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
            projectScheduling.setDelay(DateUtil.differentHoursByMillisecond(bigSchedulingDetail.getPlanEndTime(),new Date()));
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
}
