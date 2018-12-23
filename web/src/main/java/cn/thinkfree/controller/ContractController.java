package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.ContractInfo;
import cn.thinkfree.database.vo.ContractSEO;
import cn.thinkfree.database.vo.ContractVo;
import cn.thinkfree.database.vo.contract.ContractDetailsVo;
import cn.thinkfree.database.vo.contract.ContractParam;
import cn.thinkfree.service.contract.ContractService;
import cn.thinkfree.service.contracttemplate.ContractTemplateService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
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
@Api(value = "合同管理",description = "合同管理信息描述")
@RestController
@RequestMapping(value = "/contract")
public class ContractController extends AbsBaseController{
	
	@Autowired
	ContractService contractService;
	
	@Autowired
	ContractTemplateService contractTemplateService;
	
	/**
     * 财务审核入驻合同列表
     * @author lqd
     * @param ContractSEO
     * @return pageList
     */
    @ApiOperation(value = "前端--运营后台--财务合同管理列表--吕启栋", notes = "根据一定条件获取分页合同记录")
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
    @ApiOperation(value = "前端--运营后台--财务合同数据导出--吕启栋", notes = "根据一定条件获取分页数据导出")
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
    @ApiOperation(value = "前端--运营后台--财务合同审批--吕启栋", notes = "财务审核")
    @PostMapping("/audit")
    @MyRespBody
    //@MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_CONTRACT,desc = "合同审批")
    public MyRespBundle<String> audit(@ApiParam("合同编号")@RequestParam String contractNumber,
    		@ApiParam("公司编号")@RequestParam String companyId,
    		@ApiParam("审批状态 1代表通过 0 拒绝 ")@RequestParam String auditStatus,
    		@ApiParam("审核成功或者失败的原因 ")@RequestParam String auditCase){
        
    	 boolean flag = contractService.auditContract(contractNumber, companyId,auditStatus,auditCase);
    		 
    	 return sendJsonData(ResultMessage.SUCCESS,flag);
    }
    
    
    /**
     * @{确认保证金}
     * @author lvqidong
     * @return Message
     */
    @ApiOperation(value = "前端--运营后台--财务确认保证金--吕启栋", notes = "财务保证金收到后确认保证金")
    @PostMapping("/ackEarnestMoney")
    @MyRespBody
   // @MySysLog(action = SysLogAction.CHANGE_STATE,module = SysLogModule.PC_CONTRACT,desc = "确认保证金信息")
    public MyRespBundle<String> ackEarnestMoney(@ApiParam("合同编号")@RequestParam String contractNumber,
    		@ApiParam("公司编号")@RequestParam String companyId){
    	
    	Map<String,String>  map = contractService.ackEarnestMoney(contractNumber, companyId);
    	
    	if(String.valueOf(map.get("falg")).equals("true")){
    		
    		return sendJsonData(ResultMessage.SUCCESS,true);
    		
    	}else{
    		
    		return sendJsonData(ResultMessage.ERROR,String.valueOf(map.get("msg")));
    	}
    	
    	
    }
    
    /**
     * 
     * 合同详情
     * @author lvqidong
     * @return Message
     */
    @ApiOperation(value = "前端--运营后台--合同详情--吕启栋", notes = "合同详情")
    @GetMapping("/contractDetails")
    @MyRespBody
    public MyRespBundle<ContractDetailsVo> contractDetails(@ApiParam("合同编号")@RequestParam String contractNumber,
    		@ApiParam("公司编号")@RequestParam String companyId){
    	ContractDetailsVo  jbj =  contractService.contractDetails(contractNumber, companyId);
        return sendJsonData(ResultMessage.SUCCESS,jbj);
    }

    
    
    /**
     * 合同下载
     * @author lvqidong
     */
    
    @ApiOperation(value = "前端--运营后台--合同下载--吕启栋", notes = "根据合同编号和公司的编号下载合同", consumes = "application/pdf")
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
     * 施工合同录入
     */
    @ApiOperation(value = "B端--施工合同输入--吕启栋", notes = "设计合同录入",consumes = "application/json")
    @PostMapping("/insertRoadWorkOrderContract/{orderNumber}/{companyId}")
    @MyRespBody
    public MyRespBundle<String> insertRoadWorkOrderContract(@PathVariable("orderNumber") String orderNumber,
    		@PathVariable("companyId") String companyId,
    		@ApiParam("合同条款key和value值")@RequestBody  Map<String, String> paramMap ){
        if(paramMap != null &&  paramMap.size() > 0){

            boolean flag  = contractService.insertRoadWorkOrderContract(orderNumber, companyId, paramMap);

            return sendJsonData(ResultMessage.SUCCESS,flag);

        }else{

            return sendJsonData(ResultMessage.ERROR,"开始时间和结束时间必填");
        }


    }
    
    /**
     * 
     * 合同字典
     * 
     */
    @ApiOperation(value = "B端--施工合同输入--吕启栋", notes = "合同字典  0 入住设计公司 1 入住装饰公司 2 设计公司C-B 3 装饰公司C-B")
    @PostMapping("/ContractDic")
    @MyRespBody
    public MyRespBundle<Map<String,String>> ContractDic(
    		@RequestParam String type){

    	Map<String,String> map = contractTemplateService.queryContractDic(type);

    	return sendJsonData(ResultMessage.SUCCESS,map);
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
    
    
    /**
     * 入住协议
     * @param companyId
     * @author lvqidong
     */
    @ApiOperation(value = "前端--B端--入住协议列表--吕启栋", notes = "入住协议 ")
    @PostMapping("/enterAgreementContract")
    @MyRespBody
    public MyRespBundle<List<ContractInfo>> enterAgreementContract(@ApiParam("公司编号")@RequestParam String companyId){

    	List<ContractInfo> resList =  contractService.getEnterContractBycompanyId(companyId);

       return sendJsonData(ResultMessage.SUCCESS,resList);
    }
    
    
    /**
     * 设计或者施工合同审批
     * @param companyId
     * @author lvqidong
     */
    
    @ApiOperation(value = "设计或者施工合同审批", notes = "设计或者施工合同审批")
    @PostMapping("/examineOrderContract")
    @MyRespBody
    public MyRespBundle<String> examineOrderContract(@ApiParam("订单编号")@RequestParam String orderNumber,@ApiParam("审核状态 0 不通过1通过")@RequestParam String status,
    		@ApiParam("不通过原因")@RequestParam(required = false) String cause){
    	
    	boolean  flag  = contractService.examineOrderContract(orderNumber, status, cause);
    	   
        return sendJsonData(ResultMessage.SUCCESS,flag);
    }


    
}
