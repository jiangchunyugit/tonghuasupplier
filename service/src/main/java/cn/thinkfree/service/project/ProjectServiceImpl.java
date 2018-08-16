package cn.thinkfree.service.project;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.database.mapper.PreProjectCompanySetMapper;
import cn.thinkfree.database.mapper.PreProjectGuideMapper;
import cn.thinkfree.database.mapper.PreProjectUserRoleMapper;
import cn.thinkfree.database.model.PreProjectCompanySet;
import cn.thinkfree.database.model.PreProjectGuide;
import cn.thinkfree.database.model.PreProjectUserRoleExample;
import cn.thinkfree.database.vo.IndexProjectReportVO;
import cn.thinkfree.database.vo.ProjectSEO;
import cn.thinkfree.database.vo.ProjectVO;
import cn.thinkfree.service.constants.UserJobs;
import cn.thinkfree.service.constants.UserRegisterType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    PreProjectCompanySetMapper preProjectCompanySetMapper;

    @Autowired
    PreProjectUserRoleMapper preProjectUserRoleMapper;

    @Autowired
    PreProjectGuideMapper preProjectGuideMapper;

    /**
     * 汇总公司项目情况
     *
     * @param companyID
     * @return
     */
    @Override
    public IndexProjectReportVO countProjectReportVO(String companyID) {
        return preProjectCompanySetMapper.countCompanyProject(companyID);
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

//        PageHelper.startPage()
        List<ProjectVO> projectVOS=preProjectGuideMapper.selectProjectVOBySEO(projectSEO);


        return new PageInfo<>(projectVOS);
    }
}
