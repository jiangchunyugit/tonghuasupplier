package cn.thinkfree.service.newscheduling;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.mapper.ProjectBigSchedulingMapper;
import cn.thinkfree.database.mapper.ProjectSmallSchedulingMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.database.vo.ProjectSmallSchedulingVO;
import cn.thinkfree.database.vo.SchedulingSeo;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.remote.CloudService;
import cn.thinkfree.service.utils.BaseToVoUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 排期基础信息
 *
 * @author gejiaming
 */
@Service
public class NewSchedulingBaseServiceImpl implements NewSchedulingBaseService {
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
        projectSmallSchedulingExample.setOrderByClause("create_time");
        ProjectSmallSchedulingExample.Criteria criteria = projectSmallSchedulingExample.createCriteria();
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        if (StringUtils.isNotBlank(schedulingSeo.getCompanyId())) {
            criteria.andCompanyIdEqualTo(schedulingSeo.getCompanyId());
        }
        PageHelper.startPage(schedulingSeo.getPage(), schedulingSeo.getRows());
        List<ProjectSmallScheduling> list = projectSmallSchedulingMapper.selectByExample(projectSmallSchedulingExample);
        List<ProjectSmallSchedulingVO> voList = BaseToVoUtils.getListVo(list, ProjectSmallSchedulingVO.class);
        if (voList == null) {
            System.out.println("工具类转换失败!!");
        }
        return new PageInfo<>(voList);
    }

    /**
     * 添加本地大排期
     *
     * @param projectBigSchedulingVOList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> addBigScheduling(List<ProjectBigSchedulingVO> projectBigSchedulingVOList) {
        for (ProjectBigSchedulingVO projectBigSchedulingVO : projectBigSchedulingVOList) {
            if (projectBigSchedulingVO.getSchemeNo() == null || projectBigSchedulingVO.getSchemeNo().isEmpty()) {
                return RespData.error("请给" + projectBigSchedulingVO.getName() + "的schemo_no赋值");
            }
            ProjectBigScheduling projectBigScheduling = new ProjectBigScheduling();
            projectBigScheduling.setSchemeNo(projectBigSchedulingVO.getSchemeNo());
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
            projectBigScheduling.setIsNeedCheck(Scheduling.INVALID_STATUS.getValue());
            int result = projectBigSchedulingMapper.insertSelective(projectBigScheduling);
            if (result != Scheduling.INSERT_SUCCESS.getValue()) {
                return RespData.error("操作失败!");
            }
        }
        return RespData.success();
    }

    /**
     * 获取基础大排期
     *
     * @param schedulingSeo
     * @return
     */
    @Override
    public MyRespBundle<PageInfo<ProjectBigSchedulingVO>> listBigScheduling(SchedulingSeo schedulingSeo) {
        if (schedulingSeo == null) {
            return RespData.error("请检查上传参数");
        }
        PageInfo pageInfo = new PageInfo<>();
        ProjectBigSchedulingExample projectBigSchedulingExample = new ProjectBigSchedulingExample();
        projectBigSchedulingExample.setOrderByClause("create_time desc");
        ProjectBigSchedulingExample.Criteria criteria = projectBigSchedulingExample.createCriteria();
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        if (schedulingSeo.getSchemeNo() == null || schedulingSeo.getSchemeNo().isEmpty()) {
            return RespData.error("请上传案例编号");
        }
        criteria.andSchemeNoEqualTo(schedulingSeo.getSchemeNo());
        PageHelper.startPage(schedulingSeo.getPage(), schedulingSeo.getRows());
        List<ProjectBigScheduling> list = projectBigSchedulingMapper.selectByExample(projectBigSchedulingExample);
        List<ProjectBigSchedulingVO> listVo = BaseToVoUtils.getListVo(list, ProjectBigSchedulingVO.class);
        pageInfo.setList(listVo);
        return RespData.success(pageInfo);
    }

    /**
     * 关联小排期与大排期
     *
     * @param projectSmallSchedulingVOList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateSmallScheduling(List<ProjectSmallSchedulingVO> projectSmallSchedulingVOList) {
        for (ProjectSmallSchedulingVO projectSmallSchedulingVO : projectSmallSchedulingVOList) {
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
        }
        return "操作成功";
    }

    /**
     * 同步上海基础小排期
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String listShangHai(SchedulingSeo schedulingSeo) {
        //获取上海基础小排期信息
        String result = cloudService.getBaseScheduling(Scheduling.BASE_STATUS.getValue(), Scheduling.LIMIT.getValue(), schedulingSeo.getCompanyId());
        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray json = jsonObject.getJSONArray("data");
        if (json.size() == 0) {
            return "上海暂无此公司的施工基础信息!";
        }
        String jsonString = JSONObject.toJSONString(json);
        List<ProjectSmallScheduling> smallList = JSONObject.parseArray(jsonString, ProjectSmallScheduling.class);
        if (smallList.size() == Scheduling.INSERT_FAILD.getValue()) {
            return "操作失败!";
        }
        //获取本地已存在的小排期信息
        List<ProjectSmallScheduling> oldList = projectSmallSchedulingMapper.selectByStatus(Scheduling.BASE_STATUS.getValue());
        if (smallList.size() != oldList.size()) {
            Integer oldVersion = 0;
            Integer sort = 1;
            if (oldList.size() > 0) {
                for (ProjectSmallScheduling old : oldList) {
                    oldVersion = old.getVersion();
                    ProjectSmallSchedulingExample example = new ProjectSmallSchedulingExample();
                    ProjectSmallSchedulingExample.Criteria criteria = example.createCriteria();
                    criteria.andConstructCodeEqualTo(old.getConstructCode());
                    criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
                    criteria.andVersionEqualTo(oldVersion);
                    old.setStatus(Scheduling.INVALID_STATUS.getValue());
                    int i = projectSmallSchedulingMapper.updateByExampleSelective(old, example);
                    if (i != Scheduling.INSERT_SUCCESS.getValue()) {
                        return "操作失败!";
                    }
                }
            }
            for (ProjectSmallScheduling small : smallList) {
                //添加版本
                small.setVersion(oldVersion + 1);
                small.setSort(sort);
                sort++;
                int i = projectSmallSchedulingMapper.insertSelective(small);
                if (i != Scheduling.INSERT_SUCCESS.getValue()) {
                    return "操作失败!";
                }
            }
        } else {
            return "无更新!";
        }
        return "操作成功";
    }

    /**
     * 修改基础大排期
     *
     * @param projectBigSchedulingVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle<String> updateBigScheduling(ProjectBigSchedulingVO projectBigSchedulingVO) {
        if (projectBigSchedulingVO.getSchemeNo().isEmpty()) {
            return RespData.error("请上传案例编号");
        }
        ProjectBigScheduling projectBigScheduling = new ProjectBigScheduling();
        if (projectBigSchedulingVO.getName() != null && !projectBigSchedulingVO.getName().trim().isEmpty()) {
            projectBigScheduling.setName(projectBigSchedulingVO.getName());
        }
        if (projectBigSchedulingVO.getDescription() != null && !projectBigSchedulingVO.getDescription().trim().isEmpty()) {
            projectBigScheduling.setDescription(projectBigSchedulingVO.getDescription());
        }
        if (projectBigSchedulingVO.getIsNew() != null) {
            projectBigScheduling.setIsNew(projectBigSchedulingVO.getIsNew());
        }
        if (projectBigSchedulingVO.getSquareMetreStart() != null) {
            projectBigScheduling.setSquareMetreStart(projectBigSchedulingVO.getSquareMetreStart());
        }
        if (projectBigSchedulingVO.getSquareMetreEnd() != null) {
            projectBigScheduling.setSquareMetreEnd(projectBigSchedulingVO.getSquareMetreEnd());
        }
        if (projectBigSchedulingVO.getRoomNum() != null) {
            projectBigScheduling.setRoomNum(projectBigSchedulingVO.getRoomNum());
        }
        if (projectBigSchedulingVO.getWorkload() != null) {
            projectBigScheduling.setWorkload(projectBigSchedulingVO.getWorkload());
        }
        if (projectBigSchedulingVO.getIsNeedCheck() != null) {
            projectBigScheduling.setIsNeedCheck(projectBigSchedulingVO.getIsNeedCheck());
        }
        if (projectBigSchedulingVO.getRename() != null && !projectBigSchedulingVO.getRename().trim().isEmpty()) {
            projectBigScheduling.setRename(projectBigSchedulingVO.getRename());
        }
        if (projectBigSchedulingVO.getIsWaterTest() != null) {
            projectBigScheduling.setIsWaterTest(projectBigSchedulingVO.getIsWaterTest());
        }
        ProjectBigSchedulingExample example = new ProjectBigSchedulingExample();
        ProjectBigSchedulingExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        criteria.andSortEqualTo(projectBigSchedulingVO.getSort());
        criteria.andSchemeNoEqualTo(projectBigSchedulingVO.getSchemeNo());
        int i = projectBigSchedulingMapper.updateByExampleSelective(projectBigScheduling, example);
        if (i != Scheduling.INSERT_SUCCESS.getValue()) {
            return RespData.error("操作失败!");
        }
        return RespData.success();
    }

    /**
     * 删除基础大排期
     *
     * @param schemeNo
     * @param sort
     * @return
     */
    @Override
    public MyRespBundle<String> deleteBigScheduling(String schemeNo, Integer sort) {
        if (schemeNo == null || schemeNo.isEmpty() || sort == null) {
            return RespData.error("入参不可为空");
        }
        ProjectBigSchedulingExample example = new ProjectBigSchedulingExample();
        ProjectBigSchedulingExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
        criteria.andSchemeNoEqualTo(schemeNo);
        List<ProjectBigScheduling> projectBigSchedulings = projectBigSchedulingMapper.selectByExample(example);
        if (projectBigSchedulings.size() == Scheduling.MATHCHING_NO.getValue()) {
            return RespData.error("此方案编号下此序号大排期不存在");
        }
        Collections.sort(projectBigSchedulings);
        for (ProjectBigScheduling projectBigScheduling : projectBigSchedulings) {
            if (projectBigScheduling.getSort().equals(sort)) {
                ProjectBigSchedulingExample example1 = new ProjectBigSchedulingExample();
                ProjectBigSchedulingExample.Criteria criteria1 = example1.createCriteria();
                criteria1.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
                criteria1.andSchemeNoEqualTo(schemeNo);
                criteria1.andSortEqualTo(sort);
                int i = projectBigSchedulingMapper.deleteByExample(example1);
                if (i != Scheduling.INSERT_SUCCESS.getValue()) {
                    return RespData.error("操作失败!");
                }
            }
            if (projectBigScheduling.getSort() > sort) {
                ProjectBigScheduling scheduling = new ProjectBigScheduling();
                scheduling.setSort(projectBigScheduling.getSort() - 1);
                ProjectBigSchedulingExample example2 = new ProjectBigSchedulingExample();
                ProjectBigSchedulingExample.Criteria criteria2 = example2.createCriteria();
                criteria2.andStatusEqualTo(Scheduling.BASE_STATUS.getValue());
                criteria2.andSchemeNoEqualTo(schemeNo);
                criteria2.andSortEqualTo(projectBigScheduling.getSort());
                int i = projectBigSchedulingMapper.updateByExampleSelective(scheduling, example2);
                if (i != Scheduling.INSERT_SUCCESS.getValue()) {
                    return RespData.error("操作失败!");
                }
            }
        }
        return RespData.success();
    }
}
