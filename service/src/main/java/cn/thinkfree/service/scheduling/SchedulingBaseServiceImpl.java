package cn.thinkfree.service.scheduling;

import cn.thinkfree.database.mapper.ProjectSmallSchedulingMapper;
import cn.thinkfree.database.model.ProjectSmallScheduling;
import cn.thinkfree.database.model.ProjectSmallSchedulingExample;
import cn.thinkfree.database.vo.SchedulingSeo;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.remote.CloudService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 排期基础信息
 *
 * @author gejiaming
 */
@Service
public class SchedulingBaseServiceImpl implements SchedulingBaseService {
    @Autowired
    private CloudService cloudService;
    @Autowired
    private ProjectSmallSchedulingMapper projectSmallSchedulingMapper;

    /**
     * 获取本地基础小排期信息
     *
     * @param schedulingSeo
     * @return
     */
    @Override
    public PageInfo<ProjectSmallScheduling> listSmallScheduling(SchedulingSeo schedulingSeo) {

        ProjectSmallSchedulingExample projectSmallSchedulingExample = new ProjectSmallSchedulingExample();
        projectSmallSchedulingExample.setOrderByClause("create_time desc");
        ProjectSmallSchedulingExample.Criteria criteria = projectSmallSchedulingExample.createCriteria();
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        PageHelper.startPage(schedulingSeo.getPage(), schedulingSeo.getRows());
        List<ProjectSmallScheduling> list = projectSmallSchedulingMapper.selectByExample(projectSmallSchedulingExample);
        return new PageInfo<>(list);
    }
}
