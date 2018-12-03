package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.ConstructionStageNunVO;
import cn.thinkfree.service.construction.OrderListCommonService;
import cn.thinkfree.service.construction.vo.ConstructionOrderCommonVo;
import cn.thinkfree.service.construction.vo.SiteDetailsVo;
import cn.thinkfree.service.neworder.NewOrderUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: jiang
 * @Date: 2018/11/12 10:15
 * @Description:
 */
@Api(value = "施工工地相关API接口---->孙宇专用", tags = "施工工地相关API接口---->孙宇专用")
@Controller
@RequestMapping("construction")
public class SiteDetailsController extends AbsBaseController {
    @Autowired
    OrderListCommonService orderListCommonService;
    @Autowired
    NewOrderUserService newOrderUserService;
    @ApiOperation("工地信息详情接口---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getSiteDetails", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<SiteDetailsVo> getSiteDetails(@RequestParam(required = false) @ApiParam(value = "项目编号 1223098338392")String projectNo) {
        if (null == projectNo || "".equals(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        return newOrderUserService.getSiteDetails( projectNo);
    }

    @ApiOperation("施工工地列表接口---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getConstructionSiteList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderCommonVo> getConstructionSiteList(@RequestParam @ApiParam(value = "页码",required = true) int pageNum,
                                                                           @RequestParam @ApiParam(value = "每页条数",required = true) int pageSize,
                                                                           @RequestParam(required = false) @ApiParam(value = "城市名称")  String cityName){

        return newOrderUserService.getConstructionSiteList(pageNum,pageSize,cityName);
    }

    @ApiOperation("施工工地列表进度数量接口---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getScheduleNum", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionStageNunVO> getScheduleNum(){

        return newOrderUserService.getScheduleNum();
    }

    }
