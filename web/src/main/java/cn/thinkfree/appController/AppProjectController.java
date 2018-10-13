package cn.thinkfree.appController;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.appVo.*;
import cn.thinkfree.database.model.LogInfo;
import cn.thinkfree.database.model.ProjectBigScheduling;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags ="项目相关" )
@RestController
@RequestMapping(value = "project")
public class AppProjectController extends AbsBaseController{

    @RequestMapping(value = "getAllProject",method = RequestMethod.POST)
    @ApiOperation(value = "项目列表")
    public MyRespBundle<PageInfo<ProjectVo>> getAllProject(@ApiParam(name = "appProjectSEO",value = "项目列表入参实体") AppProjectSEO appProjectSEO){
        PageInfo<ProjectVo> page = new PageInfo<ProjectVo>();
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
        page.setList(projectVoList);
        return sendJsonData(ResultMessage.SUCCESS,page);
    }

    @RequestMapping(value = "getProjectDetail",method = RequestMethod.POST)
    @ApiOperation(value = "获取项目详情接口")
    public MyRespBundle<ProjectVo> getProjectDetail(@ApiParam(name = "projectNo",value = "项目编号")String projectNo ){
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
        Map<String,Object> map = new HashMap<String,Object>();
        //设计订单
        DesignOrderVo designOrderVo = new DesignOrderVo();
        designOrderVo.setOrderNo("2918308308303883");
        designOrderVo.setType(1);
        designOrderVo.setTaskStage(1);
        List<DesignBaseVo> designList = new LinkedList<DesignBaseVo>();
        DesignBaseVo designBaseVo1 = new DesignBaseVo(3,"量房费待支付");
        DesignBaseVo designBaseVo2 = new DesignBaseVo(1,"派单中");
        DesignBaseVo designBaseVo3 = new DesignBaseVo(5,"户型图待确认");
        DesignBaseVo designBaseVo4 = new DesignBaseVo(2,"已接单");
        DesignBaseVo designBaseVo5 = new DesignBaseVo(4,"户型图待交付");
        designList.add(designBaseVo1);
        designList.add(designBaseVo2);
        designList.add(designBaseVo3);
        designList.add(designBaseVo4);
        designList.add(designBaseVo5);
        designOrderVo.setDesignTask(designList);
        designOrderVo.setTaskStage(3);
        designOrderVo.setDesignCompany("北京居然设计家装饰有限公司");
        designOrderVo.setDesigner("马云");
        designOrderVo.setPhone("15666666666");
        designOrderVo.setTaskNum(11);
        designOrderVo.setCost(100000);
        designOrderVo.setSchedule(20);
        designOrderVo.setDelay(2);
        designOrderVo.setCancle(false);
        map.put("装修设计",designOrderVo);
        //施工订单
        ConstructionOrderVo constructionOrderVo = new ConstructionOrderVo();
        constructionOrderVo.setOrderNo("2918308308303883");
        constructionOrderVo.setType(1);
        constructionOrderVo.setTaskStage(3);
        List<SchedulingBaseBigVo> proList = new LinkedList<SchedulingBaseBigVo>();
        SchedulingBaseBigVo pro1 = new SchedulingBaseBigVo();
        pro1.setSort(2);
        pro1.setName("水电暖阶段2");
        proList.add(pro1);
        SchedulingBaseBigVo pro2 = new SchedulingBaseBigVo();
        pro2.setSort(4);
        pro2.setName("水电暖阶段4");
        proList.add(pro2);
        SchedulingBaseBigVo pro3 = new SchedulingBaseBigVo();
        pro3.setSort(5);
        pro3.setName("水电暖阶段5");
        proList.add(pro3);
        SchedulingBaseBigVo pro4 = new SchedulingBaseBigVo();
        pro4.setSort(1);
        pro4.setName("水电暖阶段");
        proList.add(pro4);
        SchedulingBaseBigVo pro5 = new SchedulingBaseBigVo();
        pro5.setSort(3);
        pro5.setName("水电暖阶段3");
        proList.add(pro5);
        constructionOrderVo.setProjectBigSchedulingList(proList);
        constructionOrderVo.setTaskStage(2);
        constructionOrderVo.setConstructionCompany("北京居然设计家装饰有限公司");
        constructionOrderVo.setForeman("刘强东");
        constructionOrderVo.setPhone("17888888888");
        constructionOrderVo.setTaskNum(11);
        constructionOrderVo.setCost(100000);
        constructionOrderVo.setSchedule(20);
        constructionOrderVo.setDelay(2);
        constructionOrderVo.setCancle(true);
        map.put("施工任务",constructionOrderVo);
        projectVo1.setModular(map);
        return sendJsonData(ResultMessage.SUCCESS,projectVo1);
    }


//    @RequestMapping(value = "",method = RequestMethod.POST)
//    @ApiOperation(value = "")
//    public MyRespBundle<> (@ApiParam(name = "",value = "") ){
//
//        return sendJsonData(ResultMessage.SUCCESS,);
//    }


}
