package cn.thinkfree.service.newproject;

import cn.thinkfree.core.base.RespData;
import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.core.constants.ConstructionStateEnum;
import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.database.appvo.OrderTaskSortVo;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.pcvo.*;
import cn.thinkfree.service.constants.ProjectDataStatus;
import cn.thinkfree.service.neworder.NewOrderService;
import cn.thinkfree.service.neworder.NewOrderUserService;
import cn.thinkfree.service.neworder.ReviewDetailsService;
import cn.thinkfree.service.remote.CloudService;
import cn.thinkfree.service.utils.BaseToVoUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * PC项目相关
 *
 * @author gejiaming
 */
@Service
public class NewPcProjectServiceImpl implements NewPcProjectService {
    @Autowired
    OrderUserMapper orderUserMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    NewOrderService newOrderService;
    @Autowired
    ProjectDataMapper projectDataMapper;
    @Autowired
    DesignerOrderMapper designerOrderMapper;
    @Autowired
    ConstructionOrderMapper constructionOrderMapper;
    @Autowired
    EmployeeMsgMapper employeeMsgMapper;
    @Autowired
    OrderApplyRefundMapper orderApplyRefundMapper;
    @Autowired
    NewOrderUserService newOrderUserService;
    @Autowired
    ProjectStageLogMapper projectStageLogMapper;
    @Autowired
    CloudService cloudService;
    @Autowired
    ProjectQuotationMapper projectQuotationMapper;
    @Autowired
    ProjectQuotationRoomsMapper projectQuotationRoomsMapper;
    @Autowired
    ProjectQuotationRoomsConstructMapper roomsConstructMapper;
    @Autowired
    ProjectQuotationRoomsHardDecorationMapper hardDecorationMapper;
    @Autowired
    ProjectQuotationRoomsSoftDecorationMapper softDecorationMapper;
    @Autowired
    ProjectQuotationCheckMapper checkMapper;
    @Autowired
    ReviewDetailsService reviewDetailsService;


    /**
     * PC获取项目详情接口--项目阶段
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<List<OrderTaskSortVo>> getPcProjectTask(String projectNo) {
        //获取项目阶段信息,所有的阶段时间都以开始时间展示为主,展示所有的PC项目阶段
        List<OrderTaskSortVo> allOrderTask = new ArrayList<>();
        List<Map<String, Object>> designMaps = DesignStateEnum.allStates(ProjectDataStatus.PLAY_PLATFORM.getValue());
        List<Map<String, Object>> constructioMaps = ConstructionStateEnum.allStates(ProjectDataStatus.PLAY_PLATFORM.getValue());
        for (Map<String, Object> map : constructioMaps) {
            OrderTaskSortVo orderTaskSortVo = new OrderTaskSortVo();
            orderTaskSortVo.setSort((Integer) map.get("key"));
            orderTaskSortVo.setName(map.get("val").toString());
            allOrderTask.add(orderTaskSortVo);
        }
        for (Map<String, Object> map : designMaps) {
            OrderTaskSortVo orderTaskSortVo = new OrderTaskSortVo();
            orderTaskSortVo.setName(map.get("val").toString());
            orderTaskSortVo.setSort((Integer) map.get("key"));
            allOrderTask.add(orderTaskSortVo);
        }
        List<OrderTaskSortVo> orderTaskSortVoList = projectStageLogMapper.selectByProjectNo(projectNo);
        for (OrderTaskSortVo taskSortVo : orderTaskSortVoList) {
            for (OrderTaskSortVo taskSortVo1 : allOrderTask) {
                if (taskSortVo.getSort().equals(taskSortVo1.getSort())) {
                    taskSortVo1.setBeginTime(taskSortVo.getBeginTime());
                }
            }
        }
        return RespData.success(allOrderTask);
    }

    /**
     * PC获取项目详情接口--施工订单
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ConstructionOrderVO> getPcProjectConstructionOrder(String projectNo) {
        //组合施工订单信息
        ConstructionOrderVO constructionOrderVO = constructionOrderMapper.selectConstructionOrderVo(projectNo);
        return RespData.success(constructionOrderVO);
    }

    /**
     * PC获取项目详情接口--设计信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<DesignerOrderVo> getPcProjectDesigner(String projectNo) {
        //假信息
        DesignerOrderVo designerOrderVo = new DesignerOrderVo(true, "刘强东", "审核中", new Date(), "方案1", "https://www.baidu.com/", "方案2", "https://www.baidu.com/", "方案3", "https://www.baidu.com/");
        return RespData.success(designerOrderVo);
    }

    /**
     * PC获取项目详情接口--预交底信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<PreviewVo> getPcProjectPreview(String projectNo) {
        //组合预交底信息
        PreviewVo previewVo = new PreviewVo(new Date(), "未审核", "张三", "李四", "王五");
        return RespData.success(previewVo);
    }

    /**
     * PC获取项目详情接口--报价信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<OfferVo> getPcProjectOffer(String projectNo) {
        //组合报价信息
        OfferVo offerVo = new OfferVo("86", "1099", "20000", "1200", "2700", "2700", "2700", "通过", "刘万东", new Date());
        return RespData.success(offerVo);
    }

    /**
     * PC获取项目详情接口--合同信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<ContractVo> getPcProjectContract(String projectNo) {
        //组合合同信息
        ContractVo contractVo = new ContractVo("HGSD6893", new Date(), "生效");
        return RespData.success(contractVo);
    }

    /**
     * PC获取项目详情接口--施工信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<SchedulingVo> getPcProjectScheduling(String projectNo) {
        //组合施工信息
        SchedulingVo schedulingVo = new SchedulingVo("个性化", new Date(), new Date(), "水电工程", "隐蔽验收完成", "20000.0", "刘欢", "张富贵", "刘勋", "张恒");
        return RespData.success(schedulingVo);
    }

    /**
     * PC获取项目详情接口--结算管理
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<SettlementVo> getPcProjectSettlement(String projectNo) {
        //组合结算信息
        SettlementVo settlementVo = new SettlementVo("20000.00", "20000.00", "20000.00", "20000.00", "4000.00", "4000.00", "4000.00", "首付款", "0", "0");
        return RespData.success(settlementVo);
    }

    /**
     * PC获取项目详情接口--评价管理
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<EvaluateVo> getPcProjectEvaluate(String projectNo) {
        //组合评价管理
        EvaluateVo evaluateVo = new EvaluateVo(45, 36, 66);
        return RespData.success(evaluateVo);
    }

    /**
     * PC获取项目详情接口--发票管理
     *
     * @param projectNo
     * @return
     */
    @Override
    public MyRespBundle<InvoiceVo> getPcProjectInvoice(String projectNo) {
        //组合发票管理
        InvoiceVo invoiceVo = new InvoiceVo("电子普通发票", "装修服务", "个人", "02956156154", true);
        return RespData.success(invoiceVo);
    }

    /**
     * 获取上海报价信息
     *
     * @param designId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyRespBundle getShangHaiPriceDetail(String designId, String projectNo) {
        String result = cloudService.getShangHaiPriceDetail(designId);
        JSONObject jsonObject = JSON.parseObject(result);
        JSONObject data = jsonObject.getJSONObject("data");
        JSONObject quoteResult = data.getJSONObject("quoteResult");
        String dataString = JSONObject.toJSONString(data);
        String quoteResultString = JSONObject.toJSONString(quoteResult);
        //添加报价总表信息
        ProjectQuotation projectQuotation = JSONObject.parseObject(dataString, ProjectQuotation.class);
        ProjectQuotation projectQuotation1 = JSONObject.parseObject(quoteResultString, ProjectQuotation.class);
        projectQuotation.setConstructionTotalPrice(projectQuotation1.getConstructionTotalPrice());
        projectQuotation.setExtraPrice(projectQuotation1.getExtraPrice());
        projectQuotation.setHardDecorationPrice(projectQuotation1.getHardDecorationPrice());
        projectQuotation.setMaterialTotalPrice(projectQuotation1.getMaterialTotalPrice());
        projectQuotation.setSoftDecorationPrice(projectQuotation1.getSoftDecorationPrice());
        projectQuotation.setTotalPrice(projectQuotation1.getTotalPrice());
        projectQuotation.setUnitPrice(projectQuotation1.getUnitPrice());
        projectQuotation.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
        projectQuotation.setProjectNo(projectNo);
        int projectQuotationResult = projectQuotationMapper.insertSelective(projectQuotation);
        if (projectQuotationResult != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
            return RespData.error("插入报价总表信息失败!");
        }
        JSONArray rooms = data.getJSONArray("rooms");
        for (int i = 0; i < rooms.size(); i++) {
            JSONObject room = rooms.getJSONObject(i);
            //添加报价房屋信息表
            String roomString = JSONObject.toJSONString(room);
            ProjectQuotationRooms projectQuotationRooms = JSONObject.parseObject(roomString, ProjectQuotationRooms.class);
            projectQuotationRooms.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
            projectQuotationRooms.setProjectNo(projectNo);
            int roomsResult = projectQuotationRoomsMapper.insertSelective(projectQuotationRooms);
            if (roomsResult != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                return RespData.error("插入报价房屋信息表失败!");
            }
            //房屋基础施工信息
            JSONArray constructList = room.getJSONArray("constructList");
            if (constructList.size() > 0) {
                String constructString = JSONObject.toJSONString(constructList);
                List<ProjectQuotationRoomsConstruct> projectQuotationRoomsSoftConstructs = JSONObject.parseArray(constructString, ProjectQuotationRoomsConstruct.class);
                for (ProjectQuotationRoomsConstruct construct : projectQuotationRoomsSoftConstructs) {
                    construct.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    construct.setRoomType(projectQuotationRooms.getRoomType());
                    construct.setRoomName(projectQuotationRooms.getRoomName());
                    construct.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
                    construct.setProjectNo(projectNo);
                    int constructResult = roomsConstructMapper.insertSelective(construct);
                    if (constructResult != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                        return RespData.error("插入房屋基础施工信息表失败!");
                    }
                }
            }
            //添加硬装报价信息
            JSONArray hardDecorationMaterials = room.getJSONArray("hardDecorationMaterials");
            if (hardDecorationMaterials.size() > 0) {
                String hardDecorationString = JSONObject.toJSONString(hardDecorationMaterials);
                List<ProjectQuotationRoomsHardDecoration> projectQuotationRoomsHardConstructs = JSONObject.parseArray(hardDecorationString, ProjectQuotationRoomsHardDecoration.class);
                for (ProjectQuotationRoomsHardDecoration hardDecoration : projectQuotationRoomsHardConstructs) {
                    hardDecoration.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    hardDecoration.setRoomType(projectQuotationRooms.getRoomType());
                    hardDecoration.setRoomName(projectQuotationRooms.getRoomName());
                    hardDecoration.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
                    hardDecoration.setProjectNo(projectNo);
                    int hardResult = hardDecorationMapper.insertSelective(hardDecoration);
                    if (hardResult != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                        return RespData.error("插入硬装报价信息表失败!");
                    }
                }
            }
            //插入软装报价信息
            JSONArray softDecorationMaterials = room.getJSONArray("softDecorationMaterials");
            if (softDecorationMaterials.size() > 0) {
                String softDecorationString = JSONObject.toJSONString(softDecorationMaterials);
                List<ProjectQuotationRoomsSoftDecoration> projectQuotationRoomsSoftDecorations = JSONObject.parseArray(softDecorationString, ProjectQuotationRoomsSoftDecoration.class);
                for (ProjectQuotationRoomsSoftDecoration softDecoration : projectQuotationRoomsSoftDecorations) {
                    softDecoration.setId(UUID.randomUUID().toString().replaceAll("-",""));
                    softDecoration.setRoomType(projectQuotationRooms.getRoomType());
                    softDecoration.setRoomName(projectQuotationRooms.getRoomName());
                    softDecoration.setStatus(ProjectDataStatus.BASE_STATUS.getValue());
                    softDecoration.setProjectNo(projectNo);
                    int softResult = softDecorationMapper.insertSelective(softDecoration);
                    if (softResult != ProjectDataStatus.INSERT_SUCCESS.getValue()) {
                        return RespData.error("插入软装报价信息表失败!");
                    }
                }
            }
        }
        return RespData.success();
    }



}
