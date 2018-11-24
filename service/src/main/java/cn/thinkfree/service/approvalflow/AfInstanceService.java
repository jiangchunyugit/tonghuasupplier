package cn.thinkfree.service.approvalflow;

import cn.thinkfree.database.vo.AfInstanceDetailVO;
import cn.thinkfree.database.vo.AfInstanceListVO;

import java.util.List;
import java.util.Map;

/**
 * 审批流实例服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/26 10:22
 */
public interface AfInstanceService {

    /**
     * 访问发起页面
     * @param projectNo 项目编号
     * @param userId 用户编号
     * @param configNo 审批流配置编号
     * @param scheduleSort 排期编号
     * @return 审批流实例信息
     */
    AfInstanceDetailVO start(String projectNo, String userId, String configNo, Integer scheduleSort);

    /**
     * 提交发起
     * @param projectNo 项目编号
     * @param userId 用户编号
     * @param configNo 审批流配置编号
     * @param scheduleSort 排期编号
     * @param data 审批数据
     * @param remark 备注信息
     */
    void submitStart(String projectNo, String userId, String configNo, Integer scheduleSort, String data, String remark);

    /**
     * 获取审批流实例详情
     * @param instanceNo 审批流实例编号
     * @param userId 用户编号
     * @return 审批流实例详情
     */
    AfInstanceDetailVO detail(String instanceNo, String userId);

    /**
     * 执行审批
     * @param instanceNo 审批流实例编号
     * @param userId 用户编号
     * @param option 用户选择
     * @param remark 备注
     */
    void approval(String instanceNo, String userId, Integer option, String remark);

    /**
     * 获取审批流实例列表
     * @param userId 用户编号
     * @param projectNo 项目编号
     * @param approvalType 审批流类型
     * @param scheduleSort 排期编号
     * @return 审批流实例列表
     */
    AfInstanceListVO list(String userId, String projectNo, String approvalType, Integer scheduleSort);

    /**
     * 获取项目的审批结果
     * @param projectNo 项目编号
     * @return 审批结果，0：未审批或正在审批；1：同意；2：拒绝
     */
    int getProjectCheckResult(String projectNo);

    /**
     * 批量获取项目的审批结果
     * @param projectNos 项目编号
     * @return 审批结果，null：未审批或正在审批；1：同意；2：拒绝
     */
    Map<String, Integer> getProjectCheckResult(List<String> projectNos);

    /**
     * 获取项目审批列表（PDF地址）
     * @param projectNo
     * @return
     */
    List<String> projectApprovalList(String projectNo);

    /**
     * 获取项目开工报告的状态
     * @param projectNo
     * @return
     */
    int getStartReportStatus(String projectNo);

    /**
     * 获取项目开工报告的状态
     * @param projectNo
     * @return
     */
    int getScheduleEditable(String projectNo);
}
