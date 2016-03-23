package cn.hdu.examsignup.dao;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExNews;

public class NewsDao extends AbstractHibernateDao<ExNews> {

	NewsDao() {
		super(ExNews.class);
	}
	public List<Map> loadNewsByInstitution(String institutionnum) {
		String hql = "select new map(e.id as id,e.title as title,e.publisher as publisher," +
				"e.begindate as begindate,e.enddate as enddate) from cn.hdu.examsignup.model.ExNews e " +
				"where institutionnum=:institutionnum";
		Query query=this.getCurrentSession().createQuery(hql);
		query.setParameter("institutionnum", institutionnum);
		return (List<Map>)query.list();
	}
}
