package cn.thinkfree.service.newproject;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.appvo.*;
import cn.thinkfree.database.model.OrderApplyRefund;
import cn.thinkfree.database.pcvo.ConstructionOrderVO;
import cn.thinkfree.database.pcvo.PcProjectDetailVo;
import cn.thinkfree.service.platform.vo.PageVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 项目相关
 *
 * @author gejiaming
 */
public interface NewProjectService {
    /**
     * 项目列表
     *
     * @param appProjectSEO
     * @return
     */
    MyRespBundle<PageInfo<ProjectVo>> getAllProject(AppProjectSEO appProjectSEO);

    /**
     * 确认资料
     *
     * @param dataVo
     * @return
     */
    MyRespBundle<String> confirmVolumeRoomData(CaseDataVo dataVo);

    /**
     * 获取设计资料
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<DataVo> getDesignData(String projectNo);

    /**
     * 获取施工资料
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<ConstructionDataVo> getConstructionData(String projectNo);

    /**
     * 获取报价单资料
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<List<UrlDetailVo>> getQuotationData(String projectNo);

    /**
     * 获取项目详情接口
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<ProjectVo> getAppProjectDetail(String userId, String projectNo);

    /**
     * 获取项目阶段
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<Integer> getProjectStatus(String projectNo);


    /**
     * 退款
     *
     * @param orderNo
     * @param payOrderNo
     * @param otherReason
     * @param money
     * @param moneyName
     * @param userId
     * @param cancelReason
     * @return
     */
    MyRespBundle<String> applyRefund(String orderNo, String payOrderNo, String otherReason, Integer money, String moneyName, String userId, String cancelReason);

    /**
     * 取消订单
     *
     * @param orderNo
     * @param projectNo
     * @param userId
     * @param cancelReason
     * @return
     */
    MyRespBundle cancleOrder(String orderNo, String projectNo, String userId, String cancelReason);

    /**
     * APP-获取项目详情头接口
     *
     * @param projectNo
     * @return
     */
    MyRespBundle<ProjectTitleVo> getAppProjectTitleDetail(String projectNo);

    /**
     * C/B-项目个数
     *
     * @param userId
     * @return
     */
    MyRespBundle<Integer> getProjectNum(String userId);

    /**
     * C端确认资料
     *
     * @param projectNo
     * @param category
     * @return
     */
    MyRespBundle<String> confirmVolumeRoomDataUser(String projectNo, Integer category);

    /**
     * 更具设计师ID获取设计信息
     *
     * @param designerId
     * @return
     */
    MyRespBundle<List<DesignOrderVo>> getDesignOrderData(String designerId);

    /**
     * C/B-项目列表--施工端
     *
     * @param pageSize
     * @param pageNum
     * @param userId
     * @param inputData
     * @return
     */
    MyRespBundle<PageVo<List<ConstructionProjectVo>>> getConstructionAllProject(int pageSize, int pageNum, String userId, String inputData, Integer projectType);

    /**
     * 获取施工端项目搜索项(进度阶段+验收阶段)
     * @param userId
     * @param projectNo
     * @return
     */
    MyRespBundle<ProjectScreenVo> getProjectScreen(String userId, String projectNo);

    /**
     * 施工端项目列表--筛选
     * @param pageSize
     * @param pageNum
     * @param userId
     * @param delayBegin
     * @param delayEnd
     * @param schedulingSort
     * @param checkSort
     * @param checkComplete
     * @param projectNo
     * @return
     */
    MyRespBundle<PageVo<List<ConstructionProjectVo>>> getProjectByScreen(int pageSize, int pageNum, String userId, Integer delayBegin, Integer delayEnd, Integer schedulingSort, Integer checkSort, Integer checkComplete, String projectNo);
}
