package cn.thinkfree.service.account;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.constants.RegionsRoleLevel;
import cn.thinkfree.database.constants.RoleScope;
import cn.thinkfree.database.constants.UserEnabled;
import cn.thinkfree.database.constants.UserLevel;
import cn.thinkfree.database.mapper.SystemRoleMapper;
import cn.thinkfree.database.mapper.SystemRolePermissionMapper;
import cn.thinkfree.database.mapper.SystemUserStoreMapper;
import cn.thinkfree.database.mapper.SystemUserStoreRoleMapper;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.account.RegionsRoleVO;
import cn.thinkfree.database.vo.account.SystemRoleSEO;
import cn.thinkfree.database.vo.account.SystemRoleVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SystemRoleServiceImpl extends AbsLogPrinter implements SystemRoleService {

    @Autowired
    SystemRoleMapper systemRoleMapper;

    @Autowired
    SystemRolePermissionMapper systemRolePermissionMapper;

    @Autowired
    SystemUserStoreRoleMapper systemUserStoreRoleMapper;

    @Autowired
    SystemUserStoreMapper systemUserStoreMapper;

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
        systemRoleExample.setOrderByClause(" pc_system_role.create_time desc");
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

    /**
     * 根据适用范围查询角色名称
     *
     * @param scope
     * @return
     */
    @Override
    public List<SystemRole> listRoleByScope(Integer scope) {
        SystemRoleExample systemRoleExample = new SystemRoleExample();
        systemRoleExample.createCriteria()
                .andIsDelEqualTo(SysConstants.YesOrNo.NO.shortVal())
                .andIsEnableEqualTo(UserEnabled.Enabled_true.shortVal())
                .andScopeIn(convertScope(scope));
        return systemRoleMapper.selectByExample(systemRoleExample);

    }

    /**
     * 查询区域角色
     *
     * @param id    账号主键
     * @param rid   区域主键
     * @param level 区域级别
     * @return
     */
    @Override
    public List<SystemRole> listRoleByRegions(String id, String rid, String level) {
        SystemUserStoreRoleExample condition = new SystemUserStoreRoleExample();
        condition.createCriteria().andUserIdEqualTo(id).andUserStroeIdEqualTo(Integer.parseInt(rid));
        List<SystemUserStoreRole> systemUserStoreRoles = systemUserStoreRoleMapper.selectByExample(condition);

        if(systemUserStoreRoles.isEmpty()){
            return listRoleByScope(Integer.parseInt(level));
        }else{
            List<SystemRole> systemRoles = listRoleByScope(Integer.parseInt(level));
            systemUserStoreRoles.forEach(s->{
                Optional<SystemRole> isHas = systemRoles.stream().filter(role -> role.getId().equals(s.getRoleId())).findFirst();
                if(isHas.isPresent()){
                    SystemRoleVO vo= (SystemRoleVO) isHas.get();
                    vo.setIsGrant(SysConstants.YesOrNo.YES.val.toString());
                }
            });
        }
        return Collections.emptyList();
    }

    /**
     * 授权区域角色
     *
     * @param regionsRoleVO 区域角色信息
     * @return
     */
    @Transactional
    @Override
    public String authRegionsRole(RegionsRoleVO regionsRoleVO) {


        printInfoMes("授权区域角色:{}",regionsRoleVO);
        SystemUserStore systemUserStore = systemUserStoreMapper.selectByPrimaryKey(Integer.parseInt(regionsRoleVO.getUserStoreID()));

        printInfoMes("授权区域角色,清理旧角色,{}",regionsRoleVO.getUserStoreID());
        cleanUserStoreRole(regionsRoleVO,systemUserStore);

        insertNewRegionsRole(regionsRoleVO);

        return "操作成功!";
    }

    /**
     * 插入新的区域权限
     * @param regionsRoleVO
     */
    private void insertNewRegionsRole(RegionsRoleVO regionsRoleVO) {

    }

    /**
     * 清理用户门店角色
     * @param regionsRoleVO
     * @param systemUserStore
     */
    private void cleanUserStoreRole(RegionsRoleVO regionsRoleVO, SystemUserStore systemUserStore) {

        SystemUserStoreRoleExample condition = new SystemUserStoreRoleExample();
        SystemUserStoreRoleExample.Criteria criteria = condition.createCriteria().andUserIdEqualTo(regionsRoleVO.getUserID());
       if (RegionsRoleLevel.Store.code.equals(regionsRoleVO.getLevel())){
            // 门店
            criteria.andUserStroeIdEqualTo(Integer.parseInt(regionsRoleVO.getUserStoreID()));
        }else {

            SystemUserStoreExample preCondition = new SystemUserStoreExample();
            SystemUserStoreExample.Criteria preCriteria = preCondition.createCriteria().andUserIdEqualTo(systemUserStore.getUserId());

            if(RegionsRoleLevel.Branch.code.equals(regionsRoleVO.getLevel())){
                preCriteria.andBranchCodeEqualTo(systemUserStore.getBranchCode());
            }else if(RegionsRoleLevel.City.code.equals(regionsRoleVO.getLevel())){
                preCondition.createCriteria().andUserIdEqualTo(systemUserStore.getUserId())
                        .andCityBranchCodeEqualTo((systemUserStore.getCityBranchCode()));
            }

            List<SystemUserStore> ids = systemUserStoreMapper.selectByExample(preCondition);
            if(ids != null && !ids.isEmpty()){
                criteria.andUserStroeIdIn(ids.stream().map(SystemUserStore::getId).collect(Collectors.toList()));
            }
        }
        systemUserStoreRoleMapper.deleteByExample(condition);

    }

    /**
     * 转换角色区域
     * @param scope
     * @return
     */
    private List<Integer> convertScope(Integer scope) {
        List<Integer> scopes = Lists.newArrayList(RoleScope.COMMON.code);

        if(RoleScope.PROVINCE.code.equals(scope)){
            scopes.add(scope);
            scopes.add(RoleScope.PROVINCE.code + RoleScope.CITY.code);
            scopes.add(RoleScope.ROOT.code + RoleScope.PROVINCE.code);
        }else if(RoleScope.CITY.code.equals(scope)){
            scopes.add(scope);
            scopes.add(RoleScope.PROVINCE.code + RoleScope.CITY.code);
            scopes.add(RoleScope.ROOT.code + RoleScope.CITY.code);
        }else if(RoleScope.ROOT.code.equals(scope)){
            scopes.add(scope);
            scopes.add(RoleScope.ROOT.code + RoleScope.PROVINCE.code);
            scopes.add(RoleScope.ROOT.code + RoleScope.CITY.code);
        }

        return scopes;
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
