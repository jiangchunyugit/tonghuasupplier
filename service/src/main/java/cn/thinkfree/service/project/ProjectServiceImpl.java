package cn.thinkfree.service.project;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.event.MyEventBus;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.core.utils.WebFileUtil;
import cn.thinkfree.database.event.ProjectStopEvent;
import cn.thinkfree.database.event.ProjectUpOnline;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.constants.ProjectStatus;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.remote.CloudService;
import cn.thinkfree.service.remote.RemoteResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl extends AbsLogPrinter implements ProjectService {


    @Autowired
    PreProjectCompanySetMapper preProjectCompanySetMapper;

    @Autowired
    PreProjectUserRoleMapper preProjectUserRoleMapper;

    @Autowired
    PreProjectGuideMapper preProjectGuideMapper;

    @Autowired
    PreProjectInfoMapper preProjectInfoMapper;

    @Autowired
    PreProjectMaterialMapper preProjectMaterialMapper;

    @Autowired
    PreProjectConstructionMapper preProjectConstructionMapper;

    @Autowired
    PreProjectConstructionInfoMapper preProjectConstructionInfoMapper;

    @Autowired
    CompanyUserSetMapper companyUserSetMapper;

    @Autowired
    CloudService cloudService;




    /**
     * 汇总公司项目情况
     *
     * @param companyRelationMap
     * @return
     */
    @Override
    public IndexProjectReportVO countProjectReportVO(List<String> companyRelationMap) {
        return preProjectCompanySetMapper.countCompanyProject(companyRelationMap);
    }

    /**
     * 分页查询项目信息
     *
     * 因为 角色存储的时候存在了多份,且关联关系较复杂
     * 所以 当有角色条件时,先查询出角色所负责的项目,在依次进行筛选和分页
     *
     * @param projectSEO
     * @return
     */
    @Override
    public PageInfo<ProjectVO> pageProjectBySEO(ProjectSEO projectSEO) {

        Boolean needFilter = false;
        List<String> projectNos = Lists.newArrayList();
        // 过滤管家所负责的项目
        if(projectSEO.getSteward() != null  ){
            printDebugMes("项目分页查询,存在管家条件:{}",projectSEO.getSteward());
            PreProjectUserRoleExample preProjectUserRoleExample = new PreProjectUserRoleExample();
            preProjectUserRoleExample.createCriteria().andIsJobEqualTo(SysConstants.YesOrNo.YES.shortVal())
                    .andRoleIdEqualTo(UserJobs.Steward.mes)
                    .andIsTransferEqualTo(SysConstants.YesOrNo.NO.shortVal()).andUserIdEqualTo(projectSEO.getSteward());
           projectNos.addAll(preProjectUserRoleMapper.selectProjectNoByExample(preProjectUserRoleExample));
           needFilter = true;
        }
        // 过滤项目经理所负责的项目
        if(projectSEO.getProjectManager() != null){
            printDebugMes("项目分页查询,存在项目经理条件:{}",projectSEO.getProjectManager());
            PreProjectUserRoleExample preProjectUserRoleExample = new PreProjectUserRoleExample();
            preProjectUserRoleExample.createCriteria().andIsJobEqualTo(SysConstants.YesOrNo.YES.shortVal())
                    .andRoleIdEqualTo(UserJobs.ProjectManager.mes)
                    .andIsTransferEqualTo(SysConstants.YesOrNo.NO.shortVal()).andUserIdEqualTo(projectSEO.getProjectManager());
            projectNos.addAll(preProjectUserRoleMapper.selectProjectNoByExample(preProjectUserRoleExample));
            needFilter = true;
        }

        if(needFilter && projectNos.isEmpty()){
            printDebugMes("存在管家或项目经理,但过滤后无项目编号");
            // 为空直接返回
            return new PageInfo();
        }

        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        projectSEO.setCompanyRelationMap(userVO.getRelationMap());
        PageHelper.startPage(projectSEO.getPage(),projectSEO.getRows());
        List<ProjectVO> projectVOS = preProjectGuideMapper.selectProjectVOBySEO(projectSEO);


        return new PageInfo<>(projectVOS);
    }

    /**
     * 删除项目根据项目主键
     *
     * @param projectNo
     * @return
     */
    @Transactional
    @Override
    public String deleteProjectByProjectNo(String projectNo) {

        PreProjectGuideExample preProjectGuideExample = new PreProjectGuideExample();
        preProjectGuideExample.createCriteria().andProjectNoEqualTo(projectNo).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        List<PreProjectGuide> preProjectGuides = preProjectGuideMapper.selectByExample(preProjectGuideExample);
        printInfoMes("删除项目编号{},查询到项目引导信息:{}",projectNo,preProjectGuides);
        if(preProjectGuides.isEmpty() || preProjectGuides.size() > 1){
            printErrorMes("删除项目编号{},查询到项目引导信息不唯一",projectNo);
            return "数据异常,无法删除";
        }
        PreProjectGuide preProjectGuide = preProjectGuides.get(0);
        if(!ProjectStatus.NotOnLine.shortVal().equals(preProjectGuide.getStatus())){
            return "数据状态不可删除";
        }

        PreProjectGuide deleteObj = new PreProjectGuide();
        deleteObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        preProjectGuideMapper.updateByExampleSelective(deleteObj,preProjectGuideExample);

        delProjectInfo(projectNo);
        delProjectConstructionAndInfo(projectNo);
        delProjectMaterial(projectNo);
        return "操作成功";
    }

    /**
     * 停工
     *
     * @param projectNo
     * @return
     */
    @Override
    public String updateProjectStateForStop(String projectNo) {
        printInfoMes("停工项目:{}",projectNo);
        PreProjectGuideExample preProjectGuideExample = new PreProjectGuideExample();
        preProjectGuideExample.createCriteria().andProjectNoEqualTo(projectNo).andStatusEqualTo(ProjectStatus.Working.shortVal());
        List<PreProjectGuide> preProjectGuides = preProjectGuideMapper.selectByExample(preProjectGuideExample);

        if(preProjectGuides.isEmpty()){
            return "数据状态不可停工!";
        }

        PreProjectGuide preProjectGuide = new PreProjectGuide();
        preProjectGuide.setStatus(ProjectStatus.StopTheWork.shortVal());
        preProjectGuideMapper.updateByExampleSelective(preProjectGuide,preProjectGuideExample);

        MyEventBus.getInstance().publicEvent(new ProjectStopEvent(projectNo));
        return "操作成功";
    }

    /**
     * 查询项目详情 根据项目编号
     *
     * @param projectNo
     * @return
     */
    @Override
    public ProjectDetailsVO selectProjectDetailsVOByProjectNo(String projectNo) {

        ProjectDetailsVO projectDetailsVO = preProjectGuideMapper.selectProjectDetailsByProjectNo(projectNo);
        if(projectDetailsVO == null){
            return new ProjectDetailsVO();
        }
        PreProjectUserRoleExample roleExample = new PreProjectUserRoleExample();
        roleExample.createCriteria().andProjectNoEqualTo(projectNo).andIsTransferEqualTo(SysConstants.YesOrNo.NO.shortVal());
        List<PreProjectUserRole> preProjectUserRoles = preProjectUserRoleMapper.selectByExample(roleExample);
        projectDetailsVO.setStaffs(preProjectUserRoles);
//        projectDetailsVO.setProjectQuotationVO(selectProjectQuotationVoByProjectNo(projectNo));


        return projectDetailsVO;
    }

    /**
     * 根据职位查询员工
     *
     * @param job
     * @return
     */
    @Override
    public List<StaffsVO> selectStaffsByJob(String job) {

        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        CompanyUserSetExample companyUserSetExample = new CompanyUserSetExample();
        companyUserSetExample.createCriteria().andCompanyIdEqualTo(userVO.getCompanyID())
                .andIsJobEqualTo(SysConstants.YesOrNo.YES.shortVal())
                .andIsBindEqualTo(SysConstants.YesOrNo.YES.shortVal())
                .andRoleIdEqualTo(job);
        return companyUserSetMapper.selectStaffsVOByExample(companyUserSetExample);
    }

    /**
     * 查询项目报价单
     *
     * @param projectNo
     * @return
     */
    @Override
    public ProjectQuotationVO selectProjectQuotationVoByProjectNo(String projectNo) {
        ProjectQuotationVO projectQuotationVO = preProjectInfoMapper.selectProjectQuotationVOByProjectNo(projectNo);
        if(projectQuotationVO == null){
            return new ProjectQuotationVO();
        }
        List<ProjectQuotationItemVO> projectQuotationVOList= preProjectConstructionMapper.selectProjectQuotationItemVoByProjectNo(projectNo);
        projectQuotationVO.setItems(projectQuotationVOList);
        return projectQuotationVO;
    }

    /**
     * 编辑报价单
     *
     * @param projectQuotationVO
     * @return
     */
    @Transactional
    @Override
    public String editQuotation(ProjectQuotationVO projectQuotationVO) {
        String projectNo = projectQuotationVO.getProjectNo();

        PreProjectGuideExample preProjectGuideExample = new PreProjectGuideExample();
        preProjectGuideExample.createCriteria().andProjectNoEqualTo(projectNo);
        List<PreProjectGuide> preProjectGuides = preProjectGuideMapper.selectByExample(preProjectGuideExample);
        if(preProjectGuides.isEmpty() || preProjectGuides.size() > 1){
            throw  new RuntimeException("数据完整性破损,操作失败!");
        }
        PreProjectGuide preProjectGuide = preProjectGuides.get(0);
        if(!ProjectStatus.NotOnLine.shortVal().equals(preProjectGuide.getStatus())){
            return "数据状态不可编辑!";
        }

        PreProjectInfo updateInfo = new PreProjectInfo();
        SpringBeanUtil.copy(projectQuotationVO,updateInfo);
        PreProjectInfoExample preProjectInfoExample = new PreProjectInfoExample();
        preProjectInfoExample.createCriteria().andProjectNoEqualTo(projectNo).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        preProjectInfoMapper.updateByExampleSelective(updateInfo,preProjectInfoExample);

        printInfoMes("编辑报价单,汇总信息编辑完成,处理报价单条目");
        PreProjectGuide updateGuide = new PreProjectGuide();
        updateGuide.setTotalPrice(projectQuotationVO.getTotalPrice());

        preProjectGuideMapper.updateByExampleSelective(updateGuide,preProjectGuideExample);

        delProjectConstructionAndInfo(projectNo);

        saveProjectConstructionAndInfo(projectQuotationVO);


        return "操作成功!";
    }

    /**
     * 项目上线
     * @param projectDetailsVO
     * @return
     */
    @Transactional
    @Override
    public String updateProjectForUpOnline(ProjectDetailsVO projectDetailsVO) {

        PreProjectGuideExample preProjectGuideExample = new PreProjectGuideExample();
        preProjectGuideExample.createCriteria().andProjectNoEqualTo(projectDetailsVO.getProjectNo());
        projectDetailsVO.setStatus(ProjectStatus.WaitStart.shortVal());
        if(projectDetailsVO.getQRCodeFile() != null){
            String filePath = WebFileUtil.fileCopy("/project", projectDetailsVO.getQRCodeFile());
            projectDetailsVO.getInfo().setProjectCode(filePath);
        }
        if(projectDetailsVO.getThumbnail() != null){
            String filePath = WebFileUtil.fileCopy("/project",projectDetailsVO.getThumbnail());
            projectDetailsVO.setProjectPic(filePath);
        }
        preProjectGuideMapper.updateByExampleSelective(projectDetailsVO,preProjectGuideExample);

        printInfoMes("处理项目员工信息");

        replacement(projectDetailsVO.getStaffs());

        PreProjectInfo preProjectInfo = projectDetailsVO.getInfo();
        PreProjectInfoExample preProjectInfoExample = new PreProjectInfoExample();
        preProjectInfoExample.createCriteria().andIdEqualTo(preProjectInfo.getId());
        preProjectInfoMapper.updateByExampleSelective(preProjectInfo,preProjectInfoExample);

//        MyEventBus.getInstance().publicEvent(new ProjectUpOnline(projectDetailsVO.getProjectNo()));

        RemoteResult<String> rs = cloudService.projectUpOnline(projectDetailsVO.getProjectNo(), ProjectStatus.WaitStart.shortVal());
        if(!rs.isComplete()){
            throw  new RuntimeException("神奇的操作,无法理解");
        }

        return "操作成功!";
    }


    /**
     * 移交项目
     * 替换缩略图
     * 替换人员
     * @param projectTransferVO
     * @return
     */
    @Transactional
    @Override
    public String updateProjectByTransfer(ProjectTransferVO projectTransferVO) {



        if(projectTransferVO.getThumbnail() != null){
            String filePath = WebFileUtil.fileCopy("/project",projectTransferVO.getThumbnail());

            PreProjectGuide updateObj = new PreProjectGuide();
            updateObj.setProjectPic(filePath);
            PreProjectGuideExample preProjectGuideExample = new PreProjectGuideExample();
            preProjectGuideExample.createCriteria().andProjectNoEqualTo(projectTransferVO.getProjectNo());
            preProjectGuideMapper.updateByExampleSelective(updateObj,preProjectGuideExample);
        }

        replacement(projectTransferVO.getStaffs());
        return "操作成功!";
    }

    /**
     * 项目图标 总览
     *
     * @param firstDayOfWeek
     * @param lastDayOfWeek
     * @return
     */
    @Override
    public List<IndexProjectChartItemVO> summaryProjectChart(LocalDate firstDayOfWeek, LocalDate lastDayOfWeek) {
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        return preProjectGuideMapper.selectProjectLineChat(firstDayOfWeek.toString(),lastDayOfWeek.toString(),userVO.getRelationMap());
    }

    /**
     * 查询一个员工参与的项目
     *
     * @param userID
     * @param status
     * @param rows
     * @param page
     * @return
     */
    @Override
    public PageInfo<ProjectVO> selectProjectVOForPerson(String userID, Integer status, Integer rows, Integer page) {
        PageHelper.startPage(page,rows);
        List<ProjectVO> list = preProjectGuideMapper.selectProjectVOForPerson(userID,status);
        return new PageInfo<>(list);
    }

    /**
     * 汇总一个员工的所有项目状态
     *
     * @param userID
     * @return
     */
    @Override
    public IndexProjectReportVO countProjectForPerson(String userID) {

        return preProjectCompanySetMapper.countProjectForPerson(userID);
    }

    /**
     * 根据项目编号查询主材信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public List<PreProjectMaterial> selectProjectMaterilsByProjectNo(String projectNo) {

        PreProjectMaterialExample preProjectMaterialExample = new PreProjectMaterialExample();

        preProjectMaterialExample.setOrderByClause(" create_time ");

        preProjectMaterialExample.createCriteria().andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal())
                .andProjectNoEqualTo(projectNo);

        return preProjectMaterialMapper.selectByExample(preProjectMaterialExample);
    }

    /**
     * 编辑主材信息
     *
     *
     * @param projectNo
     * @param preProjectMaterials
     * @return
     */
    @Override
    public String editMaterials(String projectNo, List<PreProjectMaterial> preProjectMaterials) {

        printInfoMes("删除主材信息:{}",projectNo);
        PreProjectMaterialExample preProjectMaterialExample = new PreProjectMaterialExample();
        preProjectMaterialExample.createCriteria().andProjectNoEqualTo(projectNo);
        PreProjectMaterial delObj = new PreProjectMaterial();
        delObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        preProjectMaterialMapper.updateByExampleSelective(delObj,preProjectMaterialExample);
        if(preProjectMaterials.isEmpty()){
            return "操作成功!";
        }

        preProjectMaterials.forEach(m->{
            m.setProjectNo(projectNo);
            m.setCreateTime(new Date());
            m.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
            preProjectMaterialMapper.insertSelective(m);
        });

        return "操作成功!";
    }

    /**
     * 查询一个公司及子公司的项目
     *
     * @param companyID
     * @param status
     * @param rows
     * @param page
     * @return
     */
    @Override
    public PageInfo<ProjectVO> selectProjectVOForCompany(String companyID, Integer status, Integer rows, Integer page) {


        PageHelper.startPage(page,rows);
        List<ProjectVO> list = preProjectGuideMapper.selectProjectVOForCompany(companyID,status);
        return new PageInfo<>(list);
    }


    /**
     * 新增报价单条目信息
     * @param projectQuotationVO
     */
    private void saveProjectConstructionAndInfo(ProjectQuotationVO projectQuotationVO) {
        List<ProjectQuotationItemVO> allWaitSave = projectQuotationVO.getItems();
        PreProjectConstructionInfo update = null;
        for(ProjectQuotationItemVO vo : allWaitSave){
            vo.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
            vo.setCreatTime(new Date());
            vo.setProjectNo(projectQuotationVO.getProjectNo());
            preProjectConstructionMapper.insert(vo);

            update = vo.getPreProjectConstructionInfo();
            update.setCreateTime(new Date());
            update.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
            update.setProjectNo(projectQuotationVO.getProjectNo());
            preProjectConstructionInfoMapper.insert(update);
        }
    }

    /**
     * 替换人员
     * @param preProjectUserRoles
     */
    private void replacement(List<PreProjectUserRole> preProjectUserRoles) {

        for(PreProjectUserRole vo : preProjectUserRoles){
            PreProjectUserRole preProjectUserRole = new PreProjectUserRole();
            preProjectUserRole.setRoleId(vo.getRoleId());
            preProjectUserRole.setUserId(vo.getUserId());
            PreProjectUserRoleExample preProjectUserRoleExample = new PreProjectUserRoleExample();
            preProjectUserRoleExample.createCriteria().andIdEqualTo(vo.getId());
            preProjectUserRoleMapper.updateByExampleSelective(preProjectUserRole,preProjectUserRoleExample);
        }
    }


    /**
     * 删除主材信息
     * @param projectNo
     */
    private void delProjectMaterial(String projectNo) {
        PreProjectMaterial delObj = new PreProjectMaterial();
        delObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        PreProjectMaterialExample preProjectMaterialExample = new PreProjectMaterialExample();
        preProjectMaterialExample.createCriteria().andProjectNoEqualTo(projectNo).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        preProjectMaterialMapper.updateByExampleSelective(delObj,preProjectMaterialExample);

    }

    /**
     * 删除报价单及详情
     * @param projectNo
     */
    private void delProjectConstructionAndInfo(String projectNo) {

        PreProjectConstruction delObj = new PreProjectConstruction();
        delObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        PreProjectConstructionExample preProjectConstructionExample = new PreProjectConstructionExample();
        preProjectConstructionExample.createCriteria().andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal()).andProjectNoEqualTo(projectNo);
        preProjectConstructionMapper.updateByExampleSelective(delObj,preProjectConstructionExample);

        PreProjectConstructionInfo delInfo = new PreProjectConstructionInfo();
        delInfo.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        PreProjectConstructionInfoExample preProjectConstructionInfoExample = new PreProjectConstructionInfoExample();
        preProjectConstructionInfoExample.createCriteria().andProjectNoEqualTo(projectNo).andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        preProjectConstructionInfoMapper.updateByExampleSelective(delInfo,preProjectConstructionInfoExample);


    }

    /**
     * 删除项目信息
     * @param projectNo
     */
    private void delProjectInfo(String projectNo) {
        // 标记项目基本信息
        PreProjectInfo delObj = new PreProjectInfo();
        delObj.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        PreProjectInfoExample preProjectInfoExample = new PreProjectInfoExample();
        preProjectInfoExample.createCriteria().andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal()).andProjectNoEqualTo(projectNo);
        preProjectInfoMapper.updateByExampleSelective(delObj,preProjectInfoExample);
    }

}
