package cn.thinkfree.controller;


import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.*;
import cn.thinkfree.service.construction.impl.ConstrutionDistributionOrderImpl;
import cn.thinkfree.service.construction.vo.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 *  施工状态相关接口
 */
@Api(value = "施工订单相关API接口", tags = "施工订单相关API接口---->孙宇/迎喜专用")
@Controller
@RequestMapping("construction")
public class ConstructionOrderController extends AbsBaseController {

    @Autowired
    ConstructionStateService constructionStateService;

    @Autowired
    ConstructionOrderOperate constructionOrderOperate;

    @Autowired
    ConstrutionDistributionOrder construtionDistributionOrder;

    @Autowired
    DecorationDistributionOrder decorationDistributionOrder;

    @Autowired
    DecorationOrderOperate decorationOrderOperate;

    @Autowired
    OtherService otherService;




    @ApiOperation("运营平台接口（获取施工订单列表）---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getOperateList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderCommonVo> getConstructionInfoList(@RequestParam @ApiParam(value = "页码",required = true) int pageNum,
                                                                           @RequestParam @ApiParam(value = "每页条数",required = true) int pageSize,
                                                                           @RequestParam(required = false) @ApiParam(value = "城市名称")  String cityName){

        return constructionOrderOperate.getOrderList(pageNum,pageSize,cityName);
    }

    @ApiOperation("运营平台接口（获取施工订单统计/城市）---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getOperateNum", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderManageVo> getOperateNum(){

        return constructionOrderOperate.getOrderNum();
    }

    @ApiOperation("运营平台接口（获取施工订单统计/城市(项目派单)）---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getComDistributionOrderNum", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderDistributionNumVo> getComDistributionOrderNum(){

        return construtionDistributionOrder.getComDistributionOrderNum();
    }

    @ApiOperation("运营平台接口（施工派单-公司列表接口-含搜索公司）---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getCityList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<DistributionOrderCityVo>> getCityList(@RequestParam(required = false) @ApiParam(value = "公司名称")   String companyName){

        return construtionDistributionOrder.getCityList(companyName);
    }

    @ApiOperation("运营平台接口（施工派单-公司列表接口-含搜索公司）---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "distributionCompany", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> DistributionCompany(@RequestParam @ApiParam(value = "订单编号",required = true) String orderNo,
                                                                     @RequestParam @ApiParam(value = "公司ID",required = true) String companyId){

        return construtionDistributionOrder.DistributionCompany(orderNo,companyId);
    }


    @ApiOperation("装饰平台接口（施工派单-给员工-含搜索）---->迎喜专用")
    @MyRespBody
    @RequestMapping(value = "decorationOrderList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<DecorationOrderCommonVo> getOrderList(@RequestParam @ApiParam(value = "公司编号",required = true) String companyNo,
                                                              @RequestParam @ApiParam(value = "页码",required = true) int pageNum,
                                                              @RequestParam @ApiParam(value = "每页条数",required = true) int pageSize,
                                                              @RequestParam(required = false) @ApiParam(value = "项目编号")  String projectNo,
                                                              @RequestParam(required = false) @ApiParam(value = "预约时间")  String appointmentTime,
                                                              @RequestParam(required = false) @ApiParam(value = "详细地址")  String addressDetail,
                                                              @RequestParam(required = false) @ApiParam(value = "业主姓名")  String owner,
                                                              @RequestParam(required = false) @ApiParam(value = "业主电话")  String phone,
                                                              @RequestParam(required = false) @ApiParam(value = "订单状态")  String orderStage){

        return decorationDistributionOrder.getOrderList(companyNo,pageNum,pageSize, projectNo, appointmentTime,addressDetail,owner,phone,orderStage);
    }

    @ApiOperation("装饰平台接口（施工派单-给员工-数码统计）---->迎喜专用")
    @MyRespBody
    @RequestMapping(value = "decorationOrderNum", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderManageVo> getDistributionOrderNum(){

        return decorationDistributionOrder.getOrderNum();
    }

    @ApiOperation("装饰平台接口（施工派单-员工列表）---->迎喜专用")
    @MyRespBody
    @RequestMapping(value = "appointWorkerList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<Map<String, List<appointWorkerListVo>>> appointWorkerList(@RequestParam @ApiParam(value = "公司编号",required = true) String companyId){

        return decorationDistributionOrder.appointWorkerList(companyId);
    }

    @ApiOperation("装饰平台接口（施工派单-确认派单）---->迎喜专用")
    @MyRespBody
    @RequestMapping(value = "appointWorker", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> appointWorker(@RequestBody@ApiParam(value = "员工信息",required = true) List<Map<String,String>> workerInfo){

        return decorationDistributionOrder.appointWorker(workerInfo);
    }

    @ApiOperation("装饰平台接口（获取施工订单列表）---->迎喜专用")
    @MyRespBody
    @RequestMapping(value = "getDecorationOrderList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderCommonVo> getDecorationOrderList(@RequestParam @ApiParam(value = "公司编号",required = true) String companyNo,
                                                                          @RequestParam @ApiParam(value = "页码",required = true) int pageNum,
                                                                          @RequestParam @ApiParam(value = "每页条数",required = true) int pageSize){

        return decorationOrderOperate.getDecorationOrderList(companyNo,pageNum,pageSize);
    }

    @ApiOperation("装饰平台接口（获取施工订单统计）---->迎喜专用")
    @MyRespBody
    @RequestMapping(value = "getDecorationtOrderNum", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderManageVo> getDecorationtOrderNum(){

        return decorationOrderOperate.getDecorationtOrderNum();
    }

    @ApiOperation("装饰平台接口（精准报价列表）---->松辉专用")
    @MyRespBody
    @RequestMapping(value = "getPrecisionPriceList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageInfo<PrecisionPriceVo>> getPrecisionPriceList(
            @RequestParam @ApiParam(value = "公司编号",required = true) String companyNo,
            @RequestParam(defaultValue = "10") @ApiParam(value = "页码",required = true) int pageNum,
            @RequestParam(defaultValue = "1") @ApiParam(value = "每页条数",required = true) int pageSize){
        return otherService.getPrecisionPriceList(companyNo,pageNum,pageSize);
    }

}
