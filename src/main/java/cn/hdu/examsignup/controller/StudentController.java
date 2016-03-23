package cn.hdu.examsignup.controller;

import java.io.File;


import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.json.types.JsonArray;
import org.directwebremoting.json.types.JsonObject;
import org.directwebremoting.spring.SpringCreator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hdu.examsignup.model.ExStudent;
import cn.hdu.examsignup.model.ExSupervisor;
import cn.hdu.examsignup.model.Function;
import cn.hdu.examsignup.service.ClearHistoryDataService;
import cn.hdu.examsignup.service.ExamNumEditService;
import cn.hdu.examsignup.service.StudentService;



/**
 * 
 * 主要用于管理考生各种基本信息
 * 
 */
@RemoteProxy(creator = SpringCreator.class)
public class StudentController {
	
	@Autowired
	private StudentService studentservice;
	
	@Autowired
	private ClearHistoryDataService clearHistoryDataService;
	@Autowired
	private ExamNumEditService examNumEditService;
	
	@RemoteMethod
	public Map paginationShow(String filterString, String pageNum, String pageSize) {
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		Map map = new HashMap();
		String whereString = Function.initSearchHql_whereString_(filterString);
		Map<String, Object> paramsMap = Function.initSearchValues(filterString);
		List<Map> result = studentservice.getPageStudents(whereString, paramsMap, pageNum, pageSize,schoolnum);
		map.put("totalProperty", studentservice.getStudentsTotalCount(whereString, paramsMap,schoolnum));
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public Map paginationShowForApproval(String filterString, String pageNum, String pageSize) {
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		Map map = new HashMap();
		String whereString = Function.initSearchHql_whereString_(filterString);
		Map<String, Object> paramsMap = Function.initSearchValues(filterString);
		List<Map> result = studentservice.paginationShowForApproval(whereString, paramsMap, pageNum, pageSize,schoolnum);
		map.put("totalProperty", studentservice.getStudentsTotalCount(whereString, paramsMap,schoolnum));
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public Map ExamCollegeGetStudent(String filterString, String pageNum, String pageSize,String institutionnum) {
		//此函数提供给考试院，所以不用session institution
		Map map = new HashMap();
		String whereString = Function.initSearchHql_whereString_(filterString);
		Map<String, Object> paramsMap = Function.initSearchValues(filterString);
		List<Map> result = studentservice.ExamCollegeGetStudent(whereString, paramsMap, pageNum, pageSize,institutionnum);
		map.put("totalProperty", studentservice.getStudentsTotalCount(whereString, paramsMap,institutionnum));
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public Map statisticAllLanguageBySchool(String institutionnum, String category) {
		Map map = new HashMap();
		List<Map> result = studentservice.statisticAllLanguageBySchool(institutionnum, category);
		map.put("totalProperty", result==null?0:result.size());
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public Map AllProvinceStuInfoSum(String institutionnum, String category, boolean isByLanguage) {

			Map map = new HashMap();
			List<Map> result = studentservice.AllProvinceStuInfoSum(institutionnum, category, isByLanguage);
			map.put("totalProperty", result==null?0:result.size());
			map.put("root", result);
			return map;

	}
	
	@RemoteMethod
	public boolean importStudents(List<JSONObject> students){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return studentservice.importStudents(students,schoolnum);
	}
	@RemoteMethod
	public boolean importJFStudents(List<JSONObject> students) {
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		boolean result = this.studentservice.importJFStudents(students, schoolnum);
		if(result == true) {
			try {
				this.studentservice.clearUnPaiedStudent(schoolnum);
			} catch(Exception e) {
				e.printStackTrace();
				result = false;
			}
			
		}
		return result;
	}
	
	@RemoteMethod
	public String checkStudentsData(List<JSONObject> students){
		return studentservice.checkStudentsData(students);
	}
	
	@RemoteMethod
	public String getStudentPhotoPathByStudentNum(String studentnum){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		String   filePath   =    WebContextFactory.get().getSession().getServletContext().getRealPath( "/");
		filePath =  filePath + schoolnum + "/pictures/" + studentnum + ".jpg";
		String studentPhotoPath = "../" + schoolnum + "/pictures/" + studentnum + ".jpg";
		filePath = filePath.replace('\\', '/');
		if(new File(filePath).exists()){
			return studentPhotoPath; 
		}else{
			return "";
		}
		
		
	} 
	
	@RemoteMethod
	public String getStudentPhotoPathByFileName(String filename){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		String studentPhotoPath = "../" + schoolnum + "/pictures/" + filename ;
		return studentPhotoPath;
	} 
	//得到图片的相对路径
	@RemoteMethod
	public String getStudentPhotoPath(){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		String studentPhotoPath = "/" + schoolnum + "/pictures/";
		return studentPhotoPath;
		
	} 
	
	@RemoteMethod
	public boolean deleteStudents(List<String> ids){
		return studentservice.deleteStudents(ids);
	}
	
	@RemoteMethod
	public boolean saveStudent(JSONObject student){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		if(student.get("id").toString().equals("")||student.get("id").toString()==""){
			return  studentservice.saveStudent(student, schoolnum);
		}else
			return studentservice.updateStudent(student, schoolnum);
	}
	
	@RemoteMethod
	public Map findAbsentStudentByExamNum(String examnum){
		return studentservice.findAbsentStudentByExamNum(examnum);
	}
	
	@RemoteMethod
	public Map absentStudentPaginationShow(String pageNum, String pageSize){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		Map map = new HashMap();
		List<Map> result = studentservice.getPageAbsentStudents(pageNum, pageSize,schoolnum);
		map.put("totalProperty", studentservice.getAbsentStudentsTotalCount(schoolnum));
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public String saveAbsentStudent(JSONObject absentstudent){
		return studentservice.saveAbsentStudent(absentstudent);
	}
	
	@RemoteMethod
	public boolean deleteAbsentStudent(List<String> examnums){
		return studentservice.deleteAbsentStudent(examnums);
	}
	
	@RemoteMethod
	public Map findFraudStudentByExamNum(String examnum){
		return studentservice.findFraudStudentByExamNum(examnum);
	}
	@RemoteMethod
	public Map findCheckScoreStudentByExamNum(String examnum){
		return studentservice.findCheckScoreStudentByExamNum(examnum);
	}
	
	@RemoteMethod
	public Map fraudStudentPaginationShow(String pageNum, String pageSize){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		Map map = new HashMap();
		List<Map> result = studentservice.getPageFraudStudents(schoolnum,pageNum, pageSize);
		map.put("totalProperty", studentservice.getFraudStudentsTotalCount(schoolnum));
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public Map checkScoreStudentPaginationShow(String pageNum, String pageSize){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		Map map = new HashMap();
		List<Map> result = studentservice.getPageCheckScoreStudents(schoolnum,pageNum, pageSize);
		map.put("totalProperty", studentservice.getCheckScoreStudentsTotalCount(schoolnum));
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public String saveFraudStudent(JSONObject fraudstudent){
		return studentservice.saveFraudStudent(fraudstudent);
	}
	@RemoteMethod
	public String saveCheckScoreStudent(JSONObject checkscorestudent){
		return studentservice.saveCheckScoreStudent(checkscorestudent);
	}
	@RemoteMethod
	public String reCheck(JSONObject checkscorestudent){
		String IDnum = (String) WebContextFactory.get().getSession().getAttribute("IDnum");
		return studentservice.reCheck(checkscorestudent,IDnum);
	}
	@RemoteMethod
	public boolean deleteFraudStudent(List<String> examnums){
		return studentservice.deleteFraudStudent(examnums);
	}
	@RemoteMethod
	public boolean deleteCheckScoreStudent(List<String> examnums){
		return studentservice.deleteCheckScoreStudent(examnums);
	}
	
	@RemoteMethod
	public List<Map> getCollegeSignUpInfo(){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return studentservice.getCollegeSignUpInfo(schoolnum);
	}
	
	@RemoteMethod
	public Map passStudentShow(String filterString, String pageNum, String pageSize) { //合格学生分页，用于打印证书
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		Map map = new HashMap();
		String whereString = Function.initSearchHql_whereString_(filterString);
		Map<String, Object> paramsMap = Function.initSearchValues(filterString);
		List<Map> result = studentservice.getPassStudents(whereString, paramsMap, pageNum, pageSize,schoolnum);
		map.put("totalProperty", studentservice.getPassStudentsTotalCount(whereString, paramsMap,schoolnum));
		map.put("root", result);
		return map;
	}
	
	//学院成绩统计
	@RemoteMethod
	public List<Map> summaryScore(JSONObject summaryCondition){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return studentservice.summaryScore(summaryCondition, schoolnum);
	}

	//得到语种列表
	@RemoteMethod
	public List<Map> getLanguageList(){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
	
	    List<Map> t = studentservice.getLanguageList(schoolnum);
		return studentservice.getLanguageList(schoolnum);
		
	}
	
	@RemoteMethod
	public List<Map> getLanguageList_theory(){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
	
		return studentservice.getLanguageList_theory(schoolnum);
		
	}
//	public Map getlanguageList(){
//		Map map = new HashMap();
//		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
//		List<Map> result = studentservice.getLanguageList(schoolnum);
//		map.put("totalProperty", result==null?0:result.size());
//		map.put("root", result);
//		return map;
//	}
	
	@RemoteMethod
	public List<Map> getLanguageList_without_languagenum(){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
	
		return studentservice.getLanguageList_without_languagenum(schoolnum);
		
	}
	
	//年级成绩统计
	@RemoteMethod
	public List<Map> summaryScoreByGrade(JSONObject summaryCondition){
			String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
			return studentservice.summaryScoreByGrade(summaryCondition, schoolnum);
		}
		
	//得到学院列表
	@RemoteMethod
	public List<Map> getCollegeList(){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return studentservice.getCollegeList(schoolnum);
		}
	
	//按学院各年级成绩统计
	@RemoteMethod
	public List<Map> summaryScoreByCollege_Grade(JSONObject summaryCondition){
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				return studentservice.summaryScoreByCollege_Grade(summaryCondition, schoolnum);
			}
		
	//得到年级列表
	@RemoteMethod
	public List getGradeList(){
			String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
			return studentservice.getGradeList(schoolnum);
			}
	
	//按年级各学院成绩统计
	@RemoteMethod
	public List<Map> summaryScoreByGrade_College(JSONObject summaryCondition){
					String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
					return studentservice.summaryScoreByGrade_College(summaryCondition, schoolnum);
				}
	
	//得到年级报名信息统计
	@RemoteMethod
	public List<Map> getGradeSignUpInfo(){
			String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
			return studentservice.getGradeSignUpInfo(schoolnum);
		}
	
	//得到成绩
		@RemoteMethod
		public List<Map> getScoreInfo(){
			   //得到学生的身份证
				String IDnum = (String) WebContextFactory.get().getSession().getAttribute("IDnum");
				return studentservice.getScoreInfo(IDnum);
			}
		
	//得到学校名称
	@RemoteMethod
	public String getSchoolName(){
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				String schoolname="";
				List<Map> infolist=studentservice.getSchoolName(schoolnum);
				for(Map info : infolist){
					schoolname=	info.get("schoolname").toString();
					
				}
				return schoolname;
				
			}
	
	
	@RemoteMethod
	public Map getStudentAllInfo() {
		String IDnum = (String) WebContextFactory.get().getSession().getAttribute("IDnum");
		Map result = studentservice.getStudentAllInfo(IDnum);
		return result;
	}
	
	//得到专业列表
			@RemoteMethod
			public List<Map> getProfessionList(){
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				return studentservice.getProfessionList(schoolnum);
				}
		//得到某语种某专业的所有学生
			@RemoteMethod
			public Map getStudentByProfession_language(String filterString, String pageNum, String pageSize) { 
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				Map map = new HashMap();
				String whereString = Function.initSearchHql_whereString_(filterString);
				Map<String, Object> paramsMap = Function.initSearchValues(filterString);
				List<Map> result = studentservice.getStudentByProfession_language(whereString, paramsMap, pageNum, pageSize,schoolnum);
				map.put("totalProperty", studentservice.getStudentsTotalCountByProfession_language(whereString, paramsMap,schoolnum));
				map.put("root", result);
				return map;
			}
			
			//得到某语种的所有学生的准考证号
			@RemoteMethod
			public List<Map> getAdmissionNum(String logicExamRoomNum) { 
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				return studentservice.getAdmissionNum(logicExamRoomNum, schoolnum);
			}
			
			//学校集体报名汇总
			@RemoteMethod
			public List<Map> getSignUpInfo(){
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				return studentservice.getSignUpInfo(schoolnum);
			}
			
			//根据给定的条件查询出学校集体报名汇总
			@RemoteMethod
			public List<Map> getSignUpInfo(JSONObject Condition){
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				return studentservice.getSignUpInfo(Condition,schoolnum);
			}
			
			//得到学校代码
			@RemoteMethod
			public String getSchoolnum(){
						String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");											
						return schoolnum;					
					}

			/**
			 * @param filterString
			 * @param pageNum
			 * @param pageSize
			 * @return
			 * 
			 * 准考证设计页面，获取所有考生的准考证信息
			 * 
			 */
			@RemoteMethod
			public Map getStudentAdmissionInfo(String filterString, String pageNum, String pageSize) { 
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				Map map = new HashMap();
				String whereString = Function.initSearchHql_whereString_(filterString);
				Map<String, Object> paramsMap = Function.initSearchValues(filterString);
				List<Map> result = studentservice.getStudentAdmissionInfo(whereString, paramsMap, pageNum, pageSize,schoolnum);
				long count = studentservice.getCountAllStudentArrage(whereString, paramsMap,schoolnum);//getCountAllStudentArrage(schoolnum)
				if(result==null){
					result= new ArrayList();
					count = 0;
				}
				map.put("totalProperty",  count);
				map.put("root", result);
				return map;
			}
			
			//得到符合条件的所有学生成绩
			@RemoteMethod
			public Map getStudentScoreByCondition(String filterString, String pageNum, String pageSize) { 
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				Map map = new HashMap();
				String whereString = Function.initSearchHql_whereString_(filterString);
				Map<String, Object> paramsMap = Function.initSearchValues(filterString);
				List<Map> result = studentservice.getStudentScoreByCondition(whereString, paramsMap, pageNum, pageSize,schoolnum);
				map.put("totalProperty", studentservice.getStudentsTotalCountByProfession_language(whereString, paramsMap,schoolnum));
				map.put("root", result);
				return map;
			}
			
			//得到某语种的所有理论监考教师,按照考场
			@RemoteMethod
			public Map getExaminerTeacher(String filterString, String pageNum, String pageSize) { 
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				Map map = new HashMap();
				String whereString = Function.initSearchHql_whereString_(filterString);
				Map<String, Object> paramsMap = Function.initSearchValues(filterString);
				List<Map> result = studentservice.getExaminerTeacher(whereString, paramsMap, pageNum, pageSize,schoolnum);
				map.put("totalProperty", studentservice.getLogicExamRoomTotalCountBylanguage(whereString, paramsMap,schoolnum));
				//map.put("totalProperty", result==null?0:result.size());
				map.put("root", result);
				return map;
			}
			
			//得到考场号及考场地点
			@RemoteMethod
			public Map getLogicExamRoom(String filterString, String pageNum, String pageSize) { //合格学生分页，用于打印证书
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				Map map = new HashMap();
				String whereString = Function.initSearchHql_whereString_(filterString);
				Map<String, Object> paramsMap = Function.initSearchValues(filterString);
				List<Map> result = studentservice.getLogicExamRoom(whereString, paramsMap, pageNum, pageSize,schoolnum);
				map.put("totalProperty", studentservice.getLogicExamRoomTotalCount(whereString, paramsMap,schoolnum));
				map.put("root", result);
				return map;
			}

			//得到考场号及考场地点
			@RemoteMethod
			public Map getSection(String filterString, String pageNum, String pageSize) { //合格学生分页，用于打印证书
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				Map map = new HashMap();
				String whereString = Function.initSearchHql_whereString_(filterString);
				Map<String, Object> paramsMap = Function.initSearchValues(filterString);
				List<Map> result = studentservice.getSection(whereString, paramsMap, pageNum, pageSize,schoolnum);
				map.put("totalProperty", studentservice.getTotalCount(whereString, paramsMap,schoolnum));
				map.put("root", result);
				return map;
			}
			
			//得到考场里的学生信息
			@RemoteMethod
			public List<Map> getStudentInfo(String logicExamRoomNum){
							String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
							return studentservice.getStudentInfo(logicExamRoomNum, schoolnum);
						}
			
			//得到学生考场信息
			@RemoteMethod
			public List<Map> getStuExamInfo(String studentidnum){
				return studentservice.getStuExamInfo(studentidnum);
			} 
			//考生历史数据导入
			@RemoteMethod
			public boolean importStuHistoryInfo(List<JSONObject> students){
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				return studentservice.importStuHistoryInfo(students,schoolnum);
			}
			
			//得到上机考试的场次及考场地点
			@RemoteMethod
			public Map getSectionAndLocationRoom(String filterString, String pageNum, String pageSize) { //合格学生分页，用于打印证书
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				Map map = new HashMap();
				String whereString = Function.initSearchHql_whereString_(filterString);
				Map<String, Object> paramsMap = Function.initSearchValues(filterString);
				List<Map> result = studentservice.getSectionAndLocationRoom(whereString, paramsMap, pageNum, pageSize,schoolnum);
				map.put("totalProperty", studentservice.getOperateSectionTotalCount(whereString, paramsMap,schoolnum));
				map.put("root", result);
				return map;
			}
			
			//得到上机考试每个教室的学生信息
			@RemoteMethod
			public List<Map> getStudentInfoBySectionAndLocationRoom(String sectionnum,String roomlocation,String languagename){
							String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
							return studentservice.getStudentInfoBySectionAndLocationRoom(sectionnum,roomlocation,languagename, schoolnum);
						}

			//得到某语种的所有上机监考教师
			@RemoteMethod
			public Map getOperateExaminerTeacher(String filterString, String pageNum, String pageSize) { 
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				Map map = new HashMap();
				String whereString = Function.initSearchHql_whereString_(filterString);
				Map<String, Object> paramsMap = Function.initSearchValues(filterString);
				List<Map> result = studentservice.getOperateExaminerTeacher(whereString, paramsMap, pageNum, pageSize,schoolnum);
				
				map.put("totalProperty", studentservice.getOperateExaminerTeacherTotalCount(whereString, paramsMap,schoolnum));
				//map.put("totalProperty",result==null?0:result.size());
				map.put("root", result);
				return map;
			}
			
			////学校上传ksdb到市地考试院
			@RemoteMethod
			public String importKsdb(List<JSONObject> students){
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				return studentservice.importKsdb(students,schoolnum);
			}
			@RemoteMethod
			public boolean importFraud(List<JSONObject> students) {
				return studentservice.importFraud(students);
			}
			@RemoteMethod
			public boolean importAbsent(List<JSONObject> students) {
				return studentservice.importAbsent(students);
			}
			//收索出所有未报名学生的信息
			@RemoteMethod
			public Map unSignUpStudentShow(String filterString, String pageNum, String pageSize) {
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				Map map = new HashMap();
				String whereString = Function.initSearchHql_whereString_(filterString);
				Map<String, Object> paramsMap = Function.initSearchValues(filterString);
				List<Map> result = studentservice.unSignUpStudentShow(whereString, paramsMap, pageNum, pageSize,schoolnum);
				map.put("totalProperty", studentservice.getUnSignUpStudentsTotalCount(whereString, paramsMap,schoolnum));
				map.put("root", result);
				return map;
			}
			
			//检查未报名考生的数据
			@RemoteMethod
			public String checkUnSignUpStudentsData(List<JSONObject> students){
				return studentservice.checkUnSignUpStudentsData(students);
			}
			@RemoteMethod 
			public String checkJFStudentsData(List<JSONObject> students) {
				return this.studentservice.checkJFStudentsData(students);
			}
			
			//计算考试袋数
			@RemoteMethod
			public Map calcPaperBagsForExamCollege(String institutionnum, String category, boolean direct){
				Map map = new HashMap();
				List<Map> result = studentservice.calcPaperBagsForExamCollege(institutionnum, category,direct);
				map.put("totalProperty", result==null?0:result.size());
				map.put("root", result);
				return map;
			}
			
			@RemoteMethod
			public Map loadExamNumLanguageInfoByInstitutionnum(){//此函数只提供给高校
				Map map = new HashMap();
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				List<Map> result = examNumEditService.loadExamNumLanguageInfo(schoolnum);
				map.put("totalProperty", result.size());
				map.put("root", result);
				return map;
			}
			
			
			@RemoteMethod//此函数用于考生端打印单个考生准考证
			public Map getOneStudentAdmissionInfo(String filterString, String pageNum, String pageSize) {
				String whereString = Function.initSearchHql_whereString_(filterString);
				Map map = new HashMap();
				Map<String, Object> paramsMap = Function.initSearchValues(filterString);
				String idnum = (String) WebContextFactory.get().getSession().getAttribute("IDnum");
				List<Map> result = studentservice.getOneStudentAdmissionInfo(whereString, paramsMap, pageNum, pageSize,idnum);
				long count=1;
				if(result==null){
					result= new ArrayList();
					count = 0;
				}
				map.put("totalProperty",  count);
				map.put("root", result);
				return map;
			//	return studentservice.getOneStudentAdmissionInfo(studentidnum);
			}
			
			//得到班级列表
			@RemoteMethod
			public List<Map> getClassNameList(String collegename){
				String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				return studentservice.getClassNameList(collegename,schoolnum);
				}
			
			@RemoteMethod
			public String checkStudentsExists(List<JSONObject> students){
				return studentservice.checkStudentsExists(students);
			}	
			
			//获取打印准考证备注那句话
			@RemoteMethod
			public String getRemarkAboutZKZ(){
				String remark="";
				List<Map> infolist=studentservice.getRemarkAboutZKZ();
				for(Map info : infolist){
					remark=	info.get("remark").toString();					
				}
				return remark;				
			}
			

			
			//保存打印准考证备注那句话
			@RemoteMethod
			public boolean saveRemarkAboutZKZ(String remark){		
					return  studentservice.saveRemarkAboutZKZ(remark);
				
			}
			
			
			//保存打印准考证备注那句话
			@RemoteMethod
			public boolean SetSessionParameter(String con_language,String con_passLine,String con_excellentLine){		
				WebContextFactory.get().getSession().setAttribute("language", con_language);
				WebContextFactory.get().getSession().setAttribute("passLine", con_passLine);
				WebContextFactory.get().getSession().setAttribute("excellentLine", con_excellentLine);						
				//List<Map> infolist=studentservice.getRemarkAboutZKZ();
				return  true;
				
			}
			
			//保存打印准考证备注那句话
			@RemoteMethod
			public boolean SetSessionParameterMore(String con_college,String con_language,String con_passLine,String con_excellentLine){		
				WebContextFactory.get().getSession().setAttribute("college", con_college);
				WebContextFactory.get().getSession().setAttribute("language", con_language);
				WebContextFactory.get().getSession().setAttribute("passLine", con_passLine);
				WebContextFactory.get().getSession().setAttribute("excellentLine", con_excellentLine);						
				//List<Map> infolist=studentservice.getRemarkAboutZKZ();
				return  true;
				
			}
			
			//保存打印准考证备注那句话
			@RemoteMethod
			public boolean SetSessionParameterMore2(String con_grade,String con_language,String con_passLine,String con_excellentLine){		
				WebContextFactory.get().getSession().setAttribute("grade", con_grade);
				WebContextFactory.get().getSession().setAttribute("language", con_language);
				WebContextFactory.get().getSession().setAttribute("passLine", con_passLine);
				WebContextFactory.get().getSession().setAttribute("excellentLine", con_excellentLine);						
				//List<Map> infolist=studentservice.getRemarkAboutZKZ();
				return  true;
				
			}
			
			
			//
			@RemoteMethod
			public Map calcStuForCollege(String pageNum, String pageSize){
				Map map = new HashMap();
				String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				List<Map> result = studentservice.calcStuForCollege(institutionnum);
				map.put("totalProperty", result==null?0:result.size());
				map.put("root", result);
				return map;
			}
			
			
			@RemoteMethod
			public Map calcStuForGrade(String pageNum, String pageSize){
				Map map = new HashMap();
				String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				List<Map> result = studentservice.calcStuForGrade(institutionnum);
				map.put("totalProperty", result==null?0:result.size());
				map.put("root", result);
				return map;
			}
			
			
			@RemoteMethod
			public Map calcStuForGrade_SignUpSummary(String pageNum, String pageSize){
				Map map = new HashMap();
				String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				List<Map> result = studentservice.calcStuForGrade_SignUpSummary(institutionnum);
				map.put("totalProperty", result==null?0:result.size());
				map.put("root", result);
				return map;
			}
			
			
			
			//用于显示在考试院,所有高校的缺考信息
			@RemoteMethod
			public Map absentStudentAllSchools(String pageNum, String pageSize){
			//	String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				Map map = new HashMap();
				List<Map> result = studentservice.getPageAbsentStudentsAllSchools(pageNum, pageSize);
				map.put("totalProperty", studentservice.getAbsentStudentsTotalCountAllSchools());           
				map.put("root", result);
				return map;
			}
			
			//用于显示在考试院,所有高校的作弊信息
			@RemoteMethod
			public Map fraudStudentAllSchools(String pageNum, String pageSize){
			//	String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
				Map map = new HashMap();
				List<Map> result = studentservice.getPageFraudStudentsAllSchools(pageNum, pageSize);
				map.put("totalProperty", studentservice.getFraudStudentsTotalCountAllSchools());
				map.put("root", result);
				return map;
			}
			
			

}
