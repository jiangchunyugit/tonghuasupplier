package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.platform.update.AppUpdateService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xusonghui
 * app更新接口
 */
@RestController
@RequestMapping(value = "update")
public class AppUpdateController extends AbsBaseController{
    @Autowired
    private AppUpdateService appUpdateService;

    @RequestMapping("versionMsg")
    public MyRespBundle<Object> getVersionMsg(@RequestParam(name = "appType") @ApiParam(name = "appType", value = "app类型：1消费者，2施工app，3设计师app", required = false) String appType){
        return sendJsonData(ResultMessage.SUCCESS,appUpdateService.getVersionMsg(appType));
    }
}
