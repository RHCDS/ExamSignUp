package cn.hdu.examsignup.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExInstitutionstatus;

public class InstitutionstatusDao extends AbstractHibernateDao<ExInstitutionstatus> {

	InstitutionstatusDao() {
		super(ExInstitutionstatus.class);
	}

	public List<ExInstitutionstatus> loadinstitutionstatusExceptMaxAndMinNum() {
			String hql="select max(a.statusnum) from cn.hdu.examsignup.model.ExInstitutionstatus a" ;
			List<Integer> tempResult = this.getCurrentSession().createQuery(hql).list();
			if(tempResult.size()==0)
				return null;
			Integer maxNum= tempResult.get(0);
			hql="select min(a.statusnum) from cn.hdu.examsignup.model.ExInstitutionstatus a" ;
			tempResult = this.getCurrentSession().createQuery(hql).list();
			if(tempResult.size()==0)
				return null;
			Integer minNum= tempResult.get(0);
			hql ="select e from cn.hdu.examsignup.model.ExInstitutionstatus e where e.statusnum!=:maxNum and  e.statusnum!=:minNum";
			Query query=this.getCurrentSession().createQuery(hql);
			List result=query.setParameter("maxNum",maxNum).setParameter("minNum",minNum).list();
			return result;
	}

	public List<Map> loadStatusList() {
		String hql="select new map(a.id as statusid,a.name as statusname) from cn.hdu.examsignup.model.ExInstitutionstatus a" ;
		return this.getCurrentSession().createQuery(hql).list();
	}
}
