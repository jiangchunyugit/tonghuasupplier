package cn.thinkfree.service.account;

import cn.thinkfree.database.mapper.UserRoleSetMapper;
import cn.thinkfree.database.model.UserRoleSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleSetServiceImpl implements UserRoleSetService {

    @Autowired
    UserRoleSetMapper userRoleSetMapper;

    /**
     * 保存企业角色
     *
     * @param userRoleSet
     * @return
     */
    @Transactional
    @Override
    public String saveEnterPriseRole(UserRoleSet userRoleSet) {

        userRoleSetMapper.insertSelective(userRoleSet);
        return "操作成功!";
    }
}
