package cn.hdu.examsignup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.ArrangeSupervisorDao;
import cn.hdu.examsignup.dao.CampusDao;
import cn.hdu.examsignup.dao.TheoryExamArrangeDao;
import cn.hdu.examsignup.dao.PhysiceexamroomDao;
import cn.hdu.examsignup.dao.StudentDao;
import cn.hdu.examsignup.model.ExArrangement;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExLanguage;
import cn.hdu.examsignup.model.ExPhysicexamroom;
import cn.hdu.examsignup.model.ExSection;
import cn.hdu.examsignup.model.ExStudent;

@Service
public class TheoryExamArrangeService {

	@Autowired
	ArrangeSupervisorDao arrangeSupervisorDao;
	@Autowired
	TheoryExamArrangeDao theoryExamArrangeDao;
	@Autowired
	PhysiceexamroomDao physicexamroomDao;
	@Autowired
	StudentDao studentDao;
	@Autowired
	CampusDao campusDao;
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getLoadSignedLanguage(String institutionnum) {
		List<Map> languagesList = this.theoryExamArrangeDao.getLoadSignedLanguage(institutionnum);
		for(Map element : languagesList) {
			String language = element.get("languagenum") + "," + element.get("languagename");
			element.put("languagename", language);
		}
		return languagesList;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadArrangeInfo(String institutionnum, String languageNum) {
		List<Map> result = this.theoryExamArrangeDao.loadArrangeInfo(institutionnum,languageNum);
		for(Map element:result) {
			String arrangeid = element.get("id").toString();
			List examrooms = this.theoryExamArrangeDao.getArrangementExamrooms(arrangeid);
			element.put("examrooms", examrooms);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadAvailableTheoryExamroom(String institutionnum,int sectionnum,String languagenum) {
		List<Map> result = this.theoryExamArrangeDao.loadAvailableTheoryExamroom(institutionnum, languagenum);
		for(Map element:result) {
			String physicexamroomId = element.get("id").toString();
			long arrangeStudentCount = this.theoryExamArrangeDao.getStudentCountByPhysicexamroom(institutionnum,sectionnum,physicexamroomId);
			long capacity = Integer.parseInt(element.get("capacity").toString());
			capacity -= arrangeStudentCount;
			element.put("capacity", capacity);
		}
		return result;
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadSelectedTheoryExamroom(String institutionnum, int sectionnum,String languagenum) {
		List<Map> result = this.theoryExamArrangeDao.loadSelectedTheoryExamroom(institutionnum,sectionnum,languagenum);
		for(Map element:result) {
			String physicexamroomId = element.get("id").toString();
			long arrangeStudentCount = this.theoryExamArrangeDao.getStudentCountByPhysicexamroom(institutionnum,sectionnum,physicexamroomId);
			long capacity = Integer.parseInt(element.get("capacity").toString());
			capacity -= arrangeStudentCount;
			element.put("capacity", capacity);
		}
		return result;
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadAllSections(String institutionnum) {
		return this.theoryExamArrangeDao.loadAllSections(institutionnum);
	}

	//
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveTheoryExamroom(ExSection section,ExLanguage language,ExInstitution institution,
			List<Map> changedItems) {
		Integer remove=0,added=0;
		for(Map element:changedItems){
			if(((String)element.get("flag")).equals("old")){
				remove++;
				ExArrangement exArrangement=theoryExamArrangeDao.findByInfo(section.getSectionnum(),institution.getInstitutionnum(),
						((String)element.get("value")).trim(),language.getLanguagenum());
				if(exArrangement==null)
					return "{ success: false, errors:{info: '无法找到第"+section.getSectionnum()+"场次中"+((String)element.get("text"))+"考场！'}}";
				if(exArrangement.getExStudentsForTheoryarrangeid().size()!=0)
					return "{ success: false, errors:{info: '第"+section.getSectionnum()+"场次中"+((String)element.get("text"))+"考场已有安排考生！'}}";
				theoryExamArrangeDao.delete(exArrangement);
			}
			if(((String)element.get("flag")).equals("new")){
				added++;
				ExArrangement exArrangement=new ExArrangement();
				exArrangement.setExInstitution(institution);
				exArrangement.setExSection(section);
				exArrangement.setExLanguage(language);
				ExPhysicexamroom exPhysicexamroom= physicexamroomDao.findById(((String)element.get("value")));
				exArrangement.setExPhysicexamroom(exPhysicexamroom);
				
				if(exPhysicexamroom==null)
					return "{ success: false, errors:{info: '找不到"+((String)element.get("text"))+"考场！'}}";
				if(!exPhysicexamroom.getTheoryflag().equals("1"))
					return "{ success: false, errors:{info: '考场"+((String)element.get("text"))+"不是理论考场！'}}";
				if(!language.getTheoryflag().equals("1"))
					return "{ success: false, errors:{info: '"+language.getName()+"不包含理论考试！'}}";
				if(!section.getTheoryflag().equals("1"))
					return "{ success: false, errors:{info: '第"+section.getSectionnum().toString()+"场次不为理论场次！'}}";
				theoryExamArrangeDao.save(exArrangement);
			}
		}
		return "{ success: true, errors:{info: '第"+section.getSectionnum().toString()+
				"场次增加"+added.toString()+"个考场，删除"+remove.toString()+"个考场！'}}";
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadSpecialArrangedStudent(String institutionnum,
			String arrangeid,String languagenum) {
		return this.theoryExamArrangeDao.loadSpecialArrangedStudent(institutionnum,arrangeid,languagenum);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadExamroomItem(String institutionnum, String languagenum,String arrangeid) {
		return this.theoryExamArrangeDao.loadExamroomItem(institutionnum, languagenum,arrangeid);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadStudentBySpecialExamroomAndCampus(String institutionnum,String examroomNum,
			String arrangeid, String languagenum) {
		return this.theoryExamArrangeDao.loadStudentBySpecialExamroomAndCampus(institutionnum,examroomNum,arrangeid, languagenum);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveArrangedStudent(List<Map> readyToArrange, String arrangeid) {
		// TODO Auto-generated method stub
		return this.studentDao.saveArrangedStudent(readyToArrange,arrangeid);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String deleteArrangedStudent(List<Map> unArranged) {
		String flag = "theory";
		return this.studentDao.deleteArrangedStudent(unArranged,flag);
	}
	//获取统计信息
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String getStatisticsByLang(String institutionnum,String languagenum,String secionnum) {
		long totalCount = this.studentDao.getStudentCountByLang(institutionnum,languagenum);
		long unArrangeCount = this.studentDao.getUnarrangedStudentCountByLang(institutionnum, languagenum,"theory");
		long arrangeCount = totalCount- unArrangeCount;
		long capacity =  this.theoryExamArrangeDao.getArrangementCapacity(institutionnum,languagenum,Integer.parseInt(secionnum));
		String result = "教室总余量: " + capacity + ".已安排考场学生数：" + arrangeCount +".未安排考场学生数：" + unArrangeCount;
		//String result =  "已安排考场学生数：" + arrangeCount +".未安排考场学生数：" + unArrangeCount;
		return result;
	}
	//按校区获取统计信息
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<Map> getStatisticsByLangAndCampus(String institutionnum,String languagenum,String sectionnum) {
		List<Map> campus=this.campusDao.getCampus(institutionnum);
		List<Map> result = new ArrayList<Map>();
		for(Map element:campus){
			Map tempMap=new HashMap();
			String campusid=element.get("id").toString();
			long totalCount = this.studentDao.getStudentCountByLang(institutionnum,languagenum,campusid);
			if(totalCount==0)
				continue;
			long unArrangeCount = this.studentDao.getUnarrangedStudentCountByLang(institutionnum, languagenum,campusid,"theory");
			long arrangeCount = totalCount- unArrangeCount;
			long capacity =  this.theoryExamArrangeDao.getArrangementCapacity(institutionnum,languagenum,campusid,sectionnum);
			tempMap.put("id", campusid);
			tempMap.put("campusname", (String)element.get("name"));
			tempMap.put("arrangedNum", arrangeCount);
			tempMap.put("unArrangeCount", unArrangeCount);
			tempMap.put("capacity", capacity);
			result.add(tempMap);
		}
		return result;
	}
	//根据arrangeid获取考场
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getArrangedExamroom(String arrangeid,String institutionnum) {
		List<Map> result = this.theoryExamArrangeDao.getArrangedExamroom(arrangeid);
		for(Map element:result) {
			String examroomnum = element.get("examroomnum").toString();
			long capacity = this.theoryExamArrangeDao.getExamroomCapacity(institutionnum,examroomnum);
			element.put("capacity",capacity);
		}
		return result;
	}
	//获取未安排的逻辑考场
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getUnarrangedExamroom(String institutionnum,String languagenum,String arrangeid) {
		List<Map> result = this.theoryExamArrangeDao.getUnarrangedExamroom(institutionnum, languagenum, arrangeid);
		for(Map element:result) {
			String examroomnum = element.get("examroomnum").toString();
			long capacity = this.theoryExamArrangeDao.getExamroomCapacity(institutionnum,examroomnum);
			element.put("capacity",capacity);
		}
		return result;
	}
	//删除已安排的考场
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String deleteArrangedExamroom(List<Map> examroom,String institutionnum) {
		String operateOrTheory = "theory";
		return this.studentDao.deleteArrangedExamroom(examroom,institutionnum,operateOrTheory);
	}
	//安排理论考场
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String arrangeByExamroom(String institutionnum,String arrangeid,String examroomnum) {
		String operateOrTheory="theory";
		return this.studentDao.arrangeByExamroom(institutionnum, arrangeid, examroomnum,operateOrTheory);
	}
	//自动安排
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String autoArrange(String institutionnum,String languagenum,String sectionnum){
		this.studentDao.cancelArrange(institutionnum, languagenum, "theory");
		long capacity = this.theoryExamArrangeDao.getArrangementCapacity(institutionnum,languagenum,Integer.parseInt(sectionnum));
		long totalStudents = this.studentDao.getStudentCountByLang(institutionnum, languagenum);
		List<Map> campuses = this.campusDao.getCampus(institutionnum);
		if(capacity< totalStudents) {
			return "{success: false,info: {errors: '教室的总余量不足以安排所有考生，请添加足够的教室后，再进行安排!'} }";
		} else {
			if(campuses.size()==0)
				return "{success: false,info: {errors: '校区信息不存在，您可能需要重新导入学生信息表！!'} }";
			for(Map campus:campuses){
				String campusid = campus.get("id").toString();
				long campusCapacity = this.theoryExamArrangeDao.getArrangementCapacity(institutionnum, languagenum,campusid, sectionnum);
				long campusStudentTotal = this.studentDao.getStudentTotalByCampus(institutionnum,languagenum,campusid);
				if(campusStudentTotal > campusCapacity) {
					return "{success: false,info: {errors: '" + campus.get("name").toString() 
							+ "的教室的总余量不足以安排所有考生，请添加足够的教室后，再进行安排!'} }";
				}
			}
			return this.autoArrange(institutionnum,languagenum,campuses);
		}
	}
	//arrange
	public String autoArrange(String institutionnum,String languagenum,List<Map> campuses){
		String hql;
		Query query;
		ExStudent exStudent;
		for(Map campus:campuses) {       //获取校区，根据校区安排
			String campusid = campus.get("id").toString();
			List<ExArrangement> arrangements = this.theoryExamArrangeDao.getArrangement(institutionnum, campusid, languagenum);
			for(ExArrangement element:arrangements) {
				String arrangeid = element.getId();
				int sectionnum = element.getExSection().getSectionnum();
				String physicexamroomId = element.getExPhysicexamroom().getId();
				long capacity = element.getExPhysicexamroom().getCapacity() - this.theoryExamArrangeDao.getStudentCountByPhysicexamroom(institutionnum, sectionnum, physicexamroomId);
				String examroomnum = this.theoryExamArrangeDao.getOneUnarrangedExamroom(institutionnum,languagenum,campusid);
				long examroomCapacity = this.theoryExamArrangeDao.getExamroomCapacity(institutionnum, examroomnum);
				while ((capacity - examroomCapacity) >= 0) {
					studentDao.arrangeByExamroom(institutionnum, arrangeid, examroomnum);
					capacity -= examroomCapacity;
					examroomnum = this.theoryExamArrangeDao.getOneUnarrangedExamroom(institutionnum,languagenum,campusid);
					if(examroomnum == null) {
						break;
					}
					examroomCapacity = this.theoryExamArrangeDao.getExamroomCapacity(institutionnum, examroomnum);
				}
			}
		}
		return "{success: true,info: {errors: '安排考生考试成功！'}}";
	}
	//取消安排
		@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
		public String cancelArrange(String institutionnum,String languagenum){
			try{
				this.arrangeSupervisorDao.clearData(institutionnum);
			} catch(Exception e) {
				e.printStackTrace();
				return "{success: false,info: {errors: '取消安排失败！'}}";
			}
			return this.studentDao.cancelArrange(institutionnum,languagenum,"theory");
		}
}
