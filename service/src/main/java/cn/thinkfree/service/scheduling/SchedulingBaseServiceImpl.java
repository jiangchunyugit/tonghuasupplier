package cn.thinkfree.service.scheduling;

import cn.thinkfree.database.mapper.ProjectBigSchedulingMapper;
import cn.thinkfree.database.mapper.ProjectSmallSchedulingMapper;
import cn.thinkfree.database.model.ProjectBigScheduling;
import cn.thinkfree.database.model.ProjectSmallScheduling;
import cn.thinkfree.database.model.ProjectSmallSchedulingExample;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.database.vo.SchedulingSeo;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.remote.CloudService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 排期基础信息
 *
 * @author gejiaming
 */
@Service
public class SchedulingBaseServiceImpl implements SchedulingBaseService {
    @Autowired
    CloudService cloudService;
    @Autowired
    ProjectSmallSchedulingMapper projectSmallSchedulingMapper;
    @Autowired
    ProjectBigSchedulingMapper projectBigSchedulingMapper;

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

    /**
     * 添加本地大排期
     *
     * @param projectBigSchedulingVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addBigScheduling(ProjectBigSchedulingVO projectBigSchedulingVO) {
        ProjectBigScheduling projectBigScheduling = new ProjectBigScheduling();
        projectBigScheduling.setSort(projectBigSchedulingVO.getSort());
        projectBigScheduling.setName(projectBigSchedulingVO.getName());
        projectBigScheduling.setDescription(projectBigSchedulingVO.getDescription());
        projectBigScheduling.setIsNew(projectBigSchedulingVO.getIsNew());
        projectBigScheduling.setSquareMetreStart(projectBigSchedulingVO.getSquareMetreStart());
        projectBigScheduling.setSquareMetreEnd(projectBigSchedulingVO.getSquareMetreEnd());
        projectBigScheduling.setRoomNum(projectBigSchedulingVO.getRoomNum());
        projectBigScheduling.setWorkload(projectBigSchedulingVO.getWorkload());
        projectBigScheduling.setStatus(Scheduling.BASE_STATUS.getValue());
        projectBigScheduling.setCreateTime(new Date());
        projectBigScheduling.setVersion(Scheduling.VERSION.getValue());
        projectBigScheduling.setIsNeedCheck(Scheduling.INVALID_STATUS.getValue().shortValue());
        int result = projectBigSchedulingMapper.insertSelective(projectBigScheduling);
        if (result != Scheduling.INSERT_SUCCESS.getValue()) {
            return "添加失败!";
        }
        return "添加成功";
    }
}
