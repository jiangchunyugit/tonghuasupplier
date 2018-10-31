package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.platform.designer.ApplyRefundService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.platform.vo.DesignerOrderVo;
import cn.thinkfree.service.platform.vo.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author xusonghui
 * 设计订单派单相关接口
 */
@Api(value = "设计订单派单，状态变更，相关接口", tags = "设计订单派单，状态变更，相关接口")
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

    @ApiOperation("设计师派单管理列表")
    @MyRespBody
    @RequestMapping(value = "orderList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<DesignerOrderVo>>> queryDesignerOrder(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "userMsg", required = false, value = "业主姓名或电话") @RequestParam(name = "userMsg", required = false) String userMsg,
            @ApiParam(name = "orderSource", required = false, value = "订单来源") @RequestParam(name = "orderSource", required = false) String orderSource,
            @ApiParam(name = "createTimeStart", required = false, value = "创建时间开始") @RequestParam(name = "createTimeStart", required = false) String createTimeStart,
            @ApiParam(name = "createTimeEnd", required = false, value = "创建时间结束") @RequestParam(name = "createTimeEnd", required = false) String createTimeEnd,
            @ApiParam(name = "styleCode", required = false, value = "装饰风格") @RequestParam(name = "styleCode", required = false) String styleCode,
            @ApiParam(name = "money", required = false, value = "装修预算") @RequestParam(name = "money", required = false) String money,
            @ApiParam(name = "acreage", required = false, value = "建筑面积") @RequestParam(name = "acreage", required = false) String acreage,
            @ApiParam(name = "designerOrderState", required = false, value = "订单状态") @RequestParam(name = "designerOrderState", required = false, defaultValue = "-1") int designerOrderState,
            @ApiParam(name = "companyState", required = false, value = "公司状态") @RequestParam(name = "companyState", required = false) String companyState,
            @ApiParam(name = "optionUserName", required = false, value = "操作人姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName,
            @ApiParam(name = "optionTimeStart", required = false, value = "操作时间开始") @RequestParam(name = "optionTimeStart", required = false) String optionTimeStart,
            @ApiParam(name = "optionTimeEnd", required = false, value = "操作时间结束") @RequestParam(name = "optionTimeEnd", required = false) String optionTimeEnd,
            @ApiParam(name = "stateType", required = false, value = "1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态") @RequestParam(name = "stateType", required = false, defaultValue = "1") int stateType,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            PageVo<List<DesignerOrderVo>> pageVo = designDispatchService.queryDesignerOrder(companyId, projectNo, userMsg, orderSource, createTimeStart, createTimeEnd, styleCode,
                    money, acreage, designerOrderState, companyState, optionUserName, optionTimeStart, optionTimeEnd, pageSize, pageIndex, stateType);
            return sendJsonData(ResultMessage.SUCCESS, pageVo);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }
    @ApiOperation("设计订单导出-->运营平台-->设计师派单页面")
    @MyRespBody
    @RequestMapping(value = "designOrderExcel", method = {RequestMethod.POST, RequestMethod.GET})
    public void designOrderExcel(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "userMsg", required = false, value = "业主姓名或电话") @RequestParam(name = "userMsg", required = false) String userMsg,
            @ApiParam(name = "orderSource", required = false, value = "订单来源") @RequestParam(name = "orderSource", required = false) String orderSource,
            @ApiParam(name = "createTimeStart", required = false, value = "创建时间开始") @RequestParam(name = "createTimeStart", required = false) String createTimeStart,
            @ApiParam(name = "createTimeEnd", required = false, value = "创建时间结束") @RequestParam(name = "createTimeEnd", required = false) String createTimeEnd,
            @ApiParam(name = "styleCode", required = false, value = "装饰风格") @RequestParam(name = "styleCode", required = false) String styleCode,
            @ApiParam(name = "money", required = false, value = "装修预算") @RequestParam(name = "money", required = false) String money,
            @ApiParam(name = "acreage", required = false, value = "建筑面积") @RequestParam(name = "acreage", required = false) String acreage,
            @ApiParam(name = "designerOrderState", required = false, value = "订单状态") @RequestParam(name = "designerOrderState", required = false, defaultValue = "-1") int designerOrderState,
            @ApiParam(name = "companyState", required = false, value = "公司状态") @RequestParam(name = "companyState", required = false) String companyState,
            @ApiParam(name = "optionUserName", required = false, value = "操作人姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName,
            @ApiParam(name = "optionTimeStart", required = false, value = "操作时间开始") @RequestParam(name = "optionTimeStart", required = false) String optionTimeStart,
            @ApiParam(name = "optionTimeEnd", required = false, value = "操作时间结束") @RequestParam(name = "optionTimeEnd", required = false) String optionTimeEnd,
            @ApiParam(name = "stateType", required = false, value = "1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态") @RequestParam(name = "stateType", required = false, defaultValue = "1") int stateType,
            @ApiParam(name = "fileName", required = false, value = "文件名") @RequestParam(name = "fileName", required = false) String fileName, HttpServletResponse response) {
        try {
            designDispatchService.designerOrderExcel(companyId, projectNo, userMsg, orderSource, createTimeStart, createTimeEnd, styleCode,
                    money, acreage, designerOrderState, companyState, optionUserName, optionTimeStart, optionTimeEnd, stateType, fileName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("获取所有订单状态")
    @MyRespBody
    @RequestMapping(value = "allStates", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<Map<String, String>>> queryAllOrderStates(
            @ApiParam(name = "state", required = false, value = "订单状态") @RequestParam(name = "state", required = false) int state) {
        return sendJsonData(ResultMessage.SUCCESS, DesignStateEnum.allStates(state));
    }

    @ApiOperation("设计订单不派单")
    @MyRespBody
    @RequestMapping(value = "notDispatch", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle notDispatch(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "reason", required = false, value = "不派单原因") @RequestParam(name = "reason", required = false) String reason,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            designDispatchService.notDispatch(projectNo, reason, optionUserId, optionUserName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计订单派单")
    @MyRespBody
    @RequestMapping(value = "dispatch", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle dispatch(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "contractType", required = false, value = "承包类型，1小包，2大包") @RequestParam(name = "contractType", required = false, defaultValue = "1") int contractType,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            designDispatchService.dispatch(projectNo, companyId, optionUserId, optionUserName, contractType);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("根据项目编号查询设计订单详情")
    @MyRespBody
    @RequestMapping(value = "designDel", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<DesignerOrderVo> queryDesignDel(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "stateType", required = false, value = "1获取平台状态，2获取设计公司状态，3获取设计师状态，4获取消费者状态") @RequestParam(name = "stateType", required = false, defaultValue = "1") int stateType) {
        try {
            return sendJsonData(ResultMessage.SUCCESS, designDispatchService.queryDesignerOrderVoByProjectNo(projectNo, stateType));
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("设计公司拒绝接单")
    @MyRespBody
    @RequestMapping(value = "refuseOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle refuseOrder(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "reason", required = false, value = "拒绝原因") @RequestParam(name = "reason", required = false) String reason,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            designDispatchService.refuseOrder(projectNo, companyId, reason, optionUserId, optionUserName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计公司指派设计师")
    @MyRespBody
    @RequestMapping(value = "assignDesigner", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle assignDesigner(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "designerUserId", required = false, value = "设计师ID") @RequestParam(name = "designerUserId", required = false) String designerUserId,
            @ApiParam(name = "optionUserId", required = false, value = "操作人员ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人员姓名") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            designDispatchService.assignDesigner(projectNo, companyId, designerUserId, optionUserId, optionUserName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计师拒绝接单")
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

    @ApiOperation("设计师接单")
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

    @ApiOperation("设计师发起量房预约")
    @MyRespBody
    @RequestMapping(value = "volumeRoom", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle volumeRoom(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "designerUserId", required = false, value = "设计师ID") @RequestParam(name = "designerUserId", required = false) String designerUserId) {
        try {
            designDispatchService.makeAnAppointmentVolumeRoom(projectNo, designerUserId);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("提醒业主")
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

    @ApiOperation("更新设计订单状态为量房待确认")
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
            @ApiParam(name = "designerId", required = false, value = "设计师Id") @RequestParam(name = "designerId", required = false) String designerId){
        try {
            designDispatchService.setDesignId(projectNo, designId, designerId);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("业主确认交付物")
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

    @ApiOperation("设计师提交设计合同待审核")
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

    @ApiOperation("设计公司合同审核不通过")
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

    @ApiOperation("设计公司合同审核通过")
    @MyRespBody
    @RequestMapping(value = "reviewPass", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle reviewPass(
            @ApiParam(name = "projectNo", required = false, value = "订单编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "contractType", required = false, value = "合同类型，1全款合同，2分期款合同") @RequestParam(name = "contractType", required = false, defaultValue = "-1") int contractType,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "optionId", required = false, value = "操作人Id") @RequestParam(name = "optionId", required = false) String optionId,
            @ApiParam(name = "optionName", required = false, value = "操作人名称") @RequestParam(name = "optionName", required = false) String optionName) {
        try {
            designDispatchService.reviewPass(projectNo, contractType, companyId, optionId, optionName);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("合同款/量房费支付成功")
    @MyRespBody
    @RequestMapping(value = "paySuccess", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle paySuccess(
            @ApiParam(name = "orderNo", required = false, value = "设计订单编号") @RequestParam(name = "orderNo", required = false) String orderNo) {
        try {
            designDispatchService.paySuccess(orderNo);
        } catch (Exception e) {
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

    @ApiOperation("业主确认3D效果图")
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

    @ApiOperation("3D资料上传成功后修改订单状态")
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

    @ApiOperation("业主确认施工资料")
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
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }

    @ApiOperation("设计订单申请退款")
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

    @ApiOperation("设计公司同意退款")
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

    @ApiOperation("设计公司驳回退款申请")
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

    @ApiOperation("平台同意退款")
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

    @ApiOperation("平台驳回退款申请")
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

    @ApiOperation("财务同意退款")
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

    @ApiOperation("业主终止订单")
    @MyRespBody
    @RequestMapping(value = "endOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle endOrder(
            @ApiParam(name = "projectNo", required = false, value = "项目编号") @RequestParam(name = "projectNo", required = false) String projectNo,
            @ApiParam(name = "userId", required = false, value = "用户ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "reason", required = false, value = "项目编号") @RequestParam(name = "reason", required = false) String reason) {
        try {
            designDispatchService.endOrder(projectNo, userId, reason);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
        return sendSuccessMessage(null);
    }
}
