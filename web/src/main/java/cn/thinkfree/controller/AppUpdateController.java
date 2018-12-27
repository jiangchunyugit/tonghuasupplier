package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.platform.update.AppUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public MyRespBundle<Object> getVersionMsg(){
        return sendJsonData(ResultMessage.SUCCESS,appUpdateService.getVersionMsg());
    }
}
