package cn.thinkfree.controller;

import java.util.Map;

import cn.thinkfree.database.model.PcAuditTemporaryInfo;
import cn.thinkfree.database.vo.*;
import com.github.pagehelper.PageInfo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.service.companysubmit.CompanySubmitService;
import cn.thinkfree.service.contract.ContractService;
import cn.thinkfree.service.contracttemplate.ContractTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletResponse;

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
     * 入驻公司资质变更回显
     * @param companyId
     * @return
     */
    @RequestMapping(value = "/findCompanyInfo", method = RequestMethod.GET)
    @MyRespBody
    @ApiOperation(value="入驻公司资质变更回显")
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
    @ApiOperation(value="入驻公司资质变更更新")
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
    @ApiOperation(value="入驻公司资质变更审批回显。注：申请列表的申请事项如果是资质变更，则使用此接口")
    public MyRespBundle<PcAuditTemporaryInfo> findCompanyTemporaryInfo(@ApiParam("公司id")@RequestParam(value = "companyId") String companyId){
        PcAuditTemporaryInfo pcAuditTemporaryInfo = companySubmitService.findCompanyTemporaryInfo(companyId);
        return sendJsonData(success, "操作成功", pcAuditTemporaryInfo);
    }


    /**
     * 入驻公司资质变更审批
     * @param companyId
     * @param auditStatus
     * @param auditCase
     * @return
     */
    @ApiOperation(value = "入驻公司资质变更审批。注：申请列表的申请事项如果是资质变更，则使用此接口", notes = "运营审核")
    @PostMapping("/auditChangeCompany")
    @MyRespBody
    public MyRespBundle<String> auditChangeCompany(
            @ApiParam("公司编号")@RequestParam String companyId,
            @ApiParam("审批状态 0 代表通过 1 拒绝 ")@RequestParam String auditStatus,
            @ApiParam("审核成功或者失败的原因 ")@RequestParam String auditCase){
        String msg = companySubmitService.auditChangeCompany(companyId,auditStatus,auditCase);
        return sendJsonData(ResultMessage.SUCCESS, msg);

    }


    @RequestMapping(value = "/upCompanyInfo", method = RequestMethod.POST)
    @MyRespBody
    @ApiOperation(value="审核资质上传")
    public MyRespBundle<String> upCompanyInfo(@ApiParam("公司资质信息")CompanySubmitVo companySubmitVo){
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
    @ApiOperation(value="公司申请信息审批列表")
    public MyRespBundle<PageInfo<CompanyListVo>> list(@ApiParam("条件查询参数")CompanyListSEO companyListSEO){
        PageInfo<CompanyListVo> pageInfo = companySubmitService.list(companyListSEO);
        return sendJsonData(success, "操作成功", pageInfo);
    }

    /**
     * 导出
     */
    @RequestMapping(value = "/downLoad", method = RequestMethod.GET)
    @ExceptionHandler(value=Exception.class)
    @ApiOperation(value="公司申请信息审批列表导出")
    public void downLoad(HttpServletResponse response, @ApiParam("条件查询参数")CompanyListSEO companyListSEO){
        companySubmitService.downLoad(response, companyListSEO);
    }

    //补全合同
    
    /**
     * 查看合同
     * @author lqd
     * @return Message
     * 
     */
    @ApiOperation(value = "查看合同", notes = "查看合同",consumes = "application/text")
    @PostMapping("/getContractDetailInfo")
    public MyRespBundle<Map<String,Object>> getContractDetailInfo(@ApiParam("合同编号")@RequestParam(required = true) String contractNumber,
    		@ApiParam("公司编号")@RequestParam(required = true) String companyId){
    	Map<String,Object>  resMap  =  contractService.getContractDetailInfo(contractNumber, companyId);
        return sendJsonData(ResultMessage.SUCCESS,resMap);
    }
    /**
     * 查询字典
     * @author lqd
     * @return Message
     * 
     */
    @ApiOperation(value = "查看合同条款字典", notes = "合同条款设置(设置合同条款需要查询字典接口)",consumes = "application/text")
    @PostMapping("/queryContractDic/")
    public MyRespBundle<String> queryContractDic(@RequestParam String type){
    	 Map<String,String>  resMap  =  null;
                 //contractTemplateService.queryContractDic(type);
        return sendJsonData(ResultMessage.SUCCESS,resMap);
    }
    
    
    
    /**
     * 
     * 合同条款设置
     * @author lqd
     * @return Message
     */
    @ApiOperation(value = "合同条款设置", notes = "合同条款设置(设置合同条款需要查询字典接口)",consumes = "application/json")
    @PostMapping("/settingContractClause/{contractNumber}/{companyId}")
    @MyRespBody
    public MyRespBundle<String> settingContractClause(@PathVariable("contractNumber") String contractNumber,
    		@PathVariable("companyId") String companyId,
    		@ApiParam("合同条款key和value值")@RequestBody (required = true) Map<String,String> paramMap){
    	 Map<String,String>  resMap  =  contractService.insertContractClause(contractNumber, companyId, paramMap);
        return sendJsonData(ResultMessage.SUCCESS,resMap);
    }


    
    
    /**
     * 公司详情
     * @author lqd
     * @return Message
     */
    
    @ApiOperation(value = "公司详情", notes = "公司详情")
    @PostMapping("/companyDetails")
    @MyRespBody
    public MyRespBundle<String> companyDetails(@ApiParam("合同编号")@RequestParam String contractNumber,
    		@ApiParam("公司编号")@RequestParam String companyId){
    	ContractDetails jbj =  contractService.contractDetails(contractNumber, companyId);
        return sendJsonData(ResultMessage.SUCCESS,jbj);
    }

    
    /**
     * 运营人员审批
     * @author lqd
     * @return Message
     */
    @ApiOperation(value = "资质审批", notes = "运营审核")
    @PostMapping("/auditCompany")
    @MyRespBody
    //@MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_CONTRACT,desc = "合同审批")
    public MyRespBundle<String> auditCompany(
    		@ApiParam("公司编号")@RequestParam() String companyId,
    		@ApiParam("审批状态 0 代表通过 1 拒绝 ")@RequestParam String auditStatus,
    		@ApiParam("审核成功或者失败的原因 ")@RequestParam String auditCase,
            @ApiParam("审核级别0运营审核1财务审核 ")@RequestParam String auditLevel){
        
    	 Map<String,String>  resMap = companySubmitService.auditContract(companyId,auditStatus,auditCase,auditLevel);
    	 
    	 String code = String.valueOf(resMap.get("code"));
    	 
    	 String mes = String.valueOf(resMap.get("msg"));
    	 
    	 if(code.equals("1")){//失败的情况
    		 return sendFailMessage(mes);
    	 }else{//成功的情况
    		 return sendSuccessMessage(mes);
    	 }
    }
}
