package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.AfConfigEditVO;
import cn.thinkfree.database.vo.AfConfigListVO;
import cn.thinkfree.service.approvalflow.AfConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 审批流配置控制层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 18:21
 */
@RestController
@Api(description = "前端-审批流配置-宋传让")
@RequestMapping("af-config")
public class AfConfigController extends AbsBaseController {


    @Autowired
    private AfConfigService configService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端-查询所有审批流-宋传让")
    @ApiParam(name = "schemeNo", value= "方案编号", required = true)
    public MyRespBundle<AfConfigListVO> list(@RequestParam(name = "schemeNo") String schemeNo){
        return sendJsonData(ResultMessage.SUCCESS, configService.list(schemeNo));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端-修改审批流配置-宋传让")
    @ApiParam(name = "configEditVO", value= "审批流信息", required = true)
    public MyRespBundle edit(@RequestBody AfConfigEditVO configEditVO){
        configService.edit(configEditVO);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }
}
