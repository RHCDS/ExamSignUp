package cn.hdu.examsignup.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExArrangement;
import cn.hdu.examsignup.model.ExStudent;

public class OperateExamArrangeDao extends AbstractHibernateDao<ExArrangement> {

	OperateExamArrangeDao() {
		super(ExArrangement.class);
	}
	//获取语言数据
	public List<Map> getLoadSignedLanguage(String institutionnum) {
		String hql = "select new map(a.exLanguage.languagenum as languagenum,a.exLanguage.name as languagename) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.exInstitution.institutionnum=:institutionnum and a.exLanguage.operateflag=:operateflag "
				+ "group by a.exLanguage.languagenum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("operateflag", "1");

		return query.list();
	}
	//获取arrangement信息
	public List<Map> loadArrangeInfo(String institutionnum, String languageNum) {
		List<Map> result;
		String temp;
		Date startTime;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String hql = "select new map(a.id as id,a.exSection.id as sectionid, "
				+ "a.exSection.starttime as starttime,a.exSection.sectionnum as sectionnum, "
				+ "a.exPhysicexamroom.id as physicexamroomid,a.exPhysicexamroom.roomlocation as roomlocation, "
				+ "a.exPhysicexamroom.capacity as capacity,a.exPhysicexamroom.exCampus.name as campusname)"
				+ "from cn.hdu.examsignup.model.ExArrangement a "
				+ "where a.exInstitution.institutionnum=:institutionnum and a.exLanguage.languagenum=:languageNum "
				+ "and a.exSection.operateflag=:operateflag order by a.exSection.sectionnum, a.exPhysicexamroom.exCampus.name,a.exPhysicexamroom.roomlocation";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("languageNum", languageNum)
				.setParameter("operateflag", "1");
		
		result = query.list();
		for (Map element : result) {
			String sectionnum = element.get("sectionnum").toString();
			String physicexamroomId = element.get("physicexamroomid").toString();
			long surplus = Long.valueOf(element.get("capacity").toString()) 
					- this.getStudentCountByPhysicexamroom(institutionnum, sectionnum,physicexamroomId);;
			temp = ((Integer) element.get("sectionnum")).toString();
			temp = "第" + temp + "场次,开始时间";
			startTime = (Date) element.get("starttime");
			temp += df.format(startTime);
			element.remove("starttime");
			element.put("sectioninfo", temp);
			element.put("surplus", surplus);
		}
		return result;
	}
	//根据arrangeid获取该arrangement安排的考场
	public List getArrangementExamrooms(String arrangeid) {
		String hql = "select substring(a.examnum,11,3) as examroom from cn.hdu.examsignup.model.ExStudent a where " +
				"a.exArrangementByOperatearrangeid.id=:arrangeid group by substring(a.examnum,11,3)";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid);
		List result = query.list();
		return result;
	}
	//根据arrangeid获取学生总数
	public long getStudentCountByArrangeId(String arrangeid,String institutionnum) {
		String hql = "select a.id from cn.hdu.examsignup.model.ExStudent as a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exArrangementByOperatearrangeid.id=:arrangeid";
		long result = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum).setParameter("arrangeid", arrangeid).list().size();
		return result;
	}
	

	public List<Map> loadAvailableOperateExamroom(String institutionnum,String languagenum,int sectionnum) {
		List<Map> result;
		String hql = "select new map(a.id as id,a.roomlocation as location," +
				"a.capacity as capacity, a.exCampus.name as campusname) " +
				"from cn.hdu.examsignup.model.ExPhysicexamroom a " +
				"where a.id not in " +
				"(select b.exPhysicexamroom.id from cn.hdu.examsignup.model.ExArrangement b " +
				"where b.exPhysicexamroom.operateflag='1' " +
				"and b.exInstitution.institutionnum=:institutionnum " +
				"and b.exLanguage.languagenum=:languagenum " +
				"and b.exSection.sectionnum=:sectionnum " +
				"group by b.exPhysicexamroom.id) " +
				"and a.exInstitution.institutionnum=:institutionnum " +
				"and a.operateflag='1' group by a.id";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum)
				.setParameter("sectionnum", sectionnum);
		result = query.list();
		return result;
	}
	public List<Map> loadSelectedOperateExamroom(String institutionnum,
			int sectionnum, String languagenum) {
		List<Map> result;

		String hql = "select new map(a.exPhysicexamroom.id as id,a.exPhysicexamroom.roomlocation as location,a.exPhysicexamroom.capacity as capacity,a.exPhysicexamroom.exCampus.name as campusname) "
				+ "from cn.hdu.examsignup.model.ExArrangement a "
				+ "where a.exSection.sectionnum =:sectionnum "
				+ "and a.exInstitution.institutionnum=:institutionnum  and a.exPhysicexamroom.operateflag=:operateflag and a.exLanguage.languagenum=:languagenum "
				+ "group by a.exPhysicexamroom.id";

		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("sectionnum", sectionnum)
				.setParameter("operateflag", "1")
				.setParameter("languagenum", languagenum);

		return query.list();
	}

	public List<Map> loadAllSections(String institutionnum) {
		List<Map> result;
		String temp;
		Date startTime;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String hql = "select new map(a.sectionnum as sectionnum,a.starttime as starttime,a.sectionnum as sectionnum) "
				+ "from cn.hdu.examsignup.model.ExSection a "
				+ "where a.exInstitution.institutionnum=:institutionnum "
				+ "and a.operateflag=:operateflag ";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("operateflag", "1");

		result = query.list();
		for (Map element : result) {
			temp = ((Integer) element.get("sectionnum")).toString();
			temp = "第" + temp + "场次,开始时间";
			startTime = (Date) element.get("starttime");
			temp += df.format(startTime);
			temp += ".";
			element.remove("starttime");
			element.put("sectioninfo", temp);
		}
		return result;
	}

	public ExArrangement findByInfo(Integer sectionnum, String institutionnum,
			String physicexamroomid, String languagenum) {
		List<ExArrangement> result;

		String hql = "select a from cn.hdu.examsignup.model.ExArrangement a "
				+ "where a.exSection.sectionnum =:sectionnum "
				+ "and a.exInstitution.institutionnum=:institutionnum "
				+ "and a.exPhysicexamroom.id=:physicexamroomid and a.exLanguage.languagenum=:languagenum ";

		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("sectionnum", sectionnum)
				.setParameter("physicexamroomid", physicexamroomid)
				.setParameter("languagenum", languagenum);
		result = query.list();
		if (result.size() == 1)
			return result.get(0);
		return null;
	}
	//获取已经安排的学生
	public List<Map> loadSpecialArrangedStudent(String institutionnum,
			String arrangeid, String languagenum) {

		String hql = "select new map(a.id as id,a.name as name,a.examnum as examnum) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.exInstitution.institutionnum=:institutionnum "
				+ "and a.exArrangementByOperatearrangeid.id=:arrangeid and a.exLanguage.languagenum=:languagenum";

		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("arrangeid", arrangeid)
				.setParameter("languagenum", languagenum);
		return query.list();
	}
	//获取未安排考生
	public List<Map> loadSpecialUnarrangedStudent(String institutionnum,String languagenum) {

		String hql = "select new map(a.id as id,a.name as name,a.examnum as examnum) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.exInstitution.institutionnum=:institutionnum "
				+ "and a.exLanguage.languagenum=:languagenum" +
				" and a.exArrangementByOperatearrangeid=null";

		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum);
		return query.list();
	}
	//自动安排
	public String autoArrange(String institutionnum,String languagenum,List<Map> campuses){
		String hql;
		StudentDao studentDao = new StudentDao();
		ExStudent exStudent;
		for(Map campus:campuses) {       //获取校区，根据校区安排
			String campusid = campus.get("id").toString();
			List<ExArrangement> arrangements = this.getArrangement(institutionnum, campusid, languagenum);
			for(ExArrangement element:arrangements) {
				int capacity = element.getExPhysicexamroom().getCapacity();
				for(int i=0;i<capacity;++i) {
					exStudent = studentDao.getOneUnarrangedStudent(institutionnum,campusid,languagenum,"operate");
					if(null == exStudent) {
						i = capacity;
						continue;
					}
					exStudent.setExArrangementByOperatearrangeid(element);
					studentDao.update(exStudent);
				}
			}
		}
		return "{success: true,info: {errors: '安排考生考试成功！'}}";
	}
	
	//根据学校，校区，语种获取arrangement
	public List<ExArrangement> getArrangement(String institutionnum,String campusid,String languagenum){
		String hql = "select a from cn.hdu.examsignup.model.ExArrangement a where " +
				"a.exInstitution.institutionnum=:institutionnum and a.exPhysicexamroom.exCampus.id=:campusid " +
				"and a.exLanguage.languagenum=:languagenum and a.exSection.operateflag='1' " +
				"order by a.exSection.sectionnum asc,a.exPhysicexamroom.roomlocation asc";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("campusid", campusid).setParameter("languagenum", languagenum);
		return query.list();
	}
	//取得未安排的考场
	public List<Map> getUnarrangedExamroom(String institutionnum,String languagenum,String arrangeid) {
		String hql;
		Query query;
		if (arrangeid.trim().isEmpty()) { //不根据校区获取数据
			hql = "select new map(substring(a.examnum,11,3) as examroomnum,concat('考场号',substring(a.examnum,11,3)) as info,a.exCampus.name as campus) "
					+ "from cn.hdu.examsignup.model.ExStudent a "
					+ "where a.exLanguage.operateflag=:operateflag and a.exLanguage.languagenum=:languagenum and a.exInstitution.institutionnum=:institutionnum " +
					"and a.exArrangementByOperatearrangeid=null  and a.examnum is not null "
					+ "group by substring(a.examnum,11,3)";

			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("languagenum", languagenum)
					.setParameter("operateflag", "1");
		}else{//根据校区获取数据
			hql = "select a.exPhysicexamroom.exCampus.id from cn.hdu.examsignup.model.ExArrangement a "
					+ "where a.id=:arrangeid";
			query = this.getCurrentSession().createQuery(hql)
					.setParameter("arrangeid", arrangeid);
			List<String> temp = query.list();
			String campusid;
			if (temp.size() != 1)
				return null;
			campusid = temp.get(0);
			
			hql = "select new map(substring(a.examnum,11,3) as examroomnum,concat('考场号',substring(a.examnum,11,3)) as info,a.exCampus.name as campus) "
					+ "from cn.hdu.examsignup.model.ExStudent a "
					+ "where a.exLanguage.operateflag=:operateflag and a.exLanguage.languagenum=:languagenum and a.exInstitution.institutionnum=:institutionnum "
					+ "and a.exArrangementByOperatearrangeid=null "
					+ "and a.exCampus.id=:campusid and a.paied='1' and a.examnum is not null "
					+ "group by substring(a.examnum,11,3)";

			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("languagenum", languagenum)
					.setParameter("operateflag", "1")
					.setParameter("campusid", campusid);
		}
		return query.list();
	}
	//获取已经安排的逻辑考场
	public List<Map> getArrangedExamroom(String arrangeid) {
		String hql = "select new map(substring(a.examnum,11,3) as examroomnum,concat('考场号',substring(a.examnum,11,3)) as info,a.exCampus.name as campus) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.exArrangementByOperatearrangeid.id=:arrangeid "
				+ "group by substring(a.examnum,11,3)";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid);
		return query.list();
	}
	//获取考场的容量
	public long getExamroomCapacity(String institutionnum,String examroomnum) {
		String hql = "select a.id from cn.hdu.examsignup.model.ExStudent a " +
				"where substring(a.examnum,11,3)=:examroomnum and a.exInstitution.institutionnum=:institutionnum";
		Query query= this.getCurrentSession().createQuery(hql).setParameter("examroomnum", examroomnum)
				.setParameter("institutionnum", institutionnum);
		return query.list().size();
	}
	//获取某语种，教室总容量
	public long getArrangementCapacity(String institutionnum,String languagenum){
		List sections = this.getSection(institutionnum, languagenum);
		long count = 0;
		for(Object section:sections) {
			String sectionnum = section.toString();
			count += this.getArrangementCapacity(institutionnum, languagenum, sectionnum);
		}
		return count;
	}
	public long getCapacityByCampus(String institutionnum,String campusid,String languagenum) {
		List sections = this.getSection(institutionnum, languagenum);
		long count = 0;
		for(Object section:sections) {
			String sectionnum = section.toString();
			count += this.getCapacityByCampus(institutionnum, campusid, languagenum,sectionnum);
		}
		return count;
	}
	public long getCapacityByCampus(String institutionnum,String campusid,String languagenum,String sectionnum) {
		String hql = "select sum(a.exPhysicexamroom.capacity) from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exLanguage.languagenum=:languagenum and " +
				"a.exPhysicexamroom.exCampus.id=:campusid " +
				"and a.exSection.sectionnum=:sectionnum " +
				"and a.exSection.operateflag='1'";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum).setParameter("campusid", campusid)
				.setParameter("sectionnum", Integer.parseInt(sectionnum));
		long result = (query.uniqueResult()==null)?0:Long.valueOf(query.uniqueResult().toString())
				-this.getArrangementSurplusCount(institutionnum, languagenum, sectionnum, campusid);
		return result;
	}
	//
	public long getArrangementCapacity(String institutionnum,String languagenum,String sectionnum) {
		String hql = "select sum(a.exPhysicexamroom.capacity) from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exLanguage.languagenum=:languagenum and a.exSection.operateflag='1' " +
				"and a.exSection.sectionnum=:sectionnum";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum)
				.setParameter("sectionnum", Integer.parseInt(sectionnum));
		long result = (query.uniqueResult()==null)?0:Long.valueOf(query.uniqueResult().toString())
				- this.getArrangementSurplusCount(institutionnum, languagenum, sectionnum);
		return result;
	}
	public List getAllRoom(String institutionnum,String languagenum,String sectionnum) {
		String hql = "select a.exPhysicexamroom.id from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exLanguage.languagenum=:languagenum " +
				"and a.exSection.sectionnum=:sectionnum and a.exSection.operateflag='1'";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum)
				.setParameter("sectionnum", Integer.parseInt(sectionnum));
		return query.list();
	}
	public List getAllRoom(String institutionnum,String languagenum,String sectionnum,String campusid) {
		String hql = "select a.exPhysicexamroom.id from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exLanguage.languagenum=:languagenum " +
				"and a.exSection.sectionnum=:sectionnum and a.exSection.operateflag='1' " +
				"and a.exPhysicexamroom.exCampus.id=:campusid";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum)
				.setParameter("sectionnum", Integer.parseInt(sectionnum))
				.setParameter("campusid", campusid);
		return query.list();
	}
	public long getArrangementSurplusCount(String institutionnum,String languagenum,String sectionnum) {
		List rooms = this.getAllRoom(institutionnum,languagenum ,sectionnum);
		long count = 0;
		for(Object room:rooms) {
			String roomId = room.toString();
			count += this.getStudentCountByPhysicexamroom(institutionnum, sectionnum, roomId);
		}
		return count;
	}
	public long getArrangementSurplusCount(String institutionnum,String languagenum,String sectionnum,String campusid) {
		List rooms = this.getAllRoom(institutionnum,languagenum ,sectionnum,campusid);
		long count = 0;
		for(Object room:rooms) {
			String roomId = room.toString();
			count += this.getStudentCountByPhysicexamroom(institutionnum, sectionnum, roomId);
		}
		return count;
	}
	public long getStudentCountByPhysicexamroom(String institutionnum,String sectionnum,String physicexamroomId) {
		String hql = "select a.id from cn.hdu.examsignup.model.ExStudent as a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exArrangementByOperatearrangeid.exSection.sectionnum=:sectionnum " +
				"and a.exArrangementByOperatearrangeid.exPhysicexamroom.id=:physicexamroomId "; 
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("sectionnum", Integer.parseInt(sectionnum))
				.setParameter("physicexamroomId", physicexamroomId);
		return query.list().size();		
	}
	public List getSection(String institutionnum,String languagenum) {
		String hql = "select a.exSection.sectionnum from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exLanguage.languagenum=:languagenum " +
				"and a.exSection.operateflag='1' group by a.exSection.sectionnum"; 
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum);
		return query.list();
	}
	public List getSection(String institutionnum,String languagenum,String campusid) {
		String hql = "select a.exSection.sectionnum from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exLanguage.languagenum=:languagenum " +
				"and a.exPhysicexamroom.exCampus.id=:campusid " +
				"and a.exSection.operateflag='1' group by a.exSection.sectionnum"; 
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum);
		return query.list();
	}
	//获取某语种，某校区教室总容量

	
	public void clearData(String institutionnum) {
		String hql;
		Query query;
		hql = "select a.id from cn.hdu.examsignup.model.ExArrangement a where " +
				"a.exInstitution.institutionnum=:institutionnum";
		query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum);
		List deleteList = query.list();
		if(deleteList.size() == 0) return;
		hql = "delete from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.id in(:list)";
		query = this.getCurrentSession().createQuery(hql).setParameterList("list", deleteList);
		query.executeUpdate();
	
	}
}
