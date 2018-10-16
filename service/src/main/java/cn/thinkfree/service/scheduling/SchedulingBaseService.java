package cn.thinkfree.service.scheduling;

import cn.thinkfree.database.model.ProjectSmallScheduling;
import cn.thinkfree.database.vo.SchedulingSeo;
import com.github.pagehelper.PageInfo;

/**
 * 排期基础信息
 *
 * @author gejiaming
 */
public interface SchedulingBaseService {

    /**
     * 获取本地基础小排期信息
     *
     * @param schedulingSeo
     * @return
     */
    PageInfo<ProjectSmallScheduling> listSmallScheduling(SchedulingSeo schedulingSeo);
}
