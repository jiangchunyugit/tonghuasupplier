package cn.thinkfree.service.companyapply;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.utils.MultipleMd5;
import cn.thinkfree.core.utils.RandomNumUtils;
import cn.thinkfree.core.utils.SpringBeanUtil;
import cn.thinkfree.database.constants.CompanyAuditStatus;
import cn.thinkfree.database.constants.CompanyClassify;
import cn.thinkfree.database.mapper.CompanyInfoExpandMapper;
import cn.thinkfree.database.mapper.CompanyInfoMapper;
import cn.thinkfree.database.mapper.PcApplyInfoMapper;
import cn.thinkfree.database.mapper.UserRegisterMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.*;
import cn.thinkfree.service.constants.CompanyConstants;
import cn.thinkfree.database.constants.UserRegisterType;
import cn.thinkfree.service.remote.CloudService;
import cn.thinkfree.service.utils.UserNoUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


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
    public void sendMessage(String email) {
        String activeCode = RandomNumUtils.random(4);
        //TODO 发送邮件
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
    public boolean addCompanyAdmin(PcApplyInfoSEO pcApplyInfoSEO) {
        Date date = new Date();
        //公司id
//        String companyId = generateCompanyId(pcApplyInfoSEO.getCompanyRole());
        String companyId = pcApplyInfoSEO.getCompanyId();
        //插入公司表

        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setCreateTime(date);
        companyInfo.setUpdateTime(date);
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
        int infoLine = companyInfoMapper.insertSelective(companyInfo);

        //插入公司拓展表
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
        int expandLine = companyInfoExpandMapper.insertSelective(companyInfoExpand);

        //运营插入申请表  app更新申请表
        int applyLine = 0;
        //是否办理
        pcApplyInfoSEO.setTransactType(SysConstants.YesOrNo.YES.shortVal());
        //营运添加
        if(pcApplyInfoSEO.getApplyType() == 1){
            applyLine = pcApplyInfoMapper.insertSelective(pcApplyInfoSEO);
        }else{ //app前端申请
            PcApplyInfoExample example = new PcApplyInfoExample();
            example.createCriteria().andIdEqualTo(pcApplyInfoSEO.getId());
            applyLine = pcApplyInfoMapper.updateByExampleSelective(pcApplyInfoSEO, example);
        }

        //TODO 插入pc_user_info？？？
        //插入注册表
        UserRegister userRegister = new UserRegister();
        userRegister.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
        userRegister.setRegisterTime(date);
        userRegister.setUpdateTime(date);
        userRegister.setType(UserRegisterType.Enterprise.shortVal());
        MultipleMd5 md5 = new MultipleMd5();
        if(pcApplyInfoSEO.getPassword() == null){
            pcApplyInfoSEO.setPassword("123456");
        }
        userRegister.setPassword(md5.encode(pcApplyInfoSEO.getPassword()));

        userRegister.setPhone(pcApplyInfoSEO.getContactPhone());
        userRegister.setUserId(companyId);
        int registerLine = userRegisterMapper.insertSelective(userRegister);

        //TODO：添加账号发送短信 and 发送邮件

        if(infoLine > 0 && expandLine > 0 && applyLine> 0 && registerLine > 0){
            return true;
        }
        return false;
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
        //TODO 校验验证码

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
        String param = companyApplySEO.getParam();
        if(StringUtils.isNotBlank(param)){
            companyApplySEO.setParam("%" + param + "%");
        }
        PageHelper.startPage(companyApplySEO.getPage(), companyApplySEO.getRows());
        List<PcApplyInfoVo> pcUserInfoVos = pcApplyInfoMapper.findByParam(companyApplySEO);
        PageInfo<PcApplyInfoVo> pageInfo = new PageInfo<>(pcUserInfoVos);
        return pageInfo;
    }

}
