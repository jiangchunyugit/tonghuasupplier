package cn.thinkfree.service.newproject;

import cn.thinkfree.database.appvo.AppProjectSEO;
import cn.thinkfree.database.appvo.ProjectVo;
import com.github.pagehelper.PageInfo;

/**
 * 项目相关
 * @author gejiaming
 */
public interface NewProjectService {
    /**
     * 项目列表
     * @param appProjectSEO
     * @return
     */
    PageInfo<ProjectVo> getAllProject(AppProjectSEO appProjectSEO);
}
