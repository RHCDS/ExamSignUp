package cn.hdu.examsignup.dao;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import cn.hdu.examsignup.model.ExArrangement;
import cn.hdu.examsignup.model.ExStudent;

public class TheoryExamArrangeDao extends AbstractHibernateDao<ExArrangement> {

	TheoryExamArrangeDao() {
		super(ExArrangement.class);
	}
	//获取理论考试语种
	public List<Map> getLoadSignedLanguage(String institutionnum) {           
		String hql = "select new map(a.exLanguage.languagenum as languagenum,a.exLanguage.name as languagename) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.exInstitution.institutionnum=:institutionnum and a.exLanguage.theoryflag=:theoryflag "
				+ "group by a.exLanguage.languagenum";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("theoryflag", "1");

		return query.list();
	}
	//根据arrangeid获取学生总数
		public long getStudentCountByArrangeId(String arrangeid,String institutionnum) {
			String hql = "select a.id from cn.hdu.examsignup.model.ExStudent as a " +
					"where a.exInstitution.institutionnum=:institutionnum " +
					"and a.exArrangementByTheoryarrangeid.id=:arrangeid";
			long result = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum).setParameter("arrangeid", arrangeid).list().size();
			return result;
		}
	public long getStudentCountByPhysicexamroom(String institutionnum,int sectionnum,String physicexamroomId) {
		String hql = "select a.id from cn.hdu.examsignup.model.ExStudent as a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exArrangementByTheoryarrangeid.exSection.sectionnum=:sectionnum " +
				"and a.exArrangementByTheoryarrangeid.exPhysicexamroom.id=:physicexamroomId "; 
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("sectionnum", sectionnum)
				.setParameter("physicexamroomId", physicexamroomId);
		return query.list().size();		
	}
	//获取编排信息
	public List<Map> loadArrangeInfo(String institutionnum, String languageNum) {   
		List<Map> result;
		String temp;
		Date startTime;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String hql = "select new map(a.id as id,a.exSection.id as sectionid, "
				+ "a.exSection.starttime as starttime,a.exSection.sectionnum as sectionnum, "
				+ "a.exPhysicexamroom.id as physicexamroomid,a.exPhysicexamroom.roomlocation as roomlocation, "
				+ "a.exPhysicexamroom.capacity as capacity,a.exPhysicexamroom.exCampus.name as campusname) "
				+ "from cn.hdu.examsignup.model.ExArrangement a "
				+ "where a.exInstitution.institutionnum=:institutionnum and a.exLanguage.languagenum=:languageNum "
				+ "and a.exSection.theoryflag=:theoryflag order by a.exSection.sectionnum, a.exPhysicexamroom.exCampus.name,a.exPhysicexamroom.roomlocation";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("languageNum", languageNum)
				.setParameter("theoryflag", "1");

		result = query.list();
		for (Map element : result) {
			int sectionnum = Integer.parseInt(element.get("sectionnum").toString());
			String physicexamroomId = element.get("physicexamroomid").toString();
			long surplus = Long.valueOf(element.get("capacity").toString()) - this.getStudentCountByPhysicexamroom(institutionnum, sectionnum,physicexamroomId);
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
	//获取可用的理论考试考场
	public List<Map> loadAvailableTheoryExamroom(String institutionnum,String languagenum) {
		List<Map> result;
		String hql = "select new map(a.id as id,a.roomlocation as location,a.capacity as capacity, a.exCampus.name as campusname) " +
				"from cn.hdu.examsignup.model.ExPhysicexamroom a " +
				"where a.id not in " +
				"(select b.exPhysicexamroom.id from cn.hdu.examsignup.model.ExArrangement b " +
				"where b.exPhysicexamroom.theoryflag='1' " +
				"and b.exInstitution.institutionnum=:institutionnum " +
				"and b.exLanguage.languagenum=:languagenum " +
				"group by b.exPhysicexamroom.id) " +
				"and a.exInstitution.institutionnum=:institutionnum " +
				"and a.theoryflag='1' group by a.id";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum);
		result = query.list();
		return result;
	}
	//获取选定理论考试考场信息
	public List<Map> loadSelectedTheoryExamroom(String institutionnum,
			int sectionnum, String languagenum) {
		List<Map> result;

		String hql = "select new map(a.exPhysicexamroom.id as id,a.exPhysicexamroom.roomlocation as location,a.exPhysicexamroom.capacity as capacity, a.exPhysicexamroom.exCampus.name as campusname) "
				+ "from cn.hdu.examsignup.model.ExArrangement a "
				+ "where a.exSection.sectionnum =:sectionnum "
				+ "and a.exInstitution.institutionnum=:institutionnum  and a.exPhysicexamroom.theoryflag=:theoryflag and a.exLanguage.languagenum=:languagenum "
				+ "group by a.exPhysicexamroom.id";

		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("sectionnum", sectionnum)
				.setParameter("theoryflag", "1")
				.setParameter("languagenum", languagenum);

		return query.list();
	}
	//获取所以场次信息
	public List<Map> loadAllSections(String institutionnum) {
		List<Map> result;
		String temp;
		Date startTime;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		String hql = "select new map(a.id as id,a.starttime as starttime,a.sectionnum as sectionnum) "
				+ "from cn.hdu.examsignup.model.ExSection a "
				+ "where a.exInstitution.institutionnum=:institutionnum "
				+ "and a.theoryflag=:theoryflag ";
		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("theoryflag", "1");

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
	//
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
	//
	public List<Map> loadExamroomItem(String institutionnum,
			String languagenum, String arrangeid) {
		String hql;
		Query query;
		if (arrangeid.trim().isEmpty()) {
			hql = "select new map(substring(a.examnum,11,3) as examroomnum,concat(concat(a.exLanguage.name,'(考场号'),concat(substring(a.examnum,11,3),')')) as info) "
					+ "from cn.hdu.examsignup.model.ExStudent a "
					+ "where a.exLanguage.theoryflag=:theoryflag and a.exLanguage.languagenum=:languagenum and a.exInstitution.institutionnum=:institutionnum "
					+ "group by substring(a.examnum,11,3)";

			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("languagenum", languagenum)
					.setParameter("theoryflag", "1");
		}else{
			hql = "select a.exPhysicexamroom.exCampus.id from cn.hdu.examsignup.model.ExArrangement a "
					+ "where a.id=:arrangeid";
			query = this.getCurrentSession().createQuery(hql)
					.setParameter("arrangeid", arrangeid);
			List<String> temp = query.list();
			String campusid;
			if (temp.size() != 1)
				return null;
			campusid = temp.get(0);
			
			hql = "select new map(substring(a.examnum,11,3) as examroomnum,concat(concat(a.exLanguage.name,'(考场号'),concat(substring(a.examnum,11,3),')')) as info) "
					+ "from cn.hdu.examsignup.model.ExStudent a "
					+ "where a.exLanguage.theoryflag=:theoryflag and a.exLanguage.languagenum=:languagenum and a.exInstitution.institutionnum=:institutionnum "
					+ "and a.exCampus.id=:campusid "
					+ "group by substring(a.examnum,11,3)";

			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("languagenum", languagenum)
					.setParameter("theoryflag", "1")
					.setParameter("campusid", campusid);
		}
		return query.list();
	}
	//
	public List<Map> loadSpecialArrangedStudent(String institutionnum,
			String arrangeid, String languagenum) {

		String hql = "select new map(a.id as id,a.name as name,a.examnum as examnum) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.exInstitution.institutionnum=:institutionnum "
				+ "and a.exArrangementByTheoryarrangeid.id=:arrangeid and a.exLanguage.languagenum=:languagenum";

		Query query = this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("arrangeid", arrangeid)
				.setParameter("languagenum", languagenum);
		return query.list();
	}
	//
	public List<Map> loadStudentBySpecialExamroomAndCampus(
			String institutionnum, String examroomNum, String arrangeid,
			String languagenum) {
		String hql;
		Query query;
		if (arrangeid.trim().isEmpty()) {
			hql = "select new map(a.id as id,a.name as name,a.examnum as examnum,a.exCampus.name as campusname,a.studentnum as studentnum) "
					+ "from cn.hdu.examsignup.model.ExStudent a "
					+ "where a.exInstitution.institutionnum=:institutionnum "
					+ "and substring(a.examnum,11,3)=:examroomNum and a.exLanguage.languagenum=:languagenum";

			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("examroomNum", examroomNum)
					.setParameter("languagenum", languagenum);
		} else {
			hql = "select a.exPhysicexamroom.exCampus.id from cn.hdu.examsignup.model.ExArrangement a "
					+ "where a.id=:arrangeid";
			query = this.getCurrentSession().createQuery(hql)
					.setParameter("arrangeid", arrangeid);
			List<String> temp = query.list();
			String campusid;
			if (temp.size() != 1)
				return null;
			campusid = temp.get(0);
			hql = "select new map(a.id as id,a.name as name,a.examnum as examnum,a.exCampus.name as campusname,a.studentnum as studentnum) "
					+ "from cn.hdu.examsignup.model.ExStudent a "
					+ "where a.exInstitution.institutionnum=:institutionnum "
					+ "and substring(a.examnum,11,3)=:examroomNum and a.exLanguage.languagenum=:languagenum "
					+ "and a.exCampus.id=:campusid";

			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("examroomNum", examroomNum)
					.setParameter("languagenum", languagenum)
					.setParameter("campusid", campusid);
		}
		return query.list();
	}
	//根据arrangeid获取该arrangement安排的考场
	public List getArrangementExamrooms(String arrangeid) {
		String hql = "select substring(a.examnum,11,3) as examroom from cn.hdu.examsignup.model.ExStudent a where " +
				"a.exArrangementByTheoryarrangeid.id=:arrangeid group by substring(a.examnum,11,3)";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid);
		List result = query.list();
		return result;
	}
	//获取某语种，教室总容量
	public long getArrangementCapacity(String institutionnum,String languagenum,int sectionnum){
		String hql = "select sum(a.exPhysicexamroom.capacity) from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exLanguage.languagenum=:languagenum and a.exSection.theoryflag='1'";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum);
		long result = (query.uniqueResult()==null)?0:Long.valueOf(query.uniqueResult().toString());
		result -= this.getArrangementSurplusCount(institutionnum, languagenum, sectionnum);
		return result;
	}
	public List getAllRoomOfLanguage(String languagenum,String institutionnum) {
		String hql = "select a.exPhysicexamroom.id from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.exLanguage.languagenum=:languagenum and a.exSection.theoryflag='1' " +
				"and a.exInstitution.institutionnum=:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("languagenum", languagenum)
				.setParameter("institutionnum", institutionnum);
		return query.list();
	}
	public List getAllRoomOfLanguage(String languagenum,String institutionnum,String campusid) {
		String hql = "select a.exPhysicexamroom.id from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.exPhysicexamroom.exCampus.id=:campusid " +
				"and a.exLanguage.languagenum=:languagenum and a.exSection.theoryflag='1' " +
				"and a.exInstitution.institutionnum=:institutionnum";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("languagenum", languagenum)
				.setParameter("institutionnum", institutionnum)
				.setParameter("campusid", campusid);
		return query.list();
	}
	public long getArrangementSurplusCount(String institutionnum,String languagenum,int sectionnum) {
		List rooms = this.getAllRoomOfLanguage(languagenum, institutionnum);
		long count = 0;
		for(Object room:rooms) {
			String roomId = room.toString();
			count += this.getStudentCountByPhysicexamroom(institutionnum, sectionnum, roomId);
		}
		return count;
	}
	public long getArrangementSurplusCount(String institutionnum,String languagenum,int sectionnum,String campusid) {
		List rooms = this.getAllRoomOfLanguage(languagenum, institutionnum,campusid);
		long count = 0;
		for(Object room:rooms) {
			String roomId = room.toString();
			count += this.getStudentCountByPhysicexamroom(institutionnum, sectionnum, roomId);
		}
		return count;
	}
	//获取某语种，教室总容量,某校区
	public long getArrangementCapacity(String institutionnum,String languagenum,String campusid,String sectionnum){
		String hql = "select sum(a.exPhysicexamroom.capacity) from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exLanguage.languagenum=:languagenum and a.exSection.theoryflag='1'" +
				" and a.exPhysicexamroom.exCampus.id=:campusid";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum)
				.setParameter("campusid", campusid);
		long result = (query.uniqueResult()==null)?0:Long.valueOf(query.uniqueResult().toString()) 
				- this.getArrangementSurplusCount(institutionnum, languagenum, Integer.parseInt(sectionnum), campusid);
		return result;
	}
	//获取已经安排的逻辑考场
	public List<Map> getArrangedExamroom(String arrangeid) {
		String hql = "select new map(substring(a.examnum,11,3) as examroomnum,concat('考场号',substring(a.examnum,11,3)) as info,a.exCampus.name as campus) "
				+ "from cn.hdu.examsignup.model.ExStudent a "
				+ "where a.exArrangementByTheoryarrangeid.id=:arrangeid "
				+ "group by substring(a.examnum,11,3)";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("arrangeid", arrangeid);
		return query.list();
	}
	//取得未安排考场（校区）
	public String getOneUnarrangedExamroom(String institutionnum,String languagenum,String campusid) {
		String hql = "select substring(a.examnum,11,3) from cn.hdu.examsignup.model.ExStudent a " 
				+ "where a.exLanguage.theoryflag=:theoryflag and a.exLanguage.languagenum=:languagenum "
				+ "and a.exInstitution.institutionnum=:institutionnum " 
				+ "and a.exCampus.id=:campusid "
				+ "and a.exArrangementByTheoryarrangeid=null and a.examnum is not null "
				+ "group by substring(a.examnum,11,3)";
		Query query =  this.getCurrentSession().createQuery(hql)
				.setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum)
				.setParameter("campusid", campusid)
				.setParameter("theoryflag", "1");
		if(query.list().size() == 0) {
			return null;
		}
		return query.list().get(0).toString();
	}
	//取得未安排的考场
	public List<Map> getUnarrangedExamroom(String institutionnum,String languagenum,String arrangeid) {
		String hql;
		Query query;
		if (arrangeid.trim().isEmpty()) { //不根据校区获取数据
			hql = "select new map(substring(a.examnum,11,3) as examroomnum,concat('考场号',substring(a.examnum,11,3)) as info,a.exCampus.name as campus) "
					+ "from cn.hdu.examsignup.model.ExStudent a "
					+ "where a.exLanguage.theoryflag=:theoryflag and a.exLanguage.languagenum=:languagenum and a.exInstitution.institutionnum=:institutionnum " +
					"and a.exArrangementByTheoryarrangeid=null and  a.examnum is not null "
					+ "group by substring(a.examnum,11,3)";

			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("languagenum", languagenum)
					.setParameter("theoryflag", "1");
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
					+ "where a.exLanguage.theoryflag=:theoryflag and a.exLanguage.languagenum=:languagenum and a.exInstitution.institutionnum=:institutionnum "
					+ "and a.exArrangementByTheoryarrangeid=null "
					+ "and a.exCampus.id=:campusid and  a.examnum is not null "
					+ "group by substring(a.examnum,11,3)";

			query = this.getCurrentSession().createQuery(hql)
					.setParameter("institutionnum", institutionnum)
					.setParameter("languagenum", languagenum)
					.setParameter("theoryflag", "1")
					.setParameter("campusid", campusid);
		}
		return query.list();
	}
	//获取某语种，某校区教室总容量
	public long getCapacityByCampus(String institutionnum,String campusid,String languagenum) {
		String hql = "select sum(a.exPhysicexamroom.capacity) from cn.hdu.examsignup.model.ExArrangement a " +
				"where a.exInstitution.institutionnum=:institutionnum " +
				"and a.exLanguage.languagenum=:languagenum and " +
				"a.exPhysicexamroom.exCampus.id=:campusid and a.exSection.theoryflag='1'";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("languagenum", languagenum).setParameter("campusid", campusid);
		long result = (query.uniqueResult()==null)?0:Long.valueOf(query.uniqueResult().toString()) ;
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
	//根据学校，校区，语种获取arrangement
	public List<ExArrangement> getArrangement(String institutionnum,String campusid,String languagenum){
		String hql = "select a from cn.hdu.examsignup.model.ExArrangement a where " +
				"a.exInstitution.institutionnum=:institutionnum and a.exPhysicexamroom.exCampus.id=:campusid " +
				"and a.exLanguage.languagenum=:languagenum and a.exSection.theoryflag='1' " +
				"order by a.exSection.sectionnum asc,a.exPhysicexamroom.roomlocation asc,a.exPhysicexamroom.capacity asc";
		Query query = this.getCurrentSession().createQuery(hql).setParameter("institutionnum", institutionnum)
				.setParameter("campusid", campusid).setParameter("languagenum", languagenum);
		return query.list();
	}
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
