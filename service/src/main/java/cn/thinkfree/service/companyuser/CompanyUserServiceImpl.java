package cn.thinkfree.service.companyuser;

import java.util.Date;
import java.util.List;

import cn.thinkfree.core.base.MyLogger;
import cn.thinkfree.core.constants.SysConstants;
import cn.thinkfree.core.security.utils.MultipleMd5;
import cn.thinkfree.core.utils.LogUtil;
import cn.thinkfree.database.event.account.AccountCreate;
import cn.thinkfree.database.mapper.*;
import cn.thinkfree.database.model.*;
import cn.thinkfree.database.constants.UserRegisterType;
import cn.thinkfree.service.event.EventService;
import cn.thinkfree.service.utils.AccountHelper;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.thinkfree.core.utils.WebFileUtil;
import cn.thinkfree.database.vo.CompanyUserSEO;
import cn.thinkfree.database.vo.CompanyUserVo;

@Service
public class CompanyUserServiceImpl implements CompanyUserService {

	
	@Autowired
	CompanyUserMapper companyUserMapper;
	
	@Autowired
	CompanyUserRoleMapper companyUserRoleMapper;
	
	@Autowired
	CompanyRoleMapper companyRoleMapper;

	@Autowired
	CompanyRoleResourceMapper companyRoleResourceMapper;

	@Autowired
	UserRegisterMapper userRegisterMapper;

	@Autowired
	EventService eventService;

	MyLogger logger = LogUtil.getLogger(getClass());
	

	final static String TARGET = "static/";

	@Override
	public PageInfo<CompanyUser> queryCompanyUserList(CompanyUserSEO companyUser) {
		PageHelper.startPage(companyUser.getPage(),companyUser.getRows());
		CompanyUserExample example = new CompanyUserExample();
		if(!StringUtil.isEmpty(companyUser.getEmpName())) {
			example.createCriteria().andEmpNameLike("%"+companyUser.getEmpName()+"%");
		}if(!StringUtil.isEmpty(companyUser.getEmpNumber())){
			example.createCriteria().andEmpNumberLike("%"+companyUser.getEmpNumber()+"%");
		}
		List<CompanyUser>  list =  companyUserMapper.selectByExample(example);
		
		return new PageInfo<>(list);
	}

	/**
	 * 新增或编辑企业用户
	 * @param companyUser
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insertOrUpdateCompanyUser(CompanyUserVo companyUser) {

		CompanyUser saveObj = companyUser.getCompanyUser();

		if(saveObj.getId() == null ){//判断主键值是否空
			saveObj.setEmpNumber(AccountHelper.createUserNo(AccountHelper.UserType.PE.prefix));
			saveObj.setCreateTime(new Date());
			saveObj.setUpdateTime(new Date());
			companyUserMapper.insertSelective(saveObj);
			// 处理账号信息
			UserRegister account = initAccount();
			account.setHeadPortraits(saveObj.getPhotoUrl());
			account.setPhone(saveObj.getEmail());
			account.setUserId(saveObj.getEmpNumber());
			String password = AccountHelper.createUserPassWord();
			account.setPassword(new MultipleMd5().encode(password));
			userRegisterMapper.insertSelective(account);
			eventService.publish(new AccountCreate(account.getUserId(),account.getPhone(),password,saveObj.getEmpName()));
		}else{
			companyUser.getCompanyUser().setUpdateTime(new Date());
			CompanyUserExample userem = new CompanyUserExample();
			userem.createCriteria().andIdEqualTo(companyUser.getCompanyUser().getId());
			companyUserMapper.updateByExampleSelective(companyUser.getCompanyUser(),userem);
		}
		//添加角色（删除当前用户得角色 重写插入）
		List<String> list = companyUser.getRoleList();//角色列表
		if(!list.isEmpty()){
			String userId = companyUser.getCompanyUser().getEmpNumber();
			CompanyUserRoleExample examp = new CompanyUserRoleExample();
			examp.createCriteria().andUserIdEqualTo(userId);
			companyUserRoleMapper.deleteByExample(examp);
			for (int i = 0; i < list.size(); i++) {
				CompanyUserRole record = new CompanyUserRole();
				record.setUserId(userId);
				record.setRoleId(list.get(i));
				companyUserRoleMapper.insertSelective(record);
			}
		}
		return true;
	}

	private UserRegister initAccount() {
		UserRegister userRegister = new UserRegister();
		userRegister.setIsDelete(SysConstants.YesOrNo.NO.shortVal());
		userRegister.setType(UserRegisterType.Enterprise.shortVal());
		userRegister.setRegisterTime(new Date());
		return userRegister;
	}

	@Override
	public boolean updateUserStatus(String userId,String stauts) {
		CompanyUser user = new CompanyUser();
		user.setEmpNumber(userId);
		user.setStatus(stauts);
		int  falg = companyUserMapper.updateByExampleSelective(user, new CompanyUserExample());
		if(falg > 0 ){
			return true;
		}
		return false;
	}

	@Override
	public CompanyUser getCompanyUserInfo(String empNumber) {
		CompanyUserExample example = new CompanyUserExample();
		example.createCriteria().andEmpNumberEqualTo(empNumber);
		List<CompanyUser>  list =  companyUserMapper.selectByExample(example);
		return list == null?null:list.get(0);
	}

	@Override
	public List<CompanyRole> getRoleList() {
		CompanyRoleExample example = new CompanyRoleExample();
		//根据登陆者得公司id查询
		return companyRoleMapper.selectByExample(example);
	}

	@Override
	public boolean inserRoleInfo(CompanyRole role) {
		int  flag = 0;
		if(role != null) {
			if ((role.getId() != null && !role.getId().equals(""))) {
				CompanyRoleExample example = new CompanyRoleExample();
				example.createCriteria().andIdEqualTo(role.getId());
				flag = companyRoleMapper.updateByExampleSelective(role, example);
			} else {
				role.setCreateTime(new Date());
				flag = companyRoleMapper.insertSelective(role);
			}
		}
		if(flag > 0 ){
			return true;
		}
		return false;
	}

	/**
	 * 更新企业角色资源
	 * @param id
	 * @param resources
	 * @return
	 */
	@Transactional
	@Override
	public String updateEnterPriseRoleResource(Integer id, Integer[] resources) {
		logger.info("企业角色资源分配,清空旧资源");
		CompanyRoleResourceExample condition = new CompanyRoleResourceExample();
		condition.createCriteria().andRoleIdEqualTo(id);
		companyRoleResourceMapper.deleteByExample(condition);

		logger.info("企业角色资源分配,写入新资源");
		if(resources == null || resources.length == 0 ){
			logger.info("企业角色资源分配,清空角色");
			return "操作成功!";
		}

		for(Integer rid : resources){
			CompanyRoleResource saveObj = new CompanyRoleResource();
			saveObj.setRoleId(id);
			saveObj.setResourceId(rid);
			companyRoleResourceMapper.insertSelective(saveObj);
		}
		return "操作成功!";
	}


}
