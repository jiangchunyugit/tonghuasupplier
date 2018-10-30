package cn.thinkfree.controller;


import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.construction.ConstructionStateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *  施工状态相关接口
 */
@Api(value = "施工状态相关API接口", tags = "施工状态相关API接口")
@Controller
@RequestMapping("construction")
public class ConstructionStateController extends AbsBaseController {

    @Autowired
    ConstructionStateService constructionStateService;


    @ApiOperation("获取状态接口")
    @MyRespBody
    @RequestMapping(value = "getState", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> getStateByRole(@RequestParam @ApiParam(value = "项目编号",required = true) String projectNo,
                                               @RequestParam @ApiParam(value = "操作角色",required = true) String role) {

        return constructionStateService.getConstructionState(projectNo,role);
    }

    @ApiOperation("修改状态接口")
    @MyRespBody
    @RequestMapping(value = "updateState", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<String> updateStateByRole(@RequestParam @ApiParam(value = "项目编号",required = true) String projectNo,
                                               @RequestParam @ApiParam(value = "操作角色",required = true) String role) {

        return constructionStateService.getConstructionState(projectNo,role);
    }

}
