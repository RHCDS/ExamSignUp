package cn.hdu.examsignup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExStudent;

public class ExamNumEditDao extends AbstractHibernateDao<ExStudent> {

	ExamNumEditDao() {
		super(ExStudent.class);
	}

	/**
	 * @param whereString
	 * @param valuesMap
	 * @param institutionnum
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * 
	 * * 获取高校本校所有已经缴费的考生信息
	 * 注：如果有存在学校还没有缴费通过，则没有显示信息
	 * 
	 * 1、如果还没有编排准考证，则准考证为空
	 * 2、如果以及编排准考证，则显示准考证信息
	 * 
	 * 3、根据校区、语种等信息排序
	 */
	public List<Map> getPageExamNumEdit(String whereString, Map<String,Object> valuesMap, String institutionnum, Integer pageNum,
			Integer pageSize) {
		String hql = "select new map(student.id as id,student.name as name,student.studentnum as studentnum,student.idnum as idnum,student.examnum as examnum,student.exCampus.name as campusname,student.exLanguage.name as languagename,student.paied as paied) "
				+ "from cn.hdu.examsignup.model.ExStudent student ";
		hql += whereString;
		hql +=  "and student.exInstitution.institutionnum=:institutionnum "
				+ "order by student.exCampus.campusnum,student.exLanguage.languagenum ,student.studentcategory,student.examnum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setFirstResult(pageNum).setMaxResults(pageSize);
		String[] perchs = valuesMap.keySet().toArray(new String[0]);
		for(int i = 0; i < perchs.length; i++){
			query.setParameter(perchs[i], valuesMap.get(perchs[i]));
		}
		return (List<Map>)query.list();
	}
	
	public long getExamNumEditTotalCount(String whereString, Map<String,Object> valuesMap, String institutionnum){
		String hql = "select new map(student.id as id,student.name as name,student.studentnum as studentnum,student.idnum as idnum,student.examnum as examnum,student.exCampus.name as campusname,student.exLanguage.name as languagename,student.paied as paied) "
				+ "from cn.hdu.examsignup.model.ExStudent student ";
		hql += whereString;
		hql +=  "and student.exInstitution.institutionnum=:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		String[] perchs = valuesMap.keySet().toArray(new String[0]);
		for(int i = 0; i < perchs.length; i++){
			query.setParameter(perchs[i], valuesMap.get(perchs[i]));
		}
		return query.list().size();
	}

	public Long countUnpaiedStudent(String institutionnum) {
		String hql ="select count(*) from cn.hdu.examsignup.model.ExStudent t where t.paied='0' and t.exInstitution.institutionnum=:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		return ((Long) query.uniqueResult());
	}
	
	public List<String> campusIsNull(String institutionnum) {
		String hql ="select t.id from cn.hdu.examsignup.model.ExStudent t where t.exCampus is null and t.exInstitution.institutionnum=:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	
	public List<String> collegeIsNull(String institutionnum) {
		String hql ="select t.id from cn.hdu.examsignup.model.ExStudent t where t.exCollege is null and t.exInstitution.institutionnum=:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	
	public List<String> professionIsNull(String institutionnum) {
		String hql ="select t.id from cn.hdu.examsignup.model.ExStudent t where t.exProfession is null and t.exInstitution.institutionnum=:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	
	public List<String> validateCampus(String institutionnum) {
		String hql ="select t.id from cn.hdu.examsignup.model.ExCampus t where (t.campusnum not like '_' or not (t.campusnum<='9' and t.campusnum>='0')) and t.exInstitution.institutionnum=:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	
	public List<String> validateStudentCategory(String institutionnum) {
		String hql ="select t.id from cn.hdu.examsignup.model.ExStudent t where (t.studentcategory not like '_' or not (t.studentcategory<='9' and t.studentcategory>='0')) and t.exInstitution.institutionnum=:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}

	public List<Map> loadExamNumLanguageInfo(String institutionnum) {
		String hql = "select new map(a.exLanguage.name as languagename,count(*) as count) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.exInstitution.institutionnum=:institutionnum " 
				+ "group by a.exLanguage.languagenum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	
	//不对是否交费进行检查
	public List<Map> getSignedLanguage(String institutionnum){
		String hql = "select new map(a.exLanguage.name as name, a.exLanguage.languagenum as languagenum,count(*) as count) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.exInstitution.institutionnum=:institutionnum " 
				+ "group by a.exLanguage.languagenum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	
	public List<String> getCampusListInStudent(String institutionnum){
		String hql = "select a.exCampus.campusnum from "
				+ "cn.hdu.examsignup.model.ExStudent a "
				+ " where a.exInstitution.institutionnum=:institutionnum "
				+ "group by a.exCampus.campusnum order by a.exCampus.campusnum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	public List<String> getLanguageListInStudent(String institutionnum){
		String hql = "select a.exLanguage.languagenum from "
				+ "cn.hdu.examsignup.model.ExStudent a "
				+ " where a.exInstitution.institutionnum=:institutionnum "
				+ "group by a.exLanguage.languagenum order by a.exLanguage.languagenum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	public int getMaxExamroomNum(String institutionnum) {
		String hql = "select substring(max(a.examnum),11,3) from " +
				"cn.hdu.examsignup.model.ExStudent a " +
				"where a.exInstitution.institutionnum=:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		Object result = query.uniqueResult();
		if(result == null) {
			return 1;
		}
		return Integer.valueOf((String) result) + 1;
	}
	public List<Map> getWaitingArrangeInfo(String institutionnum,String campusnum,String languagenum,String appendFlag) {
		String hql = "select new map(a.id as id,a.studentcategory as studentcategory) "
				+ "from cn.hdu.examsignup.model.ExStudent a ";
		if(appendFlag.equals("append")) {
			hql += "where a.exInstitution.institutionnum=:institutionnum and " 
					+ "a.exLanguage.languagenum=:languagenum and a.exCampus.campusnum=:campusnum " 
					+ "and (a.examnum='' or a.examnum is null) "
					+ "order by a.studentcategory, rand()";
		} else {
			hql += "where a.exInstitution.institutionnum=:institutionnum and " 
					+ "a.exLanguage.languagenum=:languagenum and a.exCampus.campusnum=:campusnum "
					+ "order by a.studentcategory, rand()";
		}
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum)
				.setParameter("campusnum", campusnum);
		return query.list();
	}

	public List<String> saveExamNum(List<Map> waitingForArrangement) {
		List<String> result= new ArrayList();
		
		for(int i=0;i<waitingForArrangement.size();i++){
			String hql="update cn.hdu.examsignup.model.ExStudent a set a.examnum=:examnum where a.id =:id";
			Query query=this.getCurrentSession().createQuery(hql)
					.setParameter("examnum", (String)waitingForArrangement.get(i).get("examNum"))
					.setParameter("id", (String)waitingForArrangement.get(i).get("id"));
			if(query.executeUpdate()!=1){
				String hqlExcept="select a.idnum from cn.hdu.examsignup.model.ExStudent a where a.id=:id";
				Query queryExcept=this.getCurrentSession().createQuery(hql)
						.setParameter("id", (String)waitingForArrangement.get(i).get("id"));
				result.add((String)query.uniqueResult());
			}
		}
		return result;
	}
}
