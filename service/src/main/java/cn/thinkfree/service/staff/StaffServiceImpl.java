package cn.thinkfree.service.staff;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.RandomNumUtils;
import cn.thinkfree.database.constants.OneTrue;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.remote.CloudService;
import cn.thinkfree.service.remote.RemoteResult;
import cn.thinkfree.service.utils.UserNoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import cn.thinkfree.database.mapper.CompanyUserSetMapper;
import cn.thinkfree.database.mapper.PreProjectUserRoleMapper;
import cn.thinkfree.database.vo.UserVO;


@Service
public class StaffServiceImpl extends AbsLogPrinter implements StaffService {

    @Autowired
    private CompanyUserSetMapper companyUserSetMapper;

    @Autowired
    UserRegisterMapper userRegisterMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    CloudService cloudService;

    @Autowired
    PreProjectCompanySetMapper preProjectCompanySetMapper;

    @Autowired
    ConsumerSetMapper consumerSetMapper;


    /**
     * 所谓五分钟
     */
    private static Long threshold ; // 300000L

    @Value("${custom.sendSMS.threshold}")
    public  void setThreshold(Long threshold) {
        StaffServiceImpl.threshold = threshold;
    }

    @Override
    public Integer deletCompanyByNo(Integer id) {
        return this.companyUserSetMapper.deleteByPrimaryKey(id);
    }


    @Override
    @Transactional
    public Integer updateCompanyWei(Integer id, String roleName) {
        PreProjectUserRole preProjectUserRole = new PreProjectUserRole();
        preProjectUserRole.setId(id);
        preProjectUserRole.setRoleId(roleName);
        return this.preProjectUserRoleMapper.updateByPrimaryKey(preProjectUserRole);
    }

    @Override
    @Transactional
    public String insetCompanyUser(CompanyUserSet companyUserSet) {
        //判断输入的手机号码是否已经注册过
        List<String> phones = companyUserSetMapper.listAlreadyUsedPhone();
        // TODO 临时增加业主手机号判断
        phones.addAll(consumerSetMapper.listAlreadyUsedPhone());
        boolean flag = phones.contains(companyUserSet.getPhone());
        if(flag){
            return "手机号码已被注册过，请更换手机号码！！";
        }
        String activationCode = RandomNumUtils.random(6);
        String userID = UserNoUtils.getUserNo(companyUserSet.getRoleId());
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();
        printInfoMes("插入员工记录表");
        companyUserSet.setBindTime(new Date());
        companyUserSet.setActivationCode(activationCode);
        companyUserSet.setIsBind(OneTrue.YesOrNo.NO.shortVal());
        companyUserSet.setIsJob(OneTrue.YesOrNo.YES.shortVal());
        companyUserSet.setUserId(userID);
        companyUserSet.setCompanyId(userVO.getCompanyID());

        companyUserSetMapper.insertSelective(companyUserSet);

        printInfoMes("插入用户信息表");
        UserInfo userInfo = new UserInfo();
        userInfo.setCompanyId(userVO.getCompanyID());
        userInfo.setUserId(userID);
        userInfo.setPhone(companyUserSet.getPhone());
        userInfo.setCreateTime(new Date());
        // 该处是因为 服务组使用是否不统一
        // 已授权为2 未授权为1
        userInfo.setIsAuth(SysConstants.YesOrNo.YES.shortVal());
        userInfo.setProvinceCode(Short.valueOf(userVO.getPcUserInfo().getProvince()));
        userInfo.setCityCode(Short.valueOf(userVO.getPcUserInfo().getCity()));
        userInfo.setAreaCode(Integer.valueOf(userVO.getPcUserInfo().getArea()));
        userInfo.setRoleId(companyUserSet.getRoleId());
        userInfo.setName(companyUserSet.getName());
        userInfoMapper.insertSelective(userInfo);

        RemoteResult<String> rs = cloudService.sendSms(companyUserSet.getPhone(), activationCode);
        if(!rs.isComplete())throw new RuntimeException("总有你想不到的意外");
        return "操作成功!";
    }

    /**
     * 再次邀请用户
     *
     * @param userID
     * @return
     */
    @Transactional
    @Override
    public String reInvitation(String userID) {

        CompanyUserSetExample companyUserSetExample = new CompanyUserSetExample();
        companyUserSetExample.createCriteria().andUserIdEqualTo(userID);
        List<CompanyUserSet> companyUserSets = companyUserSetMapper.selectByExample(companyUserSetExample);

        if(companyUserSets.isEmpty() && companyUserSets.size() > 1){
            return "数据记录不符合规则,无法邀请";
        }
        CompanyUserSet companyUserSet = companyUserSets.get(0);
        if(SysConstants.YesOrNo.YES.shortVal().equals(companyUserSet.getIsBind())){
            return "员工已激活";
        }

        Date lastBindTime = companyUserSet.getBindTime();
        String activeCode = RandomNumUtils.random(6);
        if(lastBindTime != null){
            Long ms=lastBindTime.getTime();
            Long now = new Date().getTime();
            if((now-ms) > threshold){
                CompanyUserSet update = new CompanyUserSet();
                update.setBindTime(new Date());
                update.setActivationCode(activeCode);
                companyUserSetMapper.updateByExampleSelective(update,companyUserSetExample);
            }else{
                return "邀请频率过高,请稍后重试";
            }
        }else{
            CompanyUserSet update = new CompanyUserSet();
            update.setBindTime(new Date());
            update.setActivationCode(activeCode);
            companyUserSetMapper.updateByExampleSelective(update,companyUserSetExample);
        }
        RemoteResult<String> rs = cloudService.sendSms(companyUserSet.getPhone(), activeCode);
        if(!rs.isComplete())throw  new RuntimeException("超乎你的想象");
        return "操作成功";
    }


    @Autowired
    private PreProjectUserRoleMapper preProjectUserRoleMapper;


    @Override
    public List<PreProjectUserRole> queryRole() {
        return this.preProjectUserRoleMapper.selectByExample(null);
    }

    @Override
    public CompanyUserSet queryCompanyUser(Integer id) {
        return this.companyUserSetMapper.selectByPrimaryKey(id);
    }

    /**
     *
     * 员工列表，条件查询
     * @param staffSEO
     * @return
     */
    @Override
    public PageInfo<StaffsVO> queryStaffList(StaffSEO staffSEO) {
        if(StringUtils.isNotBlank(staffSEO.getName())){
            String name = staffSEO.getName();
            staffSEO.setName("%"+name+"%");
        }
        if(StringUtils.isNotBlank(staffSEO.getPhone())){
            String phone = staffSEO.getPhone();
            staffSEO.setPhone("%"+phone+"%");
        }
        PageHelper.startPage(staffSEO.getPage(), staffSEO.getRows());
        List<StaffsVO> staffsVOS = companyUserSetMapper.findStaffByParam(staffSEO);
        return new PageInfo<>(staffsVOS);
    }

    /**
     * 判断员工是否有进行中的项目
     * @param userId
     * @return
     */
    @Override
    public boolean updateDelCompanyUser(String userId) {
        if(isJob(userId)){
            return true;
        }
        return false;
    }

    /**
     * 判断员工是否有在建项目 true:有项目  false：无项目
     * @param userId
     * @return
     */
    public boolean isJob(String userId){
        IndexProjectReportVO indexProjectReportVO = preProjectCompanySetMapper.countProjectForPerson(userId);

        if(indexProjectReportVO != null && indexProjectReportVO.getWorking() > 0){
            return true;
        }
        return false;
    }
    /**
     * 移除员工  修改isJob字段  userRegister 修改isDelete字段
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public boolean updateIsJob(String userId){
        //修改注册表is_delete
        UserRegister userRegister = new UserRegister();
        userRegister.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
        userRegister.setUserId(userId);
        UserRegisterExample example = new UserRegisterExample();
        example.createCriteria().andUserIdEqualTo(userId);
        int line = userRegisterMapper.updateByExampleSelective(userRegister, example);
        //修改companyUserSet表is_job
        int setLine = companyUserSetMapper.updateIsJob(userId);
        if(line > 0 && setLine > 0){
            return true;
        }
        return false;
    }

    @Override
    public StaffsVO detail(String userId) {
        return companyUserSetMapper.findByUserId(userId);
    }

    /**
     * 修改岗位
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    @Transactional
    public String updateRole(String userId, String roleId) {
        if(isJob(userId)){
            return "该员工还有进行中的项目请先移交项目后，再修改岗位";
        }
        //companyUserSet表
        CompanyUserSet companyUserSet = new CompanyUserSet();
        companyUserSet.setUserId(userId);
        companyUserSet.setRoleId(roleId);
        CompanyUserSetExample example = new CompanyUserSetExample();
        example.createCriteria().andUserIdEqualTo(userId);
        int line = companyUserSetMapper.updateByExampleSelective(companyUserSet,example);

        //userInfo表
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setRoleId(roleId);
        UserInfoExample example1 = new UserInfoExample();
        example1.createCriteria().andUserIdEqualTo(userId);
        int infoLine = userInfoMapper.updateByExampleSelective(userInfo, example1);
        if(line > 0 && infoLine > 0){
            return "操作成功";
        }
        return "操作失败";
    }


}



