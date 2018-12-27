package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.CompanyInfo;
import cn.thinkfree.database.model.PcAuditInfo;
import cn.thinkfree.database.model.SystemMessage;
import cn.thinkfree.database.vo.DesignContractToVo;
import cn.thinkfree.database.vo.EnterCompanyOrganizationVO;
import cn.thinkfree.database.vo.ProjectQuotationVO;
import cn.thinkfree.database.vo.SelectItem;
import cn.thinkfree.database.vo.contract.ContractParam;
import cn.thinkfree.service.branchcompany.BranchCompanyService;
import cn.thinkfree.service.cache.RedisService;
import cn.thinkfree.service.company.CompanyInfoService;
import cn.thinkfree.service.contract.ContractService;
import cn.thinkfree.service.platform.designer.DesignDispatchService;
import cn.thinkfree.service.project.ProjectService;
import cn.thinkfree.service.sysMsg.SystemMessageService;
import cn.thinkfree.service.user.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 不需要token的部分
 */
@ApiOperation("手机端或Pc端 不需要token的接口")
@RestController
@RequestMapping("/open/v1")
public class OpenApiController extends AbsBaseController {

    @Autowired
    ProjectService projectService;

    @Autowired
    SystemMessageService systemMessageService;

    @Autowired
    CompanyInfoService companyInfoService;

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    BranchCompanyService branchCompanyService;

    @Autowired
    ContractService contractService;

    @Autowired
    DesignDispatchService designDispatchService;

    @ApiOperation(value = "APP模糊查询公司列表",notes = "默认30条数据")
    @PostMapping("/companyInfo")
    @MyRespBody
    public MyRespBundle<List<SelectItem>> companyInfo(String name,String type){

        List<SelectItem> items = companyInfoService.listCompanyByLikeName(name,type);

        return sendJsonData(ResultMessage.SUCCESS,items);
    }


    /**
     * 忘记密码
     * @param email
     * @return
     */
    @ApiOperation(value = "忘记密码1",notes = "忘记密码第一步,验证邮箱")
    @PostMapping("/forget")
    @MyRespBody
    public MyRespBundle<String> forgetPwd(String email){
        String mes = userService.forgetPwd(email);
        return sendSuccessMessage(mes);
    }


    /**
     * 忘记密码 重置密码
     * @param email
     * @param pwd
     * @param code
     * @return
     */
    @ApiOperation(value = "重置密码",notes = "忘记密码第二部,重置密码")
    @MyRespBody
    @PostMapping("/forget/reset")
    public MyRespBundle<String> resetPwd(String email,String pwd,String code){
        if(StringUtils.isBlank(email) || StringUtils.isBlank(pwd)|| StringUtils.isBlank(code)){
            return sendFailMessage("参数不对");
        }
        String mes = userService.updatePassWordOnForget(email,pwd,code);
        return sendSuccessMessage(mes);
    }


    /**
     * 报价单
     * @param projectNo
     * @return
     */
    @GetMapping("/quotation")
    @MyRespBody
    public MyRespBundle<ProjectQuotationVO> quotation(@RequestParam String projectNo){
        ProjectQuotationVO projectQuotationVO = projectService.selectProjectQuotationVoByProjectNo(projectNo);
        return sendJsonData(ResultMessage.SUCCESS,projectQuotationVO);
    }

    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @MyRespBody
    public MyRespBundle<PageInfo<SystemMessage>> findById(@RequestParam(value = "id")Integer id){
        SystemMessage sysMsg = systemMessageService.selectByPrimaryKey(id);
        if(null == sysMsg){
            return sendJsonData(ResultMessage.FAIL, "失败");
        }
        return sendJsonData(ResultMessage.SUCCESS, sysMsg);
    }

    /**
     * 通过公司编号获取运营平台组织架构
     * @param companyId
     * @return
     */
    @PostMapping("/getCompanyOrganizationByCompanyId")
    @ApiOperation(value = "for徐洋---通过公司编号获取运营平台组织架构---蒋春雨",notes = "通过公司编号获取运营平台组织架构")
    @MyRespBody
    public MyRespBundle<EnterCompanyOrganizationVO> getCompanyOrganizationByCompanyId(@ApiParam("入驻公司id")@RequestParam String companyId){
        EnterCompanyOrganizationVO enterCompanyOrganizationVO = branchCompanyService.getCompanyOrganizationByCompanyId(companyId);
        return sendJsonData(ResultMessage.SUCCESS,enterCompanyOrganizationVO);
    }

    /**
     * 通过用户id获取运营平台组织架构
     * @param userId
     * @return
     */
    @PostMapping("/getCompanyInfoByUserId")
    @ApiOperation(value = "for徐洋---通过用户id获取运营平台组织架构---蒋春雨",notes = "通过用户id获取运营平台组织架构")
    @MyRespBody
    public MyRespBundle<List<CompanyInfo>> getCompanyInfoByUserId(@ApiParam("userId")@RequestParam String userId){
        List<CompanyInfo> companyInfos = branchCompanyService.getCompanyOrganizationByUser(userId);
        return sendJsonData(ResultMessage.SUCCESS,companyInfos);
    }



    /**
     * 设计合同录入
     *
     */

    @ApiOperation(value = "B端--设计师输入合同--吕启栋", notes = "设计合同录入 for 江宁哥 新增 （合同总金额—c21） " +
            "（设计合同开始时间—c22）（设计合同开始时间—c22）（设计合同结束时间—c23）" +
            "",consumes = "application/json")
    @PostMapping("/insertDesignOrderContract/{orderNumber}")
    @MyRespBody
    public MyRespBundle<Map<String,Object> > insertDesignOrderContract(@PathVariable("orderNumber") String orderNumber,
                                                          @ApiParam("合同条款key和value值")@RequestBody Map<String, String> paramMap){

    	Map<String,Object>  map  = contractService.insertDesignOrderContract(orderNumber, paramMap);

        return sendJsonData(ResultMessage.SUCCESS,map);
    }

    
    
    

    /**
     * 根据设计设计设计定单 no
     * 返回pdf
     *
     */

    @ApiOperation(value = "B端--根据订单编号返回pdf url--吕启栋", notes = "返回pdf",consumes = "application/json")
    @PostMapping("/getDesignOrderContract")
    @MyRespBody
    public MyRespBundle<String> getDesignOrderContract(@ApiParam("合同编号orderNumber") @RequestParam String orderNumber){

    	String  pdfUrl  = contractService.getPdfUrlByOrderNumber(orderNumber);

        return sendJsonData(ResultMessage.SUCCESS,pdfUrl);
    }
    
    

    /**
     * 根据设计设计设计定单 no
     * 返回pdf
     *
     */

    @ApiOperation(value = "B端--订单合同不通的原因--吕启栋", notes = "返回pdf",consumes = "application/json")
    @PostMapping("/getDesignOrderContractNoPassCase/pdfUrl")
    @MyRespBody
    public MyRespBundle< List<PcAuditInfo> > getDesignOrderContractNoPassCase(@ApiParam("合同编号orderNumber")@RequestParam  String orderNumber){

    	 List<PcAuditInfo> list   = contractService.getAuditInfoList(orderNumber);

        return sendJsonData(ResultMessage.SUCCESS,list);
    }


    /***
     * 设计师录入合同的带出的数据
     *
     */


    @PostMapping("/getDesignContractInfo")
    @ApiOperation(value = "for江宁哥---通过订单编号查询初始合同信息--吕启栋",notes = "通过用户id获取运营平台组织架构")
    @MyRespBody
    public MyRespBundle<DesignContractToVo> getDesignContractInfo(@ApiParam("orderNo")@RequestParam String orderNo){

        DesignContractToVo resInfo = designDispatchService.getDesigneContractInfo(orderNo);

        return sendJsonData(ResultMessage.SUCCESS,resInfo);
    }

    /**
     * 业主同意或者不同意合同
     *
     */
    @PostMapping("/auditOrderContractToOwner")
    @ApiOperation(value = "for App---业主确认设计合同--吕启栋",notes = "通过用户id获取运营平台组织架构")
    @MyRespBody
    public MyRespBundle<String> auditOrderContractToOwner(@ApiParam("orderNo")@RequestParam String orderNo,
                                                          @ApiParam("cause")@RequestParam String cause,@ApiParam("status")@RequestParam String status){
        boolean flag = contractService.insertOrderContractToOwner( orderNo, cause, status);
        return sendJsonData(ResultMessage.SUCCESS,flag);
    }


    /**
     * 根据合同编号查询 之前的合同信息
     */
    @PostMapping("/getDesignerContractInfo")
    @ApiOperation(value = "for江宁哥---通过订单编号查询之前录入的合同--吕启栋",notes = "通过用户id获取运营平台组织架构")
    @MyRespBody
    public MyRespBundle<Map<String,String>> getDesignerContractInfo(@ApiParam("contractNo")@RequestParam String contractNo){
        Map<String,String> map = contractService.getDesignerContractInfo(contractNo);
        if( map!= null && map.size() > 0 ){
            return sendJsonData(ResultMessage.SUCCESS,map);
        }else{
            return sendJsonData(ResultMessage.ERROR,"该订单合同不存在");
        }

    }

}
