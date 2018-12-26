package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.MaterialsRemLeaseContract;
import cn.thinkfree.database.model.MaterialsRemShop;
import cn.thinkfree.service.materialsremshop.MaterialsRemShopService;
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
 * @Description 门店
 */
@RestController
@RequestMapping(value = "/materialsRemShop")
@Api(value = "前端使用---门店---蒋春雨",description = "前端使用---门店---蒋春雨")
public class MaterialsRemShopController extends AbsBaseController{

    @Autowired
    MaterialsRemShopService materialsRemShopService;

    @GetMapping(value = "/materialsRemShopList")
    @MyRespBody
    @ApiOperation(value="经销商：门店信息")
    public MyRespBundle<List<MaterialsRemLeaseContract>> materialsRemShopList(@ApiParam("经销商编号")@RequestParam(value = "dealerCompanyId") String dealerCompanyId){

        if (StringUtils.isBlank(dealerCompanyId)) {
            return sendJsonData(ResultMessage.FAIL,"操作失败");
        }
        return sendJsonData(ResultMessage.SUCCESS, materialsRemShopService.getMaterialsRemShops(dealerCompanyId));
    }

    @GetMapping(value = "/materialsRemLeaseContractList")
    @MyRespBody
    @ApiOperation(value="经销商：摊位信息")
    public MyRespBundle<List<MaterialsRemLeaseContract>> materialsRemLeaseContractList(@ApiParam("经销商编号")@RequestParam(value = "dealerCompanyId") String dealerCompanyId,
                                                                                       @ApiParam("门店编码")@RequestParam(value = "fddm") String fddm){

        if (StringUtils.isNotBlank(dealerCompanyId) && StringUtils.isNotBlank(fddm)) {
            return sendJsonData(ResultMessage.SUCCESS, materialsRemShopService.getMaterialsRemLeaseContracts(dealerCompanyId,fddm));
        }
        return sendJsonData(ResultMessage.FAIL,"操作失败");
    }
}

