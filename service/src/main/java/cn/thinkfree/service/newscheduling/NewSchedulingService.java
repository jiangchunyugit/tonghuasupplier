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
     * 获取排期信息
     *
     * @param projectNo
     * @return
     */
    List<ProjectBigSchedulingDetailsVO> getScheduling(String projectNo);

    /**
     * 添加公司施工节点
     *
     * @param projectBigSchedulingDetailsVO
     * @return
     */
    String saveProjectScheduling(ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO);

    /**
     * 删除公司施工节点
     *
     * @param projectBigSchedulingDetailsVO
     * @return
     */
    String deleteProjectScheduling(ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO);


}
