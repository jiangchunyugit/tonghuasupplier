package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.JSONUtil;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.VolumeReservationDetailsVO;
import cn.thinkfree.service.platform.designer.ApplyRefundService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.vo.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 设计订单派单相关接口
 */
@Api(value = "设计订单派单，状态变更，相关接口", tags = "设计订单派单，状态变更，相关接口--->app后台公用===>徐松辉")
@Controller
@RequestMapping("designerOrder")
public class DesignDispatchController extends AbsBaseController {
    /**
     * try{
     * }catch (Exception e){
     * return sendFailMessage(e.getMessage());
     * }
     */
    @Autowired
    private DesignDispatchService designDispatchService;
    @Autowired
    private ApplyRefundService applyRefundService;

    @ApiOperation("设计师派单管理列表---->王玲组")
    @MyRespBody
    @RequestMapping(value = "orderList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<DesignerOrderVo>>> queryDesignerOrder(
            @ApiParam(name = "queryStage", required = false, value = "查询的数据阶段，具体字段待定，非必填，设计合同列表(DOCL)") @RequestParam(name = "queryStage", required = false) String queryStage,
            @ApiParam(name = "orderTpye", required = false, value = "订单类别 必传1:订单派单 2:订单列表") @RequestParam(name = "orderTpye", required = false) Integer orderTpye,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "userMsg", required = false, value = "业主姓名或电话") @RequestParam(name = "userMsg", required = false) String userMsg,
            @ApiParam(name = "orderSource", required = false, value = "订单来源") @RequestParam(name = "orderSource", required = false) String orderSource,
            @ApiParam(name = "createTimeStart", required = false, value = "创建时间开始") @RequestParam(name = "createTimeStart", required = false) String createTimeStart,
            @ApiParam(name = "createTimeEnd", required = false, value = "创建时间结束") @RequestParam(name = "createTimeEnd", required = false) String createTimeEnd,
            @ApiParam(name = "styleCode", required = false, value = "装饰风格") @RequestParam(name = "styleCode", required = false) String styleCode,
            @ApiParam(name = "provinceCode", required = false, value = "省份编码") @RequestParam(name = "provinceCode", required = false) String provinceCode,
            @ApiParam(name = "cityCode", required = false, value = "城市编码") @RequestParam(name = "cityCode", required = false) String cityCode,
            @ApiParam(name = "areaCode", required = false, value = "区域编码") @RequestParam(name = "areaCode", required = false) String areaCode,
            @ApiParam(name = "money", required = false, value = "装修预算") @RequestParam(name = "money", required = false) String money,
            @ApiParam(name = "acreage", required = false, value = "建筑面积") @RequestParam(name = "acreage", required = false) String acreage,
            @ApiParam(name = "designerOrderState", required = false, value = "订单状态") @RequestParam(name = "designerOrderState", required = false, defaultValue = "-1") int designerOrderState,
            @ApiParam(name = "companyState", required = false, value = "公司入驻状态 0入驻中 1资质待审核 2资质审核通过 3资质审核不通过4财务审核中5财务审核成功6财务审核失败7待交保证金8入驻成功") @RequestParam(name = "companyState", required = false, defaultValue = "-1") int companyState,
            @ApiParam(name = "optionUserName", required = false, value = "操作人姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName,
            @ApiParam(name = "optionTimeStart", required = false, value = "操作时间开始") @RequestParam(name = "optionTimeStart", required = false) String optionTimeStart,
            @ApiParam(name = "optionTimeEnd", required = false, value = "操作时间结束") @RequestParam(name = "optionTimeEnd", required = false) String optionTimeEnd,
            @ApiParam(name = "stateType", required = false, value = "1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态") @RequestParam(name = "stateType", required = false, defaultValue = "1") int stateType,
            @ApiParam(name = "companyName", required = false, value = "所属公司名称") @RequestParam(name = "companyName", required = false) String companyName,
            @ApiParam(name = "designerName", required = false, value = "所属设计师名称") @RequestParam(name = "designerName", required = false) String designerName,
            @ApiParam(name = "branchCompanyCode", required = false, value = "分公司") @RequestParam(name = "branchCompanyCode", required = false) String branchCompanyCode,
            @ApiParam(name = "cityBranchCode", required = false, value = "城市分站名称") @RequestParam(name = "cityBranchCode", required = false) String cityBranchCode,
            @ApiParam(name = "storeCode", required = false, value = "门店名称") @RequestParam(name = "storeCode", required = false) String storeCode,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            PageVo<List<DesignerOrderVo>> pageVo = designDispatchService.queryDesignerOrder(queryStage, orderTpye, companyId, projectNo, userMsg, orderSource, createTimeStart, createTimeEnd, styleCode,
                    provinceCode, cityCode, areaCode, money, acreage, designerOrderState, companyState, optionUserName, optionTimeStart, optionTimeEnd, pageSize, pageIndex, stateType, companyName, designerName, branchCompanyCode, cityBranchCode, storeCode);
            return sendJsonData(ResultMessage.SUCCESS, pageVo);
        } catch (Exception e) {
            e.printStackTrace();
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("设计师公司查询派单管理列表---->王玲组")
    @MyRespBody
    @RequestMapping(value = "design/orderList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<DesignerOrderVo>>> queryDesignerOrderByCompanyId(
            @ApiParam(name = "queryStage", required = false, value = "查询的数据阶段，具体字段待定，非必填，设计合同列表(DOCL)") @RequestParam(name = "queryStage", required = false) String queryStage,
            @ApiParam(name = "orderTpye", required = false, value = "订单类别 必传1:订单派单 2:订单列表") @RequestParam(name = "orderTpye", required = false) Integer orderTpye,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "userMsg", required = false, value = "业主姓名或电话") @RequestParam(name = "userMsg", required = false) String userMsg,
            @ApiParam(name = "orderSource", required = false, value = "订单来源") @RequestParam(name = "orderSource", required = false) String orderSource,
            @ApiParam(name = "createTimeStart", required = false, value = "创建时间开始") @RequestParam(name = "createTimeStart", required = false) String createTimeStart,
            @ApiParam(name = "createTimeEnd", required = false, value = "创建时间结束") @RequestParam(name = "createTimeEnd", required = false) String createTimeEnd,
            @ApiParam(name = "styleCode", required = false, value = "装饰风格") @RequestParam(name = "styleCode", required = false) String styleCode,
            @ApiParam(name = "provinceCode", required = false, value = "省份编码") @RequestParam(name = "provinceCode", required = false) String provinceCode,
            @ApiParam(name = "cityCode", required = false, value = "城市编码") @RequestParam(name = "cityCode", required = false) String cityCode,
            @ApiParam(name = "areaCode", required = false, value = "区域编码") @RequestParam(name = "areaCode", required = false) String areaCode,
            @ApiParam(name = "money", required = false, value = "装修预算") @RequestParam(name = "money", required = false) String money,
            @ApiParam(name = "acreage", required = false, value = "建筑面积") @RequestParam(name = "acreage", required = false) String acreage,
            @ApiParam(name = "designerOrderState", required = false, value = "订单状态") @RequestParam(name = "designerOrderState", required = false, defaultValue = "-1") int designerOrderState,
            @ApiParam(name = "optionUserName", required = false, value = "操作人姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName,
            @ApiParam(name = "optionTimeStart", required = false, value = "操作时间开始") @RequestParam(name = "optionTimeStart", required = false) String optionTimeStart,
            @ApiParam(name = "optionTimeEnd", required = false, value = "操作时间结束") @RequestParam(name = "optionTimeEnd", required = false) String optionTimeEnd,
            @ApiParam(name = "stateType", required = false, value = "1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态") @RequestParam(name = "stateType", required = false, defaultValue = "1") int stateType,
            @ApiParam(name = "designerName", required = false, value = "所属设计师名称") @RequestParam(name = "designerName", required = false) String designerName,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            PageVo<List<DesignerOrderVo>> pageVo = designDispatchService.queryDesignerOrderByCompanyId(queryStage, orderTpye, companyId, projectNo, userMsg, orderSource, createTimeStart, createTimeEnd, styleCode,
                    provinceCode, cityCode, areaCode, money, acreage, designerOrderState, optionUserName, optionTimeStart, optionTimeEnd, pageSize, pageIndex, stateType, designerName);
            return sendJsonData(ResultMessage.SUCCESS, pageVo);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("设计订单导出-->运营平台-->设计师派单页面---->王玲组")
    @MyRespBody
    @RequestMapping(value = "designOrderExcel", method = {RequestMethod.POST, RequestMethod.GET})
    public void designOrderExcel(
            @ApiParam(name = "queryStage", required = false, value = "查询的数据阶段，具体字段待定，非必填，设计合同列表(DOCL)") @RequestParam(name = "queryStage", required = false) String queryStage,
            @ApiParam(name = "orderTpye", required = false, value = "订单类别 必传1:订单派单 2:订单列表") @RequestParam(name = "orderTpye", required = false) Integer orderTpye,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "userMsg", required = false, value = "业主姓名或电话") @RequestParam(name = "userMsg", required = false) String userMsg,
            @ApiParam(name = "orderSource", required = false, value = "订单来源") @RequestParam(name = "orderSource", required = false) String orderSource,
            @ApiParam(name = "createTimeStart", required = false, value = "创建时间开始") @RequestParam(name = "createTimeStart", required = false) String createTimeStart,
            @ApiParam(name = "createTimeEnd", required = false, value = "创建时间结束") @RequestParam(name = "createTimeEnd", required = false) String createTimeEnd,
            @ApiParam(name = "styleCode", required = false, value = "装饰风格") @RequestParam(name = "styleCode", required = false) String styleCode,
            @ApiParam(name = "provinceCode", required = false, value = "省份编码") @RequestParam(name = "provinceCode", required = false) String provinceCode,
            @ApiParam(name = "cityCode", required = false, value = "城市编码") @RequestParam(name = "cityCode", required = false) String cityCode,
            @ApiParam(name = "areaCode", required = false, value = "区域编码") @RequestParam(name = "areaCode", required = false) String areaCode,
            @ApiParam(name = "money", required = false, value = "装修预算") @RequestParam(name = "money", required = false) String money,
            @ApiParam(name = "acreage", required = false, value = "建筑面积") @RequestParam(name = "acreage", required = false) String acreage,
            @ApiParam(name = "designerOrderState", required = false, value = "订单状态") @RequestParam(name = "designerOrderState", required = false, defaultValue = "-1") int designerOrderState,
            @ApiParam(name = "companyState", required = false, value = "公司入驻状态 0入驻中 1资质待审核 2资质审核通过 3资质审核不通过4财务审核中5财务审核成功6财务审核失败7待交保证金8入驻成功") @RequestParam(name = "companyState", required = false, defaultValue = "-1") int companyState,
            @ApiParam(name = "optionUserName", required = false, value = "操作人姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName,
            @ApiParam(name = "optionTimeStart", required = false, value = "操作时间开始") @RequestParam(name = "optionTimeStart", required = false) String optionTimeStart,
            @ApiParam(name = "optionTimeEnd", required = false, value = "操作时间结束") @RequestParam(name = "optionTimeEnd", required = false) String optionTimeEnd,
            @ApiParam(name = "stateType", required = false, value = "1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态") @RequestParam(name = "stateType", required = false, defaultValue = "1") int stateType,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex,
            @ApiParam(name = "fileName", required = false, value = "文件名") @RequestParam(name = "fileName", required = false) String fileName, HttpServletResponse response,
            @ApiParam(name = "branchCompanyCode", required = false, value = "分公司") @RequestParam(name = "branchCompanyCode", required = false) String branchCompanyCode,
            @ApiParam(name = "cityBranchCode", required = false, value = "城市分站名称") @RequestParam(name = "cityBranchCode", required = false) String cityBranchCode,
            @ApiParam(name = "storeCode", required = false, value = "门店名称") @RequestParam(name = "storeCode", required = false) String storeCode) {
        try {
            designDispatchService.designerOrderExcel(orderTpye, companyId, projectNo, userMsg, orderSource, createTimeStart, createTimeEnd, styleCode,
                    provinceCode, cityCode, areaCode, money, acreage, designerOrderState, companyState, optionUserName, optionTimeStart, optionTimeEnd, stateType, fileName, response, branchCompanyCode, cityBranchCode, storeCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("获取所有订单状态---->王玲组")
    @MyRespBody
    @RequestMapping(value = "allStates", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<Map<String, String>>> queryAllOrderStates(
            @ApiParam(name = "type", required = false, value = "参数：1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态") @RequestParam(name = "type", required = false) int type) {
        return sendJsonData(ResultMessage.SUCCESS, DesignStateEnum.getSelectStates(type));
    }

    @ApiOperation("设计订单不派单---->王玲组")
    @MyRespBody
    @RequestMapping(value = "notDispatch", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle notDispatch(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "reason", required = false, value = "不派单原因") @RequestParam(name = "reason", required = false) String reason,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
            if (userVO != null) {
                optionUserId = userVO.getUserID();
                optionUserName = userVO.getName();
            }
            designDispatchService.notDispatch(projectNo, reason, optionUserId, optionUserName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计订单派单---->王玲组")
    @MyRespBody
    @RequestMapping(value = "dispatch", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle dispatch(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "contractType", required = false, value = "承包类型，1小包，2大包") @RequestParam(name = "contractType", required = false, defaultValue = "1") int contractType,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
            if (userVO != null) {
                optionUserId = userVO.getUserID();
                optionUserName = userVO.getName();
            }
            designDispatchService.dispatch(projectNo, companyId, optionUserId, optionUserName, contractType);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("根据项目编号查询设计订单详情---->王玲组")
    @MyRespBody
    @RequestMapping(value = "designDel", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PcDesignOrderMsgVo> queryDesignDel(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "stateType", required = false, value = "1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态") @RequestParam(name = "stateType", required = false, defaultValue = "1") int stateType) {
        try {
            return sendJsonData(ResultMessage.SUCCESS, designDispatchService.queryDesignerOrderVoByProjectNo(projectNo, stateType));
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("设计公司拒绝接单---->王玲组")
    @MyRespBody
    @RequestMapping(value = "refuseOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle refuseOrder(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "reason", required = false, value = "拒绝原因") @RequestParam(name = "reason", required = false) String reason,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
            if (userVO != null) {
                optionUserId = userVO.getUserID();
                optionUserName = userVO.getName();
            }
            designDispatchService.refuseOrder(projectNo, companyId, reason, optionUserId, optionUserName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计公司指派设计师---->王玲组")
    @MyRespBody
    @RequestMapping(value = "assignDesigner", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle assignDesigner(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "designerUserId", required = false, value = "设计师ID") @RequestParam(name = "designerUserId", required = false) String designerUserId,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
            if (userVO != null) {
                optionUserId = userVO.getUserID();
                optionUserName = userVO.getName();
            }
            designDispatchService.assignDesigner(projectNo, companyId, designerUserId, optionUserId, optionUserName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计师拒绝接单---->app使用")
    @MyRespBody
    @RequestMapping(value = "designerRefuse", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle designerRefuse(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "reason", required = false, value = "拒绝原因") @RequestParam(name = "reason", required = false) String reason,
            @ApiParam(name = "designerUserId", required = false, value = "设计师ID") @RequestParam(name = "designerUserId", required = false) String designerUserId) {
        try {
            designDispatchService.designerRefuse(projectNo, reason, designerUserId);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计师接单---->app使用")
    @MyRespBody
    @RequestMapping(value = "designerReceipt", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle designerReceipt(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "designerUserId", required = false, value = "设计师ID") @RequestParam(name = "designerUserId", required = false) String designerUserId) {
        try {
            designDispatchService.designerReceipt(projectNo, designerUserId);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计师发起量房预约---->app使用")
    @MyRespBody
    @RequestMapping(value = "volumeRoom", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle volumeRoom(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "volumeRoomDate", required = false, value = "预约时间") @RequestParam(name = "volumeRoomDate", required = false) String volumeRoomDate,
            @ApiParam(name = "designerUserId", required = false, value = "设计师ID") @RequestParam(name = "designerUserId", required = false) String designerUserId,
            @ApiParam(name = "appointmentAmount", required = false, value = "量房费") @RequestParam(name = "appointmentAmount", required = false) String appointmentAmount
    ) {
        try {
            designDispatchService.makeAnAppointmentVolumeRoom(projectNo, designerUserId, volumeRoomDate, appointmentAmount);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计师发起量房预约详情页---->app使用")
    @MyRespBody
    @RequestMapping(value = "volumeReservationDetails", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<VolumeReservationDetailsVO> queryVolumeReservationDetails(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo) {
        try {
            return designDispatchService.queryVolumeReservationDetails(projectNo);
        } catch (Exception e) {
            e.printStackTrace();
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("提醒业主---->app使用")
    @MyRespBody
    @RequestMapping(value = "remindOwner", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle remindOwner(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "designerUserId", required = false, value = "设计师ID") @RequestParam(name = "designerUserId", required = false) String designerUserId) {
        try {
            designDispatchService.remindOwner(projectNo, designerUserId);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("更新设计订单状态为量房待确认---->app使用")
    @MyRespBody
    @RequestMapping(value = "volumeRoomConfirmed", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle volumeRoomConfirmed(@ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo) {
        try {
            designDispatchService.updateOrderState(projectNo, DesignStateEnum.STATE_60.getState(), "system", "system");
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设置项目关联的案例Id")
    @MyRespBody
    @RequestMapping(value = "setDesignId", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle setDesignId(
            @ApiParam(name = "projectNo", required = false, value = "项目编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "designId", required = false, value = "案例Id") @RequestParam(name = "designId", required = false) String designId,
            @ApiParam(name = "designerId", required = false, value = "设计师Id") @RequestParam(name = "designerId", required = false) String designerId) {
        try {
            designDispatchService.setDesignId(projectNo, designId, designerId);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("业主确认交付物---->app使用")
    @MyRespBody
    @RequestMapping(value = "confirmedDeliveries", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle confirmedDeliveries(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "ownerId", required = false, value = "业主Id") @RequestParam(name = "ownerId", required = false) String ownerId) {
        try {
            designDispatchService.confirmedDeliveries(projectNo, ownerId);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计师提交设计合同待审核---->app使用")
    @MyRespBody
    @RequestMapping(value = "submitContract", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle submitContract(@ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo) {
        try {
            designDispatchService.updateOrderState(projectNo, DesignStateEnum.STATE_130.getState(), "system", "system");
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计公司合同审核不通过---->王玲组")
    @MyRespBody
    @RequestMapping(value = "reviewReject", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle reviewReject(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "reason", required = false, value = "审核不通过原因") @RequestParam(name = "reason", required = false) String reason) {
        try {
            designDispatchService.updateOrderState(projectNo, DesignStateEnum.STATE_131.getState(), "system", "system", reason);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计公司合同审核通过---->王玲组")
    @MyRespBody
    @RequestMapping(value = "reviewPass", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle reviewPass(
            @ApiParam(name = "orderNo", required = false, value = "设计订单编号") @RequestParam(name = "orderNo", required = false) String orderNo,
            @ApiParam(name = "contractType", required = false, value = "合同类型，1全款合同，2分期款合同") @RequestParam(name = "contractType", required = false, defaultValue = "-1") int contractType) {
        try {
            designDispatchService.reviewPass(orderNo, contractType);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("合同款/量房费支付成功--->支付回调")
    @MyRespBody
    @RequestMapping(value = "paySuccess", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle paySuccess(
            @ApiParam(name = "orderNo", required = false, value = "设计订单编号") @RequestParam(name = "orderNo", required = false) String orderNo) {
        try {
            designDispatchService.paySuccess(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("3D资料上传成功后修改订单状态")
    @MyRespBody
    @RequestMapping(value = "upload3D", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle upload3D(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo) {
        try {
            DesignStateEnum stateEnum = DesignStateEnum.STATE_240;
            //1全款合同，2分期合同
            if (designDispatchService.queryDesignerOrder(projectNo).getContractType() == 2) {
                stateEnum = DesignStateEnum.STATE_160;
            }
            designDispatchService.updateOrderState(projectNo, stateEnum.getState(), "system", "system");
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("业主确认3D效果图---->app使用")
    @MyRespBody
    @RequestMapping(value = "confirm3D", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle confirm3D(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo) {
        try {
            DesignStateEnum stateEnum = DesignStateEnum.STATE_250;
            //1全款合同，2分期合同
            if (designDispatchService.queryDesignerOrder(projectNo).getContractType() == 2) {
                stateEnum = DesignStateEnum.STATE_170;
            }
            designDispatchService.updateOrderState(projectNo, stateEnum.getState(), "system", "system");
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("施工资料上传成功后修改订单状态")
    @MyRespBody
    @RequestMapping(value = "uploadData", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle uploadData(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo) {
        try {
            DesignStateEnum stateEnum = DesignStateEnum.STATE_260;
            //1全款合同，2分期合同
            if (designDispatchService.queryDesignerOrder(projectNo).getContractType() == 2) {
                stateEnum = DesignStateEnum.STATE_190;
            }
            designDispatchService.updateOrderState(projectNo, stateEnum.getState(), "system", "system");
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("业主确认施工资料---->app使用")
    @MyRespBody
    @RequestMapping(value = "confirmData", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle confirmData(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo) {
        try {
            DesignStateEnum stateEnum = DesignStateEnum.STATE_270;
            //1全款合同，2分期合同
            if (designDispatchService.queryDesignerOrder(projectNo).getContractType() == 2) {
                stateEnum = DesignStateEnum.STATE_200;
            }
            designDispatchService.updateOrderState(projectNo, stateEnum.getState(), "system", "system");
        } catch (Exception e) {
            e.printStackTrace();
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计订单申请退款---->app使用")
    @MyRespBody
    @RequestMapping(value = "applyRefund", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle applyRefund(
            @ApiParam(name = "orderNo", required = false, value = "设计订单编号") @RequestParam(name = "orderNo", required = false) String orderNo,
            @ApiParam(name = "userId", required = false, value = "用户ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "reason", required = false, value = "退款原因") @RequestParam(name = "reason", required = false) String reason,
            @ApiParam(name = "money", required = false, value = "退款金额") @RequestParam(name = "money", required = false) String money,
            @ApiParam(name = "title", required = false, value = "退款项目") @RequestParam(name = "title", required = false) String title,
            @ApiParam(name = "flowNumber", required = false, value = "支付流水号") @RequestParam(name = "flowNumber", required = false) String flowNumber) {
        try {
            applyRefundService.applyRefund(orderNo, userId, reason, money, title, flowNumber);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计公司同意退款--->王玲组")
    @MyRespBody
    @RequestMapping(value = "companyRefundAgree", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle companyRefundAgree(
            @ApiParam(name = "refundNo", required = false, value = "审批退款的记录编号") @RequestParam(name = "refundNo", required = false) String refundNo,
            @ApiParam(name = "optionId", required = false, value = "操作人Id") @RequestParam(name = "optionId", required = false) String optionId,
            @ApiParam(name = "optionName", required = false, value = "操作人名称") @RequestParam(name = "optionName", required = false) String optionName) {
        try {
            applyRefundService.companyRefundAgree(refundNo, optionId, optionName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计公司驳回退款申请--->王玲组")
    @MyRespBody
    @RequestMapping(value = "companyRefundReject", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle companyRefundReject(
            @ApiParam(name = "refundNo", required = false, value = "审批退款的记录编号") @RequestParam(name = "refundNo", required = false) String refundNo,
            @ApiParam(name = "optionId", required = false, value = "操作人Id") @RequestParam(name = "optionId", required = false) String optionId,
            @ApiParam(name = "optionName", required = false, value = "操作人名称") @RequestParam(name = "optionName", required = false) String optionName,
            @ApiParam(name = "reason", required = false, value = "驳回原因") @RequestParam(name = "reason", required = false) String reason) {
        try {
            applyRefundService.companyRefundReject(refundNo, optionId, optionName, reason);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("平台同意退款--->王玲组")
    @MyRespBody
    @RequestMapping(value = "platformRefundAgree", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle platformRefundAgree(
            @ApiParam(name = "refundNo", required = false, value = "审批退款的记录编号") @RequestParam(name = "refundNo", required = false) String refundNo,
            @ApiParam(name = "optionId", required = false, value = "操作人Id") @RequestParam(name = "optionId", required = false) String optionId,
            @ApiParam(name = "optionName", required = false, value = "操作人名称") @RequestParam(name = "optionName", required = false) String optionName) {
        try {
            applyRefundService.platformRefundAgree(refundNo, optionId, optionName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("平台驳回退款申请--->王玲组")
    @MyRespBody
    @RequestMapping(value = "platformRefundReject", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle platformRefundReject(
            @ApiParam(name = "refundNo", required = false, value = "审批退款的记录编号") @RequestParam(name = "refundNo", required = false) String refundNo,
            @ApiParam(name = "optionId", required = false, value = "操作人Id") @RequestParam(name = "optionId", required = false) String optionId,
            @ApiParam(name = "optionName", required = false, value = "操作人名称") @RequestParam(name = "optionName", required = false) String optionName,
            @ApiParam(name = "reason", required = false, value = "驳回原因") @RequestParam(name = "reason", required = false) String reason) {
        try {
            applyRefundService.platformRefundReject(refundNo, optionId, optionName, reason);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("财务同意退款--->王玲组")
    @MyRespBody
    @RequestMapping(value = "financeRefundAgree", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle financeRefundAgree(
            @ApiParam(name = "refundNo", required = false, value = "审批退款的记录编号") @RequestParam(name = "refundNo", required = false) String refundNo,
            @ApiParam(name = "optionId", required = false, value = "操作人Id") @RequestParam(name = "optionId", required = false) String optionId,
            @ApiParam(name = "optionName", required = false, value = "操作人名称") @RequestParam(name = "optionName", required = false) String optionName) {
        try {
            applyRefundService.financeRefundAgree(refundNo, optionId, optionName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("支付超时")
    @MyRespBody
    @RequestMapping(value = "payTimeOut", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle payTimeOut(
            @ApiParam(name = "projectNo", required = false, value = "项目编号") @RequestParam(name = "projectNo", required = false) String projectNo) {
        try {
            designDispatchService.payTimeOut(projectNo);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("业主终止订单---->app使用")
    @MyRespBody
    @RequestMapping(value = "endOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle endOrder(
            @ApiParam(name = "projectNo", required = false, value = "项目编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "userId", required = false, value = "用户ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "reason", required = false, value = "终止的原因") @RequestParam(name = "reason", required = false) String reason) {
        try {
            designDispatchService.endOrder(projectNo, userId, reason);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("是否展示操作按钮---->app使用：[\"LFFY(提醒支付量房费用)\",\"LFZL(提交量房资料)\",\"SJZL(提交设计资料)\",\"CKHT(查看合同)\",\"ZSG(转施工)\"]")
    @MyRespBody
    @RequestMapping(value = "showBtn", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<String>> showBtn(
            @ApiParam(name = "designOrderNo", required = false, value = "设计订单编号") @RequestParam(name = "designOrderNo", required = false) String designOrderNo) {
        try {
            return sendJsonData(ResultMessage.SUCCESS, designDispatchService.showBtn(designOrderNo));
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("是否展示操作按钮---->app使用：[\"LFFY(提醒支付量房费用)\",\"LFZL(提交量房资料)\",\"SJZL(提交设计资料)\",\"CKHT(查看合同)\",\"ZSG(转施工)\"]")
    @MyRespBody
    @RequestMapping(value = "showBtnByUserId", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<String>> showBtnByUserId(
            @ApiParam(name = "projectNo", required = false, value = "项目编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "userId", required = false, value = "用户ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "designOrderNo", required = false, value = "设计订单编号") @RequestParam(name = "designOrderNo", required = false) String designOrderNo) {
        try {
            if (projectNo == null || projectNo.trim().isEmpty()) {
                return RespData.error("请检查入参projectNo=" + projectNo);
            }
            if (designOrderNo == null || designOrderNo.trim().isEmpty()) {
                return RespData.error("请检查入参designOrderNo=" + designOrderNo);
            }
            if (userId == null || userId.trim().isEmpty()) {
                return RespData.error("请检查入参userId=" + userId);
            }
            return sendJsonData(ResultMessage.SUCCESS, designDispatchService.showBtnByUserId(projectNo, designOrderNo, userId));
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("根据项目编号查询业主和公司信息")
    @MyRespBody
    @RequestMapping(value = "queryContractMsg", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ContractMsgVo> queryContractMsg(
            @ApiParam(name = "projectNo", required = false, value = "项目编号") @RequestParam(name = "projectNo", required = false) String projectNo) {
        try {
            return sendJsonData(ResultMessage.SUCCESS, designDispatchService.queryContractMsg(projectNo));
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("app-C端确认量房")
    @MyRespBody
    @RequestMapping(value = "confirmeVolumeRoom", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle confirmeVolumeRoom(
            @ApiParam(name = "projectNo", required = false, value = "项目编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "userId", required = false, value = "用户ID") @RequestParam(name = "userId", required = false) String userId) {
        try {
            return designDispatchService.confirmeVolumeRoom(projectNo, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error(e.getMessage());
        }
    }

    @ApiOperation("运营平台---设计平台---设计合同管理列表")
    @MyRespBody
    @RequestMapping(value = "designContract", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<ContractListItemVo>>> designContract(
            @ApiParam(name = "contractNo", required = false, value = "合同编号") @RequestParam(name = "contractNo", required = false) String contractNo,
            @ApiParam(name = "projectNo", required = false, value = "项目编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "orderSource", required = false, value = "订单来源") @RequestParam(name = "orderSource", required = false) String orderSource,
            @ApiParam(name = "provinceCode", required = false, value = "省份编码") @RequestParam(name = "provinceCode", required = false) String provinceCode,
            @ApiParam(name = "cityCode", required = false, value = "城市编码") @RequestParam(name = "cityCode", required = false) String cityCode,
            @ApiParam(name = "areaCode", required = false, value = "区域编码") @RequestParam(name = "areaCode", required = false) String areaCode,
            @ApiParam(name = "contractState", required = false, value = "合同状态") @RequestParam(name = "contractState", required = false) String contractState,
            @ApiParam(name = "signTimeS", required = false, value = "签约时间开始") @RequestParam(name = "signTimeS", required = false) String signTimeS,
            @ApiParam(name = "signTimeE", required = false, value = "签约时间结束") @RequestParam(name = "signTimeE", required = false) String signTimeE,
            @ApiParam(name = "ownerMsg", required = false, value = "业主手机号/姓名") @RequestParam(name = "ownerMsg", required = false) String ownerMsg,
            @ApiParam(name = "branchCompanyCode", required = false, value = "分公司") @RequestParam(name = "branchCompanyCode", required = false) String branchCompanyCode,
            @ApiParam(name = "cityBranchCode", required = false, value = "城市分站名称") @RequestParam(name = "cityBranchCode", required = false) String cityBranchCode,
            @ApiParam(name = "storeCode", required = false, value = "门店名称") @RequestParam(name = "storeCode", required = false) String storeCode,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        PageVo<List<ContractListItemVo>> itemVo = designDispatchService.designContract(contractNo, projectNo, orderSource, provinceCode, cityCode,
                areaCode, contractState, signTimeS, signTimeE, ownerMsg, branchCompanyCode, cityBranchCode, storeCode, pageSize, pageIndex);
        return sendJsonData(ResultMessage.SUCCESS, itemVo);
    }

    @ApiOperation("返回是否能撤换设计师 0不能 1能")
    @MyRespBody
    @RequestMapping(value = "replaceDesigners", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<Integer> replaceDesigners(
            @ApiParam(name = "orderNo", required = false, value = "订单编号") @RequestParam(name = "orderNo", required = false) String orderNo) {
        try {
            return sendJsonData(ResultMessage.SUCCESS, designDispatchService.replaceDesigners(orderNo));
        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error(e.getMessage());
        }
    }

    @ApiOperation("返回订单为进行或进行数量")
    @MyRespBody
    @RequestMapping(value = "getOrderStatusValue", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<Map<String, String>>> getOrderStatusValue(
            @ApiParam(name = "userMap", required = false, value = "用户id集合 userId state 1设计 2施工") @RequestParam(name = "userMap", required = false) String userMap) {
        try {
            List<Map<String, String>> listObjectFir = new ArrayList<>();
            if (StringUtils.isNotBlank(userMap)) {
                listObjectFir = (List<Map<String, String>>) JSONArray.parse(userMap);
            }
            return sendJsonData(ResultMessage.SUCCESS, designDispatchService.getOrderStatusValue(listObjectFir));

        } catch (Exception e) {
            e.printStackTrace();
            return RespData.error(e.getMessage());
        }
    }

}
