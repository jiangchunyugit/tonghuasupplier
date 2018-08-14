package cn.thinkfree.service.user;

import cn.thinkfree.database.mapper.CompanyUserSetMapper;
import cn.thinkfree.database.mapper.UserMapper;
import cn.thinkfree.database.model.CompanyUserSet;
import cn.thinkfree.database.model.User;

import cn.thinkfree.database.vo.IndexUserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{


    @Autowired
    UserMapper userMapper;

    @Autowired
    CompanyUserSetMapper companyUserSetMapper;

    @Override
    public User findById(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void save(User user) {
        userMapper.insertSelective(user);

    }

    @Override
    public IndexUserReportVO countCompanyUser(String companyID) {
        System.out.println(companyUserSetMapper);
        List<CompanyUserSet> rs = companyUserSetMapper.selectByExample(null);
        System.out.println(rs);
        companyUserSetMapper.countCompanyUser(companyID);


        return null;
    }
}
