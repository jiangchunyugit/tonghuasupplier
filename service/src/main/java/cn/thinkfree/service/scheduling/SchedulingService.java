package cn.thinkfree.service.scheduling;

import cn.thinkfree.database.vo.ProjectBigSchedulingVO;

/**
 * 正常排期操作
 *
 * @author gejiaming
 * @Date 2018-09-30
 */
public interface SchedulingService {
    /**
     * 项目列表
     *
     * @param companyId
     * @return
     */
    ProjectBigSchedulingVO selectProjectBigSchedulingByCompanyId(String companyId);

    /**
     * 添加公司施工节点
     *
     * @param projectBigSchedulingVO
     * @return
     */
    String saveProjectScheduling(ProjectBigSchedulingVO projectBigSchedulingVO);

    /**
     * 删除公司施工节点
     *
     * @param projectBigSchedulingVO
     * @return
     */
    String deleteProjectScheduling(ProjectBigSchedulingVO projectBigSchedulingVO);
}
