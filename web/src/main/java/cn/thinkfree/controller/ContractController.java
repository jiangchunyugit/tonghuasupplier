package cn.thinkfree.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.annotation.MySysLog;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.core.constants.SysLogAction;
import cn.thinkfree.core.constants.SysLogModule;
import cn.thinkfree.database.vo.ContractDetails;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractVo;
import cn.thinkfree.service.contract.ContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * 合同管理
 * @date 2018-09-20
 * @author lvqidong
 */
@Api(value = "合同管理",description = "合同管理信息描述")
@RestController
@RequestMapping(value = "/contract")
public class ContractController extends AbsBaseController{
	
	@Autowired
	ContractService contractService;
	
	/**
     * 财务审核入驻合同列表
     * @author lqd
     * @param ContractSEO
     * @return pageList
     */
    @ApiOperation(value = "财务合同管理列表", notes = "根据一定条件获取分页合同记录")
    @PostMapping("/list")
    @MyRespBody
    public MyRespBundle<PageInfo<ContractVo>> list(@ApiParam("项目搜索条件")   ContractSEO contractSEO){

        PageInfo<ContractVo> pageInfo =contractService.pageContractBySEO(contractSEO);
        
        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }
    
    /**
     * 合同导出
     * @author lqd
     * @param ContractSEO
     * @return pageList
     */
    @ApiOperation(value = "财务合同数据导出", notes = "根据一定条件获取分页数据导出")
    @PostMapping("/exportList")
    @MyRespBody
    public void exportList(HttpServletResponse response,
    		@ApiParam("项目搜索条件")   ContractSEO contractSEO){
        contractService.exportList(contractSEO, response);
    }
    
    
    /**
     * 合同审批
     * @author lqd
     * @return Message
     */
    @ApiOperation(value = "财务合同审批", notes = "财务审核")
    @PostMapping("/audit")
    @MyRespBody
    //@MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_CONTRACT,desc = "合同审批")
    public MyRespBundle<String> audit(@ApiParam("合同编号")@RequestParam String contractNumber,
    		@ApiParam("公司编号")@RequestParam String companyId,
    		@ApiParam("审批状态 0 代表通过 1 拒绝 ")@RequestParam String auditStatus,
    		@ApiParam("审核成功或者失败的原因 ")@RequestParam String auditCase){
        
    	 Map<String,String>  resMap = contractService.auditContract(contractNumber, companyId,auditStatus,auditCase);
    	 
    	 String code = String.valueOf(resMap.get("code"));
    	 
    	 String mes = String.valueOf(resMap.get("msg"));
    	 
    	 if(code.equals("1")){//失败的情况
    		 return sendFailMessage(mes);
    	 }else{//成功的情况
    		 return sendSuccessMessage(mes);
    	 }
       
    }
    
    
    /**
     * @{确认保证金}
     * @author lvqidong
     * @return Message
     */
    @ApiOperation(value = "财务确认保证金", notes = "财务保证金收到后确认")
    @PostMapping("/ackEarnestMoney")
    @MyRespBody
    @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_CONTRACT,desc = "确认保证金信息")
    public MyRespBundle<String> ackEarnestMoney(@ApiParam("合同编号")@RequestParam String contractNumber,
    		@ApiParam("公司编号")@RequestParam String companyId){
    	Map<String,String>  resMap = contractService.ackEarnestMoney(contractNumber, companyId);
    	String code = String.valueOf(resMap.get("code"));
   	 
	   	 String mes = String.valueOf(resMap.get("msg"));
	   	 
	   	 if(code.equals("1")){//失败的情况
	   		 return sendFailMessage(mes);
	   	 }else{//成功的情况
	   		 return sendSuccessMessage(mes);
	   	 }
    }
    
    /**
     * 
     * 合同详情
     * @author lvqidong
     * @return Message
     */
    @ApiOperation(value = "合同详情", notes = "合同详情")
    @PostMapping("/contractDetails")
    @MyRespBody
    public MyRespBundle<String> contractDetails(@ApiParam("合同编号")@RequestParam String contractNumber,
    		@ApiParam("公司编号")@RequestParam String companyId){
    	ContractDetails jbj =  contractService.contractDetails(contractNumber, companyId);
        return sendJsonData(ResultMessage.SUCCESS,jbj);
    }

    
    
    /**
     * 合同下载
     * @author lvqidong
     */
    
    @ApiOperation(value = "合同下载", notes = "根据合同编号和公司的编号下载合同", consumes = "application/pdf")
    @PostMapping(path = "downloadContract")
    public String downloadContract(@ApiParam("合同编号")@RequestParam(required=true) String contractNumber, HttpServletResponse response) {    
    	//根据合同编号查询下载地址
        String downloadUrl = contractService.selectContractBycontractNumber(contractNumber);
        //下载文件
        String fileName = "aim_test.txt";// 设置文件名，根据业务需要替换成要下载的文件名
        if (downloadUrl != null) {
            //设置文件路径
            String realPath = "D://aim//";
            File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    printInfoMes("下载成功");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }else{
        	return "下载失败，合同编号不存在";
        }
        return null;
    
     }

    
    
    
    /**
     * 
     * 施工/设计合同生成
     * @author lvqidong
     * @return Message
     */
    @ApiOperation(value = "施工/设计合同生成", notes = "施工/设计合同生成")
    @PostMapping("/createOrderContract")
    @MyRespBody
    public MyRespBundle<String> createOrderContract(@ApiParam("公司编号")@RequestParam String companyId,
    		@ApiParam("订单编号")@RequestParam String orderNumber,@ApiParam("订单类型（0设计 1施工）")@RequestParam String type){
    	boolean  flag = contractService.createOrderContract( companyId, orderNumber, type);
    	   
        return sendJsonData(ResultMessage.SUCCESS,flag);
    }
    
    
    /**
     * 
     * 获取施工或订单合同pdf
     * @author lvqidong
     * @return Message
     */
    @ApiOperation(value = "获取施工或订单合同pdf", notes = "根据订单编号获取施工或订单合同pdf")
    @PostMapping("/getOrderContract")
    @MyRespBody
    public MyRespBundle<String> getOrderContract(@ApiParam("订单编号")@RequestParam String orderNumber){
    	
    	String  url = contractService.getPdfUrlByOrderNumber(orderNumber);
    	   
        return sendJsonData(ResultMessage.SUCCESS,url);
    }
    
    
    
    
}
