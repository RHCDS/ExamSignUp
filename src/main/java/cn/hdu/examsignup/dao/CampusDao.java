package cn.hdu.examsignup.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExCampus;

public class CampusDao extends AbstractHibernateDao<ExCampus>{
	public CampusDao(){
		super(ExCampus.class);
	}
	
	public long getCampusTotalCount(String institutionnum){
		String hql = "select t.id from cn.hdu.examsignup.model.ExCampus t where t.exInstitution.institutionnum =:institutionnum";//count(t.id)
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		return query.list().size();
	}
	
	public List<Map> getPageCampuss(String institutionnum, int pageNum, int pageSize){
		String hql = "select new map(t.id as id,t.name as name,t.campusnum as campusnum,t.remark as remark) from cn.hdu.examsignup.model.ExCampus t where t.exInstitution.institutionnum =:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("institutionnum", institutionnum);
		return (List<Map>)query.list();
	}
	//这个函数已经废弃！！！！！！！！！！！！！！！！！！！！
	public List getCampusList(String institutionnum){
		String hql = "select t.id, t.name from cn.hdu.examsignup.model.ExCampus t where t.exInstitution.institutionnum =:institutionnum";
		return this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum).list();
	}
	//获取校区
	public List<Map> getCampus(String institutionnum) {
		String hql = "select new map(a.id as id,a.campusnum as campusnum,a.name as name) from cn.hdu.examsignup.model.ExCampus a where " +
				"a.exInstitution.institutionnum=:institutionnum order by a.campusnum";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		return query.list();
	}
	
	public void clearData(String institutionnum) {
		String hql;
		Query query;
		hql = "select a.id from cn.hdu.examsignup.model.ExCampus a where " +
				"a.exInstitution.institutionnum=:institutionnum";
		query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List deleteList = query.list();
		if(deleteList.size() == 0) return;
		hql = "delete from cn.hdu.examsignup.model.ExCampus a " +
				"where a.id in(:list)";
		query = this.getCurrentSession().createQuery(hql).setParameterList("list", deleteList);
		query.executeUpdate();
	}
}
