package cn.hdu.examsignup.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.json.simple.JSONObject;
import org.hibernate.Session;

import cn.hdu.examsignup.model.ExLanguage;
import cn.hdu.examsignup.model.ExSection;

public class LanguageDao extends AbstractHibernateDao<ExLanguage> {
	public LanguageDao(){
		super(ExLanguage.class);
	}
	
	public long getLanguageTotalCount(){
		String hql = "select t.id from cn.hdu.examsignup.model.ExLanguage t where t.languagenum is not null ";
		Query query = this.getCurrentSession().createQuery(hql);
		return query.list().size();
	}
	
	public List<String> getLanguageNumList(){
		String hql = "select t.languagenum from cn.hdu.examsignup.model.ExLanguage t where t.languagenum is not null group by t.languagenum ";
		Query query = this.getCurrentSession().createQuery(hql);
		return query.list();
	}
	
	public List<Map> getPageLanguages(int pageNum, int pageSize){
		String hql = "select new map(t.id as id,t.languagenum as languagenum,t.name as name,t.theoryflag as theoryflag,t.operateflag as operateflag,t.theorylength as theorylength,t.operatelength as operatelength) from cn.hdu.examsignup.model.ExLanguage t where t.languagenum is not null";
		Query query = this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize);
		return (List<Map>)query.list();
	}
	
	public List getLanguagesName(){
		String hql = "select  t.languagenum ,t.name from cn.hdu.examsignup.model.ExLanguage t where t.languagenum is not null";
		return this.getCurrentSession().createQuery(hql).list();
	}

	public ExLanguage getLanguageBylanguageNumAndType(String languageNum,String theoryOrOperate) {
		List<ExLanguage> result;
		String hql="";
		String flag = "";
		if(theoryOrOperate.equals("theory")) {
			hql="select e from cn.hdu.examsignup.model.ExLanguage e where e.languagenum is not null and e.languagenum=:languagenum and e.theoryflag=:theoryflag";
			flag="theoryflag";
		}
		else {
			hql="select e from cn.hdu.examsignup.model.ExLanguage e where e.languagenum is not null and e.languagenum=:languagenum and e.operateflag=:operateflag";
			flag="operateflag";
		}
		Query query = this.getCurrentSession().createQuery(hql);
		query.setParameter("languagenum", languageNum).setParameter(flag,"1");
		result=query.list();
		if(result.size()==1)
			return result.get(0);
		return null;
	}
	public List<Map> getAllLanguage(){
		String hql = "select new map(t.id as id, t.languagenum as languagenum, t.name as name, t.theoryflag as theoryflag, t.operateflag as operateflag, t.theorylength as theorylength, t.operatelength as operatelength) from cn.hdu.examsignup.model.ExLanguage t where t.languagenum is not null";
		return (List<Map>)this.getCurrentSession().createQuery(hql).list();
	}

	public List<Map> loadlanguageList() {
		String hql = "select new map(t.languagenum as languagenum, t.name as languagename) from cn.hdu.examsignup.model.ExLanguage t where t.languagenum is not null order by t.languagenum";
		List<Map> rusult=(List<Map>)this.getCurrentSession().createQuery(hql).list();
	
		return (List<Map>)this.getCurrentSession().createQuery(hql).list();
	}
	
	public List<Map> getTheoryLanguageList() {
		String hql = "select new map(t.languagenum as languagenum, t.name as languagename) from cn.hdu.examsignup.model.ExLanguage t " +
				"where t.languagenum is not null and t.theoryflag ='1' order by t.languagenum";
		return (List<Map>)this.getCurrentSession().createQuery(hql).list();
	}
	
	public Session getSession(){
		return this.getCurrentSession();
	}
}
