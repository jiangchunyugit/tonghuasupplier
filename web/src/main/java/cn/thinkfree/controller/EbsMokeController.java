package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.ebsmokevo.EbsMokeBranchCompany;
import cn.thinkfree.database.vo.ebsmokevo.EbsCityBranch;
import cn.thinkfree.database.vo.ebsmokevo.StoreBusinessEntity;
import cn.thinkfree.service.ebsmoke.EbsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/ebsMoke")
@Api(value = "前端使用---埃森哲分站mock接口---蒋春雨",description = "前端使用---埃森哲分站mock接口---蒋春雨")
public class EbsMokeController extends AbsBaseController{


    @Autowired
    EbsService ebsService;
    /**
     * 埃森哲分公司查询
     */
    @GetMapping(value = "/ebsBranchCompany")
    @MyRespBody
    @ApiOperation(value="埃森哲管理：选择分公司（埃森哲分公司查询）")
    public MyRespBundle<List<EbsMokeBranchCompany>> ebsBranchCompany(){

        return sendJsonData(ResultMessage.SUCCESS, ebsService.ebsBranchCompanyList());
    }

    /**
     * 埃森哲城市分站查询
     */
    @GetMapping(value = "/ebsCityBranch")
    @MyRespBody
    @ApiOperation(value="埃森哲管理：选择城市站点||站点（埃森哲城市分站查询）")
    public MyRespBundle<List<EbsCityBranch>> ebsCityBranch(@ApiParam("埃森哲分公司id")@RequestParam(value = "id") Integer id){

        return sendJsonData(ResultMessage.SUCCESS, ebsService.ebsCityBranchList(id));
    }

    /**
     * 埃森哲分店主体查询
     */
    @GetMapping(value = "/storeBusinessEntity")
    @MyRespBody
    @ApiOperation(value="埃森哲管理：选择主体")
    public MyRespBundle<List<StoreBusinessEntity>> storeBusinessEntity(@ApiParam("埃森哲城市分站id")@RequestParam(value = "id") Integer id){

        return sendJsonData(ResultMessage.SUCCESS, ebsService.storeBusinessEntityList(id));
    }

}

