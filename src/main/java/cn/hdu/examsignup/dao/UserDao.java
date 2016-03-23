package cn.hdu.examsignup.dao;


import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExUser;

public class UserDao extends AbstractHibernateDao<ExUser>{
	
	UserDao(){
		super(ExUser.class);
	}
	
	public boolean validateManager(String managerName, String managerPassword,
			String institution) {
		String hql = "select e from cn.hdu.examsignup.model.ExUser e where e.loginname=:managerName and password=:managerPassword and e.exInstitution.institutionnum=:institution";
		Query query=this.getCurrentSession().createQuery(hql);
		query.setParameter("managerName",managerName);
		query.setParameter("managerPassword",managerPassword);
		query.setParameter("institution",institution);
		List<ExUser> result=query.list();
		if(result.size()==1)
			return true;
		return false;
	}

	public Integer getRoleNumByLoginName(String loginName,String institution) {
		// TODO Auto-generated method stub
		String  hql= "select e.exRole.rolenum from cn.hdu.examsignup.model.ExUser e where e.loginname=:loginName and e.exInstitution.institutionnum=:institution";
		Query query=this.getCurrentSession().createQuery(hql);
		query.setParameter("loginName",loginName);
		query.setParameter("institution",institution);
		List<Integer> result=query.list();
		if(result.size()==1)
			return (Integer)result.get(0);
		return 9;
	}

	public ExUser getRoleByLoginName(String loginName, String institution) {
		String hql = "select e from cn.hdu.examsignup.model.ExUser e where e.loginname=:loginName and e.exInstitution.institutionnum=:institution";
		Query query=this.getCurrentSession().createQuery(hql);
		query.setParameter("loginName",loginName);
		query.setParameter("institution",institution);
		List<ExUser> result=query.list();
		if(result.size()==1)
			return result.get(0);
		return null;
	}
	
	public String getRolePassword(String loginName, String institution){
		String hql = "select e.password from cn.hdu.examsignup.model.ExUser e where e.loginname=:loginName and e.exInstitution.institutionnum=:institution";
		Query query=this.getCurrentSession().createQuery(hql);
		query.setParameter("loginName",loginName);
		query.setParameter("institution",institution);
		List<String> result=query.list();
		if(result.size()==1)
			return result.get(0);
		return null;
	}
	
	public boolean setRolePassword(String loginName,String institution,String password){
		try {
			ExUser entity = this.getRoleByLoginName(loginName, institution);
			if(entity==null)
				return false;
			entity.setPassword(password);
			this.update(entity);
			return true;
		}catch (Exception e) {
			System.out.println("saveLanguage error!" + e.getMessage());
			return false;
		}
	}

	public String validateManagerWithCategory(String managerName, String managerPassword,String category) {
		String hql = "select e from cn.hdu.examsignup.model.ExUser e where e.loginname=:managerName and password=:managerPassword";
		ExUser tempUser;
		Query query=this.getCurrentSession().createQuery(hql);
		query.setParameter("managerName",managerName);
		query.setParameter("managerPassword",managerPassword);
		List<ExUser> result=query.list();
		if(result.size()!=1)
			return null;
		tempUser=result.get(0);
		if(tempUser.getExRole().getName().trim().equals("school")&&category.trim().endsWith("school"))
			return tempUser.getExInstitution().getInstitutionnum();
		else if(!(tempUser.getExRole().getName().trim().equals("school"))&&!(tempUser.getExRole().getName().trim().equals("student"))&&!(category.trim().equals("school")))
			return tempUser.getExInstitution().getInstitutionnum();
		else return null;
	}
	
	public List<Map> getAllUserByType(String usertype){
		String hql;
		if(usertype.equals("province")){
			hql = "select new map(e.id as id, e.exInstitution.name as institution, e.exInstitution.institutionnum as institutionnum, e.loginname as loginname, " +
					"e.password as password, e.name as name, e.contactnum as contact)" +
					"from  cn.hdu.examsignup.model.ExUser e " +
					"where e.exInstitution is not null and e.exInstitution.category like :usertype  order by e.exInstitution.institutionnum";
		}else
			hql = "select new map(e.id as id, e.exInstitution.name as institution, e.exInstitution.institutionnum as institutionnum, e.loginname as loginname, " +
					"e.password as password, e.name as name, e.contactnum as contact, e.exInstitution.higherInstitution.name as parentinstitution)" +
					"from  cn.hdu.examsignup.model.ExUser e " +
					"where e.exInstitution is not null and e.exInstitution.category like :usertype  order by e.exInstitution.institutionnum";
		Query query=this.getCurrentSession().createQuery(hql).setParameter("usertype", usertype);
		return query.list();
	}
	
}
