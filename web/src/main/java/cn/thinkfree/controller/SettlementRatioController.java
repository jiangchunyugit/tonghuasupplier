package cn.thinkfree.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.Param.SettlementRatioParam;
import cn.thinkfree.database.model.SettlementRatioInfo;
import cn.thinkfree.database.model.SystemPermission;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.settle.SettlementRatioAudit;
import cn.thinkfree.database.vo.settle.SettlementRatioSEO;
import cn.thinkfree.service.settle.ratio.SettlementRatioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 *
 *  结算比例
 * @date 2018-09-20
 * @author lvqidong
 */
@Api(value = "合同结算比例",description = "合同结算比例信息描述")
@RestController
@RequestMapping("/ratio")
public class SettlementRatioController extends AbsBaseController {


    @Autowired
    SettlementRatioService settlementRatioService;

    /**
     * 分页查询
     * @param settlementRatioSEO
     * @return
     */
    @ApiOperation(value = "结算比例列表", notes = "根据一定条件获取分页合同记录")
    @PostMapping("/queryRatioPage")
    @MyRespBody
    //@MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "分页查询结算比例")
    public MyRespBundle<SystemPermission> queryRatioPage(SettlementRatioSEO settlementRatioSEO){

        BeanValidator.validate(settlementRatioSEO);

        PageInfo<SettlementRatioInfo>   result= settlementRatioService.pageSettlementRatioBySEO(settlementRatioSEO);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }
    
    
    /**
     * 结算比列导出
     * @author lqd
     * @param ContractSEO
     * @return pageList
     */
    @ApiOperation(value = "结算比列导出", notes = "根据一定条件获取分页数据导出")
    @PostMapping("/exportList")
    @MyRespBody
    public void exportList(HttpServletResponse response,
    		@ApiParam("项目搜索条件")   SettlementRatioSEO settlementRatioSEO){
    	settlementRatioService.exportList(settlementRatioSEO, response);
    }
    
   
    
   
    /**
     * 新增或者修改结算比例
     * @param settlementRatio
     * @return
     */
    
    @ApiOperation(value = "新增或者修改结算比例", notes = "新增或者修改结算比例")
    @PostMapping("/insertRatio")
    @MyRespBody
   // @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_CONTRACT,desc = "添加结算比例")
    public MyRespBundle<String> insertRatio(SettlementRatioInfo settlementRatio){

        BeanValidator.validate(settlementRatio);

        boolean  result= settlementRatioService.insertOrupdateSettlementRatio(settlementRatio);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }
    
    /**
     * 获取结算比例
     * @param settlementRatio
     *
     */
    
    @ApiOperation(value = "获取结算比例", notes = "更加结算比例编号获取结算比例")
    @PostMapping("/getRatioByRatioNumber")
    @MyRespBody
    //@MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "添加结算比例")
    public MyRespBundle<SettlementRatioAudit> getRatioByRatioNumber(@ApiParam("结算比例编号")@RequestParam String ratioNumber){

        BeanValidator.validate(ratioNumber);

        SettlementRatioAudit  result= settlementRatioService.getSettlementRatio(ratioNumber);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }

    /**
     * copy 结算比例
     * @param settlementRatio
     * @return
     */
    @ApiOperation(value = "拷贝结算比例", notes = "拷贝结算比例")
    @PostMapping("/copyRatio")
    @MyRespBody
   // @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_CONTRACT,desc = "添加结算比例")
    public MyRespBundle<String> copyRatio(@ApiParam("结算比例编号")@RequestParam String ratioNumber){

        BeanValidator.validate(ratioNumber);

        boolean  result= settlementRatioService.copySettlementRatio(ratioNumber);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }
    
    
    /**
     * 作废结算比例结算比例
     * @param settlementRatio
     * @return
     */
    @ApiOperation(value = "作废结算比例", notes = "作废结算比例")
    @PostMapping("/cancellatRatio")
    @MyRespBody
    //@MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_CONTRACT,desc = "添加结算比例")
    public MyRespBundle<String> cancellatSettlementRatio(@ApiParam("结算比例编号")@RequestParam String ratioNumber){


        boolean  result= settlementRatioService.cancelledSettlementRatio(ratioNumber);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }
    
    
    
    /**
     * 获取费用名称
     * @param settlementRatio
     * @return
     */
    @ApiOperation(value = "费用名称Map", notes = "费用名称Map （key字符串 value字符串）")
    @GetMapping("/getCostNames")
    @MyRespBody
    //@MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "查询结算比例名称")
    public MyRespBundle<Map<String,String>> getCostNames(){

        Map<String, String>  result= settlementRatioService.getCostNames();

        return sendJsonData(ResultMessage.SUCCESS,result);
    }
    
    
    /**
     * 批量审批 
     * @param settlementRatio
     * @return
     */
    @ApiOperation(value = "费用名称Map", notes = "费用名称Map （key字符串 value字符串）")
    @PostMapping("/batchcCheckSettlementRatio")
    @MyRespBody
    //@MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "查询结算比例名称")
    public MyRespBundle<String> batchcCheckSettlementRatio(SettlementRatioParam param){

    	boolean  result= settlementRatioService.batchcCheckSettlementRatio(param.getRatioNumbers(),param.getAuditStatus(),param.getAuditCase());

        return sendJsonData(ResultMessage.SUCCESS,result);
    }


}