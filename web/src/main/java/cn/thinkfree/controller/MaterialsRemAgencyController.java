package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.MaterialsRemAgency;
import cn.thinkfree.service.materialsremagency.MaterialsRemAgencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 经销商
 */
@RestController
@RequestMapping(value = "/materialsRemAgency")
@Api(value = "前端使用---经销商---蒋春雨",description = "前端使用---经销商---蒋春雨")
public class MaterialsRemAgencyController extends AbsBaseController{

    @Autowired
    MaterialsRemAgencyService materialsRemAgencyService;

    @GetMapping(value = "/materialsRemAgencyList")
    @MyRespBody
    @ApiOperation(value="经销商：经销商信息")
    public MyRespBundle<List<MaterialsRemAgency>> materialsRemAgencyList(@ApiParam("经销商编码")@RequestParam(value = "code") String code){

        return sendJsonData(ResultMessage.SUCCESS, materialsRemAgencyService.getMaterialsRemAgencys(code));
    }
}

