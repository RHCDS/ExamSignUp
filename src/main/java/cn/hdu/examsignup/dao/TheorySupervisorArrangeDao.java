package cn.hdu.examsignup.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExSupervisor;

public class TheorySupervisorArrangeDao extends AbstractHibernateDao<ExSupervisor> {

	TheorySupervisorArrangeDao() {
		super(ExSupervisor.class);
	}
	public String getOneStudentId(String examroomnum,String institutionnum) {
		String hql = "select a.id from cn.hdu.examsignup.model.ExStudent a " +
				"where substring(a.examnum,11,3)=:examroomnum and " +
				"a.exInstitution.institutionnum=:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("examroomnum", examroomnum)
				.setParameter("institutionnum", institutionnum);
		return query.list().get(0).toString();
	}
	public Map getExamroomInfo(String examroomnum,String institutionnum) {
		String studentid = this.getOneStudentId(examroomnum, institutionnum);
		String hql = "select new map(a.exArrangementByTheoryarrangeid.id as arrangeid,a.exArrangementByTheoryarrangeid.exSection.id as sectionid, "
				+ "a.exArrangementByTheoryarrangeid.exSection.starttime as starttime,a.exArrangementByTheoryarrangeid.exSection.sectionnum as sectionnum, "
				+ "a.exArrangementByTheoryarrangeid.exPhysicexamroom.id as physicexamroomid,a.exArrangementByTheoryarrangeid.exPhysicexamroom.roomlocation as roomlocation, "
				+ "a.exArrangementByTheoryarrangeid.exPhysicexamroom.exCampus.name as campusname,"
				+ "a.exArrangementByTheoryarrangeid.exLanguage.name as language) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.id=:studentid";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("studentid", studentid);
		Map result = (Map)query.uniqueResult();
		if(null == result) {
			return null;
		}
		String temp = ((Integer) result.get("sectionnum")).toString();
		temp = "第" + temp + "场次,开始时间";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startTime = (Date) result.get("starttime");
		temp += df.format(startTime);
		result.remove("sectionnum");
		result.remove("starttime");
		result.put("sectioninfo", temp);
		
		return result;
	}
	public List<Map> loadArrangeInfo(String institutionnum) {
		
		String hql = "select new map(substring(a.examnum,11,3) as examroomnum,concat('考场',substring(a.examnum,11,3)) as examroom,a.exCampus.name as campus) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.exLanguage.theoryflag='1' and a.exInstitution.institutionnum=:institutionnum " +
				"and a.examnum is not null "
				+ "group by substring(a.examnum,11,3)";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List<Map> result;
		result = query.list();
		return result;
	}
	//获取已安排老师
	public List<Map> loadArrangedSupervisor(String examroomnum,String institutionnum,String sectionid) {
		String hql = "select new map(a.exSupervisor.id as id,a.exSupervisor.name as name," +
				"a.exSupervisor.contact as contact,a.exSupervisor.primaryflag as primaryflag) " +
				"from cn.hdu.examsignup.model.ExArrangeSupervisor a " +
				"where a.logicExamroomNum=:examroomnum and " +
				"a.exInstitution.institutionnum=:institutionnum " +
				"and a.exArrangement.exSection.id=:sectionid";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("examroomnum", examroomnum)
				.setParameter("institutionnum", institutionnum)
				.setParameter("sectionid", sectionid);
		return query.list();
	}
	public List<Map> loadUnarrangedSupervisor(String sectionid,String institutionnum) {
		String hql = "select new map(a.id as id,a.name as name," +
				"a.contact as contact,a.primaryflag as primaryflag)" +
				" from cn.hdu.examsignup.model.ExSupervisor a " +
				"where a.exInstitution.institutionnum=:institutionnum and " +
				"a.theoryflag='1'" +
				" and a.id not in " +
				"(select t.exSupervisor.id from cn.hdu.examsignup.model.ExArrangeSupervisor t " +
				"where t.exArrangement.exSection.id=:sectionid)";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("sectionid", sectionid)
				.setParameter("institutionnum", institutionnum);
		List<Map> result = query.list();
		return result;
	}
	public String getOneUnarrangedSupervisor(String sectionid,String institutionnum) {
		String hql = "select a.id " +
				"from cn.hdu.examsignup.model.ExSupervisor a " +
				"where a.exInstitution.institutionnum=:institutionnum and " +
				"a.theoryflag='1' and a.primaryflag<>'1' " +
				" and a.id not in " +
				"(select t.exSupervisor.id from cn.hdu.examsignup.model.ExArrangeSupervisor t " +
				"where t.exArrangement.exSection.id=:sectionid)";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("sectionid", sectionid)
				.setParameter("institutionnum", institutionnum);
		if(query.list().size() == 0) {
			return null;
		}
		String result = query.list().get(0).toString();
		return result;
	}
	public String getOneUnarrangedPrimarySupervisor(String sectionid,String institutionnum) {
		String hql = "select a.id " +
				"from cn.hdu.examsignup.model.ExSupervisor a " +
				"where a.exInstitution.institutionnum=:institutionnum and " +
				"a.theoryflag='1' and a.primaryflag='1' " +
				" and a.id not in " +
				"(select t.exSupervisor.id from cn.hdu.examsignup.model.ExArrangeSupervisor t " +
				"where t.exArrangement.exSection.id=:sectionid)";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("sectionid", sectionid)
				.setParameter("institutionnum", institutionnum);
		if(query.list().size() == 0) {
			return null;
		}
		String result = query.list().get(0).toString();
		return result;
	}
	//获取考场的容量
	public long getExamroomCapacity(String institutionnum,String examroomnum) {
		String hql = "select a.id from cn.hdu.examsignup.model.ExStudent a " +
				"where substring(a.examnum,11,3)=:examroomnum and a.exInstitution.institutionnum=:institutionnum";
		Query query= this.getCurrentSession().createQuery(hql).setParameter("examroomnum", examroomnum)
				.setParameter("institutionnum", institutionnum);
		return query.list().size();
	}
	//查看理论考试安排情况
	public String checkArrangeStatus(String institutionnum) {
		String hql = "select a.id from cn.hdu.examsignup.model.ExStudent a " +
				"where a.exArrangementByTheoryarrangeid is null and " +
				"a.exInstitution.institutionnum=:institutionnum and " +
				"a.exLanguage.theoryflag='1' and " +
				"a.examnum is not null";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		int flag = query.list().size();
		if(flag>0) {
			return "false";
		}
		return "true";
	}
	//获取场次
	public List<Map> getSection(String institutionnum) {
		String hql = "select new map(a.exSection.id as id,a.exSection.sectionnum as sectionnum) from cn.hdu.examsignup.model.ExArrangement a where " +
				"a.exInstitution.institutionnum=:institutionnum and " +
				"a.exPhysicexamroom.theoryflag='1' " +
				"group by a.exSection.id";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		return query.list();
	}
	public long getExamroomCountBySection(String sectionid,String institutionnum) {
		String hql = "select substring(a.examnum,11,3) from cn.hdu.examsignup.model.ExStudent a where " +
				"a.exArrangementByTheoryarrangeid.exSection.id=:sectionid and " +
				"a.exInstitution.institutionnum=:institutionnum " +
				"group by substring(a.examnum,11,3)";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("sectionid", sectionid)
				.setParameter("institutionnum", institutionnum);
		long result = query.list().size();
		return result;
	}
	public long getSupervisorCount(String institutionnum) {
		String hql = "select a.id from cn.hdu.examsignup.model.ExSupervisor a where " +
				"a.exInstitution.institutionnum=:institutionnum and " +
				"a.theoryflag='1'";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		long result = query.list().size();
		return result;
	}
	public long getPrimarySupervisorCount(String institutionnum) {
		String hql = "select a.id from cn.hdu.examsignup.model.ExSupervisor a where " +
				"a.exInstitution.institutionnum=:institutionnum and " +
				"a.theoryflag='1' and a.primaryflag='1' ";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum);
		long result = query.list().size();
		return result;
	}
	public List getExamroomBySection(String sectionid,String institutionnum) {
		String hql = "select substring(a.examnum,11,3) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.exLanguage.theoryflag='1' and a.exInstitution.institutionnum=:institutionnum " +
				"and a.examnum is not null " +
				"and a.exArrangementByTheoryarrangeid.exSection.id=:sectionid "
				+ "group by substring(a.examnum,11,3)";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("sectionid", sectionid);
		List<Map> result;
		result = query.list();
		return result;
	} 
	public String getArrangeid(String examroomnum,String institutionnum) {
		String hql = "select a.exArrangementByTheoryarrangeid.id from cn.hdu.examsignup.model.ExStudent a " +
				"where subString(a.examnum,11,3)=:examroomnum and " +
				"a.exInstitution.institutionnum=:institutionnum and " +
				"a.exArrangementByTheoryarrangeid is not null " +
				"group by a.exArrangementByTheoryarrangeid.id";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("examroomnum", examroomnum);
		String result = query.uniqueResult().toString();
		return result;
	}
}
