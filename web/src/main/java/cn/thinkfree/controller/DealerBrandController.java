package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.DealerBrandInfo;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.companysubmit.DealerBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ying007
 * @date 2018/09/14
 * 公司资质
 */
@RestController
@RequestMapping(value = "/dealerBrand")
@Api(value = "经销商品牌接口--李阳",description = "经销商品牌接口--李阳")
public class DealerBrandController  extends AbsBaseController {

    @Autowired
    DealerBrandService dealerBrandService;


    /**
     * 品牌审核回显
     * @param brandDetailVO
     * @return
     */
    @RequestMapping(value = "/applyBrandDetail", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--经销商后台--已申请品牌")
    public MyRespBundle<DealerBrandInfo> applyBrandDetail(BrandDetailVO brandDetailVO){
        List<DealerBrandInfo> auditBrandInfoVOS = dealerBrandService.applyBrandDetail(brandDetailVO);
        return sendJsonData(success, "操作成功", auditBrandInfoVOS);
    }

    /**
     * 品牌添加
     * @param
     * @return
     */
    @RequestMapping(value = "/saveBrand", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端--经销商平台--添加经销商品牌")
    public MyRespBundle<String> changeCompanyInfo(@ApiParam("经销商品牌信息")DealerBrandInfoVO dealerBrandInfo){
        Map<String, Object> map = dealerBrandService.saveBrand(dealerBrandInfo);
        return sendJsonData(ResultMessage.SUCCESS, map);
    }

    /**
     * 品牌审核回显
     * @param brandDetailVO
     * @return
     */
    @RequestMapping(value = "/showBrandDetail", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--经销商后台--品牌审核--回显接口2")
    public MyRespBundle<List<AuditBrandInfoVO>> showBrandDetail(BrandDetailVO brandDetailVO){
        List<AuditBrandInfoVO> auditBrandInfoVOS = dealerBrandService.showBrandDetail(brandDetailVO);
        return sendJsonData(success, "操作成功", auditBrandInfoVOS);
    }
    /**
     * 品牌审核回显
     * @param agencyCode
     * @return
     */
    @RequestMapping(value = "/showBrandItems", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--经销商后台--品牌审核--回显接口1")
    public MyRespBundle<BrandItemsVO> showBrandItems(@ApiParam("公司编号") @RequestParam(value = "companyId") String companyId,
                                                              @ApiParam("经销商编号") @RequestParam(value = "agencyCode")String agencyCode){
        List<BrandItemsVO> brandItemsVOS = dealerBrandService.showBrandItems(companyId, agencyCode);
        return sendJsonData(success, "操作成功", brandItemsVOS);
    }

    /**
     * 运营人员审批品牌
     * @author liyang
     * @return Message
     */
    @ApiOperation(value = "前端--运营后台--经销商公司管理--新增品牌审核--运营人员审批品牌")
    @PostMapping("/auditBrand")
    @MyRespBody
    //@MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_CONTRACT,desc = "合同审批")
    public MyRespBundle<Map<String,Object>> auditBrand(@ApiParam("审批参数")PcAuditInfoVO pcAuditInfoVO){
        Map<String,Object> result = dealerBrandService.auditBrand(pcAuditInfoVO);
        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 品牌变更:新增一条状态为变更审批中的品牌信息
     * 审批：1：如果成功：变更原来数据状态为变更成功，最新数据变更为审批通过，
     *                2：如果失败：则直接变更最新数据为变更不通过
     * @author liyang
     * @return Message
     */
    @ApiOperation(value = "前端--运营后台--经销商公司管理--品牌变更")
    @PostMapping("/updateBrand")
    @MyRespBody
    //@MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_CONTRACT,desc = "合同审批")
    public MyRespBundle<String> updateBrand(@ApiParam("经销商品牌信息")DealerBrandInfoVO dealerBrandInfo){
        boolean result = dealerBrandService.updateBrand(dealerBrandInfo);
        if(result){
            return sendSuccessMessage("变更成功");
        }
        return sendFailMessage("变更失败");
    }

    /**
     * 变更审批
     * 审批：1：如果成功：变更原来数据状态为变更成功，最新数据变更为审批通过，
     *                2：如果失败：则直接变更最新数据为变更不通过
     * @author liyang
     * @return Message
     */
    @ApiOperation(value = "前端--运营后台--运营管理--经销商列表--品牌审核--运营人员审批变更品牌")
    @PostMapping("/auditChangeBrand")
    @MyRespBody
    //@MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_CONTRACT,desc = "合同审批")
    public MyRespBundle<Map<String,Object>> auditChangeBrand(@ApiParam("审批参数")PcAuditInfoVO pcAuditInfoVO){
        Map<String,Object> result = dealerBrandService.auditChangeBrand(pcAuditInfoVO);
        return sendJsonData(ResultMessage.SUCCESS, result);
    }

    /**
     * 入驻公司签约品牌回显
     * @return
     */
    @RequestMapping(value = "/showSignBrand", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--经销商后台--入驻公司签约品牌回显")
    public MyRespBundle<List<DealerBrandInfo>> showSignBrand(@ApiParam("公司编号") @RequestParam(value = "companyId") String companyId){
        List<DealerBrandInfo> brandItemsVOS = dealerBrandService.showSignBrand(companyId);
        return sendJsonData(success, "操作成功", brandItemsVOS);
    }

    @ApiOperation(value = "前端--运营后台--经销商公司管理--品牌编辑")
    @PostMapping("/editBrand")
    @MyRespBody
    //@MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_CONTRACT,desc = "合同审批")
    public MyRespBundle<String> editBrand(@ApiParam("品牌信息")DealerBrandInfoVO dealerBrandInfo){
        boolean result = dealerBrandService.editBrand(dealerBrandInfo);
        if(result){
            return sendSuccessMessage("操作成功");
        }
        return sendFailMessage("操作失败");
    }

}
