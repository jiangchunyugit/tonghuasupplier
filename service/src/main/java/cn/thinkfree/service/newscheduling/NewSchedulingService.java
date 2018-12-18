package cn.thinkfree.service.newscheduling;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.model.Project;
import cn.thinkfree.database.model.ProjectBigScheduling;
import cn.thinkfree.database.model.ProjectBigSchedulingDetails;import cn.thinkfree.database.model.ProjectScheduling;
import cn.thinkfree.database.vo.ProjectBigSchedulingDetailsVO;

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
    MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getScheduling(String projectNo);

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

    /**
     * 获取验收阶段
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getCheckStage(String projectNo);

    /**
     * 生成排期
     *
     * @param orderNo
     * @return
     */
    MyRespBundle createScheduling(String orderNo);

    /**
     * 确认排期
     *
     * @param projectBigSchedulingDetailsVO
     * @return
     */
    MyRespBundle confirmProjectScheduling(List<ProjectBigSchedulingDetailsVO> projectBigSchedulingDetailsVO);

    /**
     * 大阶段完成,添加大阶段完成时间
     *
     * @param projectNo
     * @param bigSort
     * @return
     */
    String completeBigScheduling(String projectNo, Integer bigSort);

    /**
     * 开工申请
     *
     * @param projectNo
     * @param bigSort
     * @return
     */
    String projectStart(String projectNo, Integer bigSort);

    /**
     * 提供PC合同处获取验收阶段
     *
     * @param orderNo
     * @param type
     * @return
     */
    MyRespBundle<List<String>> getPcCheckStage(String orderNo, Integer type);

    /**
     * 获取项目总排期信息
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<ProjectScheduling> getProjectScheduling(String projectNo);

    /**
     * 修改延期天数(延期单审批通过后调用)
     * @param projectNo
     * @param delay
     * @return
     */
    MyRespBundle editProjectDelay(String projectNo,Integer delay);

    /**
     * 根据方案编号与排期编号查询排期信息
     * @param schemeNo 方案编号
     * @param scheduleSort 排期编号
     * @return 排期信息
     */
    ProjectBigScheduling findBySchemeNoAndScheduleSort(String schemeNo, Integer scheduleSort);
}
