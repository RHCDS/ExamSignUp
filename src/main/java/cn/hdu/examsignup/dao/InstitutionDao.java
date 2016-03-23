package cn.hdu.examsignup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExInstitution;

public class InstitutionDao extends AbstractHibernateDao<ExInstitution> {

	InstitutionDao() {
		super(ExInstitution.class);
	}

	public List<String[]> getSchoolNumName() {
		String hql = "select a.institutionnum,a.name from cn.hdu.examsignup.model.ExInstitution a where a.category='school'";
		return (List<String[]>) this.getCurrentSession().createQuery(hql).list();
	}
	public List getSchoolNum() {
		String hql = "select a.institutionnum from cn.hdu.examsignup.model.ExInstitution a where a.category='school'";
		return this.getCurrentSession().createQuery(hql).list();
	}
	public List<String[]> getInstitutionNumName() {
		String hql = "select a.institutionnum,a.name from cn.hdu.examsignup.model.ExInstitution a where a.category='school' or a.category='institution'";
		return (List<String[]>) this.getCurrentSession().createQuery(hql).list();
	}

	public ExInstitution getInstitutionByInstitutionNum(String institutionNum) {
		// TODO Auto-generated method stub
		List<ExInstitution> result;
		String hql ="select e from cn.hdu.examsignup.model.ExInstitution e where e.institutionnum=:institutionNum";
		Query query=this.getCurrentSession().createQuery(hql);
		query.setParameter("institutionNum",institutionNum);
		result=query.list();
		if(result.size()==1)
			return result.get(0);
		return null;
	}

	public List<Map> loadManagedInstitutionList(String institutionnum,String category) {//type类型详见.sql文档，注意为枚举类型,若为空则列出所有
		// TODO Auto-generated method stub
		List<String> tempIDlist,IDList=new ArrayList();
		List<Map> result;
		String currentInstitution=null;
		String hql ="select e.id from cn.hdu.examsignup.model.ExInstitution e where e.institutionnum=:institutionnum";
		Query query=this.getCurrentSession().createQuery(hql);
		query.setParameter("institutionnum",institutionnum);
		tempIDlist=query.list();
		if(tempIDlist.size()!=1)
			return null;
		IDList.clear();
		for(int i=100;i>0;i--){//这里仅仅是个概数，防止死循环
			tempIDlist=getNextLevelManagedInstitutionList(tempIDlist);
			if(tempIDlist.size()==0)
				break;
			else
				IDList.addAll(tempIDlist);
		}
		if(IDList.size()==0)
			return null;
		hql ="select new map(e.id as id, e.institutionnum as institutionnum,e.name as institutionname) from cn.hdu.examsignup.model.ExInstitution e where e.id in (:institutionIdList) and e.category=:category order by e.institutionnum";
		query=this.getCurrentSession().createQuery(hql);
		if(category.equals("province")){
			query.setParameter("category","school");
		}else{
			if(!category.isEmpty())
				query.setParameter("category",category);
			else{
				hql ="select new map(e.id as id, e.institutionnum as institutionnum,e.name as institutionname) from cn.hdu.examsignup.model.ExInstitution e where e.id in (:institutionIdList)  order by e.institutionnum";
				query=this.getCurrentSession().createQuery(hql);
			}
		}
				query.setParameterList("institutionIdList",IDList);
				result=query.list();
				return result;
	}
	
	public List<Map>  hasSameStatusByInstitutionnum(List<String> institutionnumList,Integer indexNum) {//察看是否所有机构状态处于一致
		String hql ="select new map(e.id as id, e.institutionnum as institutionnum,e.name as institutionname) " +
				" from cn.hdu.examsignup.model.ExInstitution e " +
				" where e.institutionnum in (:institutionnumList) and e.institutionstatus.indexnum < :indexNum";
		return  this.getCurrentSession().createQuery(hql).setParameterList("institutionnumList",institutionnumList).setParameter("indexNum", indexNum).list();
	}
	
	public List<Map>  childStatusGroupCount(List<String> institutionnumList) {//统计各状态学校数
		String hql ="select new map(e.institutionstatus.name as name, count(*) as count) " +
				" from cn.hdu.examsignup.model.ExInstitution e " +
				" where e.institutionnum in (:institutionnumList) group by e.institutionstatus.statusnum order by e.institutionstatus.indexnum";
		return  this.getCurrentSession().createQuery(hql).setParameterList("institutionnumList",institutionnumList).list();
	}
	
	public List<String> getNextLevelManagedInstitutionList(List<String> parentList){//仅仅返回ID列表,查找下属机构
		List<String> result = new ArrayList();
		for(String elementInstitutionid:parentList){
			List<String> temp =null;
			String hql ="select e.id from cn.hdu.examsignup.model.ExInstitution e where e.higherInstitution is not null and e.higherInstitution.id =:elementInstitutionid";
			Query query=this.getCurrentSession().createQuery(hql);
			query.setParameter("elementInstitutionid",elementInstitutionid);
			temp=query.list();
			if(temp.size()!=0)
				result.addAll(temp);
		}
		return result;
	}
	public ExInstitution getInstitutionByInstitutionID(String institutionid) {
		String hql = "select a from cn.hdu.examsignup.model.ExInstitution a where a.id=:institutionid";
		return (ExInstitution) this.getCurrentSession().createQuery(hql).setParameter("institutionid", institutionid).list().get(0);
	}
	
	public List<Map> loadChildInstitution(String institutionnum){
		String hql = "select new map(e.institutionnum as institutionnum, e.name as institutionname) " +
				"from cn.hdu.examsignup.model.ExInstitution e where e.higherInstitution.institutionnum =:institutionnum";
		Query query=this.getCurrentSession().createQuery(hql);
		query.setParameter("institutionnum", institutionnum);
		return (List<Map>)query.list();
	} 
	
	public List<Map> getInstitutionByType(String type){
		String hql = "select new map(e.id as id, e.higherInstitution.name as higherInstitution, e.institutionnum as institutionnum, " +
				"e.name as name, e.category as category, e.contact as contact, e.remark as remark," +
				" e.institutionstatus.name as approvalstatus) " +
				"from cn.hdu.examsignup.model.ExInstitution e where e.category like :category order by e.institutionnum ";
		Query query=this.getCurrentSession().createQuery(hql).setParameter("category", type);
		return (List<Map>)query.list();
	} 
	
	public List getAllInstitutionnum(){
		String hql = "select e.institutionnum from cn.hdu.examsignup.model.ExInstitution e group by e.institutionnum";
		Query query=this.getCurrentSession().createQuery(hql);
		return query.list();
	}
	
	public List<Map> getSchoolAndCityStatusInfo(List<Map> schoolList) {
		List<String> IDList=new ArrayList();
		for(Map element: schoolList){
			IDList.add((String) element.get("id"));
		}
		String hql = "select new map(a.id as id, a.higherInstitution.name as higherName, a.higherInstitution.institutionnum as higherInstitutionnum, " +
				"a.institutionnum as schoolNum,a.name as schoolName, a.institutionstatus.name as schoolStatus ,a.institutionstatus.id as schoolStatusId) " +
				"from cn.hdu.examsignup.model.ExInstitution a " +
				"where a.id in (:IDList)  order by a.higherInstitution.id,a.institutionnum";
		Query query=this.getCurrentSession().createQuery(hql).setParameterList("IDList", IDList);
		return query.list();
	}

	public ExInstitution findMinIndexSchool(String higherid) {
		String hql = "select min(a.institutionstatus.indexnum) from cn.hdu.examsignup.model.ExInstitution a where a.higherInstitution.id=:higherid";
		Integer minValue= (Integer) this.getCurrentSession().createQuery(hql).setParameter("higherid", higherid).list().get(0);
		hql = "select a from cn.hdu.examsignup.model.ExInstitution a where a.higherInstitution.id=:higherid and a.institutionstatus.indexnum=:minValue";
		return (ExInstitution) this.getCurrentSession().createQuery(hql).setParameter("higherid", higherid).setParameter("minValue", minValue).list().get(0);
	}
}