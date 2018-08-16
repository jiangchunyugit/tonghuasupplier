package cn.thinkfree.service.project;

import cn.thinkfree.database.vo.IndexProjectReportVO;
import cn.thinkfree.database.vo.ProjectSEO;
import cn.thinkfree.database.vo.ProjectVO;
import com.github.pagehelper.PageInfo;

public interface ProjectService {

    /**
     * 汇总公司项目情况
     * @param companyID
     * @return
     */
    IndexProjectReportVO countProjectReportVO(String companyID);

    /**
     * 分页查询项目信息
     * @param projectSEO
     * @return
     */
    PageInfo<ProjectVO> pageProjectBySEO(ProjectSEO projectSEO);
}
