package cn.thinkfree.service.account;

import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.logger.AbsLogPrinter;
import cn.thinkfree.core.security.filter.util.SessionUserDetailsUtil;
import cn.thinkfree.database.mapper.SystemPermissionMapper;
import cn.thinkfree.database.model.SystemPermission;
import cn.thinkfree.database.model.SystemPermissionExample;
import cn.thinkfree.database.vo.UserVO;
import cn.thinkfree.database.vo.account.PermissionSEO;
import cn.thinkfree.database.vo.account.PermissionVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PermissionServiceImpl extends AbsLogPrinter implements PermissionService {

    @Autowired
    SystemPermissionMapper systemPermissionMapper;


    /**
     * 新增权限
     *
     * @param permissionVO
     * @return
     */
    @Transactional
    @Override
    public SystemPermission save(PermissionVO permissionVO) {

        printInfoMes("新增权限,信息:{}",permissionVO);
        SystemPermission systemPermission = initPermission(permissionVO,true);

        printInfoMes("新增权限,初始化后信息{}",systemPermission);
        systemPermissionMapper.insertSelective(systemPermission);

        printDebugMes("新增权限,保存完成");
        return systemPermission;
    }

    /**
     * 分页查询
     *
     * @param permissionSEO
     * @return
     */
    @Override
    public PageInfo<SystemPermission> page(PermissionSEO permissionSEO) {

        printInfoMes("权限分页,查询条件:{}",permissionSEO);

        SystemPermissionExample systemPermissionExample = new SystemPermissionExample();
        SystemPermissionExample.Criteria criteria = systemPermissionExample.createCriteria()
                .andIsDelEqualTo(SysConstants.YesOrNo.NO.shortVal());
        if(StringUtils.isNotBlank(permissionSEO.getName())){
            criteria.andNameLike("%"+ permissionSEO.getName() + "%");
            systemPermissionExample.or().andCodeLike("%"+permissionSEO.getName()+"%");
        }
        if(StringUtils.isNotBlank(permissionSEO.getState())){
            criteria.andIsEnableEqualTo(Short.valueOf(permissionSEO.getState()));
        }

        PageHelper.startPage(permissionSEO.getPage(),permissionSEO.getRows());
        List<SystemPermission> page = systemPermissionMapper.selectPermissionVO(systemPermissionExample);
        return new PageInfo<>(page);
    }

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    @Override
    public SystemPermission detail(Integer id) {
        SystemPermission systemPermission = systemPermissionMapper.selectPermissionVOByID(id);
        return systemPermission;
    }

    /**
     * 编辑权限信息
     *
     * @param permissionVO
     * @return
     */
    @Transactional
    @Override
    public SystemPermission edit(PermissionVO permissionVO) {

        printInfoMes("编辑权限,权限信息:{}",permissionVO);
        SystemPermission systemPermission = initPermission(permissionVO,false);

        SystemPermissionExample systemPermissionExample = new SystemPermissionExample();
        systemPermissionExample.createCriteria().andIdEqualTo(permissionVO.getId());
        systemPermissionMapper.updateByExampleSelective(systemPermission,systemPermissionExample);
        printInfoMes("编辑权限,完成");
        return systemPermission;
    }

    /**
     * 更新权限状态
     *
     * @param id
     * @param state
     * @return
     */
    @Transactional
    @Override
    public String updatePermissionState(Integer id, Short state) {

        printInfoMes("权限状态,:{},{}",id,state);
        SystemPermission update = new SystemPermission();
        update.setIsSys(state);
        SystemPermissionExample condition = new SystemPermissionExample();
        condition.createCriteria().andIdEqualTo(id);
        systemPermissionMapper.updateByExampleSelective(update,condition);
        return "操作成功!";
    }

    /**
     * 查询权限状况根据角色ID
     *
     * @param id
     * @return
     */
    @Override
    public List<SystemPermission> listPermissionByRoleID(Integer id) {
        return systemPermissionMapper.selectPermissionForGrant(id);
    }

    /**
     * 初始化权限信息
     * @param permissionVO
     * @param isSave 是否新增
     * @return
     */
    private SystemPermission initPermission(PermissionVO permissionVO,Boolean isSave) {

        SystemPermission systemPermission = new SystemPermission();

        systemPermission.setName(permissionVO.getName());
        systemPermission.setDesc(permissionVO.getDesc());
        if(isSave){
            systemPermission.setCreateTime(new Date());
            systemPermission.setIsDel(SysConstants.YesOrNo.NO.shortVal());
            systemPermission.setIsEnable(SysConstants.YesOrNo.NO.shortVal());
            systemPermission.setIsSys(SysConstants.YesOrNo.NO.shortVal());
            UserVO user = (UserVO) SessionUserDetailsUtil.getUserDetails();
            systemPermission.setCreator(user.getUserRegister().getUserId());
        }

        return systemPermission;
    }
}
