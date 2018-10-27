package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.approvalflow.AfConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 18:21
 */
@RestController
@Api(tags = "审批流配置")
@RequestMapping("af-config")
public class AfConfigController extends AbsBaseController {

    @Resource
    private AfConfigService configService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="查询所有审批流")
    public MyRespBundle list(){
        return sendJsonData(ResultMessage.SUCCESS, configService.list());
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="审批流节点信息")
    @ApiParam(name = "configNo", value= "审批流编号", required = true)
    public MyRespBundle detail(@RequestParam(name = "configNo") String configNo){
        return sendJsonData(ResultMessage.SUCCESS, configService.detail(configNo));
    }

}
