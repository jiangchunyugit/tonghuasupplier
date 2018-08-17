package cn.thinkfree.service.project;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.constants.ProjectStatus;
import cn.thinkfree.service.constants.UserJobs;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            PreProjectUserRoleExample preProjectUserRoleExample = new PreProjectUserRoleExample();
            preProjectUserRoleExample.createCriteria().andIsJobEqualTo(SysConstants.YesOrNo.YES.shortVal())
                    .andRoleIdEqualTo(UserJobs.Steward.mes)
                    .andIsTransferEqualTo(SysConstants.YesOrNo.NO.shortVal()).andUserIdEqualTo(projectSEO.getSteward());
           projectNos.addAll(preProjectUserRoleMapper.selectProjectNoByExample(preProjectUserRoleExample));
           needFilter = true;
        }
        // 过滤项目经理所负责的项目
        if(projectSEO.getProjectManager() != null){
            PreProjectUserRoleExample preProjectUserRoleExample = new PreProjectUserRoleExample();
            preProjectUserRoleExample.createCriteria().andIsJobEqualTo(SysConstants.YesOrNo.YES.shortVal())
                    .andRoleIdEqualTo(UserJobs.ProjectManager.mes)
                    .andIsTransferEqualTo(SysConstants.YesOrNo.NO.shortVal()).andUserIdEqualTo(projectSEO.getProjectManager());
            projectNos.addAll(preProjectUserRoleMapper.selectProjectNoByExample(preProjectUserRoleExample));
            needFilter = true;
        }

        if(needFilter && projectNos.isEmpty()){
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
        preProjectGuideMapper.updateByExampleSelective(preProjectGuide,preProjectGuideExample);

        delProjectInfo(projectNo);
        delProjectProjectConstructionAndInfo(projectNo);
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

        PreProjectUserRoleExample roleExample = new PreProjectUserRoleExample();
        roleExample.createCriteria().andProjectNoEqualTo(projectNo).andIsTransferEqualTo(SysConstants.YesOrNo.NO.shortVal());
        List<PreProjectUserRole> preProjectUserRoles = preProjectUserRoleMapper.selectByExample(roleExample);
        projectDetailsVO.setStaffs(preProjectUserRoles);
        return projectDetailsVO;
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
    private void delProjectProjectConstructionAndInfo(String projectNo) {

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
