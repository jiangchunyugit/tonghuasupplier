package cn.thinkfree.service.scheduling;

import cn.thinkfree.core.constants.ResultMessage;
import cn.thinkfree.database.mapper.ProjectBigSchedulingDetailsMapper;
import cn.thinkfree.database.mapper.ProjectBigSchedulingMapper;
import cn.thinkfree.database.model.ProjectBigScheduling;
import cn.thinkfree.database.model.ProjectBigSchedulingDetails;
import cn.thinkfree.database.model.ProjectBigSchedulingDetailsExample;
import cn.thinkfree.database.model.ProjectBigSchedulingExample;
import cn.thinkfree.database.vo.BigSchedulingDetailsVo;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.service.constants.Scheduling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    @Autowired
    private ProjectBigSchedulingDetailsMapper projectBigSchedulingDetailsMapper;

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

    /**
     * 获取排期信息
     * @param projectNo
     * @return
     */
    @Override
    public List<ProjectBigSchedulingDetails> getScheduling(String projectNo) {
        List<ProjectBigSchedulingDetails> bigList = projectBigSchedulingDetailsMapper.selectByProjectNo(projectNo,Scheduling.BASE_STATUS.getValue());
        return bigList;
    }
}
