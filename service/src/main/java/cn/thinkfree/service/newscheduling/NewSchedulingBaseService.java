package cn.thinkfree.service.newscheduling;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.model.ProjectBigScheduling;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.database.vo.ProjectSmallSchedulingVO;
import cn.thinkfree.database.vo.SchedulingSeo;
import com.github.pagehelper.PageInfo;

/**
 * 排期基础信息
 *
 * @author gejiaming
 */
public interface NewSchedulingBaseService {

    /**
     * 获取本地基础小排期信息
     *
     * @param schedulingSeo
     * @return
     */
    PageInfo<ProjectSmallSchedulingVO> listSmallScheduling(SchedulingSeo schedulingSeo);

    /**
     * 添加本地大排期
     *
     * @param projectBigSchedulingVO
     * @return
     */
    String addBigScheduling(ProjectBigSchedulingVO projectBigSchedulingVO);

    /**
     * 获取基础大排期
     *
     * @param schedulingSeo
     * @return
     */
    PageInfo<ProjectBigSchedulingVO> listBigScheduling(SchedulingSeo schedulingSeo);

    /**
     * 关联小排期与大排期
     *
     * @param projectSmallSchedulingVO
     * @return
     */
    String updateSmallScheduling(ProjectSmallSchedulingVO projectSmallSchedulingVO);

    /**
     * 同步上海基础小排期
     *
     * @param schedulingSeo
     * @return
     */
    String listShangHai(SchedulingSeo schedulingSeo);

    /**
     * 修改基础大排期
     *
     * @param projectBigSchedulingVO
     * @return
     */
    MyRespBundle<String> updateBigScheduling(ProjectBigSchedulingVO projectBigSchedulingVO);
}
