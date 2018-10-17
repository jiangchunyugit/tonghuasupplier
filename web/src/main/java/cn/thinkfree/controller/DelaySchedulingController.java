package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.model.DesignOrder;
import cn.thinkfree.database.vo.OrderConfirmationVO;
import cn.thinkfree.database.vo.ProjectOrderVO;
import cn.thinkfree.service.scheduling.DelaySchedulingService;
import cn.thinkfree.service.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author gejiaming
 */
@Api(tags = "延期控相关")
@RestController
@RequestMapping(value = "delayScheduling")
public class DelaySchedulingController extends AbsBaseController {
    @Autowired
    private DelaySchedulingService delaySchedulingService;


    /**
     * @return
     * @Author jiang
     * @Description
     * @Date
     * @Param
     **/
    @RequestMapping(value = "projectOrderList", method = RequestMethod.POST)
    @ApiOperation(value = "项目派单列表", notes = "")
    public MyRespBundle<DesignOrder> projectOrderList(@RequestBody ProjectOrderVO projectOrderVO,
                                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (null == projectOrderVO.getProjectNo() || "".equals(projectOrderVO.getProjectNo())) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        Map<String, Object> params = new HashMap<>();
        List<ProjectOrderVO> preProjectGuideList = new ArrayList<>();
        preProjectGuideList = delaySchedulingService.queryProjectOrderByPage(projectOrderVO, (pageNum - 1) * pageSize, pageSize);
        //这里查询的是所有的数据
        params.put("list", preProjectGuideList);
        //这里查询的是总页数
        params.put("totalPage", delaySchedulingService.queryProjectOrderCount(projectOrderVO));
        params.put("pageSize", pageSize);
        params.put("pageNum", pageNum);
        return sendJsonData(ResultMessage.SUCCESS, params);
    }


    /**
     * @return
     * @Author jiang
     * @Description 订单确认接口
     * @Date
     * @Param
     **/
    @RequestMapping(value = "orderConfirmation", method = RequestMethod.POST)
    @ApiOperation(value = "订单确认")
    public MyRespBundle orderConfirmation(@ApiParam(value = "订单确认") OrderConfirmationVO orderConfirmationVO) {
        if (null == orderConfirmationVO.getProjectNo() || "".equals(orderConfirmationVO.getProjectNo())) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        Integer result = delaySchedulingService.updateorderConfirmation(orderConfirmationVO);
        if (result == 0) {
            return sendJsonData(ResultMessage.ERROR, "确认失败");
        }
        return sendJsonData(ResultMessage.SUCCESS, "确认成功");
    }

    /**
     * @return
     * @Author jiang
     * @Description
     * @Date
     * @Param
     **/
    @RequestMapping(value = "orderListExcel", method = RequestMethod.POST)
    @ApiOperation(value = "导出派单列表", notes = "")
    public MyRespBundle<DesignOrder> orderListExcel(@RequestBody ProjectOrderVO projectOrderVO, Integer pageNum, Integer pageSize

    ) {
        if (null == projectOrderVO.getProjectNo() || "".equals(projectOrderVO.getProjectNo())) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        Map<String, Object> params = new HashMap<>();
        List<ProjectOrderVO> projectList = new ArrayList<>();
        projectList = delaySchedulingService.queryProjectOrderByPage(projectOrderVO, pageNum, pageSize);
        //这里查询的是所有的数据
        params.put("list", projectList);
        //这里查询的是总页数
        String sheetTitle = "派单数据";
        String[] title = {"所属地区", "公司名称", "订单编号", "预约日期", "签约日期", "项目地址", "业主", "手机号码", "折后合同额", "已支付", "订单状态", "施工进度", "最近验收情况", "延期天数", "项目经理", "设计师"};
        List<Object> list = new ArrayList<Object>();
        ProjectOrderVO vo = new ProjectOrderVO();
        projectList.forEach((project) ->
                {
                    vo.setAddress(project.getAddress());
                    vo.setCompanyName(project.getCompanyName());
                    vo.setOrderNo(project.getOrderNo());
                    vo.setAppointmentTime(project.getAppointmentTime());
                    vo.setSignedTime(project.getSignedTime());
                    vo.setAddressDetail(project.getAddressDetail());
                    vo.setOwner(project.getOwner());
                    vo.setPhone(project.getPhone());
                    vo.setReducedContractAmount(project.getReducedContractAmount());
                    vo.setHavePaid(project.getHavePaid());
                    vo.setOrderStage(project.getOrderStage());
                    vo.setConstructionProgress(project.getConstructionProgress());
                    vo.setCheckCondition(project.getCheckCondition());
                    vo.setDelayDays(project.getDelayDays());
                    vo.setProjectManager(project.getProjectManager());
                    vo.setDesignerName(project.getDesignerName());
                    list.add(vo);
                }
        );
        byte b[] = ExcelUtil.export(sheetTitle, title, list);

        File f = new File("D:\\tmp\\" + sheetTitle + ".xls");
        try {
            FileUtils.writeByteArrayToFile(f, b, true);
        } catch (IOException ex) {
            Logger.getLogger(ResolverUtil.Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sendJsonData(ResultMessage.SUCCESS, "导出成功");
    }


}
