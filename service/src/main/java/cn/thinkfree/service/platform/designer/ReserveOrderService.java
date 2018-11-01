package cn.thinkfree.service.platform.designer;

import cn.thinkfree.database.model.ReserveProject;
import cn.thinkfree.service.platform.vo.PageVo;

import java.util.List;

/**
 * @author xusonghui
 */
public interface ReserveOrderService {
    /**
     * 创建设计订单
     *
     * @param ownerName 业主姓名
     * @param phone     业主手机号
     * @param address   地址
     * @param source    来源
     * @param style     装修风格
     * @param budget    装修预算
     * @param acreage   装修面积
     * @param userId    操作人Id
     * @param companyId 公司ID
     */
    void createReserveOrder(String ownerName, String phone, String address, int source, int style,
                            String budget, String acreage, String userId, String companyId);

    /**
     * 关闭待转换订单
     *
     * @param reserveNo 待转换订单编号
     * @param state     状态，3业主取消，4其他
     * @param reason    关闭原因
     */
    void closeReserveOrder(String reserveNo, int state, String reason);

    /**
     * 查询待转换订单列表
     *
     * @param ownerName 业主姓名
     * @param phone     业主手机号
     * @param pageSize  每页多少条
     * @param pageIndex 第几页
     * @return
     */
    PageVo<List<ReserveProject>> queryReserveOrder(String ownerName, String phone, int pageSize, int pageIndex);

    /**
     * 根据预约订单编号查询预约订单信息
     *
     * @param reserveNo 预约订单编号
     * @return
     */
    ReserveProject queryReserveOrderByNo(String reserveNo);

    /**
     * 创建项目订单
     *
     * @param reserveNo        待转换订单编号
     * @param companyId        所属公司ID
     * @param source           订单来源
     * @param huxing           房屋户型，1小区房，2别墅，3复式，4其他
     * @param roomNum          房屋个数
     * @param officeNum        客厅个数
     * @param toiletNum        卫生间个数
     * @param address          装修地址
     * @param addressDetail    装修详细地址
     * @param style            装修风格
     * @param area             建筑面积
     * @param houseType        房屋类型，1新房，2老房
     * @param peopleNum        常住人口
     * @param planStartTime    计划装修开始时间
     * @param planEndTime      计划装修结束时间
     * @param decorationBudget 装修预算
     * @param balconyNum       阳台个数
     * @param ownerId          业主ID
     * @param designerId       设计师ID
     */
    void createProject(String reserveNo, String companyId, int source, int huxing, int roomNum, int officeNum, int toiletNum,
                       String address, String addressDetail, int style, int area, int houseType, int peopleNum,
                       String planStartTime, String planEndTime, int decorationBudget, int balconyNum, String ownerId, String designerId);
}
