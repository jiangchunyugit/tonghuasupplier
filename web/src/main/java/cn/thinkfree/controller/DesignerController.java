package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.DesignerMsg;
import cn.thinkfree.database.model.DesignerStyleConfig;
import cn.thinkfree.database.model.EmployeeMsg;
import cn.thinkfree.service.platform.designer.DesignerService;
import cn.thinkfree.service.platform.designer.vo.DesignerMsgListVo;
import cn.thinkfree.service.platform.designer.vo.DesignerMsgVo;
import cn.thinkfree.service.platform.designer.vo.PageVo;
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
 * 设计师相关接口
 */
@Api(value = "设计师相关API接口", tags = "设计师相关API接口")
@Controller
@RequestMapping("designer")
public class DesignerController extends AbsBaseController {

    @Autowired
    private DesignerService designerService;

    @ApiOperation("设计师列表")
    @MyRespBody
    @RequestMapping(value = "list", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<DesignerMsgListVo>>> designerList(
            @ApiParam(name = "designerName", required = false, value = "设计师用户名") @RequestParam(name = "designerName", required = false) String designerName,
            @ApiParam(name = "designerRealName", required = false, value = "设计师真实姓名") @RequestParam(name = "designerRealName", required = false) String designerRealName,
            @ApiParam(name = "phone", required = false, value = "手机号") @RequestParam(name = "phone", required = false) String phone,
            @ApiParam(name = "authState", required = false, value = "认证状态") @RequestParam(name = "authState", required = false) String authState,
            @ApiParam(name = "province", required = false, value = "所在省份") @RequestParam(name = "province", required = false) String province,
            @ApiParam(name = "city", required = false, value = "所在市") @RequestParam(name = "city", required = false) String city,
            @ApiParam(name = "area", required = false, value = "所在区") @RequestParam(name = "area", required = false) String area,
            @ApiParam(name = "level", required = false, value = "设计师等级") @RequestParam(name = "level", required = false) String level,
            @ApiParam(name = "identity", required = false, value = "设计师身份") @RequestParam(name = "identity", required = false) String identity,
            @ApiParam(name = "source", required = false, value = "设计师来源") @RequestParam(name = "source", required = false) String source,
            @ApiParam(name = "tag", required = false, value = "设计师标签") @RequestParam(name = "tag", required = false) String tag,
            @ApiParam(name = "registrationTimeStart", required = false, value = "注册时间") @RequestParam(name = "registrationTimeStart", required = false) String registrationTimeStart,
            @ApiParam(name = "registrationTimeEnd", required = false, value = "注册时间") @RequestParam(name = "registrationTimeEnd", required = false) String registrationTimeEnd,
            @ApiParam(name = "sort", required = false, value = "排期区域") @RequestParam(name = "sort", required = false) String sort,
            @ApiParam(name = "pageSize", required = false, value = "每页条数") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        PageVo<List<DesignerMsgListVo>> pageVo = designerService.queryDesigners(designerName, designerRealName, phone, authState, province, city, area, level,
                identity, source, tag, registrationTimeStart, registrationTimeEnd, sort, pageSize, pageIndex);
        return sendJsonData(ResultMessage.SUCCESS, pageVo);
    }

    @ApiOperation("设计师排序")
    @MyRespBody
    @RequestMapping(value = "sort", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle setDesignerSort(
            @ApiParam(name = "userId", required = false, value = "设计师ID") @RequestParam(name = "userId", required = false) String userId,
            @ApiParam(name = "sort", required = false, value = "排序值") @RequestParam(name = "sort", required = false) int sort) {
        designerService.setDesignerSort(userId, sort);
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("获取设计师信息")
    @MyRespBody
    @RequestMapping(value = "msg", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<DesignerMsgVo> queryDesignerMsgByUserId(
            @ApiParam(name = "userId", required = false, value = "设计师ID") @RequestParam(name = "userId", required = false) String userId) {
        DesignerMsg designerMsg = designerService.queryDesignerByUserId(userId);
        EmployeeMsg employeeMsg = designerService.queryEmployeeMsgByUserId(userId);
        List<DesignerStyleConfig> designerStyleConfigs = designerService.queryDesignerStyleByUserId(userId);
        DesignerMsgVo designerMsgVo = new DesignerMsgVo();
        designerMsgVo.setDesignerMsg(designerMsg);
        designerMsgVo.setDesignerStyleConfigs(designerStyleConfigs);
        designerMsgVo.setEmployeeMsg(employeeMsg);
        return sendJsonData(ResultMessage.SUCCESS, designerMsgVo);
    }

    @ApiOperation("根据公司ID查询设计师信息")
    @MyRespBody
    @RequestMapping(value = "queryCompanyId", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<EmployeeMsg>> queryDesignerByCompanyId(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId){
        List<EmployeeMsg> employeeMsgs = designerService.queryDesignerByCompanyId(companyId);
        return sendJsonData(ResultMessage.SUCCESS, employeeMsgs);
    }

    @ApiOperation("查询设计风格")
    @MyRespBody
    @RequestMapping(value = "designerStyle", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<DesignerStyleConfig>> queryDesignerStyle() {
        List<DesignerStyleConfig> styleConfigs = designerService.queryDesignerStyle();
        return sendJsonData(ResultMessage.SUCCESS, styleConfigs);
    }

    @ApiOperation("编辑设计师信息")
    @MyRespBody
    @RequestMapping(value = "editDesigner", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle editDesignerMsg(
            @ApiParam(name = "userId", required = true, value = "设计师ID") @RequestParam(name = "userId", required = true) String userId,
            @ApiParam(name = "tag", required = true, value = "设计师标签") @RequestParam(name = "tag", required = true) String tag,
            @ApiParam(name = "identity", required = true, value = "设计师身份") @RequestParam(name = "identity", required = true) String identity,
            @ApiParam(name = "workingTime", required = true, value = "从业年限") @RequestParam(name = "workingTime", required = true) String workingTime,
            @ApiParam(name = "volumeRoomMoney", required = true, value = "量房费") @RequestParam(name = "volumeRoomMoney", required = true) String volumeRoomMoney,
            @ApiParam(name = "designerMoneyLow", required = true, value = "设计费最低") @RequestParam(name = "designerMoneyLow", required = true) String designerMoneyLow,
            @ApiParam(name = "designerMoneyHigh", required = true, value = "设计费最高") @RequestParam(name = "designerMoneyHigh", required = true) String designerMoneyHigh,
            @ApiParam(name = "masterStyle", required = true, value = "擅长风格") @RequestParam(name = "masterStyle", required = true) String masterStyle) {
        designerService.editDesignerMsg(userId,tag,identity,workingTime,volumeRoomMoney,designerMoneyLow,designerMoneyHigh,masterStyle);
        return sendJsonData(ResultMessage.SUCCESS, null);
    }

    @ApiOperation("创建设计师信息")
    @MyRespBody
    @RequestMapping(value = "createDesigner", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle createDesigner(
            @ApiParam(name = "phone", required = true, value = "手机号") @RequestParam(name = "phone", required = true) String phone,
            @ApiParam(name = "email", required = true, value = "邮箱") @RequestParam(name = "email", required = true) String email,
            @ApiParam(name = "province", required = true, value = "所在省份") @RequestParam(name = "province", required = true) String province,
            @ApiParam(name = "city", required = true, value = "所在市") @RequestParam(name = "city", required = true) String city,
            @ApiParam(name = "area", required = true, value = "所在区") @RequestParam(name = "area", required = true) String area,
            @ApiParam(name = "workingTime", required = false, value = "从业年限") @RequestParam(name = "workingTime", required = false) String workingTime,
            @ApiParam(name = "volumeRoomMoney", required = false, value = "量房费") @RequestParam(name = "volumeRoomMoney", required = false) String volumeRoomMoney,
            @ApiParam(name = "designerMoneyLow", required = false, value = "设计费最低") @RequestParam(name = "designerMoneyLow", required = false) String designerMoneyLow,
            @ApiParam(name = "designerMoneyHigh", required = false, value = "设计费最高") @RequestParam(name = "designerMoneyHigh", required = false) String designerMoneyHigh,
            @ApiParam(name = "masterStyle", required = false, value = "擅长风格") @RequestParam(name = "masterStyle", required = false) String masterStyle){
        designerService.createDesigner(phone,email,province,city,area,workingTime,masterStyle,volumeRoomMoney,designerMoneyLow,designerMoneyHigh);
        return sendJsonData(ResultMessage.SUCCESS, null);
    }
}
