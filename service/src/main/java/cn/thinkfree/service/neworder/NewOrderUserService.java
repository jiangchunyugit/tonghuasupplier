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
     * @param orderNo 项目编号
     * @return 项目用户关系
     */
    List<OrderUser> findByOrderNo(String orderNo);

    /**
     * 根据项目编号与用户编号查询项目用户关系
     * @param orderNo 项目编号
     * @param userId 用户编号
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
     * @Author jiang
     * @Description 查看订单详情
     * @Date
     * @Param
     * @return
     **/
    OrderDetailsVO selectOrderDetails(String projectNo);
    /**
     * @Author jiang
     * @Description 阶段展示
     * @Date
     * @Param
     * @return
     **/
    List<StageDetailsVO> selectStageDetailsList(String projectNo);
    /**
     * @Author jiang
     * @Description 修改订单状态
     * @Date
     * @Param
     * @return
     **/
    Integer modifyOrder(OrderConfirmationVO orderConfirmationVO);

    /**
     * @Author jiang
     * @Description 分页查询施工工地
     * @Date
     * @Param
     * @return
     **/
    List<ConstructionSiteVO> querySiteDetailsByPage(ConstructionSiteVO constructionSiteVO, Integer pageNum, Integer pageSize);
    /**
     * @Author jiang
     * @Description 查询施工工地总条数
     * @Date
     * @Param
     * @return
     **/
    Integer querySiteDetailsCount(ConstructionSiteVO constructionSiteVO);
}
