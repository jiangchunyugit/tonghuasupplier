package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.IndexReportVO;
import cn.thinkfree.service.index.IndexService;
import cn.thinkfree.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/index")
public class IndexController extends AbsBaseController {

    @Autowired
    UserService userService;

    @Autowired
    IndexService indexService;


    /**
     * 首页 -- 用户总览 项目总览
     * @return
     */
    @GetMapping("/summary")
    @MyRespBody
    public MyRespBundle<IndexReportVO> countReport(){
        IndexReportVO reportVO = indexService.summary();
        return sendJsonData(ResultMessage.SUCCESS,reportVO);
    }

    /**
     * 首页走势图
     * TODO 确认结构后再做
     */
    @GetMapping("/chart")
    @MyRespBody
    public void summaryChart(){

    }


}
