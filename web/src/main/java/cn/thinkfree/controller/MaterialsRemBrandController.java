package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.DealerBrandInfo;
import cn.thinkfree.database.model.DealerCategory;
import cn.thinkfree.database.model.MaterialsRemBrand;
import cn.thinkfree.database.model.MaterialsRemBrandSecond;
import cn.thinkfree.service.materialsrembrand.MaterialsRemBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
    public MyRespBundle<List<MaterialsRemBrand>> materialsRemBrandList(@ApiParam("品牌名称")@RequestParam(value = "sbmc" )String sbmc){

        return sendJsonData(ResultMessage.SUCCESS, materialsRemBrandService.getMaterialsRemBrands(sbmc));
    }

    @GetMapping(value = "/materialsRemBrandSecondList")
    @MyRespBody
    @ApiOperation(value="经销商：品类信息")
    public MyRespBundle<List<MaterialsRemBrandSecond>> materialsRemBrandSecondList(@ApiParam("品牌编码")@RequestParam(value = "sbbm") String sbbm){

        if (StringUtils.isBlank(sbbm)) {
            return sendJsonData(ResultMessage.FAIL,"操作失败");
        }
        return sendJsonData(ResultMessage.SUCCESS, materialsRemBrandService.getMaterialsRemBrandSecond(sbbm));
    }

    @GetMapping(value = "/dealerBrandList")
    @MyRespBody
    @ApiOperation(value="经销商：品牌信息")
    public MyRespBundle<List<DealerBrandInfo>> dealerBrandList(@ApiParam("公司id")@RequestParam(value = "companyId") String companyId){

        if (StringUtils.isBlank(companyId)) {
            return sendJsonData(ResultMessage.FAIL,"操作失败");
        }
        if (materialsRemBrandService.getDealerBrandList(companyId).size() ==0) {
            return sendFailMessage("请先去公司入驻办理品牌");
        }
        return sendJsonData(ResultMessage.SUCCESS, materialsRemBrandService.getDealerBrandList(companyId));
    }

    @GetMapping(value = "/dealerBrandSecondList")
    @MyRespBody
    @ApiOperation(value="经销商：品类信息")
    public MyRespBundle<List<DealerCategory>> dealerBrandSecondList(@ApiParam("品牌id")@RequestParam(value = "id") Integer id){

        if (id != null) {
            return sendJsonData(ResultMessage.SUCCESS, materialsRemBrandService.getDealerBrandSecondList(id));
        }
        return sendJsonData(ResultMessage.FAIL,"操作失败");

    }
}

