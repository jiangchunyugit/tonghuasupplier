package cn.thinkfree.controller;


import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.ConstructCountVO;
import cn.thinkfree.database.vo.construct.ConstructOrderDetailVO;
import cn.thinkfree.service.construction.*;
import cn.thinkfree.service.construction.vo.*;
import cn.thinkfree.service.platform.vo.PageVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    private ConstructOrderService constructOrderService;

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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数"),
            @ApiImplicitParam(name = "cityName", value = "城市名称"),
            @ApiImplicitParam(name = "orderType", value = "订单类型，1派单列表，2订单列表"),
    })
    public MyRespBundle<PageInfo<ConstructionOrderListVo>> getConstructionInfoList(
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false, defaultValue = "10")  int pageSize,
            @RequestParam(required = false) String cityName,
            @RequestParam(required = false, defaultValue = "1") int orderType){
        return sendJsonData(ResultMessage.SUCCESS, constructOrderService.getOrderList(pageNum,pageSize,cityName, orderType));
    }


    @ApiOperation("获取施工订单列表")
    @ResponseBody
    @RequestMapping(value = "consList",method = {RequestMethod.POST,RequestMethod.GET})
    public MyRespBundle<PageVo<List<ConsListVo>>> getConsList(
            @ApiParam(name = "orderType", value = "列表类型，1派单列表，2订单列表") @RequestParam(required = false, defaultValue = "2") int orderType,
            @ApiParam(name = "projectNo", value = "项目编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "companyName", value = "公司名称") @RequestParam(name = "companyName", required = false) String companyName,
            @ApiParam(name = "provinceCode", value = "省份编码") @RequestParam(name = "provinceCode", required = false) String provinceCode,
            @ApiParam(name = "cityCode", value = "城市名称") @RequestParam(name = "cityCode", required = false) String cityCode,
            @ApiParam(name = "areaCode", value = "区域名称") @RequestParam(name = "areaCode", required = false) String areaCode,
            @ApiParam(name = "createTimeS", value = "创建时间开始，yyyy-MM-dd") @RequestParam(name = "createTimeS", required = false) String createTimeS,
            @ApiParam(name = "createTimeE", value = "创建时间结束，yyyy-MM-dd") @RequestParam(name = "createTimeE", required = false) String createTimeE,
            @ApiParam(name = "againTimeS", value = "签约时间开始，yyyy-MM-dd") @RequestParam(name = "againTimeS", required = false) String againTimeS,
            @ApiParam(name = "againTimeE", value = "签约时间结束，yyyy-MM-dd") @RequestParam(name = "againTimeE", required = false) String againTimeE,
            @ApiParam(name = "address", value = "项目地址") @RequestParam(name = "address", required = false) String address,
            @ApiParam(name = "ownerName", value = "业主姓名") @RequestParam(name = "ownerName", required = false) String ownerName,
            @ApiParam(name = "ownerPhone", value = "业主手机号") @RequestParam(name = "ownerPhone", required = false) String ownerPhone,
            @ApiParam(name = "pageIndex", value = "第几页") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex,
            @ApiParam(name = "pageSize", value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize){
        try{
            return sendJsonData(ResultMessage.SUCCESS,constructOrderService.getConsList(orderType, projectNo, companyName, provinceCode, cityCode, areaCode, createTimeS,
                    createTimeE, againTimeS, againTimeE, address, ownerName, ownerPhone, pageIndex, pageSize));
        }catch (Exception e){
            e.printStackTrace();
            return sendFailMessage(e.getMessage());
        }
    }


    @ApiOperation("运营平台接口（获取施工订单统计/城市）---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getOperateNum", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderManageVo> getOperateNum(){

        return constructOrderService.getOrderNum();
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
        PageInfo<DecorationOrderListVo> pageInfo = decorationDistributionOrder.getOrderList(companyNo, pageNum, pageSize, projectNo, appointmentTime, addressDetail, owner, phone, orderStage);

        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
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
        try{
            decorationDistributionOrder.appointWorker(workerInfo);
        }catch (Exception e){
            e.printStackTrace();
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("装饰平台接口（获取施工订单列表）---->迎喜专用")
    @MyRespBody
    @RequestMapping(value = "getDecorationOrderList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderCommonVo> getDecorationOrderList(@RequestParam @ApiParam(value = "公司编号",required = true) String companyNo,
                                                                          @RequestParam @ApiParam(value = "页码",required = true) int pageNum,
                                                                          @RequestParam @ApiParam(value = "每页条数",required = true) int pageSize){

        PageInfo<ConstructionOrderListVo> pageInfo = decorationOrderOperate.getDecorationOrderList(companyNo, pageNum, pageSize);
        return sendJsonData(ResultMessage.SUCCESS, pageInfo);
    }

    @ApiOperation("装饰平台接口（获取施工订单统计）---->迎喜专用")
    @MyRespBody
    @RequestMapping(value = "getDecorationtOrderNum", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderManageVo> getDecorationtOrderNum(){

        return decorationOrderOperate.getDecorationtOrderNum();
    }

    @ApiOperation("装饰平台接口（精准报价列表）---->松辉专用")
    @MyRespBody
    @RequestMapping(value = "getOfferList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageInfo<PrecisionPriceVo>> getOfferList(
            @RequestParam @ApiParam(value = "公司编号",required = true) String companyNo,
            @RequestParam(defaultValue = "10") @ApiParam(value = "页码",required = true) int pageNum,
            @RequestParam(defaultValue = "1") @ApiParam(value = "每页条数",required = true) int pageSize){
        return otherService.getOfferList(companyNo,pageNum,pageSize);
    }

    @ApiOperation("装饰平台接口（精准报价,获取项目信息）---->松辉专用")
    @MyRespBody
    @RequestMapping(value = "getProject", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<OfferProjectVo> getProject(
            @RequestParam @ApiParam(value = "项目编号",required = true) String projectNo){
        return otherService.getProject(projectNo);
    }

    @RequestMapping(value = "/projectApprovalList", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="施工订单统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
            @ApiImplicitParam(name = "approvalType", value = "审批类型", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页个数", required = true),
    })
    public MyRespBundle<ConstructCountVO> count(@RequestParam(name = "userId") String userId,
                                                @RequestParam(name = "approvalType") String approvalType,
                                                @RequestParam(name = "pageNum") Integer pageNum,
                                                @RequestParam(name = "pageSize") Integer pageSize){
        return sendJsonData(ResultMessage.SUCCESS, constructOrderService.count(userId, approvalType, pageNum, pageSize));
    }

    @RequestMapping(value = "/detail", method = {RequestMethod.POST, RequestMethod.GET})
    @MyRespBody
    @ApiOperation(value="施工订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectNo", value = "项目编号编号", required = true)
    })
    public MyRespBundle<ConstructOrderDetailVO> detail(@RequestParam(name = "projectNo") String projectNo){
        return sendJsonData(ResultMessage.SUCCESS, constructOrderService.detail(projectNo));
    }
}
