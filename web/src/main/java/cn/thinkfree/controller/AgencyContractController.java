package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.Severitys;
import cn.thinkfree.database.vo.agency.AgencyAuditContractSEO;
import cn.thinkfree.database.vo.agency.AgencySEO;
import cn.thinkfree.database.vo.agency.ParamAgency;
import cn.thinkfree.database.vo.agency.ParamAgencySEO;
import cn.thinkfree.service.contract.AgencyService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 账号相关
 */
@Api(description="经销商合同")
@RestController
@RequestMapping("/agencyContract")
public class AgencyContractController extends AbsBaseController {

    @Autowired
    AgencyService agencyService;

    @ApiOperation(value="前端-运营平台-经销商合同(运营合同列表)", notes="经销商合同列表")
    @PostMapping("/getPageList")
    @MyRespBody
    public MyRespBundle<PageInfo<AgencyContract>> getPageList(@ApiParam("搜索条件")  AgencySEO seo){

        PageInfo<AgencyContract> pageInfo = agencyService.pageContractBySEO(seo);
        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

    @ApiOperation(value="前端-运营平台-经销商合同(运营合同审批列表)", notes="运营审批经销商合同列表")
    @PostMapping("/selectoperatingPageList")
    @MyRespBody
    public MyRespBundle<PageInfo<AgencyContract>> selectoperatingPageList(@ApiParam("搜索条件")  AgencySEO seo){

        PageInfo<AgencyContract> pageInfo = agencyService.selectoperatingPageList(seo);
        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

    @ApiOperation(value="前端-运营平台-经销商合同(财务审批合同列表)", notes="财务审批合同列表")
    @PostMapping("/selectFinancialPageList")
    @MyRespBody
    public MyRespBundle<PageInfo<AgencyContract>> selectFinancialPageList(@ApiParam("搜索条件")  AgencySEO seo){

        PageInfo<AgencyContract> pageInfo = agencyService.selectFinancialPageList(seo);
        return sendJsonData(ResultMessage.SUCCESS,pageInfo);
    }

    /**
     *  运营录入合同
     * @param
     * @return
     */
    @ApiOperation(value="前端-运营平台-运营录入经销商合同", notes="新增经销商合同(新增的时候status为0 )")
    @PostMapping("/insertContract")
    @MyRespBody
    public MyRespBundle<String> insertContract(@RequestBody ParamAgencySEO paramAgency){

        BeanValidator.validate(paramAgency,Severitys.Insert.class);
        String result = agencyService.checkRepeat(paramAgency);
        if (StringUtils.isNotBlank(result)) {
            return sendFailMessage(result);
        }
        boolean  flag = agencyService.insertContract(paramAgency);
        return sendJsonData(ResultMessage.SUCCESS,flag);
    }

    /**
     *  运营编辑合同
     * @param
     * @return
     */
    @ApiOperation(value="前端-运营平台-运营编辑经销商合同", notes="编辑经销商合同")
    @PostMapping("/updateContract")
    @MyRespBody
    public MyRespBundle<String> updateContract( @RequestBody ParamAgencySEO paramAgency){

        BeanValidator.validate(paramAgency,Severitys.Update.class);
        String result = agencyService.checkRepeat(paramAgency);
        if (StringUtils.isNotBlank(result)) {
            return sendFailMessage(result);
        }
        boolean  flag = agencyService.insertContract(paramAgency);
        return sendJsonData(ResultMessage.SUCCESS,flag);
    }

    /**
     *  合同变更续签
     * @param
     * @return
     */
    @ApiOperation(value="前端-运营平台-合同变更续签", notes="合同变更续签(变更续签的时候status为0 )")
    @PostMapping("/changeContract")
    @MyRespBody
    public MyRespBundle<String> changeContract( @RequestBody ParamAgencySEO paramAgency){

        BeanValidator.validate(paramAgency,Severitys.Insert.class);
        if (StringUtils.isBlank(paramAgency.getContractNumber())) {
            return sendFailMessage("合同编号不可为空");
        }
        String result = agencyService.checkRepeat(paramAgency);
        if (StringUtils.isNotBlank(result)) {
            return sendFailMessage(result);
        }
        boolean  flag = agencyService.changeContract(paramAgency);
        return sendJsonData(ResultMessage.SUCCESS,flag);
    }

    /**
     * 运营审批/财务审核合同
     *2018年12月5日 12:17:59
     * lqd
     */
    @ApiOperation(value="前端-运营平台-审核经销商合同", notes="审核经销商合同")
    @PostMapping("/auditContract")
    @MyRespBody
    public MyRespBundle<String> auditContract(AgencyAuditContractSEO agencyAuditContractSEO){

        BeanValidator.validate(agencyAuditContractSEO,Severitys.Insert.class);
        boolean  flag = agencyService.auditContract(agencyAuditContractSEO.getContractNumber(), agencyAuditContractSEO.getStatus()
                , agencyAuditContractSEO.getAuditStatus(), agencyAuditContractSEO.getCause());

        return sendJsonData(ResultMessage.SUCCESS,flag);
    }

    /**
     * 冻结 解冻 作废合同
     *2018年12月5日 12:17:59
     * lqd
     */
    @ApiOperation(value="前端-运营平台-(冻结,解冻,作废)经销商合同", notes="审核经销商合同（）")
    @PostMapping("/updateContractStatus")
    @MyRespBody
    public MyRespBundle<String> updateContractStatus(AgencyAuditContractSEO agencyAuditContractSEO){

        BeanValidator.validate(agencyAuditContractSEO,Severitys.Update.class);
        boolean  flag = agencyService.updateContractStatus("",agencyAuditContractSEO.getContractNumber()
                , agencyAuditContractSEO.getStatus());
        return sendJsonData(ResultMessage.SUCCESS,flag);
    }

    /**
     * 查看合同详情
     *
     */
    @ApiOperation(value="前端-运营平台-根据合同编号查询合同信息", notes="根据合同编号查询合同信息")
    @PostMapping("/getAgencyContract")
    @MyRespBody
    public MyRespBundle<ParamAgency> getAgencyContract(@ApiParam("合同编号")@RequestParam String contractNumber){

        if (StringUtils.isNotBlank(contractNumber)) {
            ParamAgency  paramAgency = agencyService.getParamAgency(contractNumber);
            return sendJsonData(ResultMessage.SUCCESS,paramAgency);
        }
        return sendJsonData(ResultMessage.FAIL,"操作失败");
    }

}
