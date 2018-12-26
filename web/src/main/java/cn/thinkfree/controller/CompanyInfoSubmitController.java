package cn.thinkfree.controller;


import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.JoinStatus;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.PcAuditTemporaryInfo;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.companysubmit.CompanySubmitService;
import cn.thinkfree.service.contract.ContractService;
import cn.thinkfree.service.contracttemplate.ContractTemplateService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * @author ying007
 * @date 2018/09/14
 * 公司资质
 */
@RestController
@RequestMapping(value = "/companyAudit")
@Api(value = "公司入驻",description = "公司入驻")
public class CompanyInfoSubmitController extends AbsBaseController {
    @Autowired
    CompanySubmitService companySubmitService;
    
    @Autowired
	ContractService contractService;
    @Autowired
    ContractTemplateService contractTemplateService;

    /**
     * 入驻公司成功前的公司状态修改
     * @return
     */
    @RequestMapping(value = "/changeNode", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端--装饰/设计公司管理中心--入驻--入驻公司操作")
    public MyRespBundle<String> changeCompanyInfo(@ApiParam("入驻公司节点信息")JoinStatus joinStatus){
        boolean flag = companySubmitService.changeNode(joinStatus);
        if(flag){
            return sendSuccessMessage("操作成功");
        }
        return sendFailMessage("操作失败");
    }

    /**
     * 查询资质变更审批信息
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/findTempAudit", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--运营后台--公司管理--点击资质变更办理--公司详情--审批状态查询--李阳")
    public MyRespBundle<PcAuditInfo> findTempAudit(@ApiParam("公司id")@RequestParam(value = "companyId") String companyId){
        PcAuditInfo auditInfo = companySubmitService.findTempAudit(companyId);
        return sendJsonData(success, "操作成功", auditInfo);
    }

    /**
     * 查询审批不通过的原因
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/findAuditCase", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--运营后台--公司管理--设计/装饰公司--列表--状态--不通过原因--李阳")
    public MyRespBundle<PcAuditInfo> findAuditCase(@ApiParam("公司id")@RequestParam(value = "companyId") String companyId){
        PcAuditInfo auditInfo = companySubmitService.findAuditCase(companyId);
        return sendJsonData(success, "操作成功", auditInfo);
    }

    /**
     * 审批详情查询
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/findAuditStatus", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--装饰/设计公司管理中心--入驻2--签约完成--审批详情查询--李阳")
    public MyRespBundle<AuditInfoVO> findAuditStatus(@ApiParam("公司id")@RequestParam(value = "companyId") String companyId){
        AuditInfoVO auditInfoVO = companySubmitService.findAuditStatus(companyId);
        return sendJsonData(success, "操作成功", auditInfoVO);
    }

    /**
     * 入驻公司资质变更回显
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/findCompanyInfo", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--装饰/设计公司管理中心--公司变更页面--资质变更回显--李阳")
    public MyRespBundle<CompanySubmitVo> findCompanyInfo(@ApiParam("公司id")@RequestParam(value = "companyId") String companyId){
        CompanySubmitVo companySubmitVo = companySubmitService.findCompanyInfo(companyId);
        return sendJsonData(success, "操作成功", companySubmitVo);
    }

    /**
     * 入驻公司资质变更更新
     * @param companyTemporaryVo
     * @return
     */
    @RequestMapping(value = "/changeCompanyInfo", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端--装饰/设计公司管理中心--公司变更页面--资质变更提交--李阳")
    public MyRespBundle<String> changeCompanyInfo(@ApiParam("变更的公司资质信息")CompanyTemporaryVo companyTemporaryVo){
        boolean flag = companySubmitService.changeCompanyInfo(companyTemporaryVo);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 入驻公司资质变更审批回显
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/findCompanyTemporaryInfo", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--运营后台--公司管理--资质变更--办理--公司详情--公司资质变更审批回显。注：申请列表的申请事项如果是资质变更，则使用此接口--李阳")
    public MyRespBundle<PcAuditTemporaryInfo> findCompanyTemporaryInfo(@ApiParam("公司id")@RequestParam(value = "companyId") String companyId){
        AuditTemporaryInfoVO auditTemporaryInfoVO = companySubmitService.findCompanyTemporaryInfo(companyId);
        return sendJsonData(success, "操作成功", auditTemporaryInfoVO);
    }


    /**
     * 入驻公司资质变更审批
     * @param auditInfoVO
     * @return
     */
    @ApiOperation(value = "前端--运营后台----公司详情--公司变更审批--李阳", notes = "运营审核")
    @PostMapping("/auditChangeCompany")
    @MyRespBody
    public MyRespBundle<String> auditChangeCompany(@ApiParam("审批参数")PcAuditInfoVO auditInfoVO){
        String msg = companySubmitService.auditChangeCompany(auditInfoVO);
        return sendJsonData(ResultMessage.SUCCESS, msg);

    }


    @RequestMapping(value = "/upCompanyInfo", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端--装饰/设计公司管理中心--完善公司资质--提交--李阳")
    public MyRespBundle<String> upCompanyInfo(@ApiParam(value = "公司资质信息", required = true)CompanySubmitVo companySubmitVo){
        boolean flag = companySubmitService.upCompanyInfo(companySubmitVo);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS, "操作成功");
        }
        return sendJsonData(ResultMessage.FAIL, "操作失败");
    }

    /**
     * 公司资质查询list
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--运营后台----公司管理--装饰/设计公司--列表--李阳")
    public MyRespBundle<PageInfo<CompanyListVo>> list(@ApiParam("条件查询参数")CompanyListSEO companyListSEO){
        PageInfo<CompanyListVo> pageInfo = companySubmitService.list(companyListSEO);
        return sendJsonData(success, "操作成功", pageInfo);
    }

    /**
     * 经销商公司资质查询list
     */
    @RequestMapping(value = "/dealerList", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--运营后台----公司管理--经销商公司--列表--李阳")
    public MyRespBundle<PageInfo<CompanyListVo>> dealerList(@ApiParam("条件查询参数")CompanyListSEO companyListSEO){
        PageInfo<CompanyListVo> pageInfo = companySubmitService.dealerList(companyListSEO);
        return sendJsonData(success, "操作成功", pageInfo);
    }

    /**
     * 导出
     */
    @RequestMapping(value = "/downLoad", method = RequestMethod.GET)
    @ExceptionHandler(value=Exception.class)
    @ApiOperation(value="前端--运营后台----公司管理--装饰/设计公司--导出excel--李阳")
    public void downLoad(HttpServletResponse response, @ApiParam("条件查询参数")CompanyListSEO companyListSEO){
        companySubmitService.downLoad(response, companyListSEO);
    }

    /**
     * 查看合同
     * @author lqd
     * @return Message
     */
    @ApiOperation(value = "前端--运营后台----公司管理--装饰/设计公司--查看合同----李阳", notes = "查看合同",consumes = "application/text")
    @PostMapping("/getContractDetailInfo")
    public MyRespBundle<Map<String,Object>> getContractDetailInfo(@ApiParam("合同编号")@RequestParam(required = true) String contractNumber,
    		@ApiParam("公司编号")@RequestParam(required = true) String companyId){
        Map<String, Object> resMap = contractService.getContractDetailInfo(contractNumber, companyId);
        return sendJsonData(ResultMessage.SUCCESS,resMap);
    }
    /**
     * 查询字典
     * @author lqd
     *
     * @return Message
     * 
     */
    @ApiOperation(value = "前端--运营后台----公司管理--装饰/设计公司--查看合同--合同条款字典--吕启栋", notes = "合同条款设置(设置合同条款需要查询字典接口)")
    @PostMapping("/queryContractDic")
    public MyRespBundle<String> queryContractDic(@RequestParam String type){
        Map<String, String> resMap = contractTemplateService.queryContractDic(type);
        return sendJsonData(ResultMessage.SUCCESS,resMap);
    }
    
    
    
    /**
     * 
     * 合同条款设置
     * @author lqd
     * @return Message
     */
    @ApiOperation(value = "前端--运营后台----公司管理--装饰/设计公司--查看合同--合同条款设置--吕启栋", notes = "合同条款设置(设置合同条款需要查询字典接口,paymentNumber为支付方案code)",consumes = "application/json")
    @PostMapping("/settingContractClause/{contractNumber}/{companyId}")
    @MyRespBody
    public MyRespBundle<String> settingContractClause(@PathVariable("contractNumber") String contractNumber,
    		@PathVariable("companyId") String companyId,
    		@ApiParam("合同条款key和value值")@RequestBody ContractClauseVO contractClausevo){

        boolean flag = contractService.insertContractClause(contractNumber, companyId, contractClausevo);
    	 
        return sendJsonData(ResultMessage.SUCCESS,flag);
    }


    
    /**
     * 公司详情
     * @author lqd
     * @return Message
     */
    
    @ApiOperation(value = "前端--运营后台----公司管理--装饰/设计公司--公司详情--李阳", notes = "公司详情")
    @PostMapping("/companyDetails")
    @MyRespBody
    public MyRespBundle<CompanyDetailsVO> companyDetails(@ApiParam("合同编号")@RequestParam(required = false) String contractNumber,
    		@ApiParam("公司编号")@RequestParam String companyId,
            @ApiParam("审核类型0入驻 1合同 2变更 3续签4结算比例 5结算规则")@RequestParam String auditType,
            @ApiParam("除入驻外需要传申请时间")@RequestParam(required = false) String applyDate){
        CompanyDetailsVO jbj =  companySubmitService.companyDetails(contractNumber, companyId, auditType, applyDate);
        return sendJsonData(ResultMessage.SUCCESS,jbj);
    }

    
    /**
     * 运营人员审批
     * @author lqd
     * @return Message
     */
    @ApiOperation(value = "前端--运营后台----公司管理---查看合同--资质审批--李阳", notes = "运营审核")
    @PostMapping("/auditCompany")
    @MyRespBody
    //@MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_CONTRACT,desc = "合同审批")
    public MyRespBundle<Map<String,Object>> auditCompany(@ApiParam("审批参数")PcAuditInfoVO pcAuditInfo){

        Map<String,Object> result = companySubmitService.auditContract(pcAuditInfo);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 运营人员审批经销商
     * @author lqd
     * @return Message
     */
    @ApiOperation(value = "前端--运营后台----公司管理--公司详情--资质审批经销商--李阳", notes = "运营审核")
    @PostMapping("/auditDealerCompany")
    @MyRespBody
    //@MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_CONTRACT,desc = "合同审批")
    public MyRespBundle<Map<String,Object>> auditDealerCompany(@ApiParam("审批参数")PcAuditInfoVO pcAuditInfo){

        Map<String,Object> result = companySubmitService.auditDealerCompany(pcAuditInfo);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 签约完成
     * @author lqd
     * @return Message
     *
     */
    @ApiOperation(value = "前端--运营后台----公司管理--装饰/设计公司--查看合同--签约完成--李阳")
    @PostMapping("/signSuccess")
    public MyRespBundle<String> signSuccess(@ApiParam("公司id")@RequestParam String companyId,
                                            @ApiParam("合同编号")@RequestParam String contractNumber){
        boolean flag  = companySubmitService.signSuccess(companyId, contractNumber);
        if(flag){
            return sendJsonData(ResultMessage.SUCCESS,"操作成功");
        }
        return sendJsonData(ResultMessage.FAIL,"操作失败");
    }


    /**
     * 入驻公司判断资质是否可以编辑
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/isEdit", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--装饰/设计公司管理中心--公司信息--公司管理:变更--审核/未通过/通过--是否可以编辑--李阳")
    public MyRespBundle<CompanyTemporaryVo> isEdit(@ApiParam("公司id") @RequestParam String companyId){
        boolean flag = companySubmitService.isEdit(companyId);
        return sendJsonData(ResultMessage.SUCCESS, flag);
    }

    /**
     * 入驻公司资质变更查询审批状态
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/findTempAuditStatus", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="前端--装饰/设计公司管理中心--公司信息--公司管理:变更--审核/未通过/通过--查询审批状态--李阳")
    public MyRespBundle<AuditInfoVO> findTempAuditStatus(@ApiParam("公司id")@RequestParam(value = "companyId") String companyId){
        AuditInfoVO auditInfoVO = companySubmitService.findTempAuditStatus(companyId);
        return sendJsonData(success, "操作成功", auditInfoVO);
    }

    /**
     * 下架，冻结，删除
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/updateByParam", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端--运营后台--下架，冻结，删除--李阳")
    public MyRespBundle<String> updateByParam(
            @ApiParam("公司id")@RequestParam(value = "companyId") String companyId,
            @ApiParam("0正常，1冻结，2下架（冻结高于下架）")@RequestParam(required = false) String platformType,
            @ApiParam("是否删除 1是 2否")@RequestParam(required = false) String isDelete){
        Integer msg = companySubmitService.updateByParam(companyId,platformType,isDelete);
        if(msg > 0){
            return sendJsonData(success, "操作成功", msg);
        }
        return sendJsonData(fail, "操作失败", msg);
    }

    /**
     * 运营平台资质变更
     * @param companySubmitVo
     * @return
     */
    @RequestMapping(value = "/updateCompanyInfo", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="前端--运营后台--公司资质变更(公司id必填项）--李阳")
    public MyRespBundle<String> updateCompanyInfo(
            @ApiParam("公司id")CompanySubmitVo companySubmitVo){
        Map<String, Object> map= companySubmitService.updateCompanyInfo(companySubmitVo);
        return sendJsonData(success, "操作成功", map);
    }

}
