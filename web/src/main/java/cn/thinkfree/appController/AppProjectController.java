package cn.thinkfree.appController;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.appVo.ProjectVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Api(tags ="项目相关" )
@RestController
@RequestMapping(value = "project")
public class AppProjectController extends AbsBaseController{

    @RequestMapping(value = "getAllProject",method = RequestMethod.POST)
    @ApiOperation(value = "sdf")
    public MyRespBundle<List<ProjectVo>> getAllProject(@ApiParam(name = "userId",value = "用户id") String userId){
        List<ProjectVo> projectVoList = new LinkedList<ProjectVo>();
        ProjectVo projectVo1 = new ProjectVo();
        projectVo1.setProjectNo("1223098338391");
        projectVo1.setDesignProgress(50);
        projectVo1.setConstructionProgress(80);
        projectVo1.setAddress("北京市昌平区立汤路天通苑小区20号楼01单元1010室");
        projectVo1.setReleaseTime(new Date());
        projectVo1.setImgUrl("http://www.photophoto.cn/show/11674265.html");
        projectVo1.setView3d(true);
        projectVo1.setProjectDynamic(0);
        projectVo1.setProjectOrder(0);
        projectVo1.setProjectData(1);

        projectVoList.add(projectVo1);
        ProjectVo projectVo2 = new ProjectVo();
        projectVo2.setProjectNo("1223098338392");
        projectVo2.setDesignProgress(60);
        projectVo2.setConstructionProgress(70);
        projectVo2.setAddress("北京市昌平区立汤路天通苑小区20号楼01单元1010室");
        projectVo2.setReleaseTime(new Date());
        projectVo2.setImgUrl("http://www.photophoto.cn/show/11674265.html");
        projectVo2.setView3d(true);
        projectVo2.setProjectDynamic(0);
        projectVo2.setProjectOrder(0);
        projectVo2.setProjectData(1);

        projectVoList.add(projectVo2);
        ProjectVo projectVo3 = new ProjectVo();
        projectVo3.setProjectNo("1223098338393");
        projectVo3.setDesignProgress(70);
        projectVo3.setConstructionProgress(90);
        projectVo3.setAddress("北京市昌平区立汤路天通苑小区20号楼01单元1010室");
        projectVo3.setReleaseTime(new Date());
        projectVo3.setImgUrl("http://www.photophoto.cn/show/11674265.html");
        projectVo3.setView3d(true);
        projectVo3.setProjectDynamic(0);
        projectVo3.setProjectOrder(0);
        projectVo3.setProjectData(1);
        projectVoList.add(projectVo3);
        return sendJsonData(ResultMessage.SUCCESS,projectVoList);
    }
}
