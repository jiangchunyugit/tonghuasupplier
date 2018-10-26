package cn.thinkfree.controller;

import cn.thinkfree.core.base.AbsBaseController;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.OrderUserMapper;
import cn.thinkfree.database.model.DesignOrder;
import cn.thinkfree.database.model.OrderUser;
import cn.thinkfree.database.model.OrderUserExample;
import cn.thinkfree.database.model.Project;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.newscheduling.NewDelaySchedulingService;
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
    private NewOrderUserService newOrderUserService;


    /**
     * @return
     * @Author jiang
     * @Description 项目派单列表
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
        preProjectGuideList = newOrderUserService.queryProjectOrderByPage(projectOrderVO, (pageNum - 1) * pageSize, pageSize);
        //这里查询的是所有的数据
        params.put("list", preProjectGuideList);
        //这里查询的是总页数
        params.put("totalPage", newOrderUserService.queryProjectOrderCount(projectOrderVO));
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
        Integer result = newOrderUserService.updateorderConfirmation(orderConfirmationVO);
        if (result == 0) {
            return sendJsonData(ResultMessage.ERROR, "确认失败");
        }
        return sendJsonData(ResultMessage.SUCCESS, "确认成功");
    }

    /**
     * @return
     * @Author jiang
     * @Description 导出派单列表
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
        projectList = newOrderUserService.queryProjectOrderByPage(projectOrderVO, pageNum, pageSize);
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


    /**
     * @return
     * @Author jiang
     * @Description 查看订单详情
     * @Date
     * @Param
     **/
    @RequestMapping(value = "findOrderDetails", method = RequestMethod.POST)
    @ApiOperation(value = "查看订单详情", notes = "")
    public MyRespBundle<Project> findOrderDetails(@RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo) {
        if (null == projectNo || "".equals(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }

        OrderDetailsVO orderDetailsVO = newOrderUserService.selectOrderDetails(projectNo);
        return sendJsonData(ResultMessage.SUCCESS, orderDetailsVO);
    }


    /**
     * @return
     * @Author jiang
     * @Description 阶段展示
     * @Date
     * @Param
     **/
    @RequestMapping(value = "stageDetailsList", method = RequestMethod.POST)
    @ApiOperation(value = "查看阶段详情", notes = "")
    public MyRespBundle<Project> stageDetailsList(@RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo) {
        if (null == projectNo || "".equals(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        List<StageDetailsVO> stageDetailsList = newOrderUserService.selectStageDetailsList(projectNo);
        return sendJsonData(ResultMessage.SUCCESS, stageDetailsList);
    }

    /**
     * @return
     * @Author jiang
     * @Description 修改订单状态
     * @Date
     * @Param
     **/
    @RequestMapping(value = "modifyOrder", method = RequestMethod.POST)
    @ApiOperation(value = "修改订单状态")
    public MyRespBundle modifyOrder(@ApiParam(value = "修改订单状态") OrderConfirmationVO orderConfirmationVO) {
        if (null == orderConfirmationVO.getProjectNo() || "".equals(orderConfirmationVO.getProjectNo())) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        Integer result = newOrderUserService.modifyOrder(orderConfirmationVO);
        if (result == 0) {
            return sendJsonData(ResultMessage.ERROR, "修改失败");
        }
        return sendJsonData(ResultMessage.SUCCESS, "修改成功");
    }


    /**
     * @return
     * @Author jiang
     * @Description 施工工地详情
     * @Date
     * @Param
     **/
    @RequestMapping(value = "siteDetailsList", method = RequestMethod.POST)
    @ApiOperation(value = "施工工地详情", notes = "")
    public MyRespBundle<DesignOrder> siteDetailsList(@RequestBody ConstructionSiteVO constructionSiteVO,
                                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                                     @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (null == constructionSiteVO.getProjectNo() || "".equals(constructionSiteVO.getProjectNo())) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        Map<String, Object> params = new HashMap<>();
        List<ConstructionSiteVO> siteDetailsList = new ArrayList<>();
        siteDetailsList = newOrderUserService.querySiteDetailsByPage(constructionSiteVO, (pageNum - 1) * pageSize, pageSize);
        //这里查询的是所有的数据
        params.put("list", siteDetailsList);
        //这里查询的是总页数
        params.put("totalPage", newOrderUserService.querySiteDetailsCount(constructionSiteVO));
        params.put("pageSize", pageSize);
        params.put("pageNum", pageNum);
        return sendJsonData(ResultMessage.SUCCESS, params);
    }


    /**
     * @return
     * @Author jiang
     * @Description 导出施工工地详情Excel
     * @Date
     * @Param
     **/
    @RequestMapping(value = "siteDetailsListExcel", method = RequestMethod.POST)
    @ApiOperation(value = "导出施工工地详情", notes = "")
    public MyRespBundle<DesignOrder> siteDetailsListExcel(@RequestBody ConstructionSiteVO constructionSiteVO,
                                                          Integer pageNum,
                                                          Integer pageSize
    ) {
        if (null == constructionSiteVO.getProjectNo() || "".equals(constructionSiteVO.getProjectNo())) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        Map<String, Object> params = new HashMap<>();
        List<ConstructionSiteVO> siteDetailsList = new ArrayList<>();
        siteDetailsList = newOrderUserService.querySiteDetailsByPage(constructionSiteVO, pageSize, pageSize);
        //这里查询的是所有的数据
        params.put("list", siteDetailsList);
        String sheetTitle = "施工工地数据";
        String[] title = {"订单编号", "地区", "公司名称", "开工日期", "竣工日期", "项目地址", "业主", "手机号码", "金额", "已支付", "施工进度", "项目经理", "设计师"};
        List<Object> list = new ArrayList<Object>();
        ConstructionSiteVO vo = new ConstructionSiteVO();
        siteDetailsList.forEach((project) ->
                {
                    vo.setOrderNo(project.getOrderNo());
                    vo.setAddress(project.getAddress());
                    vo.setCompanyName(project.getCompanyName());
                    vo.setStartTime(project.getStartTime());
                    vo.setEndTime(project.getEndTime());
                    vo.setAddressDetail(project.getAddressDetail());
                    vo.setOwner(project.getOwner());
                    vo.setPhone(project.getPhone());
                    vo.setAmount(project.getAmount());
                    vo.setHavePaid(project.getHavePaid());
                    vo.setOrderStage(project.getOrderStage());
                    vo.setConstructionProgress(project.getConstructionProgress());
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

    /**
     * @return
     * @Author jiang
     * @Description 工地详情
     * @Date
     * @Param
     **/

    @RequestMapping(value = "siteList", method = RequestMethod.POST)
    @ApiOperation(value = "工地详情", notes = "")
    public MyRespBundle<DesignOrder> siteList(@RequestBody SiteDetailsVO siteDetailsVO,
                                              @RequestParam(defaultValue = "1") Integer pageNum,
                                              @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (null == siteDetailsVO.getProjectNo() || "".equals(siteDetailsVO.getProjectNo())) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        Map<String, Object> params = new HashMap<>();
        List<SiteDetailsVO> siteDetailsList = new ArrayList<>();
        siteDetailsList = newOrderUserService.querySiteByPage(siteDetailsVO, (pageNum - 1) * pageSize, pageSize);
        //这里查询的是所有的数据
        params.put("list", siteDetailsList);
        //这里查询的是总页数
        params.put("totalPage", newOrderUserService.querySiteCount(siteDetailsVO));
        params.put("pageSize", pageSize);
        params.put("pageNum", pageNum);
        return sendJsonData(ResultMessage.SUCCESS, params);
    }

    /**
     * @return
     * @Author jiang
     * @Description 施工计划
     * @Date
     * @Param
     **/

    @RequestMapping(value = "constructionPlanList", method = RequestMethod.POST)
    @ApiOperation(value = "施工计划", notes = "")
    public MyRespBundle<DesignOrder> constructionPlanList(@RequestBody ConstructionPlanVO constructionPlanVO,
                                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                                          @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (null == constructionPlanVO.getProjectNo() || "".equals(constructionPlanVO.getProjectNo())) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        Map<String, Object> params = new HashMap<>();
        List<ConstructionPlanVO> siteDetailsList = new ArrayList<>();
        siteDetailsList = newOrderUserService.queryConstructionPlanByPage(constructionPlanVO, (pageNum - 1) * pageSize, pageSize);
        //这里查询的是所有的数据
        params.put("list", siteDetailsList);
        //这里查询的是总页数
        params.put("totalPage", newOrderUserService.queryConstructionPlanCount(constructionPlanVO));
        params.put("pageSize", pageSize);
        params.put("pageNum", pageNum);
        return sendJsonData(ResultMessage.SUCCESS, params);
    }


    /**
     * @return
     * @Author jiang
     * @Description 导出施工计划
     * @Date
     * @Param
     **/

    @RequestMapping(value = "constructionPlanExcel", method = RequestMethod.POST)
    @ApiOperation(value = "导出施工计划", notes = "")
    public MyRespBundle<DesignOrder> constructionPlanExcel(@RequestBody ConstructionPlanVO constructionPlanVO,
                                                           Integer pageNum,
                                                           Integer pageSize
    ) {
        if (null == constructionPlanVO.getProjectNo() || "".equals(constructionPlanVO.getProjectNo())) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        Map<String, Object> params = new HashMap<>();
        List<ConstructionPlanVO> siteDetailsList = new ArrayList<>();
        siteDetailsList = newOrderUserService.queryConstructionPlanByPage(constructionPlanVO, pageNum, pageSize);
        //这里查询的是所有的数据
        params.put("list", siteDetailsList);
        String sheetTitle = "施工计划";
        String[] title = {"施工阶段", "施工项目", "计划开始时间", "计划结束时间", "实际开始时间", "实际结束时间"};
        List<Object> list = new ArrayList<Object>();
        ConstructionPlanVO vo = new ConstructionPlanVO();
        siteDetailsList.forEach((project) ->
                {
                    vo.setStage(project.getStage());
                    vo.setConstructionProject(project.getConstructionProject());
                    vo.setPlanSatrtTime(project.getPlanEndTime());
                    vo.setPlanEndTime(project.getPlanEndTime());
                    vo.setActualSatrtTime(project.getActualSatrtTime());
                    vo.setActualEndTime(project.getActualEndTime());
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

    /**
     * @return
     * @Author jiang
     * @Description 获取员工信息
     * @Date
     * @Param
     **/

    @RequestMapping(value = "getEmployeeInfo", method = RequestMethod.POST)
    @ApiOperation(value = "获取员工信息", notes = "")
    public MyRespBundle<Project> getEmployeeInfo(@RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo) {
        if (null == projectNo || "".equals(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        EmployeeInfoVO employeeInfoList = newOrderUserService.selectemployeeInfoList(projectNo);
        return sendJsonData(ResultMessage.SUCCESS, employeeInfoList);
    }

    /**
     * @return
     * @Author jiang
     * @Description 验收结果
     * @Date
     * @Param
     **/

    @RequestMapping(value = "acceptanceResults", method = RequestMethod.POST)
    @ApiOperation(value = "验收结果", notes = "")
    public MyRespBundle<DesignOrder> acceptanceResults(@RequestParam(name = "projectNo") @ApiParam(value = "项目编号", name = "projectNo") String projectNo,
                                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                                          @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        if (null == projectNo || "".equals(projectNo)) {
            return sendJsonData(ResultMessage.ERROR, "项目编号为空");
        }
        Map<String, Object> params = new HashMap<>();
        List<AcceptanceResultsVO> acceptanceResultsList = new ArrayList<>();
        acceptanceResultsList = newOrderUserService.queryAcceptanceResultsByPage(projectNo, (pageNum - 1) * pageSize, pageSize);
        //这里查询的是所有的数据
        params.put("list", acceptanceResultsList);
        //这里查询的是总页数
        params.put("totalPage", newOrderUserService.queryAcceptanceResultsCount(projectNo));
        params.put("pageSize", pageSize);
        params.put("pageNum", pageNum);
        return sendJsonData(ResultMessage.SUCCESS, params);
    }



}
