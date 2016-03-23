package cn.hdu.examsignup.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.InstitutionDao;
import cn.hdu.examsignup.dao.RoleDao;
import cn.hdu.examsignup.dao.UserDao;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExRole;
import cn.hdu.examsignup.model.ExUser;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	private InstitutionDao institutiondao;
	
	@Autowired
	private RoleDao roledao;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean validateManager(String managerName, String managerPassword,
			String institution) {
		return userDao.validateManager(managerName, managerPassword,
				institution);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Integer getRoleNumByLoginName(String loginName,String institution) {
		return userDao.getRoleNumByLoginName(loginName,institution);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ExUser getRoleByLoginName(String loginName,String institution){
		return this.userDao.getRoleByLoginName(loginName,institution);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String getRolePassword(String loginName,String institution){
		return this.userDao.getRolePassword(loginName,institution);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean setRolePassword(String loginName,String institution,String password){
		return this.userDao.setRolePassword(loginName, institution, password);
	}
	

	public String validateManagerWithoutInstitution(String managerName,
			String managerPassword,String category) {
		return userDao.validateManagerWithCategory(managerName, managerPassword,category);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getAllUserByType(String uertype){
		try {
			return userDao.getAllUserByType(uertype);
		} catch (Exception e) {
			System.out.println("getAllSchoolUser error" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveUser(JSONObject user, String roletype){
		try {
			ExUser entity;
			if(user.get("id").toString().equals("")){
				if(userDao.findByProperty("loginname", user.get("loginname").toString()).size()>0){
					return "登录帐号已被注册！请更换别的登录名！";
				}
				entity = new ExUser();
				entity.setContactnum(user.get("contact").toString());
				ExInstitution institution;
				institution = institutiondao.findByProperty("name", user.get("institution").toString()).get(0);
				entity.setExInstitution(institution);
				ExRole role;
				role = roledao.findByProperty("name", roletype).get(0);
				entity.setExRole(role);
				entity.setLoginname(user.get("loginname").toString());
				entity.setName(user.get("name").toString());
				entity.setPassword(user.get("password").toString());
				userDao.save(entity);
			}else{
				entity = userDao.findById(user.get("id").toString());
				entity.setContactnum(user.get("contact").toString());
				ExInstitution institution;
				institution = institutiondao.findByProperty("name", user.get("institution").toString()).get(0);
				entity.setExInstitution(institution);
				ExRole role;
				role = roledao.findByProperty("name", roletype).get(0);
				entity.setExRole(role);
				entity.setLoginname(user.get("loginname").toString());
				entity.setName(user.get("name").toString());
				entity.setPassword(user.get("password").toString());
				userDao.update(entity);
			}
			return "保存成功！";
		} catch (Exception e) {
			System.out.println("saveUser error" + e.getMessage());
			return "保存失败！";
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteUsers(List<String> ids){
		try {
			for(String id : ids){
				ExUser user = userDao.findById(id);
				userDao.delete(user);
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteUsers error" + e.getMessage());
			return false;
		}
	}
	
}
