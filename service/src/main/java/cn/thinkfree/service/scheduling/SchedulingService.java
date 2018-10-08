package cn.thinkfree.service.scheduling;

import cn.thinkfree.database.vo.ProjectBigSchedulingVO;

/**
 * 正常排期操作
 *
 * @author gejiaming
 * @Date 2018-09-30
 */
public interface SchedulingService {
    ProjectBigSchedulingVO selectProjectBigSchedulingByCompanyId(String companyId);
}
