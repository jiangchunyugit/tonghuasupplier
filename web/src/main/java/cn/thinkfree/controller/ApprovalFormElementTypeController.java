package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.ApprovalFlowFormElementType;
import cn.thinkfree.service.approvalFlow.ApprovalFlowFormElementTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 审批流表单元素类型控制器
 * @author songchuanrang
 */
@Api(value = "审批流表单元素类型API接口", tags = "审批流表单元素类型API接口")
@Controller
@RequestMapping("approvalFormElementType")
public class ApprovalFormElementTypeController extends AbsBaseController {

    @Autowired
    private ApprovalFlowFormElementTypeService formElementTypeService;

    /**
     * 查询所有表格元素类型
     * @return
     */
    @ApiOperation("查询所有表格元素类型")
    @ResponseBody
    @PostMapping("list")
    public MyRespBundle list(){
        return sendJsonData(ResultMessage.SUCCESS, formElementTypeService.findAll());
    }

    @ApiOperation("根据UniqueCode查询表格元素类型")
    @ResponseBody
    @PostMapping(value = "findByNum")
    public MyRespBundle findByNum(@RequestParam("num")String num){
        return sendJsonData(ResultMessage.SUCCESS, formElementTypeService.findByNum(num));
    }

    @ApiOperation("增加表格元素类型")
    @ResponseBody
    @PostMapping(value = "add", produces = "application/json")
    public MyRespBundle add(ApprovalFlowFormElementType formElementType){
        formElementTypeService.add(formElementType);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @ApiOperation("保存表格元素类型")
    @ResponseBody
    @PostMapping(value = "edit", produces = "application/json")
    public MyRespBundle edit(ApprovalFlowFormElementType formElementType){
        formElementTypeService.save(formElementType);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }

    @ApiOperation("删除表格元素类型")
    @ResponseBody
    @PostMapping(value = "delete")
    public MyRespBundle deleteByUniqueCode(@RequestParam("num")String num){
        formElementTypeService.deleteByNum(num);
        return sendSuccessMessage(ResultMessage.SUCCESS.message);
    }
}
