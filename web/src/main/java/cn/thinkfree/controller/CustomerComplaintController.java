package cn.thinkfree.controller;


import cn.thinkfree.core.annotation.MyRespBody;
import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.ComplaintOrderInfo;
import cn.thinkfree.service.construction.*;
import cn.thinkfree.service.construction.vo.*;
import cn.thinkfree.service.customercomplaint.CustomerComplaintService;
import cn.thinkfree.service.customercomplaint.vo.CreateComplaintVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 客诉相关接口
 */
@Api(value = "客诉相关API接口", tags = "客诉相关API接口---->前端专用")
@RestController
@RequestMapping("complaint")
public class CustomerComplaintController extends AbsBaseController {

    @Autowired
    CustomerComplaintService customerComplaintService;

    @ApiOperation("创建投诉订单")
    @RequestMapping(value = "create", method = {RequestMethod.POST, RequestMethod.GET})
    public MyRespBundle<CreateComplaintVO> createCustomerComplaint(@RequestBody @ApiParam(value = "客诉工单信息") ComplaintOrderInfo complaintOrderInfo) {
        return sendJsonData(ResultMessage.SUCCESS, customerComplaintService.saveCustomerComplaint(complaintOrderInfo));
    }

}
