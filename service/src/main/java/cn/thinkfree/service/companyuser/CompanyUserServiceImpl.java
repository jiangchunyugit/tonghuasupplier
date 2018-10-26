package cn.thinkfree.service.companyuser;

import java.util.Date;
import java.util.List;

import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.thinkfree.core.utils.WebFileUtil;
import cn.thinkfree.database.mapper.CompanyRoleMapper;
import cn.thinkfree.database.mapper.CompanyUserMapper;
import cn.thinkfree.database.mapper.CompanyUserRoleMapper;
import cn.thinkfree.database.model.CompanyRole;
import cn.thinkfree.database.model.CompanyRoleExample;
import cn.thinkfree.database.model.CompanyUser;
import cn.thinkfree.database.model.CompanyUserExample;
import cn.thinkfree.database.model.CompanyUserRole;
import cn.thinkfree.database.model.CompanyUserRoleExample;
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean inserOrUpdateCompanyUser(CompanyUserVo companyUser) {
		boolean flag = false;
		if(companyUser == null){
			return flag;
		}
		try {
			//处理头像
			if(companyUser.getCompanyUser()!= null && companyUser.getPhotoUrl() != null ){
				companyUser.getCompanyUser().setPhotoUrl(WebFileUtil.fileCopy(TARGET, companyUser.getPhotoUrl()));
			}
			if(companyUser.getCompanyUser().getId() == null || ("").equals(companyUser.getCompanyUser().getId())){//判断主键值是否空

					companyUser.getCompanyUser().setCreateTime(new Date());
					companyUser.getCompanyUser().setUpdateTime(new Date());
					companyUserMapper.insertSelective(companyUser.getCompanyUser());

			}else{

				companyUser.getCompanyUser().setUpdateTime(new Date());
				CompanyUserExample userem = new CompanyUserExample();
				userem.createCriteria().andIdEqualTo(companyUser.getCompanyUser().getId());
				companyUserMapper.updateByExampleSelective(companyUser.getCompanyUser(),userem);
			}
			//添加角色（删除当前用户得角色 重写插入）
			List<String> list = companyUser.getRoleList();//角色列表
			if(!list.isEmpty()){
				int userId = companyUser.getCompanyUser().getId();
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
			flag = true;
		} catch (Exception e) {
			throw new RuntimeException("内部错误");
		}
		return flag;
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

	
   
}
