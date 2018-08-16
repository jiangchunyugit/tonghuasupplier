package cn.thinkfree.service.project;

import cn.thinkfree.database.vo.IndexProjectReportVO;
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
}
