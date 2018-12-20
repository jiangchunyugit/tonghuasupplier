package cn.thinkfree.service.project.impl;

import cn.thinkfree.database.mapper.ProjectStageLogMapper;
import cn.thinkfree.database.model.ProjectStageLog;
import cn.thinkfree.database.model.ProjectStageLogExample;
import cn.thinkfree.service.project.ProjectStageLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 项目状态服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/20 16:15
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ProjectStageLogServiceImpl implements ProjectStageLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectStageLogServiceImpl.class);

    @Autowired
    private ProjectStageLogMapper projectStageLogMapper;

    @Override
    public void create(String projectNo, Integer currentStage) {
        ProjectStageLog lastProjectStageLog = findLast(projectNo);
        Date currentDate = new Date();
        if (lastProjectStageLog != null) {
            if (lastProjectStageLog.getEndTime() != null) {
                LOGGER.error("最后一个订单状态记录不为空，projectStageLog.id:{}", lastProjectStageLog.getId());
                throw new RuntimeException();
            }
            updateEndTime(lastProjectStageLog, currentDate);
        }

        ProjectStageLog projectStageLog = new ProjectStageLog();
        projectStageLog.setBeginTime(currentDate);
        projectStageLog.setCreateTime(currentDate);
        projectStageLog.setProjectNo(projectNo);
        projectStageLog.setStage(currentStage);

        insert(projectStageLog);
    }

    private void insert(ProjectStageLog projectStageLog) {
        projectStageLogMapper.insertSelective(projectStageLog);
    }

    private void updateEndTime(ProjectStageLog projectStageLog, Date currentDate) {
        projectStageLog.setEndTime(currentDate);
        projectStageLogMapper.updateByPrimaryKey(projectStageLog);
    }

    private ProjectStageLog findLast(String projectNo) {
        ProjectStageLogExample example = new ProjectStageLogExample();
        example.createCriteria().andProjectNoEqualTo(projectNo);
        example.setOrderByClause("create_time desc");
        List<ProjectStageLog> projectStageLogs = projectStageLogMapper.selectByExample(example);
        return projectStageLogs != null && projectStageLogs.size() > 0 ? projectStageLogs.get(0) : null;
    }
}
