package cn.thinkfree.service.platform.designer.impl;

import cn.thinkfree.core.constants.DesignStateEnum;
import cn.thinkfree.database.mapper.DesignOrderMapper;
import cn.thinkfree.database.mapper.ProjectMapper;
import cn.thinkfree.database.mapper.ReserveProjectMapper;
import cn.thinkfree.database.model.DesignOrder;
import cn.thinkfree.database.model.Project;
import cn.thinkfree.database.model.ReserveProject;
import cn.thinkfree.database.model.ReserveProjectExample;
import cn.thinkfree.service.platform.designer.ReserveOrderService;
import cn.thinkfree.service.platform.designer.vo.PageVo;
import cn.thinkfree.service.utils.DateUtils;
import cn.thinkfree.service.utils.OrderNoUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author xusonghui
 * 预定的接口
 */
@Service
public class ReserveOrderServiceImpl implements ReserveOrderService {
    @Autowired
    private ReserveProjectMapper reserveProjectMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private DesignOrderMapper designOrderMapper;

    /**
     * 创建设计订单
     *
     * @param ownerName 业主姓名
     * @param phone     业主手机号
     * @param address   地址
     * @param source    来源
     * @param style     装修风格
     * @param budget    装修预算
     * @param acreage   装修面积
     */
    @Override
    public void createReserveOrder(String ownerName, String phone, String address, int source,
                                   int style, String budget, String acreage, String userId, String companyId) {
        // TODO 待转换订单创建
        ReserveProject reserveProject = new ReserveProject();
        reserveProject.setOwnerName(ownerName);
        reserveProject.setPhone(phone);
        reserveProject.setAddress(address);
        reserveProject.setSource(source);
        reserveProject.setStyle(style);
        reserveProject.setBudget(budget);
        reserveProject.setAcreage(acreage);
        reserveProject.setCreateUserId(userId);
        reserveProject.setCompanyId(companyId);
        //1待转换，2已转换，3业主取消，4其他
        reserveProject.setState(1);
        reserveProject.setReserveNo(OrderNoUtils.getNo("REVO"));
        reserveProject.setReserveTime(new Date());
        reserveProjectMapper.insertSelective(reserveProject);
    }

    @Override
    public void closeReserveOrder(String reserveNo, int state, String reason) {
        if (state != 3 && state != 4) {
            throw new RuntimeException("无效的状态");
        }
        ReserveProjectExample reserveProjectExample = new ReserveProjectExample();
        reserveProjectExample.createCriteria().andReserveNoEqualTo(reserveNo);
        List<ReserveProject> reserveProjects = reserveProjectMapper.selectByExample(reserveProjectExample);
        if (reserveProjects.isEmpty()) {
            throw new RuntimeException("无效的订单");
        }
        ReserveProject reserveProject = new ReserveProject();
        reserveProject.setState(state);
        reserveProject.setReason(reason);
        reserveProjectMapper.updateByExample(reserveProject, reserveProjectExample);
    }

    @Override
    public PageVo<List<ReserveProject>> queryReserveOrder(String ownerName, String phone, int pageSize, int pageIndex) {
        ReserveProjectExample reserveProjectExample = new ReserveProjectExample();
        ReserveProjectExample.Criteria criteria = reserveProjectExample.createCriteria();
        if (StringUtils.isNotBlank(ownerName)) {
            criteria.andOwnerNameLike("%" + ownerName + "%");
        }
        if (StringUtils.isNotBlank(phone)) {
            criteria.andPhoneLike("%" + phone + "%");
        }
        long total = reserveProjectMapper.countByExample(reserveProjectExample);
        PageHelper.startPage(pageIndex - 1, pageSize);
        List<ReserveProject> reserveProjects = reserveProjectMapper.selectByExample(reserveProjectExample);
        PageVo<List<ReserveProject>> pageVo = new PageVo<>();
        pageVo.setData(reserveProjects);
        pageVo.setTotal(total);
        pageVo.setPageSize(pageSize);
        return pageVo;
    }

    @Override
    public ReserveProject queryReserveOrderByNo(String reserveNo) {
        ReserveProjectExample reserveProjectExample = new ReserveProjectExample();
        reserveProjectExample.createCriteria().andReserveNoEqualTo(reserveNo);
        List<ReserveProject> reserveProjects = reserveProjectMapper.selectByExample(reserveProjectExample);
        if (reserveProjects.isEmpty()) {
            throw new RuntimeException("无效的订单");
        }
        return reserveProjects.get(0);
    }

    /**
     * @param reserveNo        待转换订单编号
     * @param companyId        所属公司ID
     * @param source           订单来源
     * @param huxing           房屋户型，1小区房，2别墅，3复式，4其他
     * @param roomNum          房屋个数
     * @param officeNum        客厅个数
     * @param toiletNum        卫生间个数
     * @param address          装修地址
     * @param addressDetail    装修详细地址
     * @param style            装修风格
     * @param area             建筑面积
     * @param houseType        房屋类型，1新房，2老房
     * @param peopleNum        常住人口
     * @param planStartTime    计划装修开始时间
     * @param planEndTime      计划装修结束时间
     * @param decorationBudget 装修预算
     * @param balconyNum       阳台个数
     * @param ownerId          业主ID
     * @param designerId       设计师ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createProject(String reserveNo, String companyId, int source, int huxing, int roomNum, int officeNum, int toiletNum,
                              String address, String addressDetail, int style, int area, int houseType, int peopleNum, String planStartTime,
                              String planEndTime, int decorationBudget, int balconyNum, String ownerId, String designerId) {
        Project project = new Project();
        project.setCompanyId(companyId);
        project.setProjectNo(OrderNoUtils.getNo("PN"));
        project.setStatus(1);
        project.setStage(1);
        project.setCreateTime(new Date());
        project.setReleaseTime(new Date());
        project.setOrderSource(source);
        project.setHouseHuxing(huxing);
        project.setHouseRoom(roomNum);
        project.setHouseOffice(officeNum);
        project.setHouseToilet(toiletNum);
        project.setAddress(address);
        project.setAddressDetail(addressDetail);
        project.setStatus(style);
        project.setArea(area);
        project.setProjectNo(peopleNum + "");
        project.setPlanStartTime(DateUtils.strToDate(planStartTime));
        project.setPlanEndTime(DateUtils.strToDate(planEndTime));
        project.setDecorationBudget(decorationBudget);
        project.setOwnerId(ownerId);
        project.setBalcony(balconyNum);
        projectMapper.insertSelective(project);
        DesignOrder designOrder = new DesignOrder();
        designOrder.setProjectNo(project.getProjectNo());
        designOrder.setCreateTime(new Date());
        designOrder.setOrderNo(OrderNoUtils.getNo("DO"));
        designOrder.setOrderStage(DesignStateEnum.STATE_1.getState());
        designOrder.setAppointmentTime(new Date());
        designOrder.setStatus(1);
        designOrder.setStyleType(style + "");
        if(designerId != null && designerId.length() > 0){
            designOrder.setUserId(designerId);
        }
        designOrderMapper.insertSelective(designOrder);
        //TODO 待创建施工订单
        if (StringUtils.isBlank(reserveNo)) {
            return;
        }
        ReserveProjectExample reserveProjectExample = new ReserveProjectExample();
        reserveProjectExample.createCriteria().andReserveNoEqualTo(reserveNo);
        ReserveProject reserveProject = new ReserveProject();
        reserveProject.setProjectNo(project.getProjectNo());
        reserveProject.setDesignOrderNo(designOrder.getOrderNo());
        reserveProject.setChangeTime(new Date());
        reserveProject.setState(2);
        reserveProjectMapper.updateByExample(reserveProject, reserveProjectExample);
    }
}
