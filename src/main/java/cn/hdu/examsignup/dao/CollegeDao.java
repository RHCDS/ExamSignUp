package cn.hdu.examsignup.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExCollege;

public class CollegeDao extends AbstractHibernateDao<ExCollege>{
	public CollegeDao(){
		super(ExCollege.class);
	}
	
	public long getCollegeTotalCount(String institutionnum){
		String hql = "select t.id from cn.hdu.examsignup.model.ExCollege t where t.exInstitution.institutionnum =:institutionnum ";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		return query.list().size();
	}
	
	public List<Map> getPageColleges(String institutionnum, int pageNum, int pageSize){
		String hql = "select new map(t.id as id,t.name as name,t.collegenum as collegenum,t.remark as remark) from cn.hdu.examsignup.model.ExCollege t where t.exInstitution.institutionnum =:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum).setFirstResult(pageNum).setMaxResults(pageSize);
		List<Map> result = query.list();
		return (List<Map>)query.list();
	}
	public void clearData(String institutionnum) {
		String hql;
		Query query;
		hql = "select a.id from cn.hdu.examsignup.model.ExCollege a where " +
				"a.exInstitution.institutionnum=:institutionnum";
		query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List deleteList = query.list();
		if(deleteList.size() == 0) return;
		hql = "delete from cn.hdu.examsignup.model.ExCollege a " +
				"where a.id in(:list)";
		query = this.getCurrentSession().createQuery(hql).setParameterList("list", deleteList);
		query.executeUpdate();
	
	}
	
	public List<Map> getAllColleges(String institutionnum){
		String hql = "select new map(t.id as id,t.name as collegename,t.collegenum as collegenum) from cn.hdu.examsignup.model.ExCollege t where t.exInstitution.institutionnum =:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List<Map> result = query.list();
		return (List<Map>)query.list();
	}
}
