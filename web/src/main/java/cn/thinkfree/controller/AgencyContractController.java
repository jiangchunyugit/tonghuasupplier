package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.agency.AgencySEO;
import cn.thinkfree.database.vo.agency.MyAgencyContract;
import cn.thinkfree.database.vo.agency.ParamAgency;
import cn.thinkfree.database.vo.agency.ParamAgencySEO;
import cn.thinkfree.service.contract.AgencyService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 账号相关
 */
@Api(description="经销商合同")
@RestController
@RequestMapping("/agencyContract")
public class AgencyContractController extends AbsBaseController {

    @Autowired
    AgencyService agencyService;


    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = MyAgencyContract.class)
    })
    @ApiOperation(value="前端-运营平台-经销商合同(运营合同列表)", notes="经销商合同列表")
    @PostMapping("/getPageList")
    @MyRespBody
    public MyRespBundle<PageInfo<MyAgencyContract>> getPageList(@ApiParam("搜索条件")  AgencySEO seo){

        PageInfo<MyAgencyContract> pageInfo = agencyService.pageContractBySEO( seo);

        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = MyAgencyContract.class)
    })
    @ApiOperation(value="前端-运营平台-经销商合同(运营合同审批列表)", notes="运营审批经销商合同列表")
    @PostMapping("/selectoperatingPageList")
    @MyRespBody
    public MyRespBundle<PageInfo<MyAgencyContract>> selectoperatingPageList(@ApiParam("搜索条件")  AgencySEO seo){

        PageInfo<MyAgencyContract> pageInfo = agencyService.selectoperatingPageList( seo);

        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }


    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = MyAgencyContract.class)
    })
    @ApiOperation(value="前端-运营平台-经销商合同(财务审批合同列表)", notes="财务审批合同列表")
    @PostMapping("/selectFinancialPageList")
    @MyRespBody
    public MyRespBundle<PageInfo<MyAgencyContract>> selectFinancialPageList(@ApiParam("搜索条件")  AgencySEO seo){

        PageInfo<MyAgencyContract> pageInfo = agencyService.selectFinancialPageList( seo);

        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }





    /**
     *  运营录入合同
     * @param
     * @return
     */
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = boolean.class)
    })
    @ApiOperation(value="前端-运营平台-运营录入经销商合同", notes="新增经销商合同(新增的时候status为0 )")
    @PostMapping("/insertContract")
    @MyRespBody
    public MyRespBundle<String> insertContract( @RequestBody List<ParamAgencySEO> paramAgencyList){

           boolean  flag = agencyService.insertContract( paramAgencyList);

        return sendJsonData(ResultMessage.SUCCESS,flag);
    }

    /**
     * 运营审批/财务审核合同
     *2018年12月5日 12:17:59
     * lqd
     */
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = boolean.class)
    })
    @ApiOperation(value="前端-运营平台-审核经销商合同", notes="审核经销商合同（财务审核通过的时候brandNo 必须传值）")
    @PostMapping("/auditContract")
    @MyRespBody
    public MyRespBundle<String> auditContract(@ApiParam("公司编号")@RequestParam  String companyId ,@ApiParam("合同编号")@RequestParam String contractNumber,
                                                        @ApiParam("合同原状态")@RequestParam String status,@ApiParam("审批状态 1 通过 0 拒绝")@RequestParam String auditStatus,
                                                        @ApiParam("审核通过或者拒绝的原因 ")@RequestParam(required = false) String cause,@ApiParam("品牌编号 ")@RequestParam String brandNo){
        boolean  flag = agencyService.auditContract(companyId,contractNumber,status,auditStatus,cause,brandNo);

        return sendJsonData(ResultMessage.SUCCESS,flag);
    }


    /**
     * 冻结 解冻 作废合同
     *2018年12月5日 12:17:59
     * lqd
     */
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = boolean.class)
    })
    @ApiOperation(value="前端-运营平台-(冻结,解冻,作废)经销商合同", notes="审核经销商合同（）")
    @PostMapping("/updateContractStatus")
    @MyRespBody
    public MyRespBundle<String> updateContractStatus(@ApiParam("公司编号")@RequestParam  String companyId ,@ApiParam("合同编号")@RequestParam String contractNumber,
                                              @ApiParam("合同原状态")@RequestParam String status){
        boolean  flag = agencyService.updateContractStatus(companyId,contractNumber,status);

        return sendJsonData(ResultMessage.SUCCESS,flag);
    }


    /**
     * 查看合同详情
     *
     */
    @ApiResponses({
            @ApiResponse(code = 200,message = "操作成功",response = ParamAgency.class)
    })
    @ApiOperation(value="前端-运营平台-根据合同编号查询合同信息", notes="根据合同编号查询合同信息")
    @PostMapping("/getAgencyContract")
    @MyRespBody
    public MyRespBundle<ParamAgency> getAgencyContract(@ApiParam("合同编号")@RequestParam String contractNumber){

        ParamAgency  paramAgency = agencyService.getParamAgency(contractNumber);

        return sendJsonData(ResultMessage.SUCCESS,paramAgency);
    }




    /**
     * 获取品牌
     * @param
     * @return
     */
    @ApiOperation(value = "获取品牌", notes = "品牌名称Map （key字符串 value字符串）")
    @GetMapping("/getBrandNames")
    @MyRespBody
    //@MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "查询结算比例名称")
    public MyRespBundle<Map<String,String>> getBrandNames(){

        Map<String, String>  result= agencyService.getBrandNames();

        return sendJsonData(ResultMessage.SUCCESS,result);
    }
    /**
     * 获取品类
     * @param
     * @return
     */

    @ApiOperation(value = "品类名称Map", notes = "品类名称Map （key字符串 value字符串）")
    @GetMapping("/getCategoryNames")
    @MyRespBody
    //@MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "查询结算比例名称")
    public MyRespBundle<Map<String,String>> getCategoryNames(){

        Map<String, String>  result= agencyService.getCategoryNames();

        return sendJsonData(ResultMessage.SUCCESS,result);
    }




}
