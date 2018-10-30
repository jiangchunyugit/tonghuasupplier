package cn.thinkfree.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
import cn.thinkfree.database.model.SettlementRatioInfo;
import cn.thinkfree.database.model.SystemPermission;
import cn.thinkfree.database.utils.BeanValidator;
import cn.thinkfree.database.vo.settle.SettlementRatioSEO;
import cn.thinkfree.service.settle.ratio.SettlementRatioService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 结算比例
 */
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
    @MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "分页查询结算比例")
    public MyRespBundle<SystemPermission> queryRatioPage(SettlementRatioSEO settlementRatioSEO){

        BeanValidator.validate(settlementRatioSEO);

        PageInfo<SettlementRatioInfo>   result= settlementRatioService.pageSettlementRatioBySEO(settlementRatioSEO);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }
   
    
   
    /**
     * 新增或者修改结算比例
     * @param settlementRatio
     * @return
     */
    
    @ApiOperation(value = "新增或者修改结算比例", notes = "新增或者修改结算比例")
    @PostMapping("/insertRatio")
    @MyRespBody
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_CONTRACT,desc = "添加结算比例")
    public MyRespBundle<String> insertRatio(SettlementRatioInfo settlementRatio){

        BeanValidator.validate(settlementRatio);

        boolean  result= settlementRatioService.insertOrupdateSettlementRatio(settlementRatio);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }
    
    /**
     * 获取结算比例
     * @param settlementRatio
     * @return
     */
    
    @ApiOperation(value = "获取结算比例", notes = "更加结算比例编号获取结算比例")
    @PostMapping("/getRatioByRatioNumber")
    @MyRespBody
    @MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "添加结算比例")
    public MyRespBundle<SettlementRatioInfo> getRatioByRatioNumber(@ApiParam("结算比例编号")@RequestParam String ratioNumber){

        BeanValidator.validate(ratioNumber);

        SettlementRatioInfo  result= settlementRatioService.getSettlementRatio(ratioNumber);

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
    @MySysLog(action = SysLogAction.SAVE,module = SysLogModule.PC_CONTRACT,desc = "添加结算比例")
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
    @MySysLog(action = SysLogAction.EDIT,module = SysLogModule.PC_CONTRACT,desc = "添加结算比例")
    public MyRespBundle<String> cancellatSettlementRatio(@ApiParam("结算比例编号")@RequestParam String ratioNumber){


        boolean  result= settlementRatioService.cancellatSettlementRatio(ratioNumber);

        return sendJsonData(ResultMessage.SUCCESS,result);
    }
    
    
    
    /**
     * 作废结算比例结算比例
     * @param settlementRatio
     * @return
     */
    @ApiOperation(value = "费用名称Map", notes = "费用名称Map （key字符串 value字符串）")
    @GetMapping("/getCostNames")
    @MyRespBody
    @MySysLog(action = SysLogAction.QUERY,module = SysLogModule.PC_CONTRACT,desc = "查询结算比例名称")
    public MyRespBundle<Map<String,String>> getCostNames(){

        Map<String, String>  result= settlementRatioService.getCostNames();

        return sendJsonData(ResultMessage.SUCCESS,result);
    }
   

}
