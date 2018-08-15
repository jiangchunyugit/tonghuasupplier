package cn.thinkfree.service.project;

import cn.thinkfree.database.vo.IndexProjectReportVO;

public interface ProjectService {

    /**
     * 汇总公司项目情况
     * @param companyID
     * @return
     */
    IndexProjectReportVO countProjectReportVO(String companyID);

}
