package cn.thinkfree.controller;


import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.ConstructionOrderOperate;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.construction.vo.ConstructionOrderCommonVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *  施工状态相关接口
 */
@Api(value = "施工订单相关API接口---->孙宇专用", tags = "施工订单相关API接口---->孙宇专用")
@Controller
@RequestMapping("construction")
public class ConstructionOrderController extends AbsBaseController {

    @Autowired
    ConstructionStateService constructionStateService;

    @Autowired
    ConstructionOrderOperate constructionOrderOperate;


    @ApiOperation("运营平台接口---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getOperateList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderCommonVo> getConstructionInfoList(@RequestParam @ApiParam(value = "页码",required = true) int pageNum,
                                                                           @RequestParam @ApiParam(value = "每页条数",required = true) int pageSize,
                                                                           @RequestParam(required = false) @ApiParam(value = "城市名称")  String cityName){

        return constructionOrderOperate.getOrderList(pageNum,pageSize,cityName);
    }

    @ApiOperation("运营平台接口---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getOperateNum", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderManageVo> getOperateNum(){

        return constructionOrderOperate.getOrderNum();
    }


}
