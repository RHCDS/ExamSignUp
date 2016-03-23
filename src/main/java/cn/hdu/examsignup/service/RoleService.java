package cn.hdu.examsignup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.RoleDao;
import cn.hdu.examsignup.model.ExRole;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleService {
	@Autowired
	private RoleDao roleDao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String getRoleRemarkByRoleNum(Integer roleNum){
		return this.roleDao.getRoleRemarkByRoleNum(roleNum);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ExRole getRoleByRoleName(String roleName)
	{
		return this.roleDao.getRoleByRoleName(roleName);
	}
}
