package cn.hdu.examsignup.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.hdu.examsignup.model.ExPhysicexamroom;

public class PhysiceexamroomDao extends AbstractHibernateDao<ExPhysicexamroom> {
	
	public PhysiceexamroomDao(){
		super(ExPhysicexamroom.class);
	}
	
	public long getExamroomTotalCount(String institutionnum){
		String hql = "select t.id from cn.hdu.examsignup.model.ExPhysicexamroom t where t.exInstitution.institutionnum =:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		return query.list().size();
	}
	
	public List<Map> getPageExamrooms(String institutionnum, int pageNum, int pageSize){
		String hql = "select new map(t.id as id,t.roomlocation as roomlocation,t.capacity as capacity,t.theoryflag as theoryflag,t.operateflag as operateflag,t.exInstitution.name as exInstitution,t.exCampus.name as exCampus) from cn.hdu.examsignup.model.ExPhysicexamroom t where t.exInstitution.institutionnum =:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum).setFirstResult(pageNum).setMaxResults(pageSize);
		return (List<Map>)query.list();
	}
	public List<Map> getAllExamrooms(String institutionnum) {
		String hql = "select new map(t.id as id,t.roomlocation as roomlocation,t.capacity as capacity," +
				"t.theoryflag as theoryflag,t.operateflag as operateflag," +
				"t.exInstitution.name as exInstitution,t.exCampus.name as exCampus) " +
				"from cn.hdu.examsignup.model.ExPhysicexamroom t " +
				"where t.exInstitution.institutionnum =:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		return (List<Map>) query.list();
	}
	public void clearData(String institutionnum) {
		String hql;
		Query query;
		hql = "select a.id from cn.hdu.examsignup.model.ExPhysicexamroom a where " +
				"a.exInstitution.institutionnum=:institutionnum";
		query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List deleteList = query.list();
		if(deleteList.size() == 0) return;
		hql = "delete from cn.hdu.examsignup.model.ExPhysicexamroom a " +
				"where a.id in(:list)";
		query = this.getCurrentSession().createQuery(hql).setParameterList("list", deleteList);
		query.executeUpdate();
	
	}
	
	public Session getSession(){
		return this.getCurrentSession();
	}
}
