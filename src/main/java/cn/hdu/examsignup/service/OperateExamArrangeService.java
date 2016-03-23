package cn.hdu.examsignup.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.ArrangeSupervisorDao;
import cn.hdu.examsignup.dao.CampusDao;
import cn.hdu.examsignup.dao.OperateExamArrangeDao;
import cn.hdu.examsignup.dao.PhysiceexamroomDao;
import cn.hdu.examsignup.dao.StudentDao;
import cn.hdu.examsignup.model.ExArrangement;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExLanguage;
import cn.hdu.examsignup.model.ExPhysicexamroom;
import cn.hdu.examsignup.model.ExSection;
import cn.hdu.examsignup.model.ExStudent;

@Service
public class OperateExamArrangeService {
	
	@Autowired
	ArrangeSupervisorDao arrangeSupervisorDao;
	@Autowired
	OperateExamArrangeDao operateExamArrangeDao;
	@Autowired
	PhysiceexamroomDao physicexamroomDao;
	@Autowired
	StudentDao studentDao;
	@Autowired
	CampusDao campusDao;
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getLoadSignedLanguage(String institutionnum) {
		List<Map> languageList = this.operateExamArrangeDao.getLoadSignedLanguage(institutionnum);
		for(Map element : languageList) {
			String language = element.get("languagenum") + "," + element.get("languagename");
			element.put("languagename", language);
		}
		return languageList;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadArrangeInfo(String institutionnum, String languageNum) {
		List<Map> result = this.operateExamArrangeDao.loadArrangeInfo(institutionnum,languageNum);
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadAvailableOperateExamroom(String institutionnum,int sectionnum,String languagenum) {
		List<Map> result = this.operateExamArrangeDao.loadAvailableOperateExamroom(institutionnum, languagenum,sectionnum);
		for(Map element:result) {
			String physicexamroomId = element.get("id").toString();
			long arrangeStudentCount = this.operateExamArrangeDao.getStudentCountByPhysicexamroom(institutionnum,String.valueOf(sectionnum),physicexamroomId);
			long capacity = Integer.parseInt(element.get("capacity").toString());
			capacity -= arrangeStudentCount;
			element.put("capacity", capacity);
		}
		return result;
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadSelectedOperateExamroom(String institutionnum, int sectionnum,String languagenum) {
		List<Map> result = this.operateExamArrangeDao.loadSelectedOperateExamroom(institutionnum,sectionnum,languagenum);
		for(Map element:result) {
			String physicexamroomId = element.get("id").toString();
			long arrangeStudentCount = this.operateExamArrangeDao.getStudentCountByPhysicexamroom(institutionnum,String.valueOf(sectionnum),physicexamroomId);
			long capacity = Integer.parseInt(element.get("capacity").toString());
			capacity -= arrangeStudentCount;
			element.put("capacity", capacity);
		}
		return result;
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadAllSections(String institutionnum) {
		return this.operateExamArrangeDao.loadAllSections(institutionnum);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveOperateExamroom(ExSection section,ExLanguage language,ExInstitution institution,
			List<Map> changedItems) {
		Integer remove=0,added=0;
		for(Map element:changedItems){
			if(((String)element.get("flag")).equals("old")){
				remove++;
				ExArrangement exArrangement=operateExamArrangeDao.findByInfo(section.getSectionnum(),institution.getInstitutionnum(),
						((String)element.get("value")).trim(),language.getLanguagenum());
				if(exArrangement==null)
					return "{ success: false, errors:{info: '无法找到第"+section.getSectionnum()+"场次中"+((String)element.get("text"))+"考场！'}}";
				if(exArrangement.getExStudentsForOperatearrangeid().size()!=0)
					return "{ success: false, errors:{info: '第"+section.getSectionnum()+"场次中"+((String)element.get("text"))+"考场已有安排考生！'}}";
				operateExamArrangeDao.delete(exArrangement);
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
				if(!exPhysicexamroom.getOperateflag().equals("1"))
					return "{ success: false, errors:{info: '考场"+((String)element.get("text"))+"不是上机考场！'}}";
				if(!language.getOperateflag().equals("1"))
					return "{ success: false, errors:{info: '"+language.getName()+"不包含上机考试！'}}";
				if(!section.getOperateflag().equals("1"))
					return "{ success: false, errors:{info: '第"+section.getSectionnum().toString()+"场次不为上机场次！'}}";
				operateExamArrangeDao.save(exArrangement);
			}
		}
		return "{ success: true, errors:{info: '第"+section.getSectionnum().toString()+
				"场次增加"+added.toString()+"个考场，删除"+remove.toString()+"个考场！'}}";
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getUnarrangedExamroom(String institutionnum,String languagenum,String arrangeid) {
		List<Map> result = this.operateExamArrangeDao.getUnarrangedExamroom(institutionnum, languagenum, arrangeid);
		for(Map element:result) {
			String examroomnum = element.get("examroomnum").toString();
			long capacity = this.operateExamArrangeDao.getExamroomCapacity(institutionnum,examroomnum);
			element.put("capacity",capacity);
		}
		return result;
	}

	//获取已经安排的考生数据
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadSpecialArrangedStudent(String institutionnum,
			String arrangeid,String languagenum) {
		return this.operateExamArrangeDao.loadSpecialArrangedStudent(institutionnum,arrangeid,languagenum);
	}
	//获取未安排的考生数据
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadSpecialUnarrangedStudent(String institutionnum,String languagenum) {
		return this.operateExamArrangeDao.loadSpecialUnarrangedStudent(institutionnum,languagenum);
	}
	//安排考生
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveArrangedStudent(List<Map> readyToArrange, String arrangeid) {
		// TODO Auto-generated method stub
		ExArrangement arrangement = this.operateExamArrangeDao.findById(arrangeid);
		String schoolnum = arrangement.getExInstitution().getInstitutionnum();
		String sectionnum = arrangement.getExSection().getSectionnum().toString();
		String physicexamroomid = arrangement.getExPhysicexamroom().getId();
		long operateSeat = this.operateExamArrangeDao.getStudentCountByPhysicexamroom(schoolnum, sectionnum, physicexamroomid);
		return this.studentDao.saveArrangedStudent(readyToArrange,arrangement,operateSeat);
	}
	//自动安排
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String autoArrange(String institutionnum,String languagenum){
		long capacity = this.operateExamArrangeDao.getArrangementCapacity(institutionnum,languagenum);
		long totalStudents = this.studentDao.getStudentCountByLang(institutionnum, languagenum);
		List<Map> campuses = this.campusDao.getCampus(institutionnum);
		if(capacity< totalStudents) {
			return "{success: false,info: {errors: '上机教室的总容量不足以安排所有考生，请添加足够的上机教室后，再进行安排!'} }";
		} else {
			if(campuses.size()==0)
				return "{success: false,info: {errors: '校区信息不存在，您可能需要重新导入学生信息表！!'} }";
			for(Map campus:campuses){
				String campusid = campus.get("id").toString();
				long campusCapacity = this.operateExamArrangeDao.getCapacityByCampus(institutionnum, campusid, languagenum);
				long campusStudentTotal = this.studentDao.getStudentTotalByCampus(institutionnum,languagenum,campusid);
				if(campusStudentTotal > campusCapacity) {
					return "{success: false,info: {errors: '" + campus.get("name").toString() 
							+ "的上机教室的总容量不足以安排所有考生，请添加足够的上机教室后，再进行安排!'} }";
				}
			}
			return this.autoArrange(institutionnum,languagenum,campuses);
		}

	}
	//arrange
	public String autoArrange(String institutionnum,String languagenum,List<Map> campuses){
		String hql;
		ExStudent exStudent;
		for(Map campus:campuses) {       //获取校区，根据校区安排
			String campusid = campus.get("id").toString();
			List<ExArrangement> arrangements = this.operateExamArrangeDao.getArrangement(institutionnum, campusid, languagenum);
			for(ExArrangement element:arrangements) {
				String sectionnum = element.getExSection().getSectionnum().toString();
				String physicexamroomId = element.getExPhysicexamroom().getId();
				long operateSeat = this.operateExamArrangeDao.getStudentCountByPhysicexamroom(institutionnum, sectionnum, physicexamroomId);
				long capacity = element.getExPhysicexamroom().getCapacity() - operateSeat;
				for(long i=0;i<capacity;++i) {
					exStudent = studentDao.getOneUnarrangedStudent(institutionnum,campusid,languagenum,"operate");
					if(null == exStudent) {
						i = capacity;
						continue;
					}
					operateSeat ++;
					exStudent.setExArrangementByOperatearrangeid(element);
					exStudent.setOperateseat(Integer.valueOf((int)operateSeat));
					studentDao.update(exStudent);
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
		return this.studentDao.cancelArrange(institutionnum,languagenum,"operate");
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String deleteArrangedStudent(List<Map> unArranged) {
		String flag = "operate";
		return this.studentDao.deleteArrangedStudent(unArranged,flag);
	}
	//删除已安排的考场
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String deleteArrangedExamroom(List<Map> examroom,String institutionnum) {
		return this.studentDao.deleteArrangedExamroom(examroom,institutionnum,"operate");
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String getStatisticsByLang(String institutionnum,String languagenum) {
		long totalCount = this.studentDao.getStudentCountByLang(institutionnum,languagenum);
		long unArrangeCount = this.studentDao.getUnarrangedStudentCountByLang(institutionnum, languagenum,"operate");
		long arrangeCount = totalCount- unArrangeCount;
		long capacity =  this.operateExamArrangeDao.getArrangementCapacity(institutionnum,languagenum);
		String result = "教室总余量: " + capacity + ".报名总人数：" + totalCount + ".已安排考场学生数：" + arrangeCount +".未安排考场学生数：" + unArrangeCount;
		return result;
	}
	//按校区获取统计信息
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<Map> getStatisticsByLangAndCampus(String institutionnum,String languagenum) {
		List<Map> campus=this.campusDao.getCampus(institutionnum);
		List<Map> result = new ArrayList<Map>();
		for(Map element:campus){
			Map tempMap=new HashMap();
			String campusid=element.get("id").toString();
			long totalCount = this.studentDao.getStudentCountByLang(institutionnum,languagenum,campusid);
			if(totalCount==0)
				continue;
			long unArrangeCount = this.studentDao.getUnarrangedStudentCountByLang(institutionnum, languagenum,campusid,"operate");
			long arrangeCount = totalCount- unArrangeCount;
			long capacity =  this.operateExamArrangeDao.getCapacityByCampus(institutionnum,campusid,languagenum);
			tempMap.put("id", campusid);
			tempMap.put("campusname", (String)element.get("name"));
			tempMap.put("arrangedNum", arrangeCount);
			tempMap.put("unArrangeCount", unArrangeCount);
			tempMap.put("capacity", capacity);
			result.add(tempMap);
		}
		return result;
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String arrangeByExamroom(String institutionnum,String arrangeid,String examroomnum) {
		String flag = "operate";
		return this.studentDao.arrangeByExamroom(institutionnum, arrangeid, examroomnum,flag);
	}
	//根据arrangeid获取考场
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getArrangedExamroom(String arrangeid,String institutionnum) {
		List<Map> result = this.operateExamArrangeDao.getArrangedExamroom(arrangeid);
		for(Map element:result) {
			String examroomnum = element.get("examroomnum").toString();
			long capacity = this.operateExamArrangeDao.getExamroomCapacity(institutionnum,examroomnum);
			element.put("capacity",capacity);
		}
		return result;
	}

}
