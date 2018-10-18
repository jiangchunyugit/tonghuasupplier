package cn.thinkfree.service.account;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.mapper.SystemRoleMapper;
import cn.thinkfree.database.mapper.SystemRolePermissionMapper;
import cn.thinkfree.database.model.SystemRole;
import cn.thinkfree.database.model.SystemRoleExample;
import cn.thinkfree.database.model.SystemRolePermission;
import cn.thinkfree.database.model.SystemRolePermissionExample;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.account.SystemRoleSEO;
import cn.thinkfree.database.vo.account.SystemRoleVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class SystemRoleServiceImpl extends AbsLogPrinter implements SystemRoleService {

    @Autowired
    SystemRoleMapper systemRoleMapper;

    @Autowired
    SystemRolePermissionMapper systemRolePermissionMapper;

    /**
     * 新增角色
     *
     * @param systemRoleVO
     * @return
     */
    @Override
    public SystemRole save(SystemRoleVO systemRoleVO) {
        printInfoMes("新增角色,信息:{}",systemRoleVO);
        SystemRole systemRole = initRole(systemRoleVO,true);

        printInfoMes("新增角色,初始化后信息{}",systemRole);
        systemRoleMapper.insertSelective(systemRole);

        printDebugMes("新增角色,保存完成");
        return systemRole;
    }

    /**
     * 分页查询角色
     *
     * @param systemRoleSEO
     * @return
     */
    @Override
    public PageInfo<SystemRole> page(SystemRoleSEO systemRoleSEO) {
        printInfoMes("角色分页,查询条件:{}",systemRoleSEO);

        SystemRoleExample systemRoleExample = new SystemRoleExample();
        SystemRoleExample.Criteria criteria = systemRoleExample.createCriteria()
                .andIsDelEqualTo(SysConstants.YesOrNo.NO.shortVal());;
        if(StringUtils.isNotBlank(systemRoleSEO.getName())){
            criteria.andNameLike("%"+ systemRoleSEO.getName() + "%");
            systemRoleExample.or().andCodeLike("%"+systemRoleSEO.getName()+"%");
        }
        if(StringUtils.isNotBlank(systemRoleSEO.getState())){
            criteria.andIsEnableEqualTo(Short.valueOf(systemRoleSEO.getState()));
        }

        PageHelper.startPage(systemRoleSEO.getPage(),systemRoleSEO.getRows());
        List<SystemRole> page = systemRoleMapper.selectSystemRoleVOByExample(systemRoleExample);
        return new PageInfo<>(page);
    }

    /**
     * 角色详情
     *
     * @param id
     * @return
     */
    @Override
    public SystemRole detail(Integer id) {


        return systemRoleMapper.selectSystemRoleVOByID(id);
    }

    /**
     * 编辑角色
     *
     * @param systemRoleVO
     * @return
     */
    @Override
    public SystemRole edit( SystemRoleVO systemRoleVO) {
        printInfoMes("编辑角色,信息:{}",systemRoleVO);
        SystemRole systemPermission = initRole(systemRoleVO,false);

        SystemRoleExample systemRoleExample = new SystemRoleExample();
        systemRoleExample.createCriteria().andIdEqualTo(systemRoleVO.getId());
        systemRoleMapper.updateByExampleSelective(systemPermission,systemRoleExample);
        printInfoMes("编辑角色,完成");
        return systemPermission;
    }

    /**
     * 更新角色权限
     *
     * @param id
     * @param permissions
     * @return
     */
    @Transactional
    @Override
    public String updateRoleByGrant(Integer id, Integer[] permissions) {

        printInfoMes("角色授权,ID:{},权限集:{}",id,permissions);
        printInfoMes("角色授权,清空旧权限");
        SystemRolePermissionExample condition = new SystemRolePermissionExample();
        condition.createCriteria().andRoleIdEqualTo(id);
        systemRolePermissionMapper.deleteByExample(condition);

        printInfoMes("角色授权,写入新权限");
        if(permissions == null || permissions.length == 0 ){
            printInfoMes("角色授权,清空权限");
            return "操作成功!";
        }

        for(Integer pid : permissions){
            SystemRolePermission saveObj = new SystemRolePermission();
            saveObj.setPermissionId(pid);
            saveObj.setRoleId(id);
            systemRolePermissionMapper.insertSelective(saveObj);
        }

        return "操作成功!";

    }

    /**
     * 修改角色状态
     *
     * @param id
     * @param state
     * @return
     */
    @Transactional
    @Override
    public String updateRoleState(Integer id, Short state) {
        printInfoMes("角色状态,ID:{},STATE:{}",id,state);
        SystemRole update = new SystemRole();
        update.setIsEnable(state);
        SystemRoleExample condition = new SystemRoleExample();
        condition.createCriteria().andIdEqualTo(id);
        systemRoleMapper.updateByExampleSelective(update,condition);
        return "操作成功!";
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public String updateRoleForDel(Integer id) {

        printInfoMes("删除角色,ID:{}",id);
        SystemRole del = new SystemRole();
        del.setIsDel(SysConstants.YesOrNo.YES.shortVal());
        SystemRoleExample condition = new SystemRoleExample();
        condition.createCriteria().andIdEqualTo(id);
        systemRoleMapper.updateByExampleSelective(del,condition);
        return "操作成功!";
    }

    private SystemRole initRole(SystemRoleVO systemRoleVO, boolean isSave) {
        SystemRole systemRole = new SystemRoleVO();
        systemRole.setName(systemRoleVO.getName());
        systemRole.setDesc(systemRoleVO.getDesc());

        Function<SystemRoleVO,Integer> sum = (s)-> Math.addExact(s.getCity(),
                                                        Math.addExact(s.getProvince(), s.getRoot()));
        // 7 全部 4 总部 2省份 1市
        systemRole.setScope(sum.apply(systemRoleVO));
        systemRole.setUpdateTime(new Date());
        if(isSave){
            UserVO user = (UserVO) SessionUserDetailsUtil.getUserDetails();
            systemRole.setCreator(user.getUserRegister().getUserId());
            systemRole.setCreateTime(new Date());
            systemRole.setIsDel(SysConstants.YesOrNo.NO.shortVal());
            systemRole.setIsEnable(UserEnabled.Enabled_false.shortVal());
            systemRole.setIsSys(SysConstants.YesOrNo.NO.shortVal());
        }
        return systemRole;
    }
}
