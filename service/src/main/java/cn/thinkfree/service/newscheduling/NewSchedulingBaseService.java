package cn.thinkfree.service.newscheduling;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.model.ProjectBigScheduling;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.database.vo.ProjectSmallSchedulingVO;
import cn.thinkfree.database.vo.SchedulingSeo;
import com.github.pagehelper.PageInfo;

import java.util.List;

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
     * @param projectBigSchedulingVOList
     * @return
     */
    MyRespBundle<String> addBigScheduling(List<ProjectBigSchedulingVO> projectBigSchedulingVOList);

    /**
     * 获取基础大排期
     *
     * @param schedulingSeo
     * @return
     */
    MyRespBundle<PageInfo<ProjectBigSchedulingVO>> listBigScheduling(SchedulingSeo schedulingSeo);

    /**
     * 关联小排期与大排期
     *
     * @param projectSmallSchedulingVOList
     * @return
     */
    String updateSmallScheduling(List<ProjectSmallSchedulingVO> projectSmallSchedulingVOList);

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
     * @param projectBigSchedulingVOList
     * @return
     */
    MyRespBundle<String> updateBigScheduling(ProjectBigSchedulingVO projectBigSchedulingVOList);

    /**
     * 删除基础大排期
     * @param schemeNo
     * @param sort
     * @return
     */
    MyRespBundle<String> deleteBigScheduling(String schemeNo, Integer sort);

    /**
     * 根据方案编号与排期编号获取排期信息
     * @param schemeNo 方案编号
     * @param sort 排期编号
     * @return 排期详细
     */
    ProjectBigScheduling findBySchemeNoAndSort(String schemeNo, Integer sort);
}
