package cn.hdu.examsignup.dao;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.json.simple.JSONObject;

import cn.hdu.examsignup.model.ExParameter;


import cn.hdu.examsignup.dao.ParameterDao;

public class ParameterDao extends AbstractHibernateDao<ExParameter> {
	
	ParameterDao(){
		super(ExParameter.class);
	}
	
	public List getParamGroup(String paramtype){
		String hql = "select p from cn.hdu.examsignup.model.ExParameter p where p.paramtype =:paramtype order by p.enumvalue";
		return this.getCurrentSession().createQuery(hql).setParameter("paramtype", paramtype).list();
	}
	
	
	//从数据库中获取打印准考证所需的那句注释
	public List<Map> getRemarkAboutZKZ() {
		String hql = "select  new map(a.paramvalue as remark) from cn.hdu.examsignup.model.ExParameter a where a.name='打印准考证备注'";
		return this.getCurrentSession().createQuery(hql).list();
	}
	


}
