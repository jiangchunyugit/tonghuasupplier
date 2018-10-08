package cn.thinkfree.service.scheduling;

import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.ProjectBigSchedulingMapper;
import cn.thinkfree.database.model.ProjectBigScheduling;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.service.constants.Scheduling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 正常排期操作
 *
 * @author gejiaming
 * @Date 2018-09-30
 */
@Service(value = "schedulingService")
public class SchedulingServiceImpl implements SchedulingService {
    @Autowired(required = false)
    private ProjectBigSchedulingMapper projectBigSchedulingMapper;

    /**
     * 项目列表
     *
     * @param companyId
     * @return
     */
    @Override
    public ProjectBigSchedulingVO selectProjectBigSchedulingByCompanyId(String companyId) {
        ProjectBigSchedulingVO projectBigSchedulingVO = projectBigSchedulingMapper.selectProjectBigSchedulingByCompanyId(companyId);
        return projectBigSchedulingVO;
    }

    /**
     *添加公司施工节点
     *
     * @param projectBigSchedulingVO
     * @return
     */
    @Override
    public String saveProjectScheduling(ProjectBigSchedulingVO projectBigSchedulingVO) {
        ProjectBigScheduling projectBigScheduling = new ProjectBigScheduling();
        projectBigScheduling.setCompanyId(projectBigSchedulingVO.getCompanyId());
        projectBigScheduling.setName(projectBigSchedulingVO.getName());
        projectBigScheduling.setRename(projectBigSchedulingVO.getRename());
        projectBigScheduling.setSort(projectBigSchedulingVO.getSort());
        projectBigScheduling.setStatus(Scheduling.BASE_STATUS.getValue());
        projectBigScheduling.setCreateTime(new Date());
        int result = projectBigSchedulingMapper.insertSelective(projectBigScheduling);
        if(result != Scheduling.INSERT_SUCCESS.getValue()){
            return Scheduling.INSERT_FAILD.getDescription();
        }
        return Scheduling.INSERT_SUCCESS.getDescription();
    }

    /**
     * 删除公司施工节点
     *
     * @param projectBigSchedulingVO
     * @return
     */
    @Override
    public String deleteProjectScheduling(ProjectBigSchedulingVO projectBigSchedulingVO) {
        ProjectBigScheduling projectBigScheduling = new ProjectBigScheduling();
        projectBigScheduling.setCompanyId(projectBigSchedulingVO.getCompanyId());
        projectBigScheduling.setSort(projectBigSchedulingVO.getSort());
        projectBigScheduling.setStatus(Scheduling.INVALID_STATUS.getValue());
        int result = projectBigSchedulingMapper.updateByProjectBigScheduling(projectBigScheduling);
        if(result != Scheduling.INSERT_SUCCESS.getValue()){
            return Scheduling.INSERT_FAILD.getDescription();
        }
        return Scheduling.INSERT_SUCCESS.getDescription();
    }
}
