package cn.thinkfree.service.construction;

import cn.thinkfree.database.appvo.PersionVo;
import cn.thinkfree.database.model.FundsOrder;
import cn.thinkfree.database.model.Project;
import cn.thinkfree.database.model.ProjectQuotation;
import cn.thinkfree.database.model.ProjectScheduling;
import cn.thinkfree.service.construction.vo.ConstructionOrderListVo;
import cn.thinkfree.service.construction.vo.DecorationOrderListVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/12/3 14:44
 */
public interface OrderListCommonService {

    /**
     * 施工订单
     *
     * @param pageNum
     * @param pageSize
     * @param cityName
     * @param orderType
     * @return
     */
    PageInfo<ConstructionOrderListVo> getConstructionOrderList(int pageNum, int pageSize, String cityName, int orderType);

    /**
     * 施工订单 (装饰公司)
     *
     * @param companyNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<ConstructionOrderListVo> getDecorateOrderList(String companyNo, int pageNum, int pageSize);

    /**
     * 项目派单 给员工
     *
     * @param pageNum
     * @param pageSize
     * @param projectNo
     * @param appointmentTime
     * @param addressDetail
     * @param owner
     * @param phone
     * @param orderStage
     * @return
     */
    PageInfo<DecorationOrderListVo> getDecorationOrderList(String companyNo, int pageNum, int pageSize, String projectNo, String appointmentTime,
                                                                  String addressDetail, String owner, String phone, String orderStage);


    /**
     * 查询项目信息
     *
     * @param listProjectNo
     * @return
     */
    List<Project> getProjectInfo(List<String> listProjectNo);

    /**
     * 查询用户信息 -用户中心接口
     *
     * @param listUserNo
     * @return
     */
    List<PersionVo> getOwnerId (List<Map<String, String>> listUserNo);

    /**
     * 设计师 & 项目经理
     *
     * @param listProjectNo
     * @return
     */
    List<Map<String, String>> getEmployeeInfo(List<String> listProjectNo, String role);

    /**
     * 公司名称
     * TODO 批量
     *
     * @param companyId
     * @return
     */
    String getCompanyInfo(String companyId);

    /**
     * 查询 施工阶段
     *
     * @param stage
     * @return
     */
    String getContstructionStage(Integer stage);

    /**
     * 查询 延期天数/开工时间/竣工时间
     *
     * @param listProjectNo
     * @return
     */
    List<ProjectScheduling> getdelayDay(List<String> listProjectNo);

    /**
     * 查询 延期天数
     *
     * @param listProjectNo
     * @return
     */
    Map<String, Integer> getApprove(List<String> listProjectNo);
    /**
     * 查询 支付
     *
     * @param listProjectNo
     * @return
     */
    List<FundsOrder> getFundsOrder(List<String> listProjectNo);

    /**
     * 报价
     *
     * @param listProjectNo
     * @return
     */
    List<ProjectQuotation> getPrice(List<String> listProjectNo);

}
