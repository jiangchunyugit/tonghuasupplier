package cn.thinkfree.service.staff;

import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.mapper.CompanyUserSetMapper;
import cn.thinkfree.database.mapper.PreProjectUserRoleMapper;
import cn.thinkfree.database.model.CompanyUserSet;
import cn.thinkfree.database.model.CompanyUserSetExample;
import cn.thinkfree.database.model.PcUserInfo;
import cn.thinkfree.database.model.PreProjectUserRole;
import cn.thinkfree.database.vo.UserVO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StaffServiceImpl extends AbsLogPrinter implements StaffService {

    @Autowired
    private CompanyUserSetMapper companyUserSetMapper;

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



