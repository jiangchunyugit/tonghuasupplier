package cn.thinkfree.service.scheduling;

import cn.thinkfree.database.model.OrderUser;

import java.util.List;

/**
 * 项目用户关系服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/18 11:37
 */
public interface OrderUserService {

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
}
