package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.ReserveProject;
import cn.thinkfree.service.platform.designer.ReserveOrderService;
import cn.thinkfree.service.platform.vo.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author xusonghui
 * 预约订单
 */
@Api(value = "待转换订单接口", tags = "待转换订单接口")
@Controller
@RequestMapping("reserveOrder")
public class ReserveOrderController extends AbsBaseController {
    @Autowired
    private ReserveOrderService reserveOrderService;

    @ApiOperation("创建待转换订单")
    @MyRespBody
    @RequestMapping(value = "create", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle createReserveOrder(
            @ApiParam(name = "ownerName", required = true, value = "业主姓名") @RequestParam(name = "ownerName", required = true) String ownerName,
            @ApiParam(name = "phone", required = true, value = "业主手机号") @RequestParam(name = "phone", required = true) String phone,
            @ApiParam(name = "address", required = false, value = "装修地址") @RequestParam(name = "address", required = false) String address,
            @ApiParam(name = "source", required = false, value = "来源，1,运营后台创建，2设计公司创建，3天猫，4业主预约") @RequestParam(name = "source", required = false) int source,
            @ApiParam(name = "style", required = false, value = "装修风格，见具体接口") @RequestParam(name = "style", required = false) int style,
            @ApiParam(name = "budget", required = false, value = "装修预算") @RequestParam(name = "budget", required = false) String budget,
            @ApiParam(name = "acreage", required = false, value = "房屋面积") @RequestParam(name = "acreage", required = false) String acreage,
            @ApiParam(name = "userId", required = false, value = "操作人ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId) {
        try {
            reserveOrderService.createReserveOrder(ownerName, phone, address, source, style, budget, acreage, userId, companyId);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("关闭待转换订单")
    @MyRespBody
    @RequestMapping(value = "close", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle closeReserveOrder(
            @ApiParam(name = "reserveNo", required = true, value = "待订单编号") @RequestParam(name = "reserveNo", required = true) String reserveNo,
            @ApiParam(name = "state", required = true, value = "订单状态,3业主取消，4其他") @RequestParam(name = "state", required = true) int state,
            @ApiParam(name = "reason", required = true, value = "关闭原因") @RequestParam(name = "reason", required = true) String reason){
        try {
            reserveOrderService.closeReserveOrder(reserveNo, state, reason);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("查询待转换订单")
    @MyRespBody
    @RequestMapping(value = "query", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<ReserveProject>>> queryReserveOrder(
            @ApiParam(name = "ownerName", required = false, value = "待订单编号") @RequestParam(name = "ownerName", required = false) String ownerName,
            @ApiParam(name = "phone", required = false, value = "订单状态,3业主取消，4其他") @RequestParam(name = "phone", required = false) String phone,
            @ApiParam(name = "pageSize", required = false, value = "订单状态,3业主取消，4其他") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "关闭原因") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex){
        try {
            PageVo<List<ReserveProject>> pageVo = reserveOrderService.queryReserveOrder(ownerName, phone, pageSize, pageIndex);
            return sendJsonData(ResultMessage.SUCCESS, pageVo);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }
    @ApiOperation("查询待转换订单详情")
    @MyRespBody
    @RequestMapping(value = "queryDel", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ReserveProject> queryReserveOrderByNo(@ApiParam(name = "reserveNo", required = true, value = "待订单编号") @RequestParam(name = "reserveNo", required = true) String reserveNo){
        try {
            ReserveProject reserveProject = reserveOrderService.queryReserveOrderByNo(reserveNo);
            return sendJsonData(ResultMessage.SUCCESS, reserveProject);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }
    /**
     * 创建项目订单
     *
     * @param reserveNo        待转换订单编号
     * @param companyId        所属公司ID
     * @param source           订单来源
     * @param huxing           房屋户型，1小区房，2别墅，3复式，4其他
     * @param roomNum          房屋个数
     * @param officeNum        客厅个数
     * @param toiletNum        卫生间个数
     * @param address          装修地址
     * @param addressDetail    装修详细地址
     * @param style            装修风格
     * @param area             建筑面积
     * @param houseType        房屋类型，1新房，2老房
     * @param peopleNum        常住人口
     * @param planStartTime    计划装修开始时间
     * @param planEndTime      计划装修结束时间
     * @param decorationBudget 装修预算
     * @param balconyNum       阳台个数
     * @param ownerId          业主ID
     * @param designerId       设计师ID
     */
    @ApiOperation("创建订单")
    @MyRespBody
    @RequestMapping(value = "createProject", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle createProject(
            @ApiParam(name = "reserveNo", required = false, value = "待订单编号") @RequestParam(name = "reserveNo", required = false) String reserveNo,
            @ApiParam(name = "companyId", required = false, value = "所属公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "source", required = false, value = "订单来源") @RequestParam(name = "source", required = false) int source,
            @ApiParam(name = "huxing", required = false, value = "房屋户型") @RequestParam(name = "huxing", required = false) int huxing,
            @ApiParam(name = "roomNum", required = false, value = "房屋个数") @RequestParam(name = "roomNum", required = false) int roomNum,
            @ApiParam(name = "officeNum", required = false, value = "客厅个数") @RequestParam(name = "officeNum", required = false) int officeNum,
            @ApiParam(name = "toiletNum", required = false, value = "卫生间个数") @RequestParam(name = "toiletNum", required = false) int toiletNum,
            @ApiParam(name = "address", required = false, value = "装修地址") @RequestParam(name = "address", required = false) String address,
            @ApiParam(name = "addressDetail", required = false, value = "装修详细地址") @RequestParam(name = "addressDetail", required = false) String addressDetail,
            @ApiParam(name = "style", required = false, value = "装修风格") @RequestParam(name = "style", required = false) int style,
            @ApiParam(name = "area", required = false, value = "建筑面积") @RequestParam(name = "area", required = false) int area,
            @ApiParam(name = "houseType", required = false, value = "房屋类型") @RequestParam(name = "houseType", required = false) int houseType,
            @ApiParam(name = "peopleNum", required = false, value = "常住人口") @RequestParam(name = "peopleNum", required = false) int peopleNum,
            @ApiParam(name = "planStartTime", required = false, value = "计划装修开始时间") @RequestParam(name = "planStartTime", required = false) String planStartTime,
            @ApiParam(name = "planEndTime", required = false, value = "计划装修结束时间") @RequestParam(name = "planEndTime", required = false) String planEndTime,
            @ApiParam(name = "decorationBudget", required = false, value = "装修预算") @RequestParam(name = "decorationBudget", required = false) int decorationBudget,
            @ApiParam(name = "balconyNum", required = false, value = "阳台个数") @RequestParam(name = "balconyNum", required = false) int balconyNum,
            @ApiParam(name = "ownerId", required = false, value = "业主ID") @RequestParam(name = "ownerId", required = false) String ownerId,
            @ApiParam(name = "designerId", required = false, value = "设计师ID") @RequestParam(name = "designerId", required = false) String designerId){
        try{
            reserveOrderService.createProject(reserveNo,companyId,source,huxing,roomNum,officeNum,toiletNum,address,addressDetail,style,area,
                    houseType,peopleNum,planStartTime,planEndTime,decorationBudget,balconyNum,ownerId,designerId);
            return sendSuccessMessage(null);
        }catch (Exception e){
            return sendFailMessage(e.getMessage());
        }
    }
}
