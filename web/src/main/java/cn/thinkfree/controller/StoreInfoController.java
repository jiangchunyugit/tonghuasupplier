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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @GetMapping(value = "/storeInfoList")
    @MyRespBody
    @ApiOperation(value="门店：全部门店信息")
    public MyRespBundle<List<StoreInfo>> storeInfoList(){

        return sendJsonData(ResultMessage.SUCCESS, storeInfoService.getHrOrganizationEntity());
    }
}
