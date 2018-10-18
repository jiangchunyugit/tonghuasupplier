package cn.thinkfree.service.neworder;

import cn.thinkfree.database.mapper.OrderUserMapper;
import cn.thinkfree.database.model.OrderUser;
import cn.thinkfree.database.model.OrderUserExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目用户关系服务层
 *
 * @author song
 * @version 1.0
 * @date 2018/10/18 11:37
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class NewOrderUserServiceImpl implements NewOrderUserService {

    @Resource
    private OrderUserMapper orderUserMapper;

    @Override
    public List<OrderUser> findByOrderNo(String orderNo) {
        OrderUserExample example = new OrderUserExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        return orderUserMapper.selectByExample(example);
    }

    @Override
    public List<OrderUser> findByOrderNoAndUserId(String orderNo, String userId) {
        OrderUserExample example = new OrderUserExample();
        example.createCriteria().andOrderNoEqualTo(orderNo).andUserIdEqualTo(userId);
        return orderUserMapper.selectByExample(example);
    }
}
