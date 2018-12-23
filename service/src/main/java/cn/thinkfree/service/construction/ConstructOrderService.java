package cn.thinkfree.service.construction;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.model.ConstructionOrder;
import cn.thinkfree.database.vo.ConstructCountVO;
import cn.thinkfree.database.vo.construct.ConstructOrderDetailVO;import cn.thinkfree.service.construction.vo.ConsListVo;import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.ConstructionOrderManageVo;
import cn.thinkfree.service.platform.vo.PageVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ConstructOrderService {

    /**
     * 施工订单列表统计
     * @return
     */
    MyRespBundle<ConstructionOrderManageVo> getOrderNum();

    /**
     *  订单列表
     * @param pageNum
     * @param pageSize
     * @param cityName
     * @param orderType
     * @return
     */
    PageInfo<ConstructionOrderListVo> getOrderList(int pageNum, int pageSize, String cityName, int orderType);

    /**
     * 施工订单统计
     * @param userId 用户编号
     * @param approvalType 审批单类型
     * @param pageNum 页码
     * @param pageSize 每页个数
     * @return 施工订单统计
     */
    ConstructCountVO count(String userId, String approvalType, Integer pageNum, Integer pageSize);

    /**
     * 根据项目编号查询施工订单
     * @param projectNo
     * @return
     */
    ConstructionOrder findByProjectNo(String projectNo);

    /**
     * 根据订单编号查询施工订单
     * @param orderNo
     * @return
     */
    ConstructionOrder findByOrderNo(String orderNo);

 	/**
     * 查询施工订单详情
     * @param projectNo 项目编号
     * @return 施工订单详情
     */
    ConstructOrderDetailVO detail(String projectNo);	/**
     * 获取施工订单列表
     * @param orderType
     * @param projectNo
     * @param companyName
     * @param provinceCode
     * @param cityCode
     * @param areaCode
     * @param createTimeS
     * @param createTimeE
     * @param againTimeS
     * @param againTimeE
     * @param address
     * @param ownerName
     * @param ownerPhone
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageVo<List<ConsListVo>> getConsList(int orderType,
            String projectNo, String companyName, String provinceCode, String cityCode, String areaCode, String createTimeS, String createTimeE,
            String againTimeS, String againTimeE, String address, String ownerName, String ownerPhone, String companyId, int pageNum, int pageSize);}
