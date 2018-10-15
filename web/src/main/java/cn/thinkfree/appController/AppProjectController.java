package cn.thinkfree.appController;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.appvo.*;
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
        Map<Integer,Object> map = new HashMap<Integer,Object>();
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
        //组合展示实体
        DesignOrderPlayVo designOrderPlayVo = new DesignOrderPlayVo();
        designOrderPlayVo.setConstructionCompany("北京居然设计家装饰有限公司");
        List<PersionVo> persionVoList = new LinkedList<PersionVo>();
        PersionVo persionVo = new PersionVo("15666666666","马云",true,"CM");
        persionVoList.add(persionVo);
        designOrderPlayVo.setPersionList(persionVoList);
        designOrderVo.setDesignOrderPlayVo(designOrderPlayVo);
        designOrderVo.setCancle(false);
        map.put(1,designOrderVo);
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
        //组合展示实体
        ConstructionOrderPlayVo constructionOrderPlayVo = new ConstructionOrderPlayVo();
        constructionOrderPlayVo.setConstructionCompany("北京居然设计家装饰有限公司");
        List<PersionVo> persionVoList1 = new LinkedList<PersionVo>();
        PersionVo persionVo1 = new PersionVo("17888888888","刘强东",true,"CM");
        persionVoList.add(persionVo1);
        constructionOrderPlayVo.setPersionList(persionVoList1);
        constructionOrderPlayVo.setTaskNum(11);
        constructionOrderPlayVo.setCost(32000);
        constructionOrderPlayVo.setSchedule(25);
        constructionOrderPlayVo.setDelay(10);
        constructionOrderVo.setConstructionOrderPlayVo(constructionOrderPlayVo);
        constructionOrderVo.setCancle(true);
        map.put(2,constructionOrderVo);
        projectVo1.setModular(map);
        return sendJsonData(ResultMessage.SUCCESS,projectVo1);
    }

    @RequestMapping(value = "getDesignData",method = RequestMethod.POST)
    @ApiOperation(value = "获取设计资料")
    public MyRespBundle<DesignDataVo> getDesignData(@ApiParam(name = "projectNo",value = "项目编号")String projectNo){
        DesignDataVo designDataVo = new DesignDataVo();
        List<DataDetailVo> dataDetailVoList = new ArrayList<DataDetailVo>();
        List<String> list1 = new ArrayList<String>();
        list1.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        list1.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        list1.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        List<String> list2 = new ArrayList<String>();
        list2.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        list2.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        list2.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        List<String> list3 = new ArrayList<String>();
        list3.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        list3.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        list3.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        DataDetailVo dataDetailVo1 = new DataDetailVo(list1,new Date(),1);
        DataDetailVo dataDetailVo2 = new DataDetailVo(list2,new Date(),2);
        DataDetailVo dataDetailVo3 = new DataDetailVo(list3,new Date(),3);
        dataDetailVoList.add(dataDetailVo1);
        dataDetailVoList.add(dataDetailVo2);
        dataDetailVoList.add(dataDetailVo3);
        designDataVo.setDesignDataList(dataDetailVoList);
        return sendJsonData(ResultMessage.SUCCESS,designDataVo);
    }

    @RequestMapping(value = "getConstructionData",method = RequestMethod.POST)
    @ApiOperation(value = "获取施工资料")
    public MyRespBundle<ConstructionDataVo> getConstructionData(@ApiParam(name = "projectNo",value = "项目编号")String projectNo){
        ConstructionDataVo constructionDataVo = new ConstructionDataVo();
        List<String> list1 = new ArrayList<String>();
        List<DataDetailVo> dataDetailVoList = new ArrayList<DataDetailVo>();
        list1.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        list1.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        list1.add("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");

        DataDetailVo dataDetailVo1 = new DataDetailVo(list1,new Date(),4);
        dataDetailVoList.add(dataDetailVo1);
        constructionDataVo.setConstructionDataList(dataDetailVoList);
        return sendJsonData(ResultMessage.SUCCESS,constructionDataVo);
    }

    @RequestMapping(value = "cancleOrder",method = RequestMethod.POST)
    @ApiOperation(value = "取消订单")
    public MyRespBundle cancleOrder(@ApiParam(name = "orderNo",value = "订单编号")String orderNo ){
        String result = "取消成功";
        return sendSuccessMessage(result);
    }


//    @RequestMapping(value = "",method = RequestMethod.POST)
//    @ApiOperation(value = "")
//    public MyRespBundle<> (@ApiParam(name = "",value = "") ){
//
//        return sendJsonData(ResultMessage.SUCCESS,);
//    }


}
