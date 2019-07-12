package cn.tonghua.controller;


import cn.tonghua.core.annotation.MyRespBody;
import cn.tonghua.core.base.AbsBaseController;
import cn.tonghua.core.bundle.MyRespBundle;
import cn.tonghua.core.constants.ResultMessage;
import cn.tonghua.service.example.ExampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 测试类
 */
@RestController
@RequestMapping(value = "/branchCompany")
@Api(value = "前端使用---测试类---蒋春雨",description = "前端使用---测试类---蒋春雨")
public class ExampleController extends AbsBaseController {

    @Autowired
    ExampleService exampleService;

    /**
     * 测试类
     */
    @RequestMapping(value = "/eample", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="测试类")
    public MyRespBundle<String> ebsBranchCompanylist(){

        return sendJsonData(ResultMessage.SUCCESS, exampleService.testFirst());
    }
}

