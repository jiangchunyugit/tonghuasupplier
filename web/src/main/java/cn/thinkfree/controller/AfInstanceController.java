package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 18:22
 */
@RestController
@Api(value = "审批流实例")
@RequestMapping("af-instance")
public class AfInstanceController extends AbsBaseController {


//    @RequestMapping(value = "/start", method = RequestMethod.POST)
//    @MyRespBody
//    @ApiOperation(value="查询所有审批流")
//    public MyRespBundle start(){
//        return sendJsonData(ResultMessage.SUCCESS, configService.list());
//    }
//
//    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//    @MyRespBody
//    @ApiOperation(value="审批流节点信息")
//    @ApiParam(name = "num", value= "审批流编号", required = true)
//    public MyRespBundle approval(@RequestParam(name = "num") String num){
//        return sendJsonData(ResultMessage.SUCCESS, configService.detail(num));
//    }
}
