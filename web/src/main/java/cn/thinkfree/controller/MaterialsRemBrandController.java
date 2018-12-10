package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.MaterialsRemBrand;
import cn.thinkfree.database.model.MaterialsRemBrandSecond;
import cn.thinkfree.service.materialsrembrand.MaterialsRemBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jiangchunyu(后台)
 * @date 2018
 * @Description 品牌
 */
@RestController
@RequestMapping(value = "/materialsRemBrand")
@Api(value = "前端使用---品牌---蒋春雨",description = "前端使用---品牌---蒋春雨")
public class MaterialsRemBrandController extends AbsBaseController{

    @Autowired
    MaterialsRemBrandService materialsRemBrandService;

    @GetMapping(value = "/materialsRemBrandList")
    @MyRespBody
    @ApiOperation(value="经销商：品牌信息")
    public MyRespBundle<List<MaterialsRemBrand>> materialsRemBrandList(){

        return sendJsonData(ResultMessage.SUCCESS, materialsRemBrandService.getMaterialsRemBrands());
    }

    @GetMapping(value = "/materialsRemBrandSecondList")
    @MyRespBody
    @ApiOperation(value="经销商：品类信息")
    public MyRespBundle<List<MaterialsRemBrandSecond>> materialsRemBrandSecondList(@ApiParam("品牌编码")@RequestParam(value = "sbbm") String sbbm){

        return sendJsonData(ResultMessage.SUCCESS, materialsRemBrandService.getMaterialsRemBrandSecond(sbbm));
    }
}

