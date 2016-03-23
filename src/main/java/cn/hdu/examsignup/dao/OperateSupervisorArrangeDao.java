package cn.hdu.examsignup.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExSupervisor;

public class OperateSupervisorArrangeDao extends AbstractHibernateDao<ExSupervisor>{
	OperateSupervisorArrangeDao() {
		super(ExSupervisor.class);
	}
	
	public List<Map> loadArrangeInfo(String institutionnum) {
		List<Map> result;
		String temp;
		Date startTime;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String hql = "select new map(a.id as id,a.exSection.id as sectionid, "
				+ "a.exSection.starttime as starttime,a.exSection.sectionnum as sectionnum, "
				+ "a.exPhysicexamroom.id as physicexamroomid,a.exPhysicexamroom.roomlocation as roomlocation, "
				+ "a.exPhysicexamroom.exCampus.name as campusname,"
				+ "a.exLanguage.name as language) "
				+ "from cn.hdu.examsignup.model.ExArrangement a "
				+ "where a.exInstitution.institutionnum=:institutionnum "
				+ "and a.exSection.operateflag=:operateflag ";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("operateflag", "1");
		
		result = query.list();
		for (Map element : result) {
			temp = ((Integer) element.get("sectionnum")).toString();
			temp = "第" + temp + "场次,开始时间";
			startTime = (Date) element.get("starttime");
			temp += df.format(startTime);
			element.remove("sectionnum");
			element.remove("starttime");
			element.put("sectioninfo", temp);
		}
		return result;
	}
	public List getArrangementsBySection(String sectionid,String institutionnum) {
		List<Map> result;
		String hql = "select a.id as id from cn.hdu.examsignup.model.ExArrangement a"
				+ " where a.exInstitution.institutionnum=:institutionnum "
				+ "and a.exSection.id=:sectionid "
				+ "and a.exSection.operateflag=:operateflag ";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("sectionid", sectionid)
				.setParameter("operateflag","1");
		result = query.list();
		return result;
		
	}
 	public List<Map> loadArrangedSupervisor(String arrangeid) {
		String hql = "select new map(a.exSupervisor.id as id,a.exSupervisor.name as name," +
				"a.exSupervisor.contact as contact) from cn.hdu.examsignup.model.ExArrangeSupervisor a " +
				"where a.exArrangement.id=:arrangeid";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid);
		return query.list();
	}
	public List<Map> loadUnarrangedSupervisor(String sectionid,String institutionnum) {
		String hql = "select new map(a.id as id,a.name as name," +
				"a.contact as contact) from cn.hdu.examsignup.model.ExSupervisor a " +
				"where a.exInstitution.institutionnum=:institutionnum and " +
				"a.operateflag='1'" +
				" and a.id not in " +
				"(select t.exSupervisor.id from cn.hdu.examsignup.model.ExArrangeSupervisor t " +
				"where t.exArrangement.exSection.id=:sectionid)";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("sectionid", sectionid)
				.setParameter("institutionnum", institutionnum);
		List<Map> result = query.list();
		return result;
	}
	public String getOneUnarrangedSupervisor(String sectionid,String institutionnum) {
		String hql = "select a.id as id" +
				" from cn.hdu.examsignup.model.ExSupervisor a " +
				"where a.exInstitution.institutionnum=:institutionnum and " +
				"a.operateflag='1'" +
				" and a.id not in " +
				"(select t.exSupervisor.id from cn.hdu.examsignup.model.ExArrangeSupervisor t " +
				"where t.exArrangement.exSection.id=:sectionid)";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("sectionid", sectionid)
				.setParameter("institutionnum", institutionnum);
		if(query.list().size() == 0) {
			return null;
		}
		String result = query.list().get(0).toString();
		return result;
	}
	public List<Map> loadArrangedSupervisor(String arrangeid,String institutionnum) {
		String hql = "select new map(a.exSupervisor.id as id,a.exSupervisor.name as name," +
				"a.exSupervisor.contact as contact,a.exSupervisor.primaryflag as primaryflag) " +
				"from cn.hdu.examsignup.model.ExArrangeSupervisor a " +
				"where a.exArrangement.id=:arrangeid and " +
				"a.exInstitution.institutionnum=:institutionnum ";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	public List<Map> getSection(String institutionnum) {
		String hql = "select new map(a.exSection.id as id,a.exSection.sectionnum as sectionnum) from cn.hdu.examsignup.model.ExArrangement a where " +
				"a.exInstitution.institutionnum=:institutionnum and " +
				"a.exPhysicexamroom.operateflag='1' " +
				"group by a.exSection.id";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		return query.list();
	}
}
