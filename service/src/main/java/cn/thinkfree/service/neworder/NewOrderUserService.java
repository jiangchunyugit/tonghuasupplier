package cn.thinkfree.service.neworder;

import cn.thinkfree.database.model.OrderUser;
import cn.thinkfree.database.vo.*;

import java.util.List;
import java.util.Map;

/**
 * 项目用户关系服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/18 11:37
 */
public interface NewOrderUserService {

    /**
     * 根据项目编号查询项目用户关系
     *
     * @param orderNo 项目编号
     * @return 项目用户关系
     */
    List<OrderUser> findByOrderNo(String orderNo);

    /**
     * 根据项目编号与角色编号查询用户ID
     *
     * @param orderNo 项目编号
     * @param roleId  角色编号
     * @return 用户ID
     */
    String findUserIdByOrderNoAndRoleId(String orderNo, String roleId);

    /**
     * 根据项目编号与用户编号查询角色ID
     *
     * @param orderNo 项目编号
     * @param userId  用户编号
     * @return 角色ID
     */
    String findRoleIdByOrderNoAndUserId(String orderNo, String userId);

    /**
     * 根据项目编号与用户编号查询项目角色用户关系
     *
     * @param orderNo 项目编号
     * @param userId  用户编号
     * @return 项目角色用户关系
     */
    OrderUser findByOrderNoAndUserId(String orderNo, String userId);

    /**
     * 分页查询项目派单
     * @param projectOrderVO
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<ProjectOrderVO> queryProjectOrderByPage(ProjectOrderVO projectOrderVO, Integer pageNum, Integer pageSize);

    /**
     * @Author jiang
     * @Description 查询项目派单总条数
     * @param projectOrderVO
     * @return
     */
    Integer queryProjectOrderCount(ProjectOrderVO projectOrderVO);

    /**
     * @Author jiang
     * @Description 订单确认接口
     * @param orderConfirmationVO
     * @return
     */
    Integer updateorderConfirmation(OrderConfirmationVO orderConfirmationVO);

    /**
     * @Author jiang
     * @Description 查看订单详情
     * @param projectNo
     * @return
     */
    OrderDetailsVO selectOrderDetails(String projectNo);

    /**
     * @return
     * @Author jiang
     * @Description 阶段展示
     * @Date
     * @Param
     **/
    List<StageDetailsVO> selectStageDetailsList(String projectNo);

    /**
     * 根据项目编号与角色编号查询项目、角色、用户关系
     *
     * @param projectNo 项目编号
     * @param roleId    角色编号
     * @return 项目、角色、用户关系
     */
    OrderUser findByOrderNoAndRoleId(String projectNo, String roleId);

    /**
     * @return
     * @Author jiang
     * @Description 修改订单状态
     * @Date
     * @Param
     **/
    Integer modifyOrder(OrderConfirmationVO orderConfirmationVO);

    /**
     * @return
     * @Author jiang
     * @Description 分页查询施工工地
     * @Date
     * @Param
     **/
    List<ConstructionSiteVO> querySiteDetailsByPage(ConstructionSiteVO constructionSiteVO, Integer pageNum, Integer pageSize);

    /**
     * @return
     * @Author jiang
     * @Description 查询施工工地总条数
     * @Date
     * @Param
     **/
    Integer querySiteDetailsCount(ConstructionSiteVO constructionSiteVO);

    /**
     * @return
     * @Author jiang
     * @Description 分页查询工地详情
     * @Date
     * @Param
     **/
    List<SiteDetailsVO> querySiteByPage(SiteDetailsVO siteDetailsVO, Integer pageNum, Integer pageSize);

    /**
     * @return
     * @Author jiang
     * @Description 查询工地详情总条数
     * @Date
     * @Param
     **/
    Integer querySiteCount(SiteDetailsVO siteDetailsVO);

    /**
     * @return
     * @Author jiang
     * @Description 分页查询施工计划
     * @Date
     * @Param
     **/
    List<ConstructionPlanVO> queryConstructionPlanByPage(ConstructionPlanVO constructionPlanVO, Integer pageNum, Integer pageSize);

    /**
     * @return
     * @Author jiang
     * @Description 查询施工计划总条数
     * @Date
     * @Param
     **/
    Integer queryConstructionPlanCount(ConstructionPlanVO constructionPlanVO);
    /**
     * @return
     * @Author jiang
     * @Description 查询员工信息
     * @Date
     * @Param
     **/
    EmployeeInfoVO selectemployeeInfoList(String projectNo);
    /**
     * @Author jiang
     * @Description 分页查询验收结果
     * @Date
     * @Param
     * @return
     **/
    List<AcceptanceResultsVO> queryAcceptanceResultsByPage(String projectNo, Integer pageNum, Integer pageSize);
    /**
     * @Author jiang
     * @Description 验收结果总条数
     * @Date
     * @Param
     * @return
     **/
    Integer queryAcceptanceResultsCount(String projectNo);

    /**
     * 获取用户信息
     * @param userId
     * @param roleId
     * @return
     */
    Map getUserName(String userId, String roleId);
}
