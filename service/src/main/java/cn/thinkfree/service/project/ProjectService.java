package cn.thinkfree.service.project;

import cn.thinkfree.database.vo.IndexProjectReportVO;
import cn.thinkfree.database.vo.ProjectDetailsVO;
import cn.thinkfree.database.vo.ProjectSEO;
import cn.thinkfree.database.vo.ProjectVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ProjectService {

    /**
     * 汇总公司项目情况
     * @param companyRelationMap
     * @return
     */
    IndexProjectReportVO countProjectReportVO(List<String> companyRelationMap);

    /**
     * 分页查询项目信息
     * @param projectSEO
     * @return
     */
    PageInfo<ProjectVO> pageProjectBySEO(ProjectSEO projectSEO);

    /**
     * 删除项目根据项目主键
     * @param  projectNo
     * @return
     */
    String deleteProjectByProjectNo(String projectNo);

    /**
     * 停工
     * @param projectNo
     * @return
     */
    String updateProjectStateForStop(String projectNo);

    /**
     * 查询项目详情 根据项目编号
     * @param projectNo
     * @return
     */
    ProjectDetailsVO selectProjectDetailsVOByProjectNo(String projectNo);
}
