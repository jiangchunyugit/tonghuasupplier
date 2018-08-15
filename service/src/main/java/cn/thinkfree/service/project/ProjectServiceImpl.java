package cn.thinkfree.service.project;

import cn.thinkfree.database.mapper.PreProjectCompanySetMapper;
import cn.thinkfree.database.model.PreProjectCompanySet;
import cn.thinkfree.database.model.PreProjectGuide;
import cn.thinkfree.database.vo.IndexProjectReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    PreProjectCompanySetMapper preProjectCompanySetMapper;


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
}
