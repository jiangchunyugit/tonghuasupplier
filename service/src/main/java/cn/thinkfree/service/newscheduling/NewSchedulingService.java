package cn.thinkfree.service.newscheduling;

import cn.thinkfree.database.model.ProjectBigSchedulingDetails;
import cn.thinkfree.database.vo.ProjectBigSchedulingDetailsVO;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;

import java.util.List;

/**
 * 正常排期操作
 *
 * @author gejiaming
 * @Date 2018-09-30
 */
public interface NewSchedulingService {
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

    /**
     * 获取排期信息
     *
     * @param projectNo
     * @return
     */
    List<ProjectBigSchedulingDetailsVO> getScheduling(String projectNo);
}
