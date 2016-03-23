package cn.hdu.examsignup.dao;

import java.util.List;
import java.util.Map;

import cn.hdu.examsignup.model.ExMainmenu;
import cn.hdu.examsignup.model.ExRole;

public class MainMenuDao extends AbstractHibernateDao<ExMainmenu> {
	
	MainMenuDao() {
		super(ExMainmenu.class);
	}
	
	public List getPageExMainmenu(int pageNum, int pageSize){
		String hql = "select t from cn.hdu.examsignup.model.ExMainmenu t";
		return this.getCurrentSession().createQuery(hql).setFirstResult(pageNum).setMaxResults(pageSize).list();
	}
	
	public long getExMainmenuCount(){
		String hql = "select count(t) from cn.hdu.examsignup.model.ExMainmenu t";
		return (Long)this.getCurrentSession().createQuery(hql).list().get(0);
	}
	
	public List parentMenuGroup(){
		String hql = "select t.parentmenu from cn.hdu.examsignup.model.ExMainmenu t group by t.parentmenu";
		return this.getCurrentSession().createQuery(hql).list();
	}
	
	public List getMainMenuForParentMenu(String parentmenu){
		String hql = "select t from cn.hdu.examsignup.model.ExMainmenu t where t.parentmenu =:parentmenu";
		return this.getCurrentSession().createQuery(hql).setParameter("parentmenu", parentmenu).list();
	}
	
	public List<ExMainmenu> getAuthorisedMenu(Integer roleNum){
		String hql ="select e from cn.hdu.examsignup.model.ExMainmenu e where e.exRole.rolenum = :roleNum order by e.sortindex";
		return this.getCurrentSession().createQuery(hql).setParameter("roleNum", roleNum).list();
	}

	public List<Map> findAllMenuByGrouping(){
		String hql = "select new map(e.id as id,e.menuid as menuid,e.showname as showname ,e.onclickscript as onclickscript,e.exMainmenu.showname as parentmenu,e.exRole.remark as rolename, e.sortindex as sortindex ) from cn.hdu.examsignup.model.ExMainmenu e";
		return (List<Map>)this.getCurrentSession().createQuery(hql).list();
	}
	
	public List<Map> findParentMenu(){
		String hql = "select new map (e.id as id, e.menuid as menuid, e.showname as showname, e.exRole.remark as rolename, e.sortindex as sortindex) from cn.hdu.examsignup.model.ExMainmenu e where e.exMainmenu = null";
		return (List<Map>)this.getCurrentSession().createQuery(hql).list();
	}
	public List<Map> loadParentMenuName() {
		String hql = "select new map (e.menuid as menuid, e.showname as showname) from cn.hdu.examsignup.model.ExMainmenu e where e.exMainmenu = null";
		return (List<Map>)this.getCurrentSession().createQuery(hql).list();
	}
	public List<ExMainmenu> getByShowname(String showname){
		String hql = "select t from cn.hdu.examsignup.model.ExMainmenu t where t.showname =:showname";
		return (List<ExMainmenu>)this.getCurrentSession().createQuery(hql).setParameter("showname", showname).list();
	}
}
