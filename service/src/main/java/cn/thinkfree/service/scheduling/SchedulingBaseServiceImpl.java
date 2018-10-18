package cn.thinkfree.service.scheduling;

import cn.thinkfree.database.mapper.ProjectBigSchedulingMapper;
import cn.thinkfree.database.mapper.ProjectSmallSchedulingMapper;
import cn.thinkfree.database.model.ProjectBigScheduling;
import cn.thinkfree.database.model.ProjectBigSchedulingExample;
import cn.thinkfree.database.model.ProjectSmallScheduling;
import cn.thinkfree.database.model.ProjectSmallSchedulingExample;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.database.vo.ProjectSmallSchedulingVO;
import cn.thinkfree.database.vo.SchedulingSeo;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.remote.CloudService;
import cn.thinkfree.service.utils.BaseToVoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public PageInfo<ProjectSmallSchedulingVO> listSmallScheduling(SchedulingSeo schedulingSeo) {
        ProjectSmallSchedulingExample projectSmallSchedulingExample = new ProjectSmallSchedulingExample();
        projectSmallSchedulingExample.setOrderByClause("create_time desc");
        ProjectSmallSchedulingExample.Criteria criteria = projectSmallSchedulingExample.createCriteria();
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        if (StringUtils.isNotBlank(schedulingSeo.getCompanyId())) {
            criteria.andCompanyIdEqualTo(schedulingSeo.getCompanyId());
        }
        PageHelper.startPage(schedulingSeo.getPage(), schedulingSeo.getRows());
        List<ProjectSmallScheduling> list = projectSmallSchedulingMapper.selectByExample(projectSmallSchedulingExample);
        List<ProjectSmallSchedulingVO> voList = new ArrayList<>();
        try {
            voList = BaseToVoUtils.getListVo(list,ProjectSmallSchedulingVO.class,BaseToVoUtils.getBaseSmallMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageInfo<>(voList);
    }

    /**
     * 添加本地大排期
     *
     * @param projectBigSchedulingVO
     * @return
     */
    @Override
    @Transactional
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
            return "操作失败!";
        }
        return "操作成功";
    }

    /**
     * 获取基础大排期
     *
     * @param schedulingSeo
     * @return
     */
    @Override
    public PageInfo<ProjectBigScheduling> listBigScheduling(SchedulingSeo schedulingSeo) {
        ProjectBigSchedulingExample projectBigSchedulingExample = new ProjectBigSchedulingExample();
        projectBigSchedulingExample.setOrderByClause("create_time desc");
        ProjectBigSchedulingExample.Criteria criteria = projectBigSchedulingExample.createCriteria();
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        if (StringUtils.isNotBlank(schedulingSeo.getCompanyId())) {
            criteria.andCompanyIdEqualTo(schedulingSeo.getCompanyId());
        }
        PageHelper.startPage(schedulingSeo.getPage(), schedulingSeo.getRows());
        List<ProjectBigScheduling> list = projectBigSchedulingMapper.selectByExample(projectBigSchedulingExample);
        return new PageInfo<>(list);
    }

    /**
     * 关联小排期与大排期
     *
     * @param projectSmallSchedulingVO
     * @return
     */
    @Override
    @Transactional
    public String updateSmallScheduling(ProjectSmallSchedulingVO projectSmallSchedulingVO) {
        if (projectSmallSchedulingVO.getSort() == null || projectSmallSchedulingVO.getParentSort() == null) {
            return "请选择施工阶段!!";
        }
        ProjectSmallScheduling projectSmallScheduling = new ProjectSmallScheduling();
        projectSmallScheduling.setParentSort(projectSmallSchedulingVO.getParentSort());
        ProjectSmallSchedulingExample example = new ProjectSmallSchedulingExample();
        ProjectSmallSchedulingExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        criteria.andSortEqualTo(projectSmallSchedulingVO.getSort());
        int result = projectSmallSchedulingMapper.updateByExampleSelective(projectSmallScheduling, example);
        if (result != Scheduling.INSERT_SUCCESS.getValue()) {
            return "操作失败!";
        }
        return "操作成功";
    }

    /**
     * 同步上海基础小排期
     * @return
     */
    @Override
    public String listShangHai() {
        cloudService.getBaseScheduling();
        return null;
    }
}
