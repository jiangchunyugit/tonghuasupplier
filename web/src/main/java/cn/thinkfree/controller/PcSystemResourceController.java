package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.MySystemResource;
import cn.thinkfree.service.userResource.PcSystemResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "systemResource")
@Api(value = "权限管理",description = "权限管理")
public class PcSystemResourceController extends AbsBaseController {

    @Autowired
    PcSystemResourceService pcSystemResourceService;

    /**
     * 查看权限
     */
//    @MySysLog(desc = "/sysMsg/delSysMsg",action = SysLogAction.QUERY,module = SysLogModule.PC_NEWS)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="查看权限", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "用户ID", required = true, dataType = "String")
    })
    public MyRespBundle<MySystemResource> list(@RequestParam(value = "userId")String userId){
        List<MySystemResource> msr = pcSystemResourceService.getUserResource(userId);

        return sendJsonData(ResultMessage.SUCCESS, msr);
    }

    /**
     * 更改权限
     */
//    @MySysLog(desc = "/sysMsg/delSysMsg",action = SysLogAction.QUERY,module = SysLogModule.PC_NEWS)
    @RequestMapping(value = "/updateByUser", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="修改权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "userId", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType="query", name = "resourceId", value = "权限资源ID", required = true, dataType = "List")
    })
    public MyRespBundle<String> updateByUser(@RequestParam(value = "userId")String userId,
    @RequestParam(value = "resourceId")List<Integer> resourceId){
        boolean flag = false;
        if(resourceId != null && resourceId.size() > 0){
            flag = pcSystemResourceService.saveByUserId(userId,resourceId);
            if(flag){
                return sendJsonData(ResultMessage.SUCCESS, flag);
            }
        }
        return sendJsonData(ResultMessage.FAIL, flag);
    }
}
