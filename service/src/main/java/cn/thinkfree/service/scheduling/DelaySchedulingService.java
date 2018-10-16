package cn.thinkfree.service.scheduling;

import cn.thinkfree.database.vo.ProjectOrderVO;

import java.util.List;

/**
 * 延期相关
 *
 * @author gejiaming
 */
public interface DelaySchedulingService {

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
}
