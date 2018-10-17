package cn.thinkfree.service.scheduling;

import cn.thinkfree.database.mapper.DesignOrderMapper;
import cn.thinkfree.database.mapper.PreProjectGuideMapper;
import cn.thinkfree.database.model.DesignOrder;
import cn.thinkfree.database.model.DesignOrderExample;
import cn.thinkfree.database.model.Project;
import cn.thinkfree.database.vo.OrderConfirmationVO;
import cn.thinkfree.database.vo.ProjectOrderVO;
import cn.thinkfree.service.constants.Scheduling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 延期相关
 *
 * @author gejiaming
 */
@Service
public class DelaySchedulingServiceImpl implements DelaySchedulingService {
    @Autowired(required = false)
    private PreProjectGuideMapper preProjectGuideMapper;
    @Autowired(required = false)
    private DesignOrderMapper designOrderMapper;

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
        designOrder.setOrderStage(orderConfirmationVO.getOrderStage());

        DesignOrderExample example = new DesignOrderExample();
        example.createCriteria().andProjectNoEqualTo(orderConfirmationVO.getProjectNo());
        return designOrderMapper.updateByExampleSelective(designOrder, example);
    }
}
