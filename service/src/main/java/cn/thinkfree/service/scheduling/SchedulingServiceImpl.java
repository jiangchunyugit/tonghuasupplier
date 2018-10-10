package cn.thinkfree.service.scheduling;

import cn.thinkfree.database.mapper.ProjectBigSchedulingMapper;
import cn.thinkfree.database.model.ProjectBigScheduling;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.remote.CloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

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
    CloudService cloudService;

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
        projectBigScheduling.setSort(projectBigSchedulingVO.getSort());
        projectBigScheduling.setName(projectBigSchedulingVO.getName());
        projectBigScheduling.setRename(projectBigSchedulingVO.getRename());
        projectBigScheduling.setSquareMetreStart(projectBigSchedulingVO.getSquareMetreStart());
        projectBigScheduling.setSquareMetreEnd(projectBigSchedulingVO.getSquareMetreEnd());
        //时间如果没有配置,用默认的
        projectBigScheduling.setWorkload(projectBigSchedulingVO.getWorkload());
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
     * 同步基础大排期数据
     * @return
     */
    @Override
    public String synchronizedBaseScheduling(String companyId) {
        //从上海处或取得大排期基础数据
        Set<String> set = new TreeSet<String>();
        TreeSet<String> localSet = projectBigSchedulingMapper.selectByStatus(Scheduling.BASE_STATUS.getValue(),companyId);

        return null;
    }
}
