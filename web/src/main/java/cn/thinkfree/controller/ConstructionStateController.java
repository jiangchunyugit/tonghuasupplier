package cn.thinkfree.controller;


import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.service.construction.ConstructionOrderOperate;
import cn.thinkfree.service.construction.ConstructionStateService;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.construction.vo.ConstructionStateVo;
import cn.thinkfree.service.construction.vo.SiteDetailsVo;
import com.github.pagehelper.PageInfo;
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
public class ConstructionStateController extends AbsBaseController {

    @Autowired
    ConstructionStateService constructionStateService;

    @Autowired
    ConstructionOrderOperate constructionOrderOperate;


    @ApiOperation("获取状态接口")
    @MyRespBody
    @RequestMapping(value = "getState", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionStateVo> getStateByRole(@RequestParam @ApiParam(value = "项目编号",required = true) String projectNo,
                                                            @RequestParam @ApiParam(value = "操作角色",required = true) String role) {

        return constructionStateService.getConstructionState(projectNo,role);
    }

    @ApiOperation("状态变更权限接口")
    @MyRespBody
    @RequestMapping(value = "isState", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> queryIsState(@RequestParam @ApiParam(value = "项目编号",required = true) String projectNo,
                                                          @RequestParam @ApiParam(value = "操作角色",required = true) String role) {

        return constructionStateService.queryIsState(projectNo,role);
    }

    @ApiOperation("修改状态接口")
    @MyRespBody
    @RequestMapping(value = "updateState", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> updateStateByRole(@RequestParam @ApiParam(value = "项目编号",required = true) String projectNo,
                                                  @RequestParam @ApiParam(value = "操作角色",required = true) String role,
                                                  @RequestParam @ApiParam(value = "状态值",required = true) int stateCode){

        return constructionStateService.updateConstructionState(projectNo, role,stateCode);
    }


    @ApiOperation("运营平台接口---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getOperateList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderManageVo> getConstructionInfoList(@RequestParam @ApiParam(value = "页码",required = true) int pageNum,
                                                                           @RequestParam @ApiParam(value = "每页条数",required = true) int pageSize,
                                                                           @RequestParam(required = false) @ApiParam(value = "城市名称")  String cityName){

        return constructionOrderOperate.getConstructionOrderList(pageNum,pageSize,cityName);
    }

    @ApiOperation("工地管理接口---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getConstructionSiteList", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<ConstructionOrderManageVo> getConstructionSiteList(@RequestParam @ApiParam(value = "页码",required = true) Integer pageNum,
                                                                           @RequestParam @ApiParam(value = "每页条数",required = true) Integer pageSize,
                                                                           @RequestParam(required = false) @ApiParam(value = "城市名称")  String cityName){

        return constructionOrderOperate.getConstructionSiteList(pageNum,pageSize,cityName);
    }

    @ApiOperation("工地详情信息接口---->孙宇专用")
    @MyRespBody
    @RequestMapping(value = "getSiteDetails", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<SiteDetailsVo> getSiteDetails(@RequestParam(required = false) @ApiParam(value = "项目编号 1223098338391")  String projectNo){

        return constructionOrderOperate.getSiteDetails(projectNo);
    }

}
