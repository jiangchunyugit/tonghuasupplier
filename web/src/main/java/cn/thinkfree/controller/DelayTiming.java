package cn.thinkfree.controller;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.database.mapper.ProjectBigSchedulingDetailsMapper;
import cn.thinkfree.database.mapper.ProjectSchedulingMapper;
import cn.thinkfree.database.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: jiang
 * @Date: 2018/11/6 11:04
 * @Description: 延期定时任务(每天凌晨1点执行)
 */
@Slf4j
@Component
public class DelayTiming {
    private static final MyLogger LOGGER = new MyLogger(DelayTiming.class);

    @Autowired
    private ProjectBigSchedulingDetailsMapper projectBigSchedulingDetailsMapper;
    @Autowired
    private ProjectSchedulingMapper projectSchedulingMapper;

    @Scheduled(cron = "0 0 1 * * ?")
    public void timing() {
        try {
            LOGGER.info("开始执行定时任务");
            ProjectBigSchedulingDetailsExample projectBigSchedulingDetailsExample = new ProjectBigSchedulingDetailsExample();
            //查询未完成的阶段项目
            projectBigSchedulingDetailsExample.createCriteria().andIsCompletedEqualTo(0);
            List<ProjectBigSchedulingDetails> projectBigSchedulingDetails = projectBigSchedulingDetailsMapper.selectByExample(projectBigSchedulingDetailsExample);
            if (projectBigSchedulingDetails != null) {
                Date currentDate = new Date();
                projectBigSchedulingDetails.forEach((big) -> {
                    //筛选当前已超过计划的时间
                    if (big.getPlanEndTime().before(currentDate)) {
                        ProjectSchedulingExample projectSchedulingExample = new ProjectSchedulingExample();
                        projectSchedulingExample.createCriteria().andProjectNoEqualTo(big.getProjectNo());
                        //查询对应项目的排期
                        List<ProjectScheduling> projectSchedulings = projectSchedulingMapper.selectByExample(projectSchedulingExample);
                        if (projectSchedulings.size() == 1) {
                            ProjectScheduling projectScheduling = projectSchedulings.get(0);
                            ProjectScheduling scheduling = new ProjectScheduling();
                            //获得当前延期天数
                            Integer delay = projectScheduling.getDelay();
                            if (projectScheduling.getDelay() != null) {
                                scheduling.setDelay(delay + 1);
                            } else if (projectScheduling.getDelay() == null) {
                                scheduling.setDelay(1);
                            }
                            projectSchedulingMapper.updateByExampleSelective(scheduling, projectSchedulingExample);
                        } else if (projectSchedulings.size() > 1) {
                            throw new RuntimeException();
                        } else if (projectSchedulings.size() == 0) {
                            throw new RuntimeException();

                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

