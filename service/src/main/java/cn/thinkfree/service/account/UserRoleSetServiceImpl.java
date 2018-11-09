package cn.thinkfree.service.account;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.mapper.UserRoleSetMapper;
import cn.thinkfree.database.model.UserRoleExample;
import cn.thinkfree.database.model.UserRoleSet;
import cn.thinkfree.database.model.UserRoleSetExample;
import cn.thinkfree.database.vo.account.UserRoleSetSEO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserRoleSetServiceImpl extends AbsLogPrinter implements UserRoleSetService {

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
        printInfoMes("企业角色信息新增");
        userRoleSet.setCreateTime(new Date());
        userRoleSet.setIsEnable(SysConstants.YesOrNo.NO.shortVal().intValue());
        userRoleSet.setIsDel(SysConstants.YesOrNo.NO.shortVal().intValue());
        userRoleSet.setCreator(SessionUserDetailsUtil.getLoginUserName());
        userRoleSetMapper.insertSelective(userRoleSet);
        return "操作成功!";
    }

    /**
     * 查询企业角色详情
     *
     * @param id
     * @return
     */
    @Override
    public UserRoleSet findUserRoleSetVO(Integer id) {
        return userRoleSetMapper.selectByPrimaryKey(id.shortValue());
    }

    /**
     * 分页查询企业角色列表
     *
     * @param userRoleSetSEO
     * @return
     */
    @Override
    public PageInfo<UserRoleSet> pageEnterPriseRole(UserRoleSetSEO userRoleSetSEO) {
        printInfoMes("企业角色信息分页:{}",userRoleSetSEO);
        PageHelper.startPage(userRoleSetSEO.getPage(),userRoleSetSEO.getRows());
        UserRoleSetExample userRoleSetExample = new UserRoleSetExample();
        userRoleSetExample.createCriteria().andIsDelEqualTo(SysConstants.YesOrNo.NO.shortVal().intValue());
        List<UserRoleSet> list = userRoleSetMapper.selectByExample(userRoleSetExample);
        return new PageInfo<>(list);
    }

    /**
     * 编辑企业角色信息
     *
     * @param id
     * @param userRoleSet
     * @return
     */
    @Transactional
    @Override
    public String updateEnterPriseRole(Integer id, UserRoleSet userRoleSet) {
        UserRoleSet update = new UserRoleSet();
        update.setRoleName(userRoleSet.getRoleName());
        update.setRoleCode(userRoleSet.getRoleCode());
        update.setUpdateTime(new Date());
        update.setId(id.shortValue());
        userRoleSetMapper.updateByPrimaryKeySelective(update);
        return "操作成功!";
    }
}
