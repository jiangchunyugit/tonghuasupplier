package cn.thinkfree.service.newproject;

import cn.thinkfree.core.bundle.MyRespBundle;
import cn.thinkfree.database.appvo.*;
import cn.thinkfree.database.model.OrderApplyRefund;
import cn.thinkfree.database.pcvo.PcProjectDetailVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 项目相关
 * @author gejiaming
 */
public interface NewProjectService {
    /**
     * 项目列表
     * @param appProjectSEO
     * @return
     */
    MyRespBundle<PageInfo<ProjectVo>> getAllProject(AppProjectSEO appProjectSEO);

    /**
     * 确认资料
     * @param projectNo
     * @param category
     * @return
     */
    MyRespBundle<String> confirmVolumeRoomData(String projectNo, Integer category);

    /**
     * 获取设计资料
     * @param projectNo
     * @return
     */
    MyRespBundle<DataVo> getDesignData(String projectNo);

    /**
     * 获取施工资料
     * @param projectNo
     * @return
     */
    MyRespBundle<List<UrlDetailVo>> getConstructionData(String projectNo);

    /**
     * 获取报价单资料
     * @param projectNo
     * @return
     */
    MyRespBundle<List<UrlDetailVo>> getQuotationData(String projectNo);

    /**
     * 获取项目详情接口
     * @param projectNo
     * @return
     */
    MyRespBundle<ProjectVo> getAppProjectDetail(String projectNo);

    /**
     * 批量获取人员信息
     * @param projectNo
     * @return
     */
    MyRespBundle<List<UserVo>> getProjectUsers(String projectNo);

    /**
     * 获取项目阶段
     * @param projectNo
     * @return
     */
    MyRespBundle<Integer> getProjectStatus(String projectNo);

    /**
     * 批量获取员工的信息
     * @param userIds
     * @return
     */
    MyRespBundle<Map<String,UserVo>> getListUserByUserIds(List<String> userIds);

    /**
     * 退款
     * @param orderNo
     * @param payOrderNo
     * @param otherReason
     * @param money
     * @param moneyName
     * @param userId
     * @param cancelReason
     * @return
     */
    MyRespBundle<String> applyRefund(String orderNo,String payOrderNo, String otherReason, Integer money, String moneyName,String userId, String cancelReason);

    /**
     * 获取Pc端项目详情
     * @param projectNo
     * @return
     */
    MyRespBundle<PcProjectDetailVo> getPcProjectDetail(String projectNo);
}
