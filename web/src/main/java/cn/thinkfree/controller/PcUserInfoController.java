package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.pcUser.PcUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/userInfo")
@Api(value = "账户信息（权限管理）", description = "账户信息（权限管理）")
public class PcUserInfoController extends AbsBaseController {

    @Autowired
    PcUserInfoService pcUserInfoService;

    /**
     * 修改密码
     */
    @RequestMapping(value = "/updatePassWord", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "oldPassWord", value = "原始密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "newPassWord", value = "新密码", required = true, dataType = "String")
    })
    public MyRespBundle<String> updatePassWord(@RequestParam String oldPassWord, @RequestParam String newPassWord){
        String msg = pcUserInfoService.updatePassWord(oldPassWord, newPassWord);
        return sendJsonData(ResultMessage.SUCCESS, msg);
    }

}
