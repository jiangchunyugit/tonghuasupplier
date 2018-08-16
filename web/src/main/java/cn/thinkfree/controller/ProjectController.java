package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.PreProjectGuide;
import cn.thinkfree.database.vo.ProjectSEO;
import cn.thinkfree.database.vo.ProjectVO;
import cn.thinkfree.service.project.ProjectService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/project")
public class ProjectController extends AbsBaseController {


    @Autowired
    ProjectService projectService;


    /**
     * 项目列表
     * @param projectSEO
     * @return
     */
    @GetMapping("/list")
    @MyRespBody
    public MyRespBundle<PageInfo<PreProjectGuide>> list(ProjectSEO projectSEO){

        PageInfo<ProjectVO> pageInfo = projectService.pageProjectBySEO(projectSEO);

        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

}
