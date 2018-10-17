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
            String projectNo, String userMsg, String orderSource, String createTimeStart, String createTimeEnd, String styleCode, String money,
            String acreage, int designerOrderState, String companyState, String optionUserName, String optionTimeStart, String optionTimeEnd, int pageSize, int pageIndex);
}
