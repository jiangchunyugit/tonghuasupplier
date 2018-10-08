package cn.thinkfree.service.scheduling;

import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.ProjectBigSchedulingMapper;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 正常排期操作
 *
 * @author gejiaming
 * @Date 2018-09-30
 */
@Service(value = "schedulingService")
public class SchedulingServiceImpl implements SchedulingService {
    @Autowired(required = false)
    private ProjectBigSchedulingMapper projectBigSchedulingMapper;

    /**
     * 项目列表
     *
     * @param companyId
     * @return
     */
    @Override
    public ProjectBigSchedulingVO selectProjectBigSchedulingByCompanyId(String companyId) {
        ProjectBigSchedulingVO projectBigSchedulingVO = projectBigSchedulingMapper.selectProjectBigSchedulingByCompanyId(companyId);
        return projectBigSchedulingVO;
    }
}
