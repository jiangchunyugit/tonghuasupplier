package cn.thinkfree.service.project;

/**
 * 项目状态服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/12/20 16:15
 */
public interface ProjectStageLogService {

    /**
     * 创建项目状态记录
     * @param projectNo 项目编号
     * @param currentStage 当前项目状态
     */
    void create(String projectNo, Integer currentStage);
}
