package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.ebsmokevo.EbsCityBranch;
import cn.thinkfree.database.vo.ebsmokevo.StoreBusinessEntity;
import cn.thinkfree.service.ebsmoke.EbsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/ebsMoke")
@Api(value = "埃森哲默克接口",description = "埃森哲默克接口")
public class EbsMokeController extends AbsBaseController{


    @Autowired
    EbsService ebsService;
    /**
     * 埃森哲分公司查询
     */
    @GetMapping(value = "/ebsBranchCompany")
    @MyRespBody
    @ApiOperation(value="埃森哲管理：埃森哲分公司查询")
    public MyRespBundle<Map> ebsBranchCompany(){

        return sendJsonData(ResultMessage.SUCCESS, ebsService.ebsBranchCompanyList());
    }

    /**
     * 埃森哲城市分站查询
     */
    @GetMapping(value = "/ebsCityBranch")
    @MyRespBody
    @ApiOperation(value="埃森哲管理：埃森哲分公司查询")
    public MyRespBundle<List<EbsCityBranch>> ebsCityBranch(@ApiParam("分公司id")@RequestParam(value = "id") Integer id){

        return sendJsonData(ResultMessage.SUCCESS, ebsService.ebsCityBranchList(id));
    }

    /**
     * 埃森哲分店主体查询
     */
    @GetMapping(value = "/storeBusinessEntity")
    @MyRespBody
    @ApiOperation(value="埃森哲管理：分店主体查询")
    public MyRespBundle<List<StoreBusinessEntity>> storeBusinessEntity(@ApiParam("埃森哲城市分站id")@RequestParam(value = "id") Integer id){

        return sendJsonData(ResultMessage.SUCCESS, ebsService.storeBusinessEntityList(id));
    }

}

