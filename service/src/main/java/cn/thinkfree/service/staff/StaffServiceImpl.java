package cn.thinkfree.service.staff;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.core.utils.RandomNumUtils;
import cn.thinkfree.database.mapper.CompanyUserSetMapper;
import cn.thinkfree.database.mapper.PreProjectUserRoleMapper;
import cn.thinkfree.database.model.CompanyUserSet;
import cn.thinkfree.database.model.CompanyUserSetExample;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.PreProjectUserRole;
import cn.thinkfree.database.vo.UserVO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StaffServiceImpl extends AbsLogPrinter implements StaffService {

    @Autowired
    private CompanyUserSetMapper companyUserSetMapper;

    /**
     * 所谓五分钟
     */
    @Value("${custom.sendSMS.threshold}")
    private static Long threshold ; // 300000L

    @Override
    public Integer deletCompanyByNo(Integer id) {
        return this.companyUserSetMapper.deleteByPrimaryKey(id);
    }


    @Override
    public Integer updateCompanyWei(Integer id, String roleName) {
        PreProjectUserRole preProjectUserRole = new PreProjectUserRole();
        preProjectUserRole.setId(id);
        preProjectUserRole.setRoleId(roleName);
        return this.preProjectUserRoleMapper.updateByPrimaryKey(preProjectUserRole);
    }

    @Override
    public Integer insetCompanyUser(CompanyUserSet companyUserSet) {


        return this.companyUserSetMapper.insertSelective(companyUserSet);
    }

    /*
    * 删除员工逻辑删除
    * */
    @Override
    public Integer updateDelCompanyUser(Integer id) {

        return this.companyUserSetMapper.updateDelCompanyUser(id);
    }

    /**
     * 再次邀请用户
     *
     * @param userID
     * @return
     */
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

        if(lastBindTime != null){
            Long ms=lastBindTime.getTime();
            Long now = new Date().getTime();
            if((now-ms) > threshold){
                CompanyUserSet update = new CompanyUserSet();
                update.setBindTime(new Date());
                update.setActivationCode(RandomNumUtils.random(6));
                companyUserSetMapper.updateByExampleSelective(update,companyUserSetExample);
            }else{
                return "邀请频率过高,请稍后重试";
            }
        }else{
            CompanyUserSet update = new CompanyUserSet();
            update.setBindTime(new Date());
            update.setActivationCode(RandomNumUtils.random(6));
            companyUserSetMapper.updateByExampleSelective(update,companyUserSetExample);
        }
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

    @Override
    public List<CompanyUserSet> queryStaffList(Integer page, Integer rows, String name, String phone, Integer isBind) {
        PageHelper.startPage(page, rows);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("phone", phone);
        map.put("isBind", isBind);
        return this.companyUserSetMapper.queryStaffList(map);
    }
}



