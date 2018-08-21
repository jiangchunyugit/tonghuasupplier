package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.IndexProjectChartItemVO;
import cn.thinkfree.database.vo.IndexReportVO;
import cn.thinkfree.service.index.IndexService;
import cn.thinkfree.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
     * @param unit 单位
     */
    @GetMapping("/chart")
    @MyRespBody
    public MyRespBundle<List<IndexProjectChartItemVO>> summaryChart(@RequestParam Integer unit){
        List<IndexProjectChartItemVO> data =indexService.summaryProjectChart(unit);
        return sendJsonData(ResultMessage.SUCCESS,data);
    }




}
