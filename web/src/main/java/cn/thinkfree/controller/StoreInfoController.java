package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.StoreInfo;
import cn.thinkfree.service.storeinfo.StoreInfoService;
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
 * @Description 门店中间表
 */
@RestController
@RequestMapping(value = "/storeInfo")
@Api(value = "前端使用---门店---蒋春雨",description = "前端使用---门店---蒋春雨")
public class StoreInfoController extends AbsBaseController {

    @Autowired
    StoreInfoService storeInfoService;
    /**
     * 查询城市分站信息
     */
    @GetMapping(value = "/storeInfoListByCityId")
    @MyRespBody
    @ApiOperation(value="门店：门店信息（通过城市分站编号查询门店信息）")
    public MyRespBundle<List<StoreInfo>> storeInfoListByCityId(@ApiParam("城市分站编号")String cityBranchCode){

        return sendJsonData(ResultMessage.SUCCESS, storeInfoService.storeInfoListByCityId(cityBranchCode));
    }

    /**
     * 查询城市分站信息
     */
    @GetMapping(value = "/storeInfoListByCompanyId")
    @MyRespBody
    @ApiOperation(value="门店：门店信息（通过分公司编号查询门店信息）")
    public MyRespBundle<List<StoreInfo>> storeInfoListByCompanyId(@ApiParam("城市分站编号")String branchCompanyCode){

        return sendJsonData(ResultMessage.SUCCESS, storeInfoService.storeInfoListByCompanyId(branchCompanyCode));
    }

    /**
     * 查询城市分站信息
     */
    @GetMapping(value = "/storeInfoListByEbsCompanyId")
    @MyRespBody
    @ApiOperation(value="门店：根据埃森哲分公司查询门店")
    public MyRespBundle<List<StoreInfo>> storeInfoListByEbsCompanyId(@ApiParam("埃森哲分公司id")
                                                                         @RequestParam(value = "branchCompEbsid") String branchCompEbsid){

        return sendJsonData(ResultMessage.SUCCESS, storeInfoService.storeInfoListByEbsCompanyId(branchCompEbsid,""));
    }

    /**
     * 查询城市分站信息
     */
    @GetMapping(value = "/updateStoreInfoListByCityBranchCode")
    @MyRespBody
    @ApiOperation(value="门店：城市分站编辑查询门店")
    public MyRespBundle<List<StoreInfo>> updateStoreInfoListByCityBranchCode(@ApiParam("埃森哲分公司id")@RequestParam(value = "branchCompEbsid") String branchCompEbsid
            ,@ApiParam("城市分站编号")@RequestParam(value = "cityBranchCode") String cityBranchCode){

        return sendJsonData(ResultMessage.SUCCESS, storeInfoService.storeInfoListByEbsCompanyId(branchCompEbsid,cityBranchCode));
    }

    /**
     * 查询城市分站信息
     */
    @GetMapping(value = "/businessEntityStoreByCityBranchCode")
    @MyRespBody
    @ApiOperation(value="门店：查询城市站点下分站")
    public MyRespBundle<List<StoreInfo>> businessEntityStoreByCityBranchCode(@ApiParam("城市分站编码")@RequestParam(value = "cityBranchCode") String cityBranchCode
            ,@ApiParam("经营主体编码") @RequestParam(value = "businessEntityCode") String businessEntityCode){

        return sendJsonData(ResultMessage.SUCCESS, storeInfoService.businessEntityStoreByCityBranchCode(cityBranchCode,businessEntityCode));
    }
}
