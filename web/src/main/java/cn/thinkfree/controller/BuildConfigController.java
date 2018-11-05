package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.BuildPayConfig;
import cn.thinkfree.database.model.BuildSchemeConfig;
import cn.thinkfree.service.platform.build.BuildConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author xusonghui
 * 施工配置控制器
 */
@Api(value = "施工配置控制", tags = {"施工配置接口, 支付方案配置接口---->王玲组专用===>徐松辉"})
@Controller
@RequestMapping("build")
public class BuildConfigController extends AbsBaseController {

    @Autowired
    private BuildConfigService buildConfigService;

    @ApiOperation("获取所有配置方案")
    @ResponseBody
    @RequestMapping(value = "allScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BuildSchemeConfig>> allScheme() {
        return sendJsonData(ResultMessage.SUCCESS, buildConfigService.allBuildScheme());
    }

    @ApiOperation("创建施工方案")
    @ResponseBody
    @RequestMapping(value = "createScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> createScheme(
            @ApiParam(name = "schemeName", required = false, value = "施工方案名称") @RequestParam(name = "schemeName", required = false) String schemeName,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "cityStation", required = false, value = "城市站ID") @RequestParam(name = "cityStation", required = false) String cityStation,
            @ApiParam(name = "storeNo", required = false, value = "门店ID") @RequestParam(name = "storeNo", required = false) String storeNo,
            @ApiParam(name = "remark", required = false, value = "备注") @RequestParam(name = "remark", required = false) String remark) {
        try {
            String schemeNo = buildConfigService.createScheme(schemeName, companyId, cityStation, storeNo, remark);
            return sendSuccessMessage(schemeNo);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("启用施工方案")
    @ResponseBody
    @RequestMapping(value = "enableScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle enableScheme(@ApiParam(name = "schemeNo", required = false, value = "施工方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo) {
        try {
            buildConfigService.enableScheme(schemeNo);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("删除施工方案")
    @ResponseBody
    @RequestMapping(value = "delScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle delScheme(@ApiParam(name = "schemeNo", required = false, value = "施工方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo) {
        try {
            buildConfigService.delScheme(schemeNo);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("根据施工方案编号，获取施工方案基础信息")
    @ResponseBody
    @RequestMapping(value = "bySchemeNo", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<BuildSchemeConfig> bySchemeNo(
            @ApiParam(name = "schemeNo", required = false, value = "施工方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo) {
        try {
            return sendJsonData(ResultMessage.SUCCESS, buildConfigService.bySchemeNo(schemeNo));
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("根据方案编号查询支付方案信息")
    @ResponseBody
    @RequestMapping(value = "payConfigBySchemeNo", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BuildPayConfig>> payConfigBySchemeNo(
            @ApiParam(name = "schemeNo", required = false, value = "施工方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo) {
        try {
            return sendJsonData(ResultMessage.SUCCESS, buildConfigService.payConfigBySchemeNo(schemeNo));
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("保存支付方案")
    @ResponseBody
    @RequestMapping(value = "savePayConfig", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle savePayConfig(
            @ApiParam(name = "schemeNo", required = false, value = "施工方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo,
            @ApiParam(name = "progressName", required = false, value = "进度名称") @RequestParam(name = "progressName", required = false) String progressName,
            @ApiParam(name = "stageNo", required = false, value = "阶段编号") @RequestParam(name = "stageNo", required = false) String stageNo,
            @ApiParam(name = "time", required = false, value = "未支付超时时间") @RequestParam(name = "time", required = false, defaultValue = "-1") int time,
            @ApiParam(name = "remark", required = false, value = "备注") @RequestParam(name = "remark", required = false) String remark) {
        try {
            buildConfigService.savePayConfig(schemeNo, progressName, stageNo, time, remark);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendSuccessMessage(e.getMessage());
        }
    }

    @ApiOperation("删除支付方案")
    @ResponseBody
    @RequestMapping(value = "delPayConfig", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle delPayConfig(
            @ApiParam(name = "paySchemeNo", required = false, value = "支付方案编号") @RequestParam(name = "paySchemeNo", required = false) String paySchemeNo) {
        try {
            buildConfigService.delPayConfig(paySchemeNo);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendSuccessMessage(e.getMessage());
        }
    }

    @ApiOperation("公司选择方案")
    @ResponseBody
    @RequestMapping(value = "chooseScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle chooseScheme(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "schemeNo", required = false, value = "方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo,
            @ApiParam(name = "optionUserId", required = false, value = "操作人ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人ID") @RequestParam(name = "optionUserName", required = false) String optionUserName){
        try {
            buildConfigService.chooseScheme(companyId, schemeNo, optionUserId, optionUserName);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendSuccessMessage(e.getMessage());
        }
    }
}
