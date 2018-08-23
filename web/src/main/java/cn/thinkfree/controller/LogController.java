package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.LogInfo;
import cn.thinkfree.database.vo.LogInfoSEO;
import cn.thinkfree.service.system.LogInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController extends AbsBaseController {


    @Autowired
    LogInfoService logInfoService;

    /**
     * 分页查询日志
     * @param logInfoSEO
     * @return
     */
    @GetMapping("/page")
    @MyRespBody
    public MyRespBundle<PageInfo<LogInfo>> page(LogInfoSEO logInfoSEO){
        PageInfo<LogInfo> page = logInfoService.pageLogInfo(logInfoSEO);
        return sendJsonData(ResultMessage.SUCCESS,page);
    }

}
