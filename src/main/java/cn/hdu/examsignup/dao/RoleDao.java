package cn.hdu.examsignup.dao;

import java.util.List;

import cn.hdu.examsignup.model.ExRole;

public class RoleDao  extends AbstractHibernateDao<ExRole> {

	RoleDao(){
		super(ExRole.class);
	}
	
	public List<ExRole> getByRemark(String remark){
		String hql = "select r from cn.hdu.examsignup.model.ExRole r where r.remark =:remark";
		return (List<ExRole>)this.getCurrentSession().createQuery(hql).setParameter("remark", remark).list();
	}
	
	public String getRoleRemarkByRoleNum(Integer roleNum){
		String hql="select e.remark from cn.hdu.examsignup.model.ExRole e where e.rolenum=:roleNum";
		List<String> result= this.getCurrentSession().createQuery(hql).setParameter("roleNum", roleNum).list();
		if(result.size()!=1)
			return null;
		return result.get(0);
	}

	public ExRole getRoleByRoleName(String name) {
		String hql="select e from cn.hdu.examsignup.model.ExRole e where e.name=:name";
		List<ExRole> result= this.getCurrentSession().createQuery(hql).setParameter("name", name).list();
		if(result.size()!=1)
			return null;
		return result.get(0);
	}
}
