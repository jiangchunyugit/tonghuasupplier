package cn.thinkfree.appcontroller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.appvo.*;
import cn.thinkfree.service.newproject.NewProjectService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags = "项目相关")
@RestController
@RequestMapping(value = "project")
public class AppProjectController extends AbsBaseController {
    @Autowired
    private NewProjectService newProjectService;

    @RequestMapping(value = "getAllProject", method = RequestMethod.POST)
    @ApiOperation(value = "项目列表")
    public MyRespBundle<PageInfo<ProjectVo>> getAllProject(@ApiParam(name = "appProjectSEO", value = "项目列表入参实体") AppProjectSEO appProjectSEO) {
        PageInfo<ProjectVo> page1 = newProjectService.getAllProject(appProjectSEO);
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
        return sendJsonData(ResultMessage.SUCCESS, page);
    }

    @RequestMapping(value = "getProjectDetail", method = RequestMethod.POST)
    @ApiOperation(value = "获取项目详情接口")
    public MyRespBundle<ProjectVo> getProjectDetail(@ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
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
        List<ProjectOrderDetailVo> projectOrderDetailVoList = new ArrayList<>();
        //设计订单
        ProjectOrderDetailVo projectOrderDetailVo = new ProjectOrderDetailVo();
        List<OrderTaskSortVo> designList = new LinkedList<>();
        OrderTaskSortVo designBaseVo1 = new OrderTaskSortVo(3, "量房费待支付");
        OrderTaskSortVo designBaseVo2 = new OrderTaskSortVo(1, "派单中");
        OrderTaskSortVo designBaseVo3 = new OrderTaskSortVo(5, "户型图待确认");
        OrderTaskSortVo designBaseVo4 = new OrderTaskSortVo(2, "已接单");
        OrderTaskSortVo designBaseVo5 = new OrderTaskSortVo(4, "户型图待交付");
        designList.add(designBaseVo1);
        designList.add(designBaseVo2);
        designList.add(designBaseVo3);
        designList.add(designBaseVo4);
        designList.add(designBaseVo5);
        projectOrderDetailVo.setOrderTaskSortVoList(designList);
        projectOrderDetailVo.setTaskStage(1);
        projectOrderDetailVo.setOrderNo("2918308308303883");
        projectOrderDetailVo.setOrderType(1);
        projectOrderDetailVo.setType(1);
        projectOrderDetailVo.setCancle(false);
        //组合展示实体
        OrderPlayVo orderPlayVo = new OrderPlayVo();
        orderPlayVo.setConstructionCompany("北京居然设计家装饰有限公司");
        List<PersionVo> persionVoList = new LinkedList<PersionVo>();
        PersionVo persionVo = new PersionVo("15666666666", "马云", true, "CM");
        persionVoList.add(persionVo);
        orderPlayVo.setPersionList(persionVoList);
        orderPlayVo.setTaskNum(10);
        orderPlayVo.setCost(100000);
        orderPlayVo.setSchedule(20);
        orderPlayVo.setDelay(2);
        projectOrderDetailVo.setOrderPlayVo(orderPlayVo);
        projectOrderDetailVoList.add(projectOrderDetailVo);
        //施工订单
        ProjectOrderDetailVo constructionOrderVo = new ProjectOrderDetailVo();
        List<OrderTaskSortVo> constructionList = new LinkedList<>();
        OrderTaskSortVo constructionBaseVo1 = new OrderTaskSortVo(3, "水电暖阶段3");
        OrderTaskSortVo constructionBaseVo2 = new OrderTaskSortVo(1, "水电暖阶段");
        OrderTaskSortVo constructionBaseVo3 = new OrderTaskSortVo(5, "水电暖阶段5");
        OrderTaskSortVo constructionBaseVo4 = new OrderTaskSortVo(2, "水电暖阶段2");
        OrderTaskSortVo constructionBaseVo5 = new OrderTaskSortVo(4, "水电暖阶段4");
        constructionList.add(constructionBaseVo1);
        constructionList.add(constructionBaseVo2);
        constructionList.add(constructionBaseVo3);
        constructionList.add(constructionBaseVo4);
        constructionList.add(constructionBaseVo5);
        constructionOrderVo.setOrderTaskSortVoList(constructionList);
        constructionOrderVo.setTaskStage(2);
        constructionOrderVo.setOrderNo("2918308308303883");
        constructionOrderVo.setOrderType(2);
        constructionOrderVo.setType(1);
        constructionOrderVo.setCancle(true);
        //组合展示实体
        OrderPlayVo orderPlayVo1 = new OrderPlayVo();
        orderPlayVo.setConstructionCompany("北京居然设计家装饰有限公司");
        List<PersionVo> persionVoList1 = new LinkedList<PersionVo>();
        PersionVo persionVo1 = new PersionVo("17888888888", "刘强东", true, "CM");
        persionVoList1.add(persionVo1);
        orderPlayVo1.setPersionList(persionVoList1);
        orderPlayVo1.setTaskNum(10);
        orderPlayVo1.setCost(100000);
        orderPlayVo1.setSchedule(20);
        orderPlayVo1.setDelay(2);
        constructionOrderVo.setOrderPlayVo(orderPlayVo1);
        projectOrderDetailVoList.add(constructionOrderVo);
        projectVo1.setProjectOrderDetailVoList(projectOrderDetailVoList);
        return sendJsonData(ResultMessage.SUCCESS, projectVo1);
    }

    @RequestMapping(value = "getDesignData", method = RequestMethod.POST)
    @ApiOperation(value = "获取设计资料")
    public MyRespBundle<DataVo> getDesignData(@ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
        DataVo designDataVo = new DataVo();
        List<DataDetailVo> dataDetailVoList = new ArrayList<DataDetailVo>();
        List<UrlDetailVo> list1 = new ArrayList<>();
        list1.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaa"));
        list1.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaa"));
        list1.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaa"));
        List<UrlDetailVo> list2 = new ArrayList<>();
        list2.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaa"));
        list2.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaa"));
        list2.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaa"));
        List<UrlDetailVo> list3 = new ArrayList<>();
        list3.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaa"));
        list3.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaa"));
        list3.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaa"));
        DataDetailVo dataDetailVo1 = new DataDetailVo(list1, "", new Date(), "客厅施工图", true, "http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        DataDetailVo dataDetailVo2 = new DataDetailVo(list2, "", new Date(), "客厅施工图", false, "http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        DataDetailVo dataDetailVo3 = new DataDetailVo(list3, "", new Date(), "客厅施工图", true, "http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202");
        dataDetailVoList.add(dataDetailVo1);
        dataDetailVoList.add(dataDetailVo2);
        dataDetailVoList.add(dataDetailVo3);
        designDataVo.setDataList(dataDetailVoList);
        return sendJsonData(ResultMessage.SUCCESS, designDataVo);
    }

    @RequestMapping(value = "getConstructionData", method = RequestMethod.POST)
    @ApiOperation(value = "获取施工资料")
    public MyRespBundle<DataDetailVo> getConstructionData(@ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
        List<UrlDetailVo> list1 = new ArrayList<>();
        list1.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaaa"));
        list1.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaaa"));
        list1.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaaa"));
        DataDetailVo dataDetailVo1 = new DataDetailVo(list1, "", new Date(), "客厅施工图", false, null);
        return sendJsonData(ResultMessage.SUCCESS, dataDetailVo1);
    }

    @RequestMapping(value = "getQuotationData", method = RequestMethod.POST)
    @ApiOperation(value = "获取报价单资料")
    public MyRespBundle<DataDetailVo> getQuotationData(@ApiParam(name = "projectNo", value = "项目编号") String projectNo) {
        List<UrlDetailVo> list1 = new ArrayList<>();
        list1.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaaa"));
        list1.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaaa"));
        list1.add(new UrlDetailVo("http://www.fang668.com/fang668-gcxz/previewimg.asp?softid=76202",new Date(),"aaaa"));
        DataDetailVo dataDetailVo1 = new DataDetailVo(list1, "", new Date(), "客厅施工图", false, null);
        return sendJsonData(ResultMessage.SUCCESS, dataDetailVo1);
    }

    @RequestMapping(value = "cancleOrder", method = RequestMethod.POST)
    @ApiOperation(value = "取消订单")
    public MyRespBundle cancleOrder(@ApiParam(name = "orderNo", value = "订单编号") String orderNo,
                                    @ApiParam(name = "cancelReason", value = "取消原因") String cancelReason) {
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
