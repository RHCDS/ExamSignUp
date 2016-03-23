package cn.hdu.examsignup.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import cn.hdu.examsignup.model.ExSupervisor;

public class SupervisorDao extends AbstractHibernateDao<ExSupervisor> {
	
	public SupervisorDao(){
		super(ExSupervisor.class);
	}
	
	public long getSupervisorTotalCount(String institutionnum,String operateOrTheory){
		String hql = "select t.id from cn.hdu.examsignup.model.ExSupervisor t where t.exInstitution.institutionnum =:institutionnum ";
		if(operateOrTheory.equals("operate")) {
			hql += "and t.operateflag='1'";
		} else {
			hql += "and t.theoryflag='1'";
		}
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		return query.list().size();
	}
	
	public List<Map> getPageSupervisors(String institutionnum,int pageNum, int pageSize,String operateOrTheory){
		String hql = "select new map(t.id as id,t.name as name,t.contact as contact,t.supervisornum as supervisornum," +
				"t.exInstitution.name as exInstitution,t.exCollege.name as exCollege,t.primaryflag as primaryflag)" +
				" from cn.hdu.examsignup.model.ExSupervisor t " +
				"where t.exInstitution.institutionnum =:institutionnum ";
		if(operateOrTheory.equals("operate")) {
			hql += "and t.operateflag='1'";
		}
		else {
			hql += "and t.theoryflag = '1'";
		}
		Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("institutionnum", institutionnum);
		return (List<Map>)query.list();
	}
	public List<Map> getAllSupervisors(String institutionnum) {
		String hql = "select new map(t.id as id,t.name as name,t.contact as contact,t.supervisornum as supervisornum," +
				"t.exInstitution.name as exInstitution,t.exCollege.name as exCollege,t.primaryflag as primaryflag " +
				",t.operateflag as operateflag,t.theoryflag as theoryflag)" +
				" from cn.hdu.examsignup.model.ExSupervisor t " +
				"where t.exInstitution.institutionnum =:institutionnum ";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List<Map> result = query.list();
		return result;
	}
	public void clearData(String institutionnum) {
		String hql;
		Query query;
		hql = "select a.id from cn.hdu.examsignup.model.ExSupervisor a where " +
				"a.exInstitution.institutionnum=:institutionnum";
		query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List deleteList = query.list();
		if(deleteList.size() == 0) return;
		hql = "delete from cn.hdu.examsignup.model.ExSupervisor a " +
				"where a.id in(:list)";
		query = this.getCurrentSession().createQuery(hql).setParameterList("list", deleteList);
		query.executeUpdate();
	}
	
	public Session getSession(){
		return this.getCurrentSession();
	}
}
