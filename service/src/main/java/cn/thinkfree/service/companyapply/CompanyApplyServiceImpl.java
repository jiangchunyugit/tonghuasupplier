package cn.thinkfree.service.companyapply;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.security.utils.MultipleMd5;
import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.constants.CompanyAuditStatus;
import cn.thinkfree.database.constants.CompanyClassify;
import cn.thinkfree.database.constants.UserRegisterType;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.cache.RedisService;
import cn.thinkfree.service.constants.CompanyApply;
import cn.thinkfree.service.constants.CompanyConstants;
import cn.thinkfree.service.event.EventService;
import cn.thinkfree.service.pcUser.PcUserInfoService;
import cn.thinkfree.service.remote.CloudService;
import cn.thinkfree.service.remote.RemoteResult;
import cn.thinkfree.service.utils.UserNoUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @author ying007
 * 公司申请
 */
@Service
public class CompanyApplyServiceImpl implements CompanyApplyService {
    @Autowired
    PcApplyInfoMapper pcApplyInfoMapper;

    @Autowired
    CloudService cloudService;

    @Autowired
    CompanyInfoMapper companyInfoMapper;

    @Autowired
    CompanyInfoExpandMapper companyInfoExpandMapper;

    @Autowired
    UserRegisterMapper userRegisterMapper;

    @Autowired
    CompanyApplyService companyApplyService;

    @Autowired
    PcCompanyFinancialMapper pcCompanyFinancialMapper;


    @Autowired
    PcUserInfoService pcUserInfoService;

    @Autowired
    RedisService redisService;

    @Autowired
    EventService eventService;

    @Autowired
    CompanyUserMapper companyUserMapper;

    @Autowired
    PcUserInfoMapper pcUserInfoMapper;

    @Autowired
    PcAuditInfoMapper pcAuditInfoMapper;

    @Autowired
    CompanyUserRoleMapper companyUserRoleMapper;

    @Autowired
    UserRoleSetMapper userRoleSetMapper;

    @Autowired
    CompanyRoleMapper companyRoleMapper;

    @Value("${depositMoney:-500000}")
    private String depositMoney;



    /**
     * 更新公司入驻状态
     * @param companyId
     * @param status
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(String companyId, String status) {
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setCompanyId(companyId);
        companyInfo.setAuditStatus(status);
        CompanyInfoExample example = new CompanyInfoExample();
        example.createCriteria().andCompanyIdEqualTo(companyId);
        int line = companyInfoMapper.updateByExampleSelective(companyInfo, example);
        if(line > 0){
            return true;
        }
        return false;
    }

    /**
     * 激活账户
     * @param userRegister
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRegister(UserRegister userRegister) {
        userRegister.setUpdateTime(new Date());
        MultipleMd5 md5 = new MultipleMd5();
        userRegister.setPassword(md5.encode(userRegister.getPassword()));
        UserRegisterExample example = new UserRegisterExample();
        example.createCriteria().andIdEqualTo(userRegister.getId()).
                andIsDeleteEqualTo(SysConstants.YesOrNo.NO.shortVal());
        int line = userRegisterMapper.updateByExampleSelective(userRegister, example);
        if(line > 0){
            return true;
        }
        return false;
    }

    @Override
    public Long countApply(String role) {
        PcApplyInfoExample example = new PcApplyInfoExample();
        //查询未办理申请数量
        example.createCriteria().andCompanyRoleLike(role).
                andTransactTypeEqualTo(SysConstants.YesOrNo.NO.shortVal());
        long line = pcApplyInfoMapper.countByExample(example);
        return line;
    }

    @Override
    public String sendMessage(String email) {
        boolean pcflag = pcUserInfoService.isEnable(email);
        if( StringUtils.isNotBlank(email)){
            boolean emailFlag = companyApplyService.checkEmail(email);
            if(emailFlag || pcflag){
                return "邮箱已被注册!";
            }
        }else{
            return "邮箱不能为空!";
        }

        redisService.saveVerificationCode(email);
        return "验证码已发送至邮箱";
    }

    /**
     * true:有结果  false:无结果
     * @param name
     * @return
     */
    @Override
    public boolean checkCompanyName(String name) {
        if(name == null || StringUtils.isBlank(name)){
            return false;
        }
        CompanyInfoExample example = new CompanyInfoExample();
        example.createCriteria().andCompanyNameEqualTo(name);
        List<CompanyInfo> companyInfos = companyInfoMapper.selectByExample(example);
        if(companyInfos.size() > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkEmail(String email) {
        if(email == null || StringUtils.isBlank(email)){
            return false;
        }
        CompanyInfoExpandExample example = new CompanyInfoExpandExample();
        example.createCriteria().andEmailEqualTo(email);
        List<CompanyInfoExpand> companyInfoExpands = companyInfoExpandMapper.selectByExample(example);
        if(companyInfoExpands.size() > 0){
            return true;
        }
        return false;
    }

    /**
     * a:添加账号---》返回公司id
     * @param roleId
     * @return
     */
    @Override
    public String generateCompanyId(String roleId) {
        String companyId = UserNoUtils.getUserNo(roleId);
        if(isEnable(companyId)){
            companyId = generateCompanyId(roleId);
        }
        return companyId;
    }



    /**
     * b:添加账号--》创建用户 发送短信
     * 注：添加账号及发送短信后申请表状态改为已办理  不显示办理按钮。。
     * 1，app注册运营添加账号：  提交：插入公司表，公司拓展表，注册表，更新申请表公司id
     * 2，运营注册：           提交：插入公司表，公司拓展表，注册表，申请表
     * @param pcApplyInfoSEO  实体里的省市公司id都是站点信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addCompanyAdmin(PcApplyInfoSEO pcApplyInfoSEO) {
        UserVO userVO = (UserVO) SessionUserDetailsUtil.getUserDetails();

        Map<String, Object> map = new HashMap<>();
        Date date = new Date();
        if (exitsEmailANDCompanyName(pcApplyInfoSEO, map)) {
            return map;
        }
        //公司id
        String companyId = pcApplyInfoSEO.getCompanyId();

        //插入账户表
        int finaLine = saveCompanyFinancial(date, companyId);

        //插入公司表
        int infoLine = saveCompanyInfo(pcApplyInfoSEO, date, companyId);

        //插入公司拓展表
        int expandLine = saveCompanyInfoExpand(pcApplyInfoSEO, date, companyId);

        //运营插入申请表  app更新申请表
        int applyLine = updateApplyInfo(pcApplyInfoSEO);

        //插入注册表
        int registerLine = saveUserRegister(pcApplyInfoSEO, date, companyId);

        List<Integer> num = new ArrayList<>();
        String[] split = pcApplyInfoSEO.getCompanyRole().split(",");
        for (String str : split) {
            //查询角色信息
            UserRoleSetExample example = new UserRoleSetExample();
            example.createCriteria().andRoleCodeEqualTo(str);
            List<UserRoleSet> userRoleSet = userRoleSetMapper.selectByExample(example);

            //插入角色表
            CompanyRole companyRole = new CompanyRole();
            int cLine = saveCompanyRole(pcApplyInfoSEO, date, str, userRoleSet, companyRole);
            if (cLine > 0) {
                num.add(1);
            }

            //插入用户角色关联表
            int uLine = saveCompanyUserRole(companyId, companyRole);
            if (uLine > 0) {
                num.add(1);
            }
        }

        //插入审批表
        String auditPersion = userVO == null ? "" : userVO.getUsername();
        String auditAccount = userVO == null ? "" : userVO.getUserRegister().getPhone();
        PcAuditInfo record = new PcAuditInfo(CompanyConstants.AuditType.ENTRY.stringVal(), "", auditPersion, CompanyConstants.AuditType.ENTRY.stringVal(), date,
                companyId, "", "", date, auditAccount);
        int line = pcAuditInfoMapper.insertSelective(record);

        //发送短信和邮件
        sendMessage(pcApplyInfoSEO);

        if (num.size() == split.length * 2 && line > 0 && infoLine > 0 && expandLine > 0 && applyLine > 0 && registerLine > 0 && finaLine > 0) {
            map.put("code", true);
            map.put("msg", "操作成功");
            return map;
        }
        map.put("code", false);
        map.put("msg", "操作失败");
        return map;
    }

    private boolean exitsEmailANDCompanyName(PcApplyInfoSEO pcApplyInfoSEO, Map<String, Object> map) {
        if(pcApplyInfoSEO != null && StringUtils.isNotBlank(pcApplyInfoSEO.getCompanyName())){
            boolean name = companyApplyService.checkCompanyName(pcApplyInfoSEO.getCompanyName());
            if(name){
                map.put("code",false);
                map.put("msg","公司名称已被注册！");
                return true;
            }
        }
        boolean pcflag = pcUserInfoService.isEnable(pcApplyInfoSEO.getEmail());
        if(pcApplyInfoSEO != null && StringUtils.isNotBlank(pcApplyInfoSEO.getEmail())){
            boolean email = companyApplyService.checkEmail(pcApplyInfoSEO.getEmail());
            if(email || pcflag){
                map.put("code",false);
                map.put("msg","邮箱已被注册！");
                return true;
            }
        }
        return false;
    }

    private int saveCompanyUserRole(String companyId, CompanyRole companyRole) {
        CompanyUserRole companyUserRole = new CompanyUserRole();
        companyUserRole.setRoleId(companyRole.getId().toString());
        companyUserRole.setUserId(companyId);
        return companyUserRoleMapper.insertSelective(companyUserRole);
    }

    private int saveCompanyRole(PcApplyInfoSEO pcApplyInfoSEO, Date date, String str, List<UserRoleSet> userRoleSet, CompanyRole companyRole) {
        companyRole.setCreateTime(date);
        companyRole.setCompanyId(pcApplyInfoSEO.getCompanyId());
        companyRole.setRoleId(Integer.parseInt(userRoleSet.get(0).getId().toString()));
        companyRole.setRoleName("公司角色");
        companyRole.setRoleType(str);
        return companyRoleMapper.insertSelective(companyRole);
    }

    private int updateApplyInfo(PcApplyInfoSEO pcApplyInfoSEO) {
        int applyLine = 0;
        //是否办理
        pcApplyInfoSEO.setTransactType(SysConstants.YesOrNo.YES.shortVal());
        //营运添加
        if (pcApplyInfoSEO.getApplyType() == 1) {
            applyLine = pcApplyInfoMapper.insertSelective(pcApplyInfoSEO);
        } else { //app前端申请
            PcApplyInfoExample example = new PcApplyInfoExample();
            example.createCriteria().andIdEqualTo(pcApplyInfoSEO.getId());
            applyLine = pcApplyInfoMapper.updateByExampleSelective(pcApplyInfoSEO, example);
        }
        return applyLine;
    }

    private int saveCompanyFinancial(Date date, String companyId) {
        PcCompanyFinancial pcCompanyFinancial = new PcCompanyFinancial();
        pcCompanyFinancial.setCompanyId(companyId);
        pcCompanyFinancial.setCreateTime(date);
        pcCompanyFinancial.setUpdateTime(date);
        return pcCompanyFinancialMapper.insertSelective(pcCompanyFinancial);
    }

    private void sendMessage(PcApplyInfoSEO pcApplyInfoSEO) {
        JSONObject object = new JSONObject();
        object.put("登录账号", pcApplyInfoSEO.getEmail());
        object.put("默认密码", "123456");
        RemoteResult<String> rs = cloudService.sendCreateAccountNotice(pcApplyInfoSEO.getContactPhone(), object.toJSONString());
        if (!rs.isComplete()) {
            throw new RuntimeException("添加账号发送短信失败");
        }

        //邮件
        RemoteResult<String> result = cloudService.sendEmail(pcApplyInfoSEO.getEmail(), "", object.toJSONString());
    }

    private int saveUserRegister(PcApplyInfoSEO pcApplyInfoSEO, Date date, String companyId) {
        int registerLine = 0;
        UserRegister userRegister = new UserRegister();
        userRegister.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
        userRegister.setRegisterTime(date);
        userRegister.setUpdateTime(date);
        userRegister.setType(UserRegisterType.Enterprise.shortVal());
        MultipleMd5 md5 = new MultipleMd5();
        if (pcApplyInfoSEO.getPassword() == null) {
            pcApplyInfoSEO.setPassword("123456");
        }
        userRegister.setPassword(md5.encode(pcApplyInfoSEO.getPassword()));

        userRegister.setPhone(pcApplyInfoSEO.getEmail());
        userRegister.setUserId(companyId);
        registerLine = userRegisterMapper.insertSelective(userRegister);
        return registerLine;
    }

    private int saveCompanyInfoExpand(PcApplyInfoSEO pcApplyInfoSEO, Date date, String companyId) {
        CompanyInfoExpand companyInfoExpand = new CompanyInfoExpand();
        companyInfoExpand.setCreateTime(date);
        companyInfoExpand.setUpdateTime(date);
        companyInfoExpand.setEmail(pcApplyInfoSEO.getEmail());
        companyInfoExpand.setContactName(pcApplyInfoSEO.getContactName());
        companyInfoExpand.setContactPhone(pcApplyInfoSEO.getContactPhone());
        companyInfoExpand.setRegisterProvinceCode(pcApplyInfoSEO.getProvinceCode());
        companyInfoExpand.setRegisterCityCode(pcApplyInfoSEO.getCityCode());
        /*companyInfoExpand.setRegisterAreaCode(pcApplyInfoSEO.getAreaCode());*/
        companyInfoExpand.setCompanyId(companyId);
        return companyInfoExpandMapper.insertSelective(companyInfoExpand);
    }

    private int saveCompanyInfo(PcApplyInfoSEO pcApplyInfoSEO, Date date, String companyId) {
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setCreateTime(date);
        companyInfo.setUpdateTime(date);
        companyInfo.setDepositMoney(Integer.parseInt(depositMoney));
        companyInfo.setCompanyId(companyId);
        companyInfo.setCompanyName(pcApplyInfoSEO.getCompanyName());
        companyInfo.setRoleId(pcApplyInfoSEO.getCompanyRole());
        companyInfo.setPhone(pcApplyInfoSEO.getContactPhone());
        companyInfo.setSiteCompanyId(pcApplyInfoSEO.getSiteCompanyId());
        //审核状态(入驻中）
        companyInfo.setAuditStatus(CompanyAuditStatus.JOINING.stringVal());
        //公司平台类型：platform_type = 0
        companyInfo.setPlatformType(CompanyConstants.PlatformType.NORMAL.shortVal());
        //is_delete = 2
        companyInfo.setIsDelete(SysConstants.YesOrNoSp.NO.shortVal());
        //公司级别：入驻公司为三级公司
        companyInfo.setCompanyClassify(CompanyClassify.TERTIARY_COMPANY.shortVal());
        return companyInfoMapper.insertSelective(companyInfo);
    }

    /**
     * 判断输入的账号是否已经注册过:true:注册过  false：没有注册过
     * @param name
     * @return
     */
    private boolean isEnable(String name) {
        //判断输入的账号是否已经注册过
        List<String> phones = userRegisterMapper.findPhoneAll();
        boolean flag = phones.contains(name);
        if(flag){
            return true;
        }
        return false;
    }

    /**
     * 添加申请记录：是否办理默认0；
     * @param pcApplyInfoSEO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addApplyInfo(PcApplyInfoSEO pcApplyInfoSEO) {
        if(StringUtils.isBlank(pcApplyInfoSEO.getEmail())){
            return false;
        }

        if(pcApplyInfoSEO != null && StringUtils.isNotBlank(pcApplyInfoSEO.getCompanyName())){
            boolean name = companyApplyService.checkCompanyName(pcApplyInfoSEO.getCompanyName());
            if(name){
                return false;
            }
        }

        String vc = redisService.validate(pcApplyInfoSEO.getEmail(), pcApplyInfoSEO.getVerifyCode());
        if("验证成功!".equals(vc)){
            PcApplyInfo pcApplyInfo = new PcApplyInfo();

            SpringBeanUtil.copy(pcApplyInfoSEO,pcApplyInfo);
            Date date = new Date();
            pcApplyInfo.setApplyDate(date);
            //是否办理
            pcApplyInfo.setTransactType(SysConstants.YesOrNo.NO.shortVal());
            //是否删除
            pcApplyInfo.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
            int line = pcApplyInfoMapper.insertSelective(pcApplyInfo);
            if(line > 0){
                return true;
            }
        }
        return false;
    }

    /**
     * 删除申请记录（修改is_delete=1) 申请类型是1：后台运营申请才可以删除
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApply(Integer id) {
        PcApplyInfo pcApplyInfo = new PcApplyInfo();
        pcApplyInfo.setId(id);
        pcApplyInfo.setIsDelete(SysConstants.YesOrNo.YES.shortVal());
//        pcApplyInfo.setApplyType(1);
        PcApplyInfoExample pcApplyInfoExample = new PcApplyInfoExample();
        pcApplyInfoExample.createCriteria().andIdEqualTo(id);
        int line = pcApplyInfoMapper.updateByExampleSelective(pcApplyInfo, pcApplyInfoExample);
        if(line > 0){
            return true;
        }
        return false;
    }

    /**
     * 根据id查询申请记录
     * @param id
     * @return
     */
    @Override
    public PcApplyInfoVo findById(Integer id) {
        return pcApplyInfoMapper.findById(id);
    }

    /**
     * 根据参数查询申请信息
     * @param companyApplySEO
     * @return
     */
    @Override
    public PageInfo<PcApplyInfoVo> findByParam(CompanyApplySEO companyApplySEO) {

        //获取登陆人信息
        UserVO userVO = (UserVO)SessionUserDetailsUtil.getUserDetails();
//        userVO.getPcUserInfo().getLevel();

        String param = companyApplySEO.getParam();
        if(StringUtils.isNotBlank(param)){
            companyApplySEO.setParam("%" + param + "%");
        }
        PageHelper.startPage(companyApplySEO.getPage(), companyApplySEO.getRows());
        List<PcApplyInfoVo> pcUserInfoVos = pcApplyInfoMapper.findByParam(companyApplySEO);
        //如果是资质变更，查询公司id，和合同编号
        CompanyListSEO companyListSEO = new CompanyListSEO();
        //解决方式不好吧！！！
        for(PcApplyInfoVo pc: pcUserInfoVos){
            if(!CompanyApply.applyThinkType.APPLYJOIN.code.toString().equals(pc.getApplyThingType())){
                companyListSEO.setCompanyId(pc.getCompanyId());
                List<CompanyListVo> companyListVoList = companyInfoMapper.list(companyListSEO);
                if(companyListVoList.size() > 0){
                    pc.setContractNumber(companyListVoList.get(0).getContractNumber());
                }
            }
        }
        PageInfo<PcApplyInfoVo> pageInfo = new PageInfo<>(pcUserInfoVos);
        return pageInfo;
    }

}
