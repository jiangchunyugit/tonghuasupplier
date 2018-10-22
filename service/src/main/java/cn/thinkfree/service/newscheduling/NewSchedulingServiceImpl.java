package cn.thinkfree.service.newscheduling;

import cn.thinkfree.database.mapper.ProjectBigSchedulingDetailsMapper;
import cn.thinkfree.database.mapper.ProjectBigSchedulingMapper;
import cn.thinkfree.database.model.ProjectBigScheduling;
import cn.thinkfree.database.model.ProjectBigSchedulingDetails;
import cn.thinkfree.database.model.ProjectBigSchedulingDetailsExample;
import cn.thinkfree.database.model.ProjectBigSchedulingExample;
import cn.thinkfree.database.vo.ProjectBigSchedulingDetailsVO;
import cn.thinkfree.database.vo.ProjectBigSchedulingVO;
import cn.thinkfree.service.constants.Scheduling;
import cn.thinkfree.service.utils.BaseToVoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 正常排期操作
 *
 * @author gejiaming
 * @Date 2018-09-30
 */
@Service(value = "schedulingService")
public class NewSchedulingServiceImpl implements NewSchedulingService {
    @Autowired
    private ProjectBigSchedulingDetailsMapper projectBigSchedulingDetailsMapper;

    /**
     * 获取排期信息
     *
     * @param projectNo
     * @return
     */
    @Override
    public List<ProjectBigSchedulingDetailsVO> getScheduling(String projectNo) {
        List<ProjectBigSchedulingDetails> bigList = projectBigSchedulingDetailsMapper.selectByProjectNo(projectNo, Scheduling.BASE_STATUS.getValue());
        List<ProjectBigSchedulingDetailsVO> playBigList = new ArrayList<>();
        try {
            playBigList = BaseToVoUtils.getListVo(bigList, ProjectBigSchedulingDetailsVO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playBigList;
    }

    /**
     * 添加公司施工节点
     *
     * @param projectBigSchedulingDetailsVO
     * @return
     */
    @Override
    public String saveProjectScheduling(ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO) {
        ProjectBigSchedulingDetails projectBigSchedulingDetails = null;
        try {
            projectBigSchedulingDetails = BaseToVoUtils.getVo(projectBigSchedulingDetailsVO, ProjectBigSchedulingDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        projectBigSchedulingDetails.setStatus(Scheduling.BASE_STATUS.getValue());
        projectBigSchedulingDetails.setCreateTime(new Date());
        int result = projectBigSchedulingDetailsMapper.insertSelective(projectBigSchedulingDetails);
        if (result != Scheduling.INSERT_SUCCESS.getValue()) {
            return Scheduling.INSERT_FAILD.getDescription();
        }
        return Scheduling.INSERT_SUCCESS.getDescription();
    }

    /**
     * 删除公司施工节点
     *
     * @param projectBigSchedulingDetailsVO
     * @return
     */
    @Override
    public String deleteProjectScheduling(ProjectBigSchedulingDetailsVO projectBigSchedulingDetailsVO) {
        ProjectBigSchedulingDetails projectBigSchedulingDetails = null;
        try {
            projectBigSchedulingDetails = BaseToVoUtils.getVo(projectBigSchedulingDetailsVO, ProjectBigSchedulingDetails.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        projectBigSchedulingDetails.setStatus(Scheduling.INVALID_STATUS.getValue());
        ProjectBigSchedulingDetailsExample example = new ProjectBigSchedulingDetailsExample();
        ProjectBigSchedulingDetailsExample.Criteria criteria = example.createCriteria();
        criteria.andBigSortEqualTo(projectBigSchedulingDetailsVO.getBigSort());
        criteria.andProjectNoEqualTo(projectBigSchedulingDetailsVO.getProjectNo());
        int result = projectBigSchedulingDetailsMapper.updateByExampleSelective(projectBigSchedulingDetails, example);
        if (result != Scheduling.INSERT_SUCCESS.getValue()) {
            return Scheduling.INSERT_FAILD.getDescription();
        }
        return Scheduling.INSERT_SUCCESS.getDescription();
    }


}
