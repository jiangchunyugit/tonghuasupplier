package cn.thinkfree.service.neworder;

import cn.thinkfree.database.model.OrderUser;
import cn.thinkfree.database.vo.*;

import java.util.List;

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
     * 根据项目编号与用户编号查询项目用户关系
     *
     * @param orderNo 项目编号
     * @param userId  用户编号
     * @return 项目用户关系
     */
    List<OrderUser> findByOrderNoAndUserId(String orderNo, String userId);

    /**
     * @return
     * @Author jiang
     * @Description 分页查询项目派单
     * @Date
     * @Param
     **/
    List<ProjectOrderVO> queryProjectOrderByPage(ProjectOrderVO projectOrderVO, Integer pageNum, Integer pageSize);

    /**
     * @return
     * @Author jiang
     * @Description 查询项目派单总条数
     * @Date
     * @Param
     **/
    Integer queryProjectOrderCount(ProjectOrderVO projectOrderVO);

    /**
     * @return
     * @Author jiang
     * @Description 订单确认接口
     * @Date
     * @Param orderConfirmationVO
     **/
    Integer updateorderConfirmation(OrderConfirmationVO orderConfirmationVO);

    /**
     * @return
     * @Author jiang
     * @Description 查看订单详情
     * @Date
     * @Param
     **/
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
}
