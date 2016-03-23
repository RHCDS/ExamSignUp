package cn.hdu.examsignup.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExProfession;

public class ProfessionDao extends AbstractHibernateDao<ExProfession> {
	public ProfessionDao(){
		super(ExProfession.class);
	}
	
	public long getProfessionTotalCount(String institutionnum){
		String hql = "select t.id from cn.hdu.examsignup.model.ExProfession t " +
				"where t.institutionid =:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		return query.list().size();
	}
	
	public List<Map> getPageProfessions(String institutionnum, int pageNum, int pageSize){
		String hql = "select new map(t.id as id,t.name as name,t.remark as remark,t.institutionid as institutionid,t.professionnum as professionnum)" +
				" from cn.hdu.examsignup.model.ExProfession t where t.institutionid =:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).setParameter("institutionnum", institutionnum);
		return (List<Map>)query.list();
	}
	public void clearData(String institutionnum) {
		String hql;
		Query query;
		hql = "select a.id from cn.hdu.examsignup.model.ExProfession a where " +
				"a.institutionid=:institutionnum";
		query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List deleteList = query.list();
		if(deleteList.size() == 0) return;
		hql = "delete from cn.hdu.examsignup.model.ExProfession a " +
				"where a.id in(:list)";
		query = this.getCurrentSession().createQuery(hql).setParameterList("list", deleteList);
		query.executeUpdate();
	
	}
	
	public List<ExProfession> findByProperty(String propertyName, Object value, String institutionnum) {
		try {
			String queryString = "from cn.hdu.examsignup.model.ExProfession "
					+ " model where model." + propertyName + "= ? and model.institutionid =:institutionid";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value).setParameter("institutionid", institutionnum);
			return (List<ExProfession>)queryObject.list();
		} catch (RuntimeException re) {
			throw re;
		}
	}
}
