package cn.thinkfree.service.newproject;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.appvo.*;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.OrderDetailsVO;
import cn.thinkfree.service.constants.*;
import cn.thinkfree.service.neworder.NewOrderService;
import cn.thinkfree.service.platform.designer.vo.DesignOrderVo;
import cn.thinkfree.service.utils.BaseToVoUtils;
import cn.thinkfree.service.utils.MathUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 项目相关
 *
 * @author gejiaming
 */
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
    DesignOrderMapper designOrderMapper;
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    EmployeeMsgMapper employeeMsgMapper;


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
        PageHelper.startPage(appProjectSEO.getPage(), appProjectSEO.getRows());
        PageInfo<ProjectVo> pageInfo = new PageInfo<>();
        //查询此人名下所有项目
        List<OrderUser> orderUsers = orderUserMapper.selectByExample(example1);
        List<String> list = new ArrayList<>();
        for (OrderUser orderUser : orderUsers) {
            list.add(orderUser.getProjectNo());
        }
        //根据项目编号查询项目信息
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoIn(list);
        List<Project> projects = projectMapper.selectByExample(example);
        List<ProjectVo> projectVoList = new ArrayList<>();
        for (Project project : projects) {
            ProjectVo projectVo = BaseToVoUtils.getVo(project, ProjectVo.class, BaseToVoUtils.getProjectMap());
            if (projectVo == null) {
                System.out.println("工具类转换失败!!");
                return RespData.error("工具类转换失败!!");
            }
            //添加进度信息
            projectVo.setConstructionProgress(MathUtil.getPercentage(project.getPlanStartTime(), project.getPlanEndTime(), new Date()));
            projectVoList.add(projectVo);
        }
        pageInfo.setList(projectVoList);
        return RespData.success(pageInfo);
    }

    /**
     * 获取项目详情接口
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ProjectVo> getProjectDetail(String projectNo) {
        List<ProjectOrderDetailVo> projectOrderDetailVoList = new ArrayList<>();
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        List<Project> projects = projectMapper.selectByExample(example);
        if (projects.size() == 0) {
            return RespData.error("项目不存在!!");
        }
        ProjectVo projectVo = BaseToVoUtils.getVo(projects.get(0), ProjectVo.class, BaseToVoUtils.getProjectMap());
        if (projectVo == null) {
            System.out.println("工具类转换失败!!");
            return RespData.error("工具类转换失败!!");
        }
        //添加灵活数据
        List<FlexibleOrderPlayVo> flexibleOrderPlayVos1 = new ArrayList<>();
        List<FlexibleOrderPlayVo> flexibleOrderPlayVos2 = new ArrayList<>();
        //组合设计订单数据
        FlexibleOrderPlayVo base1 = new FlexibleOrderPlayVo("业务编号", "1223098338391");
        FlexibleOrderPlayVo base2 = new FlexibleOrderPlayVo("风格类型", "个性化");
        FlexibleOrderPlayVo base3 = new FlexibleOrderPlayVo("承接公司", "北京市原创艺墅设计", "http://123.56.0.102/zentao/project-task-8-assignedtome.html", true);
        flexibleOrderPlayVos1.add(base1);
        flexibleOrderPlayVos1.add(base2);
        flexibleOrderPlayVos1.add(base3);
        //组合施工订单数据
        FlexibleOrderPlayVo base4 = new FlexibleOrderPlayVo("业务编号", "1223098338391");
        FlexibleOrderPlayVo base5 = new FlexibleOrderPlayVo("风格类型", "个性化");
        FlexibleOrderPlayVo base6 = new FlexibleOrderPlayVo("承接公司", "北京市原创艺墅设计", "http://123.56.0.102/zentao/project-task-8-assignedtome.html", true);
        FlexibleOrderPlayVo base7 = new FlexibleOrderPlayVo("工长", "黄蓉蓉", "15666666666", true);
        flexibleOrderPlayVos2.add(base4);
        flexibleOrderPlayVos2.add(base5);
        flexibleOrderPlayVos2.add(base6);
        flexibleOrderPlayVos2.add(base7);
        ProjectOrderDetailVo designOrderDetailVo = designOrderMapper.selectByProjectNo(projectNo);
        designOrderDetailVo.setFlexibleOrderPlayVos(flexibleOrderPlayVos1);
        ProjectOrderDetailVo constructionOrderDetailVo = constructionOrderMapper.selectByProjectNo(projectNo);
        constructionOrderDetailVo.setFlexibleOrderPlayVos(flexibleOrderPlayVos2);
        projectOrderDetailVoList.add(designOrderDetailVo);
        projectOrderDetailVoList.add(constructionOrderDetailVo);
        projectVo.setProjectOrderDetailVoList(projectOrderDetailVoList);
        return RespData.success(projectVo);
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
        criteria.andTypeEqualTo(ProjectDataStatus.DESIGN_STATUS.getValue());
        List<ProjectData> projectDataList = projectDataMapper.selectByExample(example);
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
                        detailVo.setCategory(projectData.getCategory());
                        detailVo.setUploadTime(projectData.getUploadTime());
                        urlDetailVo.setUrl(projectData.getUrl());
                        urlList.add(urlDetailVo);
                        urlStringList.add(projectData.getUrl());
                    }
                    if (projectData.getFileType().equals(ProjectDataStatus.FILE_THIRD.getValue())) {
                        detailVo.setThirdUrl(projectData.getUrl());
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
    public MyRespBundle<List<UrlDetailVo>> getConstructionData(String projectNo) {
        List<UrlDetailVo> urlList = new ArrayList<>();
        ProjectDataExample example = new ProjectDataExample();
        ProjectDataExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(projectNo);
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        criteria.andTypeEqualTo(ProjectDataStatus.CONSTRUCTION_STATUS.getValue());
        List<ProjectData> projectDataList = projectDataMapper.selectByExample(example);
        for (ProjectData projectData : projectDataList) {
            UrlDetailVo urlDetailVo = new UrlDetailVo();
            urlDetailVo.setUrl(projectData.getUrl());
            urlDetailVo.setName(projectData.getFileName());
            urlDetailVo.setUploadTime(projectData.getUploadTime());
            urlList.add(urlDetailVo);
        }
        return RespData.success(urlList);
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
            urlDetailVo.setUrl(projectData.getUrl());
            urlDetailVo.setName(projectData.getFileName());
            urlDetailVo.setUploadTime(projectData.getUploadTime());
            urlList.add(urlDetailVo);
        }
        return RespData.success(urlList);
    }

    /**
     * 确认资料
     *
     * @param dataDetailVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> confirmVolumeRoomData(DataDetailVo dataDetailVo) {
        ProjectData projectData = BaseToVoUtils.getVo(dataDetailVo, ProjectData.class);
        projectData.setIsConfirm(ProjectDataStatus.CONFIRM.getValue());
        projectData.setConfirmTime(new Date());
        ProjectDataExample example = new ProjectDataExample();
        ProjectDataExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoEqualTo(dataDetailVo.getProjectNo());
        criteria.andStatusEqualTo(ProjectDataStatus.BASE_STATUS.getValue());
        int i = projectDataMapper.updateByExample(projectData, example);
        if (i != ProjectDataStatus.BASE_STATUS.getValue()) {
            return RespData.error("确认失败!");
        }
        return RespData.success();
    }

    /**
     * 根据项目编号批量获取人员信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<List<UserVo>> getProjectUsers(String projectNo) {
        List<UserVo> userVoList = orderUserMapper.getProjectUsers(projectNo, UserStatus.NO_TRANSFER.getValue(), UserStatus.ON_JOB.getValue());
        return RespData.success(userVoList);
    }

    /**
     * 获取项目阶段
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<Integer> getProjectStatus(String projectNo) {
        List<OrderDetailsVO> orderDetailsVO = projectMapper.selectOrderDetails(projectNo, ProjectDataStatus.BASE_STATUS.getValue());
        return RespData.success(orderDetailsVO.get(0).getStage());
    }

    /**
     * 批量获取员工的信息
     *
     * @param userIds
     * @return
     */
    @Override
    public MyRespBundle<Map<String, UserVo>> getListUserByUserIds(List<String> userIds) {
        Map<String, UserVo> map = new HashMap<>();
        EmployeeMsgExample msgExample = new EmployeeMsgExample();
        msgExample.createCriteria().andUserIdIn(userIds);
        List<EmployeeMsg> employeeMsgs = employeeMsgMapper.selectByExample(msgExample);
        for (EmployeeMsg employeeMsg : employeeMsgs) {
            UserVo vo = new UserVo();
            vo.setRealName(employeeMsg.getRealName());
            vo.setRoleCode(employeeMsg.getRoleCode());
            vo.setUserId(employeeMsg.getUserId());
            vo.setRoleName(UserJobs.findByCodeStr(vo.getRoleCode()).mes);
            map.put(employeeMsg.getUserId(), vo);
        }
        return RespData.success(map);
    }
}
