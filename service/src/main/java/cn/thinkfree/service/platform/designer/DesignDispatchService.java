package cn.thinkfree.service.platform.designer;

import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.database.model.DesignOrder;
import cn.thinkfree.database.model.Project;
import cn.thinkfree.database.model.ReserveProject;
import cn.thinkfree.service.platform.designer.vo.DesignOrderVo;
import cn.thinkfree.service.platform.designer.vo.PageVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xusonghui
 * 设计派单服务
 */
public interface DesignDispatchService {
    /**
     * 查询设计订单
     *
     * @param companyId          公司ID
     * @param projectNo          订单编号
     * @param userMsg            业主姓名或电话
     * @param orderSource        订单来源
     * @param createTimeStart    创建时间开始
     * @param createTimeEnd      创建时间结束
     * @param styleCode          装饰风格
     * @param money              装修预算
     * @param acreage            建筑面积
     * @param designerOrderState 订单状态
     * @param companyState       公司状态
     * @param optionUserName     操作人姓名
     * @param optionTimeStart    操作时间开始
     * @param optionTimeEnd      操作时间结束
     * @param pageSize           每页多少条
     * @param pageIndex          第几页，从1开始
     * @param stateType          状态类型
     * @return
     */
    PageVo<List<DesignOrderVo>> queryDesignerOrder(
            String companyId, String projectNo, String userMsg, String orderSource, String createTimeStart,
            String createTimeEnd, String styleCode, String money, String acreage, int designerOrderState, String companyState,
            String optionUserName, String optionTimeStart, String optionTimeEnd, int pageSize, int pageIndex, int stateType);

    /**
     * 订单不派单
     *
     * @param projectNo      项目编号
     * @param reason         不派单原因
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人姓名
     */
    void notDispatch(String projectNo, String reason, String optionUserId, String optionUserName);

    /**
     * 订单派单
     *
     * @param projectNo      项目编号
     * @param companyId      公司ID
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人姓名
     * @param contractType    承包类型，1小包，2大包
     */
    void dispatch(String projectNo, String companyId, String optionUserId, String optionUserName, int contractType);

    /**
     * 根据项目编号查询订单详情
     *
     * @param projectNo
     * @return
     */
    DesignOrderVo queryDesignOrderVoByProjectNo(String projectNo);

    /**
     * 设计公司拒绝接单
     *
     * @param projectNo      项目编号
     * @param companyId      设计公司ID
     * @param reason         拒绝原因
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人名称
     */
    void refuseOrder(String projectNo, String companyId, String reason, String optionUserId, String optionUserName);

    /**
     * 设计公司指派设计师
     *
     * @param projectNo      项目编号
     * @param companyId      公司ID
     * @param designerUserId 设计师ID
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人名称
     */
    void assignDesigner(String projectNo, String companyId, String designerUserId, String optionUserId, String optionUserName);

    /**
     * 设计师拒绝接单
     *
     * @param projectNo      项目编号
     * @param reason         设计师拒绝原因
     * @param designerUserId 设计师ID
     */
    void designerRefuse(String projectNo, String reason, String designerUserId);

    /**
     * 设计师接单
     *
     * @param projectNo      项目编号
     * @param designerUserId 设计师ID
     */
    void designerReceipt(String projectNo, String designerUserId);

    /**
     * 设计师发起量房预约
     *
     * @param projectNo      项目编号
     * @param designerUserId 设计师ID
     */
    void makeAnAppointmentVolumeRoom(String projectNo, String designerUserId);

    /**
     * 提醒业主
     *
     * @param projectNo      项目编号
     * @param designerUserId 设计师编号
     */
    void remindOwner(String projectNo, String designerUserId);

    /**
     * 更新设计订单状态
     *
     * @param projectNo  项目编号
     * @param orderState 更新状态
     * @param optionId   操作人Id
     * @param optionName 操作人名称
     */
    void updateOrderState(String projectNo, int orderState, String optionId, String optionName);

    /**
     * 更新设计订单状态
     *
     * @param projectNo  项目编号
     * @param orderState 更新状态
     * @param optionId   操作人Id
     * @param optionName 操作人名称
     * @param reason     原因
     */
    void updateOrderState(String projectNo, int orderState, String optionId, String optionName, String reason);

    /**
     * 业主确认交付物
     *
     * @param projectNo 项目编号
     * @param optionId  操作人Id
     */
    void confirmedDeliveries(String projectNo, String optionId);

    /**
     * 查询设计合同类型
     *
     * @param type      类型，1设计合同，2施工合同
     * @param projectNo 项目编号
     * @return 1全款合同，2分期合同
     */
    int contractType(String projectNo, int type);

    /**
     * 支付成功
     *
     * @param projectNo 项目编号
     */
    void paySuccess(String projectNo);

    /**
     * 检查订单状态
     *
     * @param designOrder     订单信息
     * @param designStateEnum 目标状态枚举
     */
    void checkOrderState(DesignOrder designOrder, DesignStateEnum designStateEnum);

    /**
     * 根据项目编号查询项目信息
     *
     * @param projectNo 项目编号
     * @return 项目信息
     */
    Project queryProjectByNo(String projectNo);

    /**
     * 根据项目编号查询设计订单信息
     *
     * @param projectNo 项目编号
     * @return 设计订单信息
     */
    DesignOrder queryDesignOrder(String projectNo);

    /**
     * 根据设计订单编号查询设计订单信息
     *
     * @param orderNo 设计订单编号
     * @return 设计订单信息
     */
    DesignOrder queryDesignOrderByOrderNo(String orderNo);

    /**
     * 订单支付超时
     *
     * @param projectNo 项目编号
     */
    void payTimeOut(String projectNo);

    /**
     * 业主发起终止订单
     *
     * @param projectNo 项目编号
     * @param userId    用户Id
     * @param reason    终止合同原因
     */
    void endOrder(String projectNo, String userId, String reason);

    /**
     * 设计订单派单导出
     * @param request
     * @param response
     */
    void exportDesignOrder(HttpServletRequest request, HttpServletResponse response);

}
