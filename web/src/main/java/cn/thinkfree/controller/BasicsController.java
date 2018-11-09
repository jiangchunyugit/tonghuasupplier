package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.BasicsData;
import cn.thinkfree.service.platform.basics.BasicsService;
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
import java.util.Map;

/**
 * @author xusonghui
 * 基础字典性信息提供
 */
@Api(value = "基础信息接口", tags = "基础信息接口--->app和后台公用===>徐松辉")
@Controller
@RequestMapping("basics")
public class BasicsController extends AbsBaseController {
    @Autowired
    private BasicsService basicsService;

    @ApiOperation("证件类型")
    @MyRespBody
    @RequestMapping(value = "cardType", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BasicsData>> cardType() {
        return sendJsonData(ResultMessage.SUCCESS, basicsService.cardTypes());
    }

    @ApiOperation("国家类型")
    @MyRespBody
    @RequestMapping(value = "countryType", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BasicsData>> countryType() {
        return sendJsonData(ResultMessage.SUCCESS, basicsService.countryType());
    }

    @ApiOperation("取消设计原因，取汉字传回后台")
    @MyRespBody
    @RequestMapping(value = "cancelDesign", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BasicsData>> cancelDesign() {
        return sendJsonData(ResultMessage.SUCCESS, basicsService.cancelDesign());
    }

    @ApiOperation("退款原因，取汉字传回后台")
    @MyRespBody
    @RequestMapping(value = "refund", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BasicsData>> refund() {
        return sendJsonData(ResultMessage.SUCCESS, basicsService.refund());
    }

    @ApiOperation("取消施工原因，取汉字传回后台")
    @MyRespBody
    @RequestMapping(value = "cancelCons", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BasicsData>> cancelCons() {
        return sendJsonData(ResultMessage.SUCCESS, basicsService.cancelCons());
    }

    @ApiOperation("查询设计师拒绝接单原因")
    @MyRespBody
    @RequestMapping(value = "cancelDesigner", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BasicsData>> cancelDesigner() {
        return sendJsonData(ResultMessage.SUCCESS, basicsService.queryData("CANCEL_DESIGNER"));
    }

    @ApiOperation("根据分组编码查询配置的基础数据")
    @MyRespBody
    @RequestMapping(value = "configList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<PageVo<List<BasicsData>>> configList(
            @ApiParam(name = "groupCode", required = false, value = "分组类型") @RequestParam(name = "groupCode", required = false) String groupCode,
            @ApiParam(name = "pageSize", required = false, value = "每页条数") @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @ApiParam(name = "pageIndex", required = false, value = "第几页") @RequestParam(name = "pageIndex", required = false, defaultValue = "1") int pageIndex) {
        return sendJsonData(ResultMessage.SUCCESS, basicsService.queryData(groupCode, pageSize, pageIndex));
    }

    @ApiOperation("获取所有数据编码")
    @MyRespBody
    @RequestMapping(value = "allParentCode", method = {RequestMethod.GET})
    public MyRespBundle<List<Map<String, String>>> allParentCode() {
        return sendJsonData(ResultMessage.SUCCESS, basicsService.allParentCode());
    }

    @ApiOperation("创建户型结构，房屋类型，房屋属性，计费项目")
    @MyRespBody
    @RequestMapping(value = "createBasics", method = {RequestMethod.POST})
    public MyRespBundle createBasicsData(
            @ApiParam(name = "dataId", required = false, value = "数据唯一ID") @RequestParam(name = "dataId", required = false) String dataId,
            @ApiParam(name = "groupCode", required = false, value = "分组类型") @RequestParam(name = "groupCode", required = false) String groupCode,
            @ApiParam(name = "basicsName", required = false, value = "数据名称：如：房屋类型，房屋属性") @RequestParam(name = "basicsName", required = false) String basicsName,
            @ApiParam(name = "remark", required = false, value = "备注") @RequestParam(name = "remark", required = false) String remark) {
        try {
            basicsService.createBasics(dataId, groupCode, basicsName, remark);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("删除户型结构，房屋类型，房屋属性，计费项目")
    @MyRespBody
    @RequestMapping(value = "delBasicsData", method = {RequestMethod.POST})
    public MyRespBundle delBasicsData(
            @ApiParam(name = "dataId", required = false, value = "数据ID") @RequestParam(name = "dataId", required = false) String dataId) {
        try {
            basicsService.delBasics(dataId);
            return sendSuccessMessage(null);
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }

    @ApiOperation("根据类型查询地区信息，先调用一下，看看，直接返回的listmap集合")
    @MyRespBody
    @RequestMapping(value = "pua", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<Map<String, String>>> pua(
            @ApiParam(name = "type", required = false, value = "类型，1省份，2市，3区") @RequestParam(name = "type", required = false) int type,
            @ApiParam(name = "parentCode", required = false, value = "父级ID") @RequestParam(name = "parentCode", required = false) String parentCode) {
        try {
            return sendJsonData(ResultMessage.SUCCESS, basicsService.pua(type, parentCode));
        } catch (Exception e) {
            return sendFailMessage(e.getMessage());
        }
    }
}
