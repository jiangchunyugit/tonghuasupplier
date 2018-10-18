package cn.thinkfree.service.platform.designer;

import cn.thinkfree.service.platform.designer.vo.DesignOrderVo;
import cn.thinkfree.service.platform.designer.vo.PageVo;

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
     * @return
     */
    PageVo<List<DesignOrderVo>> queryDesignerOrder(
            String companyId, String projectNo, String userMsg, String orderSource, String createTimeStart,
            String createTimeEnd, String styleCode, String money, String acreage, int designerOrderState, String companyState,
            String optionUserName, String optionTimeStart, String optionTimeEnd, int pageSize, int pageIndex);

    /**
     * 订单不派单
     *
     * @param projectNo      项目编号
     * @param reason         不派单原因
     * @param companyId      公司ID
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人姓名
     */
    void notDispatch(String projectNo, String reason, String companyId, String optionUserId, String optionUserName);

    /**
     * 订单派单
     *
     * @param projectNo      项目编号
     * @param companyId      公司ID
     * @param optionUserId   操作人ID
     * @param optionUserName 操作人姓名
     */
    void dispatch(String projectNo, String companyId, String optionUserId, String optionUserName);

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
     * @param optionUserName 设计师名称
     */
    void designerRefuse(String projectNo, String reason, String designerUserId, String optionUserName);

}
