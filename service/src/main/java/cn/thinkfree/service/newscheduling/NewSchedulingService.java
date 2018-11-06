package cn.thinkfree.service.newscheduling;

import cn.thinkfree.core.bundle.MyRespBundle;
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
     * @param projectNo
     * @return
     */
    MyRespBundle<List<ProjectBigSchedulingDetailsVO>> getCheckStage(String projectNo);

    /**
     * 生成排期
     * @param projectNo
     * @param companyId
     * @return
     */
    MyRespBundle createScheduling(String projectNo, String companyId);

    /**
     * 确认排期
     * @param projectBigSchedulingDetailsVO
     * @return
     */
    MyRespBundle confirmProjectScheduling(List<ProjectBigSchedulingDetailsVO> projectBigSchedulingDetailsVO);

    /**
     * 大阶段完成,添加大阶段完成时间
     * @param projectNo
     * @param bigSort
     */
    String completeBigScheduling(String projectNo,Integer bigSort);

    /**
     * 开工申请
     * @param projectNo
     * @param bigSort
     * @return
     */
    String projectStart(String projectNo,Integer bigSort);
}
