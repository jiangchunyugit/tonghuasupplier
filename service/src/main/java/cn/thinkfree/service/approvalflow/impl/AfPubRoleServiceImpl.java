package cn.thinkfree.service.approvalflow.impl;

import cn.thinkfree.database.mapper.AfPubRoleMapper;
import cn.thinkfree.database.model.AfPubRole;
import cn.thinkfree.database.model.AfPubRoleExample;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.service.approvalflow.AfPubRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author song
 * @version 1.0
 * @date 2018/10/25 15:51
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AfPubRoleServiceImpl implements AfPubRoleService {

    @Resource
    private AfPubRoleMapper pubRoleMapper;
    @Override
    public List<AfPubRole> findByConfigLogNo(String configLogNo) {
        AfPubRoleExample example = new AfPubRoleExample();
        example.createCriteria().andConfigLogNoEqualTo(configLogNo);
        return pubRoleMapper.selectByExample(example);
    }

    @Override
    public List<UserRoleSet> findByConfigLogNo(String configLogNo, List<UserRoleSet> allRoles) {
        List<AfPubRole> pubRoles = findByConfigLogNo(configLogNo);
        return getRoles(pubRoles, allRoles);
    }

    private List<UserRoleSet> getRoles(List<AfPubRole> pubRoles, List<UserRoleSet> allRoles) {
        List<UserRoleSet> roles = new ArrayList<>();
        if (pubRoles != null) {
            for (AfPubRole pubRole : pubRoles) {
                UserRoleSet role = null;
                for (UserRoleSet record : allRoles) {
                    if (record.getRoleCode().equals(pubRole.getRoleId())) {
                        role = record;
                        break;
                    }
                }
                if (role == null) {
                    // TODO
                    throw new RuntimeException();
                } else {
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    @Override
    public void create(String configLogNo, List<UserRoleSet> roles) {
        if (roles != null) {
            AfPubRole pubRole;
            for (int index = 0; index < roles.size(); index++){
                pubRole = new AfPubRole();
                pubRole.setConfigLogNo(configLogNo);
                // TODO
//                approvalRole.setCompanyNo();
//                approvalRole.setScheduleSort();
                pubRole.setRoleId(roles.get(index).getRoleCode());
                insert(pubRole);
            }
        }
    }

    private void insert(AfPubRole pubRole) {
        pubRoleMapper.insertSelective(pubRole);
    }
}
