package cn.thinkfree.service.newproject;

import cn.thinkfree.database.appvo.AppProjectSEO;
import cn.thinkfree.database.appvo.PersionVo;
import cn.thinkfree.database.appvo.ProjectVo;
import cn.thinkfree.database.mapper.ConstructionOrderMapper;
import cn.thinkfree.database.mapper.OrderUserMapper;
import cn.thinkfree.database.mapper.ProjectMapper;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.model.ConstructionOrderExample;
import cn.thinkfree.database.model.Project;
import cn.thinkfree.database.model.ProjectExample;
import cn.thinkfree.service.neworder.NewOrderService;
import cn.thinkfree.service.utils.BaseToVoUtils;
import cn.thinkfree.service.utils.MathUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 项目相关
 *
 * @author gejiaming
 */
@Service
public class NewProjectServiceImpl implements NewProjectService {
    @Autowired
    OrderUserMapper orderUserMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    NewOrderService newOrderService;


    /**
     * 项目列表
     *
     * @param appProjectSEO
     * @return
     */
    @Override
    public PageInfo<ProjectVo> getAllProject(AppProjectSEO appProjectSEO) {
        PageHelper.startPage(appProjectSEO.getPage(), appProjectSEO.getRows());
        PageInfo<ProjectVo> pageInfo = new PageInfo<>();
        //查询此人名下所有项目
        List<String> list = orderUserMapper.selectByUserId(appProjectSEO.getUserId());
        //根据项目编号查询项目信息
        ProjectExample example = new ProjectExample();
        ProjectExample.Criteria criteria = example.createCriteria();
        criteria.andProjectNoIn(list);
        List<Project> projects = projectMapper.selectByExample(example);
        List<ProjectVo> projectVoList = new ArrayList<>();
        for (Project project:projects){
            ProjectVo projectVo = null;
            try {
                projectVo = BaseToVoUtils.getVo(project,ProjectVo.class,BaseToVoUtils.getProjectMap());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //添加进度信息
            projectVo.setConstructionProgress(MathUtil.getPercentage(project.getPlanStartTime(), project.getPlanEndTime(), new Date()));
            projectVoList.add(projectVo);
        }
        pageInfo.setList(projectVoList);
        return pageInfo;
    }
}
