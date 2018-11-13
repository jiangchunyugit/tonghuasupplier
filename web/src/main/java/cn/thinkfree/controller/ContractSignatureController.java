package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.ContractInfo;
import cn.thinkfree.database.model.ContractSignature;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractSignatureVO;
import cn.thinkfree.database.vo.ContractVo;
import cn.thinkfree.database.vo.contract.ContractDetailsVo;
import cn.thinkfree.service.contract.ContractService;
import cn.thinkfree.service.contractsignature.ContractSignatureService;
import cn.thinkfree.service.contracttemplate.ContractTemplateService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * 
 * 合同管理
 * @date 2018-09-20
 * @author lvqidong
 */
@Api(value = "前端用---合同签名管理---蒋春雨",description = "前端用---合同签名查询上传---蒋春雨")
@RestController
@RequestMapping(value = "/contractSignature")
public class ContractSignatureController extends AbsBaseController{
	
	@Autowired
	ContractSignatureService contractSignatureService;

    /**
     *
     * @param contractNumber
     * @return
     */
    @ApiOperation(value = "查询合同签名", notes = "查询合同签名")
    @GetMapping("/contraSignature")
    @MyRespBody
    //@MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_CONTRACT,desc = "合同审批")
    public MyRespBundle<List<ContractSignature>> contraSignature(@ApiParam("合同编号")@RequestParam String contractNumber){

    	 return sendJsonData(ResultMessage.SUCCESS,contractSignatureService.getContraSignature(contractNumber));
    }


    /**
     * 合同签名上传
     * @param contractSignatureVO
     * @return
     */
    @ApiOperation(value = "合同签名上传", notes = "合同签名上传")
    @PostMapping("/saveContractSignature")
    @MyRespBody
   // @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_CONTRACT,desc = "确认保证金信息")
    public MyRespBundle<String> saveContractSignature(@ApiParam("合同签名信息") ContractSignatureVO contractSignatureVO){
    	
    	boolean  flag = contractSignatureService.saveContractSignature(contractSignatureVO);
    	
    	return sendJsonData(ResultMessage.SUCCESS,flag);
    }
}
