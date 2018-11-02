package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.BasicsData;
import cn.thinkfree.service.platform.basics.BasicsService;
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
@Api(value = "基础信息接口", tags = "基础信息接口--->app和后台公用")
@Controller
@RequestMapping("basics")
public class BasicsController extends AbsBaseController {
    @Autowired
    private BasicsService basicsService;
    @ApiOperation("证件类型")
    @MyRespBody
    @RequestMapping(value = "cardType", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BasicsData>> cardType() {
        //暂时写死
        List<BasicsData> cardTypeVos = basicsService.idCardTypes("ID_CARD_TYPE");
        return sendJsonData(ResultMessage.SUCCESS, cardTypeVos);
    }

    @ApiOperation("国家类型")
    @MyRespBody
    @RequestMapping(value = "countryType", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BasicsData>> countryType() {
        //暂时写死
        List<BasicsData> cardTypeVos = basicsService.idCardTypes("COUNTRY_TYPE");
        return sendJsonData(ResultMessage.SUCCESS, cardTypeVos);
    }

    @ApiOperation("取消设计原因，取汉字传回后台")
    @MyRespBody
    @RequestMapping(value = "cancelDesign", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BasicsData>> cancelDesign() {
        //暂时写死
        List<BasicsData> cardTypeVos = basicsService.idCardTypes("CANCEL_DESIGN");
        return sendJsonData(ResultMessage.SUCCESS, cardTypeVos);
    }

    @ApiOperation("退款原因，取汉字传回后台")
    @MyRespBody
    @RequestMapping(value = "refund", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BasicsData>> refund() {
        //暂时写死
        List<BasicsData> cardTypeVos = basicsService.idCardTypes("REFUND");
        return sendJsonData(ResultMessage.SUCCESS, cardTypeVos);
    }

    @ApiOperation("取消施工原因，取汉字传回后台")
    @MyRespBody
    @RequestMapping(value = "cancelCons", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<BasicsData>> cancelCons() {
        //暂时写死
        List<BasicsData> cardTypeVos = basicsService.idCardTypes("CANCEL_CONS");
        return sendJsonData(ResultMessage.SUCCESS, cardTypeVos);
    }

    @ApiOperation("根据类型查询地区信息，先调用一下，看看，直接返回的listmap集合")
    @MyRespBody
    @RequestMapping(value = "pua", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<List<Map<String, String>>> pua(
            @ApiParam(name = "type", required = false, value = "类型，1省份，2市，3区") @RequestParam(name = "type", required = false) int type,
            @ApiParam(name = "parentCode", required = false, value = "父级ID") @RequestParam(name = "parentCode", required = false) String parentCode) {
        try{
            return sendJsonData(ResultMessage.SUCCESS,basicsService.pua(type,parentCode));
        }catch (Exception e){
            return sendFailMessage(e.getMessage());
        }
    }
}
