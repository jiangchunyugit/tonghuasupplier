package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.vo.CompanySubmitVo;
import cn.thinkfree.service.companysubmit.CompanySubmitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    //公司资质查询list

    //补全合同

    //公司详情

    //查看合同

    //合同条款设置

    //运营人员审批
    /**
     * 运营人员审批
     * @author lqd
     * @return Message
     */
    @ApiOperation(value = "资质审批", notes = "运营审核")
    @PostMapping("/audit")
    @MyRespBody
    //@MySysLog(action = SysLogAction.DEL,module = SysLogModule.PC_CONTRACT,desc = "合同审批")
    public MyRespBundle<String> audit(
    		@ApiParam("公司编号")@RequestParam String companyId,
    		@ApiParam("审批状态 0 代表通过 1 拒绝 ")@RequestParam String auditStatus,
    		@ApiParam("审核成功或者失败的原因 ")@RequestParam String auditCase){
        
    	 Map<String,String>  resMap = companySubmitService.auditContract(companyId,auditStatus,auditCase);
    	 
    	 String code = String.valueOf(resMap.get("code"));
    	 
    	 String mes = String.valueOf(resMap.get("msg"));
    	 
    	 if(code.equals("1")){//失败的情况
    		 return sendFailMessage(mes);
    	 }else{//成功的情况
    		 return sendSuccessMessage(mes);
    	 }
       
    }
}
