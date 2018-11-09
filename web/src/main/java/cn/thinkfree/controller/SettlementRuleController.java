package cn.thinkfree.controller;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.Param.SettlementRuleParam;
import cn.thinkfree.database.model.SettlementRuleInfo;
import cn.thinkfree.database.model.SystemPermission;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.Severitys;
import cn.thinkfree.database.vo.settle.SettlementRuleContractVO;
import cn.thinkfree.database.vo.settle.SettlementRuleSEO;
import cn.thinkfree.database.vo.settle.SettlementRuleVO;
import cn.thinkfree.service.settle.rule.SettlementRuleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *  结算规则
 * @date 2018-09-20
 * @author jiangchunyu
 */
@Api(value = "前端使用---结算系统---结算规则设置---蒋春雨",description = "前端使用---结算系统---结算规则设置---蒋春雨")
@RestController
@RequestMapping("/rule")
public class SettlementRuleController extends AbsBaseController {


    @Autowired
    SettlementRuleService settlementRuleService;

    /**
     * 分页查询
     * @param settlementRuleSEO
     * @return
     */
    @ApiOperation(value = "结算规则列表", notes = "根据一定条件获取分页结算规则记录")
    @GetMapping("/queryRulePage")
    @MyRespBody
    //@MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "分页查询结算规则")
    public MyRespBundle<SystemPermission> queryRulePage(SettlementRuleSEO settlementRuleSEO){

        BeanValidator.validate(settlementRuleSEO);
        PageInfo<SettlementRuleInfo> result= settlementRuleService.pageSettlementRuleBySEO(settlementRuleSEO);
        return sendJsonData(ResultMessage.SUCCESS,result);
    }


    /**
     * 结算比列导出
     * @author lqd
     * @return pageList
     */
    @ApiOperation(value = "结算规则导出", notes = "根据一定条件获取分页数据导出")
    @GetMapping("/exportList")
    @MyRespBody
    public void exportList(HttpServletResponse response,
                           @ApiParam("项目搜索条件")   SettlementRuleSEO settlementRuleSEO){
        settlementRuleService.exportList(settlementRuleSEO, response);
    }



    @ApiOperation(value = "根据类型，和费用类型查找全部结算规则和方法", notes = "根据类型，和费用类型查找全部规则和方法")
    @GetMapping("/ruleContraList")
    @MyRespBody
    public MyRespBundle<List<SettlementRuleContractVO>> ruleContraList(@ApiParam("结算规则类型")@RequestParam String collectionType,
                                                                       @ApiParam("结算规则费用")@RequestParam String feeNm){
        SettlementRuleInfo settlementRuleInfo = new SettlementRuleInfo();
        settlementRuleInfo.setCollectionType(collectionType);
        settlementRuleInfo.setFeeName(feeNm);
        List<SettlementRuleContractVO> settlementRuleContractVOS = settlementRuleService.getSettlementRuleContract(settlementRuleInfo);
       return sendJsonData(ResultMessage.SUCCESS,settlementRuleContractVOS);
    }


    /**
     * 新增或者修改结算规则
     * @param settlementRuleVO
     * @return
     */

    @ApiOperation(value = "创建结算规则（collectionType代收款类型 0 代收款结算规则 1 平台结算规则）", notes = "新增或者修改结算规则")
    @PostMapping("/insertRule")
    @MyRespBody
    // @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_CONTRACT,desc = "添加结算规则")
    public MyRespBundle<String> insertRule(@ApiParam("结算规则信息")   SettlementRuleVO settlementRuleVO){

        BeanValidator.validate(settlementRuleVO,Severitys.Insert.class);

        boolean  result= settlementRuleService.insertOrupdateSettlementRule(settlementRuleVO);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 获取结算规则
     *
     */
    @ApiOperation(value = "查看结算规则", notes = "根据结算规则编号获取结算规则")
    @GetMapping("/getRuleByRuleNumber")
    @MyRespBody
    //@MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "添加结算规则")
    public MyRespBundle<SettlementRuleVO> getRuleByRuleNumber(@ApiParam("结算规则编号")@RequestParam String ruleNumber){

        BeanValidator.validate(ruleNumber);

        SettlementRuleVO  result= settlementRuleService.getSettlementRule(ruleNumber);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * copy 结算规则
     * @return
     */
    @ApiOperation(value = "拷贝结算规则", notes = "拷贝结算规则")
    @PostMapping("/copyRule")
    @MyRespBody
    // @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_CONTRACT,desc = "添加结算规则")
    public MyRespBundle<String> copyRule(@ApiParam("结算规则编号")@RequestParam String ruleNumber){

        BeanValidator.validate(ruleNumber);

        boolean  result= settlementRuleService.copySettlementRule(ruleNumber);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }


    /**
     * 作废结算规则结算规则
     * @return
     */
    @ApiOperation(value = "作废结算规则", notes = "作废结算规则")
    @PostMapping("/cancellatRule")
    @MyRespBody
    //@MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_CONTRACT,desc = "添加结算规则")
    public MyRespBundle<String> cancellatSettlementRule(@ApiParam("结算规则编号")@RequestParam String ruleNumber){


        boolean  result= settlementRuleService.cancellatSettlementRule(ruleNumber);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }



    /**
     * 获取费用名称
     * @return
     */
    @ApiOperation(value = "费用名称Map", notes = "费用名称Map （key字符串 value字符串）")
    @GetMapping("/getCostNames")
    @MyRespBody
    //@MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "查询结算规则名称")
    public MyRespBundle<Map<String,String>> getCostNames(){

        Map<String, String>  result= settlementRuleService.getCostNames();

        return sendJsonData(ResultMessage.SUCCESS,result);
    }


    /**
     * 批量审批
     * @return
     */
    @ApiOperation(value = "批量审批", notes = "批量审批")
    @PostMapping("/batchcCheckSettlementRule")
    @MyRespBody
    //@MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "查询结算规则名称")
    public MyRespBundle<String> batchcCheckSettlementRule(SettlementRuleParam param){

        BeanValidator.validate(param);
        boolean  result= settlementRuleService.batchcCheckSettlementRule(param.getRuleNumbers(),param.getAuditStatus(),param.getAuditCase());

        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * 申请废除
     * @return
     */
    @ApiOperation(value = "申请废除", notes = "申请废除")
    @PostMapping("/applicationInvalid")
    @MyRespBody
    //@MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "查询结算规则名称")
    public MyRespBundle<String> applicationInvalid(@ApiParam("结算规则编号")@RequestParam String ruleNumber){

        boolean  result= settlementRuleService.applicationInvalid(ruleNumber);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }

}
