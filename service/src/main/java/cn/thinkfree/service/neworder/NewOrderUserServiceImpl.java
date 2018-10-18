package cn.thinkfree.service.neworder;

import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.DesignOrder;
import cn.thinkfree.database.model.DesignOrderExample;
import cn.thinkfree.database.model.OrderUser;
import cn.thinkfree.database.model.OrderUserExample;
import cn.thinkfree.database.vo.OrderConfirmationVO;
import cn.thinkfree.database.vo.OrderDetailsVO;
import cn.thinkfree.database.vo.ProjectOrderVO;
import cn.thinkfree.database.vo.StageDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired(required = false)
    private PreProjectGuideMapper preProjectGuideMapper;
    @Autowired(required = false)
    private DesignOrderMapper designOrderMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ConstructionOrderMapper constructionOrderMapper;

    /**
     * @return
     * @Author jiang
     * @Description 分页查询项目派单
     * @Date
     * @Param
     **/
    @Override
    public List<ProjectOrderVO> queryProjectOrderByPage(ProjectOrderVO projectOrderVO, Integer pageNum, Integer pageSize) {
        projectOrderVO.setStatus(1);
        return designOrderMapper.selectProjectOrderByPage(projectOrderVO, pageNum, pageSize);
    }

    /**
     * @return
     * @Author jiang
     * @Description 查询项目派单总条数
     * @Date
     * @Param
     **/
    @Override
    public Integer queryProjectOrderCount(ProjectOrderVO projectOrderVO) {
        projectOrderVO.setStatus(1);
        return designOrderMapper.selectProjectOrderCount(projectOrderVO);
    }

    /**
     * @return
     * @Author jiang
     * @Description 订单确认接口
     * @Date
     * @Param orderConfirmationVO
     **/
    @Override
    public Integer updateorderConfirmation(OrderConfirmationVO orderConfirmationVO) {
        DesignOrder designOrder = new DesignOrder();
        designOrder.setCompanyId(orderConfirmationVO.getCompanyId());
        designOrder.setOrderStage(orderConfirmationVO.getOrderStage().intValue());

        DesignOrderExample example = new DesignOrderExample();
        example.createCriteria().andProjectNoEqualTo(orderConfirmationVO.getProjectNo());
        return designOrderMapper.updateByExampleSelective(designOrder, example);
    }

    /**
     * @Author jiang
     * @Description 查看订单详情
     * @Date
     * @Param
     * @return
     **/
    @Override
    public OrderDetailsVO selectOrderDetails(String projectNo) {
        OrderDetailsVO orderDetailsVO = new OrderDetailsVO();
        orderDetailsVO.setProjectNo(projectNo);
        orderDetailsVO.setStatus(1);

        return projectMapper.selectOrderDetails(projectNo,orderDetailsVO.getStatus());
    }
    /**
     * @Author jiang
     * @Description 阶段展示
     * @Date
     * @Param
     * @return
     **/
    @Override
    public List<StageDetailsVO> selectStageDetailsList(String projectNo) {
        StageDetailsVO stageDetailsVO = new StageDetailsVO();
        stageDetailsVO.setType(3);//查询的是项目阶段 (1,设计订单 2,施工订单 3,项目)

        return constructionOrderMapper.selectStageDetailsList(projectNo,stageDetailsVO.getType());
    }
}
