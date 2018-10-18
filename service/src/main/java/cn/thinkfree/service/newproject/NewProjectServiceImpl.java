package cn.thinkfree.service.newproject;

import cn.thinkfree.database.appvo.AppProjectSEO;
import cn.thinkfree.database.appvo.ProjectVo;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

/**
 * 项目相关
 * @author gejiaming
 */
@Service
public class NewProjectServiceImpl implements NewProjectService {


    /**
     * 项目列表
     * @param appProjectSEO
     * @return
     */
    @Override
    public PageInfo<ProjectVo> getAllProject(AppProjectSEO appProjectSEO) {
        return null;
    }
}
