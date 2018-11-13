package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.BuildPayConfig;
import cn.thinkfree.database.model.BuildSchemeConfig;
import cn.thinkfree.service.platform.build.BuildConfigService;
import cn.thinkfree.service.platform.vo.CompanySchemeVo;
import cn.thinkfree.service.platform.vo.PageVo;
import cn.thinkfree.service.utils.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    /**
     * logger日志接口
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private BuildConfigService buildConfigService;

    @ApiOperation("获取所有配置方案====》运营后台====》施工配置")
    @ResponseBody
    @RequestMapping(value = "allScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<BuildSchemeConfig>>> allScheme(
            @ApiParam(name = "schemeNo", required = false, value = "施工方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo,
            @ApiParam(name = "schemeName", required = false, value = "施工方案名称") @RequestParam(name = "schemeName", required = false) String schemeName,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "cityStation", required = false, value = "城市站ID") @RequestParam(name = "cityStation", required = false) String cityStation,
            @ApiParam(name = "storeNo", required = false, value = "门店ID") @RequestParam(name = "storeNo", required = false) String storeNo,
            @ApiParam(name = "isEnable", required = false, value = "是否启用，-1全部，1启用，2不启用") @RequestParam(name = "isEnable", required = false, defaultValue = "-1") int isEnable,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        return sendJsonData(ResultMessage.SUCCESS, buildConfigService.allBuildScheme(schemeNo, schemeName, companyId, cityStation, storeNo, isEnable, pageSize, pageIndex));
    }

    @ApiOperation("创建施工方案====》运营后台====》施工配置")
    @ResponseBody
    @RequestMapping(value = "createScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> createScheme(
            @ApiParam(name = "schemeName", required = false, value = "施工方案名称") @RequestParam(name = "schemeName", required = false) String schemeName,
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "cityStation", required = false, value = "城市站ID") @RequestParam(name = "cityStation", required = false) String cityStation,
            @ApiParam(name = "storeNo", required = false, value = "门店ID") @RequestParam(name = "storeNo", required = false) String storeNo,
            @ApiParam(name = "remark", required = false, value = "备注") @RequestParam(name = "remark", required = false) String remark) {
        try {
            logger.info("创建施工方案：{}", JSONObject.toJSONString(HttpUtils.getHttpParams()));
            String schemeNo = buildConfigService.createScheme(schemeName, companyId, cityStation, storeNo, remark);
            return sendSuccessMessage(schemeNo);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("启用施工方案====》运营后台====》施工配置")
    @ResponseBody
    @RequestMapping(value = "enableScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle enableScheme(@ApiParam(name = "schemeNo", required = false, value = "施工方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo) {
        try {
            logger.info("启用施工方案：{}", JSONObject.toJSONString(HttpUtils.getHttpParams()));
            buildConfigService.enableScheme(schemeNo);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("删除施工方案====》运营后台====》施工配置")
    @ResponseBody
    @RequestMapping(value = "delScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle delScheme(@ApiParam(name = "schemeNo", required = false, value = "施工方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo) {
        try {
            logger.info("删除施工方案：{}", JSONObject.toJSONString(HttpUtils.getHttpParams()));
            buildConfigService.delScheme(schemeNo);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("根据施工方案编号，获取施工方案基础信息====》运营后台====》施工配置")
    @ResponseBody
    @RequestMapping(value = "bySchemeNo", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<BuildSchemeConfig> bySchemeNo(
            @ApiParam(name = "schemeNo", required = false, value = "施工方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo) {
        try {
            logger.info("根据施工方案编号，获取施工方案基础信息：{}", JSONObject.toJSONString(HttpUtils.getHttpParams()));
            return sendJsonData(ResultMessage.SUCCESS, buildConfigService.bySchemeNo(schemeNo));
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("根据方案编号查询支付方案信息====》运营后台====》施工配置")
    @ResponseBody
    @RequestMapping(value = "payConfigBySchemeNo", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<BuildPayConfig>>> payConfigBySchemeNo(
            @ApiParam(name = "schemeNo", required = false, value = "施工方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            logger.info("根据方案编号查询支付方案信息：{}", JSONObject.toJSONString(HttpUtils.getHttpParams()));
            return sendJsonData(ResultMessage.SUCCESS, buildConfigService.payConfigBySchemeNo(schemeNo, pageSize, pageIndex));
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("保存支付方案====》运营后台====》施工配置")
    @ResponseBody
    @RequestMapping(value = "savePayConfig", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle savePayConfig(
            @ApiParam(name = "schemeNo", required = false, value = "施工方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo,
            @ApiParam(name = "progressName", required = false, value = "进度名称") @RequestParam(name = "progressName", required = false) String progressName,
            @ApiParam(name = "stageNo", required = false, value = "阶段编号") @RequestParam(name = "stageNo", required = false) String stageNo,
            @ApiParam(name = "time", required = false, value = "未支付超时时间") @RequestParam(name = "time", required = false, defaultValue = "-1") int time,
            @ApiParam(name = "remark", required = false, value = "备注") @RequestParam(name = "remark", required = false) String remark) {
        try {
            logger.info("保存支付方案：{}", JSONObject.toJSONString(HttpUtils.getHttpParams()));
            buildConfigService.savePayConfig(schemeNo, progressName, stageNo, time, remark);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendSuccessMessage(e.getMessage());
        }
    }

    @ApiOperation("删除支付方案====》运营后台====》施工配置")
    @ResponseBody
    @RequestMapping(value = "delPayConfig", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle delPayConfig(
            @ApiParam(name = "paySchemeNo", required = false, value = "支付方案编号") @RequestParam(name = "paySchemeNo", required = false) String paySchemeNo) {
        try {
            logger.info("删除支付方案：{}", JSONObject.toJSONString(HttpUtils.getHttpParams()));
            buildConfigService.delPayConfig(paySchemeNo);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendSuccessMessage(e.getMessage());
        }
    }

    @ApiOperation("根据项目编号查询支付方案信息")
    @ResponseBody
    @RequestMapping(value = "queryPayScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle queryPayScheme(
            @ApiParam(name = "projectNo", required = false, value = "方案编号") @RequestParam(name = "projectNo", required = false) String projectNo) {
        try {
            return sendJsonData(ResultMessage.SUCCESS, buildConfigService.queryPayScheme(projectNo));
        } catch (Exception e) {
            return sendSuccessMessage(e.getMessage());
        }
    }

    @ApiOperation("装饰后台====》公司选择方案====》施工配置")
    @ResponseBody
    @RequestMapping(value = "chooseScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle chooseScheme(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "schemeNo", required = false, value = "方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo,
            @ApiParam(name = "optionUserId", required = false, value = "操作人ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人ID") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            logger.info("公司选择方案：{}", JSONObject.toJSONString(HttpUtils.getHttpParams()));
            buildConfigService.chooseScheme(companyId, schemeNo, optionUserId, optionUserName);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendSuccessMessage(e.getMessage());
        }
    }

    @ApiOperation("装饰后台====》查询施工方案方案====》施工配置")
    @ResponseBody
    @RequestMapping(value = "queryScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BuildSchemeConfig>> queryScheme(
            @ApiParam(name = "searchKey", required = false, value = "搜索关键字") @RequestParam(name = "searchKey", required = false) String searchKey,
            @ApiParam(name = "companyId", required = false, value = "分公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "cityStation", required = false, value = "所属分站编号") @RequestParam(name = "cityStation", required = false) String cityStation,
            @ApiParam(name = "storeNo", required = false, value = "所属门店编号") @RequestParam(name = "storeNo", required = false) String storeNo) {
        return sendJsonData(ResultMessage.SUCCESS, buildConfigService.queryScheme(searchKey, companyId, cityStation, storeNo));
    }

    @ApiOperation("装饰后台====》公司停用施工方案====》施工配置")
    @ResponseBody
    @RequestMapping(value = "stopScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle stopScheme(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "optionUserId", required = false, value = "操作人ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人ID") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            logger.info("公司停用施工方案：{}", JSONObject.toJSONString(HttpUtils.getHttpParams()));
            buildConfigService.stopScheme(companyId, optionUserId, optionUserName);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendSuccessMessage(e.getMessage());
        }
    }

    @ApiOperation("装饰后台====》公司删除施工方案====》施工配置")
    @ResponseBody
    @RequestMapping(value = "companyDelScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle companyDelScheme(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "optionUserId", required = false, value = "操作人ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人ID") @RequestParam(name = "optionUserName", required = false) String optionUserName,
            @ApiParam(name = "schemeNo", required = false, value = "方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo) {
        try {
            logger.info("公司停用施工方案：{}", JSONObject.toJSONString(HttpUtils.getHttpParams()));
            buildConfigService.companyDelScheme(companyId, optionUserId, optionUserName, schemeNo);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendSuccessMessage(e.getMessage());
        }
    }

    @ApiOperation("装饰后台====》公司启用用施工方案====》施工配置")
    @ResponseBody
    @RequestMapping(value = "companyEnableScheme", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle companyEnableScheme(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "schemeNo", required = false, value = "方案编号") @RequestParam(name = "schemeNo", required = false) String schemeNo,
            @ApiParam(name = "optionUserId", required = false, value = "操作人ID") @RequestParam(name = "optionUserId", required = false) String optionUserId,
            @ApiParam(name = "optionUserName", required = false, value = "操作人ID") @RequestParam(name = "optionUserName", required = false) String optionUserName) {
        try {
            logger.info("公司启用用施工方案：{}", JSONObject.toJSONString(HttpUtils.getHttpParams()));
            buildConfigService.companyEnableScheme(companyId, schemeNo, optionUserId, optionUserName);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendSuccessMessage(e.getMessage());
        }
    }

    @ApiOperation("装饰后台====》查询公司配置的施工方案====》施工配置")
    @ResponseBody
    @RequestMapping(value = "queryByCompanyId", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<CompanySchemeVo>>> queryByCompanyId(
            @ApiParam(name = "companyId", required = false, value = "公司ID") @RequestParam(name = "companyId", required = false) String companyId,
            @ApiParam(name = "pageSize", required = false, value = "每页多少条") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页，从1开始") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        try {
            return sendJsonData(ResultMessage.SUCCESS,buildConfigService.queryByCompanyId(companyId, pageSize, pageIndex));
        } catch (Exception e) {
            return sendSuccessMessage(e.getMessage());
        }
    }
}
