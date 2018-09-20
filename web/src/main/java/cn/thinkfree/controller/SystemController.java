package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.utils.SpringContextHolder;
import cn.thinkfree.database.vo.IndexMenuVO;
import cn.thinkfree.service.constants.ProjectStatus;
import cn.thinkfree.service.designer.service.HomeStylerService;
import cn.thinkfree.service.designer.vo.HomeStyler;
import cn.thinkfree.service.designer.vo.HomeStylerVO;
import cn.thinkfree.service.index.IndexService;
import cn.thinkfree.service.remote.CloudService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(description = "系统相关操作")
@Controller
public class SystemController extends AbsBaseController {


    @Autowired
    IndexService indexService;

    @Autowired
    CloudService cloudService;

    @Autowired
    HomeStylerService homeStylerService;


    /**
     * 获取首页菜单
     * @return
     */
    @ApiOperation(value = "获取菜单",notes = "根据当前登录用户信息获取菜单")
    @GetMapping("/menu")
    @MyRespBody
    public MyRespBundle<List<IndexMenuVO>> menu(){
        List<IndexMenuVO> indexMenuVOS = indexService.listIndexMenu();
        return sendJsonData(ResultMessage.SUCCESS,indexMenuVOS);
    }



    @RequestMapping("/loginPage")
    public String loginPage(){
        ApplicationContext ac = SpringContextHolder.getApplicationContext();
        System.out.println(ac);
        System.out.println("gotoLogin");
        return  "loginPage";
    }

    @RequestMapping("/index")
    public String index(){
        System.out.println("index");
        return "index";
    }

    @RequestMapping("/gotoPage")
    public String gotoPage(String page){
        System.out.println(page);
        int pos = page.indexOf("?");
        return  (pos > -1? page.substring(0,pos)  :page);
    }

    @RequestMapping("/test")
    public void test(){
//        SystemMessage systemMessage = new SystemMessage();
//        systemMessage.setCompanyId("2");
//        systemMessage.setSendUserId("1");
//        systemMessage.setTitle("1");
//        systemMessage.setContent("2");
//        systemMessage.setId(1);
//        systemMessage.setSendUser("user");
//        cloudService.sendNotice(systemMessage, Lists.newArrayList("1"));
//        cloudService.sendSms("18910441835","123456");
//        cloudService.projectUpOnline("ITEM18082910221300000EH", ProjectStatus.WaitStart.shortVal());

    }

    @RequestMapping("/test1")
    public void test1(){

//        cloudService.sendSms("18910471835","123456");

    }

    @RequestMapping("/spiler")
    @ResponseBody
    public String spiler(String id){

        String ls = homeStylerService.saveHomeStyler(id);

        return "Success";
    }

    @RequestMapping("/homeStyler")
    @ResponseBody
    public MyRespBundle<HomeStylerVO> mock(String id){
        HomeStyler homeStyler = homeStylerService.findDataByProjectNo(id);
        HomeStylerVO homeStylerVO = new HomeStylerVO();
        homeStylerVO.setProjectNo(id);
        homeStylerVO.setSpaceDetailsBeans(homeStyler.getSpaceDetails());
        return sendJsonData(ResultMessage.SUCCESS,homeStylerVO);
    }





}
