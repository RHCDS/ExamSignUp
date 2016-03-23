package cn.hdu.examsignup.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;

import java.util.List;

import java.util.Map;
import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.json.types.JsonObject;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.CampusDao;
import cn.hdu.examsignup.dao.CollegeDao;
import cn.hdu.examsignup.dao.InstitutionDao;
import cn.hdu.examsignup.dao.InstitutionstatusDao;
import cn.hdu.examsignup.dao.StudentDao;
import cn.hdu.examsignup.dao.LanguageDao;
import cn.hdu.examsignup.dao.ProfessionDao;
import cn.hdu.examsignup.dao.StudentstatusDao;
import cn.hdu.examsignup.dao.UserDao;
import cn.hdu.examsignup.dao.ParameterDao;
import cn.hdu.examsignup.filter.JPGFileFilter;
import cn.hdu.examsignup.model.*;
import cn.hdu.examsignup.service.IdCardCheck;


@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class StudentService {
	
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private StudentstatusDao studentstatusDao;
	
	@Autowired
	private InstitutionDao institutiondao;
	
	@Autowired
	private LanguageDao languagedao;
	
	@Autowired
	private CampusDao  campusdao;
	
	@Autowired
	private CollegeDao collegedao;
	
	@Autowired
	private ProfessionDao professiondao;
	
	@Autowired
	private UserDao userdao;
	
	@Autowired
	private InstitutionstatusDao institutionstatusDao;
	@Autowired
	private ClearHistoryDataService clearHistoryDataService;
	
	@Autowired
	private ParameterDao parameterDao;
	

	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean validateStudent(String studentID,String IDnum,String school)
	{
		return studentDao.validateStudent(studentID, IDnum, school);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String validateStudent(String studentID, String IDnum) {
		return studentDao.validateStudent(studentID, IDnum);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getStudentsTotalCount(String whereString,Map<String,Object> valuesMap,String schoolnum){
		try {
			long studentdount = studentDao.getStudentsTotalCount(whereString, valuesMap,schoolnum);
			System.out.println("getStudentsTotalCount studentdount = " + studentdount);
			return studentdount;
		} catch (Exception e) {
			System.out.println("getStudentsTotalCount error");
			e.printStackTrace();
			return 0;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getPageStudents(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
		try {
			System.out.println("获取学生信息");
			List<Map> list = studentDao.getPageStudents(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
			System.out.println("获取学生信息 = " + list);
			return list;
		} catch (Exception e) {
			System.out.println("getPageStudents error");
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getStudentByStudentnumList(List<String> studentnumList, String schoolnum){
		try {
			return  this.studentDao.getStudentByStudentnumList(studentnumList, schoolnum);
		} catch (Exception e) {
			System.out.println("getPageStudents error");
			e.printStackTrace();
			return null;
		}
	}
	
	final static String PICTURE_BASE_DIR="pictures";
	final static String PICTURE_EXTERN=".jpg";
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)//无照片
	public List<String> studentWithoutPicture(String schoolnum){
		String PICTURE_BASE_DIR="pictures";
		String PICTURE_EXTERN=".jpg";
		try {
			File parentDir=null;
			String baseDir=WebContextFactory.get().getSession().getServletContext().getRealPath("/")+schoolnum+"/"+PICTURE_BASE_DIR+"/";
			parentDir= new File(baseDir);
			if(!(parentDir.exists()&&parentDir.isDirectory())){
				return this.studentDao.getAllStudentNumList(schoolnum);
			}
			List<File> pictureFileList=Arrays.asList(parentDir.listFiles(new JPGFileFilter()));
			Collections.sort(pictureFileList, new Comparator<File>(){
			    @Override
			    public int compare(File o1, File o2) {//文件在前
				if(o1.isDirectory() && o2.isFile())
				    return -1;
				if(o1.isFile() && o2.isDirectory())
			    	    return 1;
				return o1.getName().compareTo(o2.getName());
			    }
			});
			
			List<String>savedStudentnumList= new ArrayList<String>();
			List<String>allStudentnumList = this.studentDao.getAllStudentNumList(schoolnum);
			List<String>tempStudentnumList= new ArrayList<String>();
			
			for(File element:pictureFileList){
				savedStudentnumList.add(element.getName().replace(PICTURE_EXTERN, ""));
			}
			
			tempStudentnumList.addAll(allStudentnumList);
			tempStudentnumList.retainAll(savedStudentnumList);
			allStudentnumList.removeAll(tempStudentnumList);//去除已经有照片的学生
			return allStudentnumList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)//有照片
	public List<String> studentWithPicture(String schoolnum){
		try {
			File parentDir=null;
			String baseDir=WebContextFactory.get().getSession().getServletContext().getRealPath("/")+schoolnum+"/"+PICTURE_BASE_DIR+"/";
			parentDir= new File(baseDir);
			if(!(parentDir.exists()&&parentDir.isDirectory())){
				return null;
			}
			List<File> pictureFileList=Arrays.asList(parentDir.listFiles(new JPGFileFilter()));
			Collections.sort(pictureFileList, new Comparator<File>(){
			    @Override
			    public int compare(File o1, File o2) {//文件在前
				if(o1.isDirectory() && o2.isFile())
				    return -1;
				if(o1.isFile() && o2.isDirectory())
			    	    return 1;
				return o1.getName().compareTo(o2.getName());
			    }
			});
			
			List<String>savedStudentnumList= new ArrayList<String>();
			List<String>allStudentnumList = this.studentDao.getAllStudentNumList(schoolnum);
			List<String>tempStudentnumList= new ArrayList<String>();
			
			for(File element:pictureFileList){
				savedStudentnumList.add(element.getName().replace(PICTURE_EXTERN, ""));
			}
			
			tempStudentnumList.addAll(savedStudentnumList);
			tempStudentnumList.retainAll(allStudentnumList);
			return tempStudentnumList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)//多余的照片
	public List<String> excessPicture(String schoolnum){
		try {
			File parentDir=null;
			String baseDir=WebContextFactory.get().getSession().getServletContext().getRealPath("/")+schoolnum+"/"+PICTURE_BASE_DIR+"/";
			parentDir= new File(baseDir);
			if(!(parentDir.exists()&&parentDir.isDirectory())){
				return null;
			}
			List<File> pictureFileList=Arrays.asList(parentDir.listFiles(new JPGFileFilter()));
			Collections.sort(pictureFileList, new Comparator<File>(){
			    @Override
			    public int compare(File o1, File o2) {//文件在前
				if(o1.isDirectory() && o2.isFile())
				    return -1;
				if(o1.isFile() && o2.isDirectory())
			    	    return 1;
				return o1.getName().compareTo(o2.getName());
			    }
			});
			
			List<String>savedStudentnumList= new ArrayList<String>();
			List<String>allStudentnumList = this.studentDao.getAllStudentNumList(schoolnum);
			List<String>tempStudentnumList= new ArrayList<String>();
			
			for(File element:pictureFileList){
				savedStudentnumList.add(element.getName().replace(PICTURE_EXTERN, ""));
			}
			
			tempStudentnumList.addAll(savedStudentnumList);
			tempStudentnumList.retainAll(allStudentnumList);
			savedStudentnumList.removeAll(tempStudentnumList);
			return savedStudentnumList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> paginationShowForApproval(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
		try {
			return studentDao.paginationShowForApproval(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
		} catch (Exception e) {
			System.out.println("getPageStudents error");
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> ExamCollegeGetStudent(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String institutionnum){
		try {
			return studentDao.ExamCollegeGetStudent(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),institutionnum);
		} catch (Exception e) {
			System.out.println("getPageStudents error");
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getAbsentStudentsTotalCount(String schoolnum){
		try {
			return studentDao.getAbsentStudentsTotalCount(schoolnum);
		} catch (Exception e) {
			System.out.println("getAbsentStudentsTotalCount error");
			e.printStackTrace();
			return 0;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getPageAbsentStudents(String pageNum, String pageSize,String schoolnum){
		try {
			return studentDao.getPageAbsentStudents(Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
		} catch (Exception e) {
			System.out.println("getPageAbsentStudents error");
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ExStudent getStudentByIDnum(String IDnum,String school){
		return this.studentDao.getStudentByIDnum(IDnum, school);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String checkStudentsData(List<JSONObject> students){
		try {
			int studentcount = 1;
			String languageNumReg="(";
			for(String temp : (this.languagedao.getLanguageNumList())){
				languageNumReg+=temp+"|";
			}
			languageNumReg=languageNumReg.substring(0,languageNumReg.length()-1)+")";
			System.out.println("所有语种：" + languageNumReg);
			for(JSONObject student : students){
				studentcount++;
				String error = this.checkStudentData(student,languageNumReg);
				if(!error.equals("输入正确！")){
					return "第" + studentcount + "行" + error;
				}
				
			}
			return null;
		} catch (Exception e) {
			System.out.println("checkStudentsData error!");
			return e.getMessage();
		}

	}
	
	public String checkStudentData(JSONObject student, String LanguageNumList){
		if(!student.get("exLanguage_num").toString().matches(LanguageNumList)){
			return "语种代码格式错误!";
		}
		if(student.get("exCampus_num").toString().equals("")){
			return "校区代码为空！";
		}else{
			if(!student.get("exCampus_num").toString().matches("\\d")){
				return "校区代码格式错误!";
			}
		}
		if(!student.get("exCollege_num").toString().equals("")){
			if(!student.get("exCollege_num").toString().matches("\\d+")){
				return "学院代码格式错误！";
			}
		}
		if(!student.get("exProfession_num").toString().equals("")){
			if(!student.get("exProfession_num").toString().matches("\\d+")){
				return "专业代码格式错误！";
			}
		}
//		if(!student.get("exProfession_num").toString().equals("")){
//			if(!student.get("exProfession_num").toString().matches("\\d+")){
//				return "专业代码格式错误！";
//			}
//		}
//		if(!student.get("studentnum").toString().matches("\\d+")){
//			return "学号格式错误！";
//		}
//		if(!idcardcheck.isValidatedAllIdcard(student.get("idnum").toString())){
//			return "身份证格式错误！";
//		}
		if(student.get("name").toString().contains(" ")){
			return "姓名中有空格！";
		}
		if(student.get("exCampus_name").toString().equals("")){
			return "校区名称不能为空！";
		}
		if(student.get("exCollege_name").toString().equals("")){
			return "学院名称不能为空！";
		}
		if(student.get("studentnum").toString().equals("")){
			return "学号不能为空！";
		}
		if(student.get("name").toString().equals("")){
			return "姓名不能为空！";
		}
		if(student.get("classnum").toString().equals("")){
			return "行政班字段不能为空！";
		}
		if(student.get("studentcategory").toString().equals("")){
			return "考生类别不能为空！";
		}
		if(student.get("paied").toString().equals("")){
			return "交费不能为空！";
		}
		if(student.get("idnum").toString().length() != 18) {
			return "身份证长度有误";
		}
		return "输入正确！";
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean importStudents(List<JSONObject> students,String schoolnum){
		try {
			clearHistoryDataService.clear(schoolnum);
			int studentcount = 0;
			ExStudent entity;
			for(JSONObject student : students){
//				if(studentDao.findByProperty("idnum",student.get("idnum").toString()).size()>0){
//					entity = studentDao.findByProperty("idnum", student.get("idnum").toString()).get(0);
//				}else{
//					entity = new ExStudent();
//				}
				entity = new ExStudent();
				ExInstitution school = institutiondao.getInstitutionByInstitutionNum(schoolnum);
				entity.setExInstitution(school);
				if((!student.get("exLanguage_num").toString().equals("")) && languagedao.findByProperty("languagenum", student.get("exLanguage_num").toString()).size()>0){
					entity.setExLanguage(languagedao.findByProperty("languagenum", student.get("exLanguage_num").toString()).get(0));
				}else{
					if((!student.get("exLanguage_name").toString().equals("")) && languagedao.findByProperty("name", student.get("exLanguage_name").toString()).size()>0){
						entity.setExLanguage(languagedao.findByProperty("name", student.get("exLanguage_name").toString()).get(0));
					}else{
						if((!student.get("exLanguage_num").toString().equals("")) || (!student.get("exLanguage_name").toString().equals(""))){
							ExLanguage newlanguage = new ExLanguage();
							newlanguage.setLanguagenum(student.get("exLanguage_num").toString());
							newlanguage.setName(student.get("exLanguage_name").toString());
							languagedao.save(newlanguage);
							entity.setExLanguage(newlanguage);
						}else{
							entity.setExLanguage(null);
						}
					}
				}
				if((!student.get("exCampus_num").toString().equals("")) && campusdao.findByProperty("campusnum", student.get("exCampus_num").toString(),schoolnum).size()>0){
					
					entity.setExCampus(campusdao.findByProperty("campusnum", student.get("exCampus_num").toString(),schoolnum).get(0));
				}else{
					if((!student.get("exCampus_name").toString().equals("")) && campusdao.findByProperty("name", student.get("exCampus_name").toString(),schoolnum).size()>0){
						entity.setExCampus(campusdao.findByProperty("name", student.get("exCampus_name").toString(),schoolnum).get(0));
					}else{
						if((!student.get("exCampus_num").toString().equals("")) || (!student.get("exCampus_name").toString().equals(""))){
							ExCampus newcampus = new ExCampus();
							if(student.get("exCampus_num").toString().equals("")){
								newcampus.setCampusnum("1");
							}else
								newcampus.setCampusnum(student.get("exCampus_num").toString());
							newcampus.setName(student.get("exCampus_name").toString());
							newcampus.setExInstitution(school);
							campusdao.save(newcampus);
							entity.setExCampus(newcampus);
						}
					}
				}
				if((!student.get("exCollege_num").toString().equals("")) && collegedao.findByProperty("collegenum", student.get("exCollege_num").toString(),schoolnum).size()>0){
					entity.setExCollege(collegedao.findByProperty("collegenum", student.get("exCollege_num").toString(),schoolnum).get(0));
				}else{
					if((!student.get("exCollege_name").toString().equals("")) && collegedao.findByProperty("name", student.get("exCollege_name").toString(),schoolnum).size()>0){
						entity.setExCollege(collegedao.findByProperty("name", student.get("exCollege_name").toString(),schoolnum).get(0));
					}else{
						if((!student.get("exCollege_num").toString().equals("")) || (!student.get("exCollege_name").toString().equals(""))){
							ExCollege newcollege = new ExCollege();
							newcollege.setCollegenum(student.get("exCollege_num").toString());
							newcollege.setName(student.get("exCollege_name").toString());
							newcollege.setExInstitution(school);
							collegedao.save(newcollege);
							entity.setExCollege(newcollege);
						}
					}
				}
				if((!student.get("exProfession_num").toString().equals("")) && professiondao.findByProperty("professionnum", student.get("exProfession_num").toString(),schoolnum).size()>0){
					entity.setExProfession(professiondao.findByProperty("professionnum", student.get("exProfession_num").toString(),schoolnum).get(0));
				}else{
					if((!student.get("exProfession_name").toString().equals("")) && professiondao.findByProperty("name", student.get("exProfession_name").toString(),schoolnum).size()>0){
						entity.setExProfession(professiondao.findByProperty("name", student.get("exProfession_name").toString(),schoolnum).get(0));
					}else{
						if((!student.get("exProfession_num").toString().equals("")) || (!student.get("exProfession_name").toString().equals(""))){
							ExProfession newprofession = new ExProfession();
							newprofession.setName(student.get("exProfession_name").toString());
							newprofession.setProfessionnum(student.get("exProfession_num").toString());
							newprofession.setInstitutionid(schoolnum);
							professiondao.save(newprofession);
							entity.setExProfession(newprofession);
						}
					}
					
				}
				entity.setGrade(student.get("grade").toString());
				entity.setClassnum(student.get("classnum").toString());
				entity.setLengthofyear(student.get("lengthofyear").toString());
				entity.setStudentnum(student.get("studentnum").toString());
				entity.setName(student.get("name").toString());
				if(student.get("sex").toString().equals("男") || student.get("sex").toString().equals("M")){
					entity.setSex("M");
				}else{entity.setSex("F");}
				entity.setEthno(student.get("ethno").toString());
				entity.setIdnum(student.get("idnum").toString());
				BigDecimal score = new BigDecimal(0);
				entity.setScore(score);
				entity.setTheoryabsent("0");
				entity.setTheoryfraud("0");
				entity.setOperateabsent("0");
				entity.setOperatefraud("0");
				if(student.get("paied").toString().equals("是")||student.get("paied").toString().equals("1")){
					entity.setPaied("1");
				}else{entity.setPaied("0");}
				if(student.get("studentcategory").toString().equals("1")||student.get("studentcategory").toString().equals("普通本科")){
					entity.setStudentcategory("1");
				}else if(student.get("studentcategory").toString().equals("2")||student.get("studentcategory").toString().equals("普通专科")){
					entity.setStudentcategory("2");
				}else{
					entity.setStudentcategory("3");
				}
				studentDao.save(entity);
				if(studentcount%50==0){
					studentDao.getSession().flush();
					studentDao.getSession().clear();
				}
				studentcount++;
			}
			return true;
		} catch (Exception e) {
			System.out.println("importStudents error");
			e.printStackTrace();
			studentDao.getSession().getTransaction().rollback();
			return false;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteStudents(List<String> ids){

		try {
			for(String id : ids){
				ExStudent entity = studentDao.findById(id);
				studentDao.delete(entity);
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteStudents error");
			e.printStackTrace();
			return false;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean saveStudent(JSONObject student,String schoolnum){
		
		try {
			if(studentDao.findByProperty("idnum",student.get("idnum").toString()).size()>0){
				this.updateStudent(student, schoolnum);
			}else{
				ExStudent entity = new ExStudent();
				ExInstitution school = institutiondao.getInstitutionByInstitutionNum(schoolnum);
				entity.setExInstitution(school);
				if((!student.get("exLanguage").toString().equals("")) && languagedao.findByProperty("name", student.get("exLanguage").toString()).size()>0){
					entity.setExLanguage(languagedao.findByProperty("name", student.get("exLanguage").toString()).get(0));
				}else{
							ExLanguage newlanguage = new ExLanguage();
							newlanguage.setName(student.get("exLanguage").toString());
							languagedao.save(newlanguage);
							entity.setExLanguage(newlanguage);}
				
				if((!student.get("exCampus").toString().equals("")) && campusdao.findByProperty("name", student.get("exCampus").toString(), schoolnum).size()>0){
					entity.setExCampus(campusdao.findByProperty("name", student.get("exCampus").toString(), schoolnum).get(0));
				}else{
							ExCampus newcampus = new ExCampus();
							newcampus.setName(student.get("exCampus").toString());
							newcampus.setCampusnum("1");
							newcampus.setExInstitution(school);
							campusdao.save(newcampus);
							entity.setExCampus(newcampus);}
				if((!student.get("exCollege").toString().equals("")) && collegedao.findByProperty("name", student.get("exCollege").toString(), schoolnum).size()>0){
					entity.setExCollege(collegedao.findByProperty("name", student.get("exCollege").toString(), schoolnum).get(0));
				}else{
							ExCollege newcollege = new ExCollege();
							newcollege.setName(student.get("exCollege").toString());
							newcollege.setExInstitution(school);
							collegedao.save(newcollege);
							entity.setExCollege(newcollege);
					}
				if((!student.get("exProfession").toString().equals("")) && professiondao.findByProperty("name", student.get("exProfession").toString(), schoolnum).size()>0){
					entity.setExProfession(professiondao.findByProperty("name", student.get("exProfession").toString(), schoolnum).get(0));
				}else{
							ExProfession newprofession = new ExProfession();
							newprofession.setName(student.get("exProfession").toString());
							newprofession.setInstitutionid(schoolnum);
							professiondao.save(newprofession);
							entity.setExProfession(newprofession);
					}
				entity.setGrade(student.get("grade").toString());
				entity.setClassnum(student.get("classnum").toString());
				entity.setLengthofyear(student.get("lengthofyear").toString());
				entity.setStudentnum(student.get("studentnum").toString());
				entity.setName(student.get("name").toString());
				if(student.get("sex").toString().equals("男") || student.get("sex").toString().equals("M")){
					entity.setSex("M");
				}else{entity.setSex("F");}
				entity.setEthno(student.get("ethno").toString());
				entity.setIdnum(student.get("idnum").toString());
				entity.setTheoryabsent("0");
				entity.setTheoryfraud("0");
				entity.setOperateabsent("0");
				entity.setOperatefraud("0");
				BigDecimal score = new BigDecimal(0);
				entity.setScore(score);
				if(student.get("paied").toString().equals("是")||student.get("paied").toString().equals("1")){
					entity.setPaied("1");
				}else{entity.setPaied("0");}
				if(student.get("studentcategory").toString().equals("1")||student.get("studentcategory").toString().equals("普通本科")){
					entity.setStudentcategory("1");
				}else if(student.get("studentcategory").toString().equals("2")||student.get("studentcategory").toString().equals("普通专科")){
					entity.setStudentcategory("2");
				}else{
					entity.setStudentcategory("3");
				}
				studentDao.save(entity);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean updateStudent(JSONObject student,String schoolnum){
		try {
			ExStudent entity = studentDao.findById(student.get("id").toString());
			if((!student.get("exLanguage").toString().equals("")) && languagedao.findByProperty("name", student.get("exLanguage").toString()).size()>0){
				entity.setExLanguage(languagedao.findByProperty("name", student.get("exLanguage").toString()).get(0));
			}else{
						ExLanguage newlanguage = new ExLanguage();
						newlanguage.setName(student.get("exLanguage").toString());
						languagedao.save(newlanguage);
						entity.setExLanguage(newlanguage);}
			
			if((!student.get("exCampus").toString().equals("")) && campusdao.findByProperty("name", student.get("exCampus").toString(), schoolnum).size()>0){
				entity.setExCampus(campusdao.findByProperty("name", student.get("exCampus").toString(), schoolnum).get(0));
			}else{
						ExCampus newcampus = new ExCampus();
						newcampus.setName(student.get("exCampus").toString());
						newcampus.setCampusnum("1");
						newcampus.setExInstitution(entity.getExInstitution());
						campusdao.save(newcampus);
						entity.setExCampus(newcampus);}
			if((!student.get("exCollege").toString().equals("")) && collegedao.findByProperty("name", student.get("exCollege").toString(), schoolnum).size()>0){
				entity.setExCollege(collegedao.findByProperty("name", student.get("exCollege").toString(), schoolnum).get(0));
			}else{
						ExCollege newcollege = new ExCollege();
						newcollege.setName(student.get("exCollege").toString());
						newcollege.setExInstitution(entity.getExInstitution());
						collegedao.save(newcollege);
						entity.setExCollege(newcollege);
				}
			if((!student.get("exProfession").toString().equals("")) && professiondao.findByProperty("name", student.get("exProfession").toString(), schoolnum).size()>0){
				entity.setExProfession(professiondao.findByProperty("name", student.get("exProfession").toString(), schoolnum).get(0));
			}else{
						ExProfession newprofession = new ExProfession();
						newprofession.setName(student.get("exProfession").toString());
						newprofession.setInstitutionid(entity.getExInstitution().getInstitutionnum());
						professiondao.save(newprofession);
						entity.setExProfession(newprofession);
				}
			entity.setGrade(student.get("grade").toString());
			entity.setClassnum(student.get("classnum").toString());
			entity.setLengthofyear(student.get("lengthofyear").toString());
			entity.setStudentnum(student.get("studentnum").toString());
			entity.setName(student.get("name").toString());
			if(student.get("sex").toString().equals("男") || student.get("sex").toString().equals("M")){
				entity.setSex("M");
			}else{entity.setSex("F");}
			entity.setEthno(student.get("ethno").toString());
			entity.setIdnum(student.get("idnum").toString());
			if(student.get("paied").toString().equals("是")||student.get("paied").toString().equals("1")){
				entity.setPaied("1");
			}else{entity.setPaied("0");}
			if(student.get("studentcategory").toString().equals("1")||student.get("studentcategory").toString().equals("普通本科")){
				entity.setStudentcategory("1");
			}else if(student.get("studentcategory").toString().equals("2")||student.get("studentcategory").toString().equals("普通专科")){
				entity.setStudentcategory("2");
			}else{
				entity.setStudentcategory("3");
			}
			studentDao.update(entity);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Map findAbsentStudentByExamNum(String examnum){
		try {
			Map student = studentDao.findAbsentStudentByExamNum(examnum);
			if(studentDao.findAbsentStudentByExamNum(examnum).get("theoryabsent").equals("1")){
				student.put("theoryabsent", "是");
			}else{
				student.put("theoryabsent", "否");
			}
			if(studentDao.findAbsentStudentByExamNum(examnum).get("operateabsent").equals("1")){
				student.put("operateabsent", "是");
			}else{
				student.put("operateabsent", "否");
			}
			return student;
		} catch (Exception e) {
			System.out.println("findAbsentStudentByExamNum error" );
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveAbsentStudent(JSONObject absentstudent){
		try {
			ExStudent entity =  studentDao.findByProperty("examnum", absentstudent.get("examnum").toString()).get(0);
			if(entity.getTheoryfraud().equals("1")) {
				return "{sucess:false,info:'该学生已经录入作弊，请确认信息的正确性！'}";
			}
			if(!absentstudent.get("theoryabsent").toString().isEmpty()){
				if(absentstudent.get("theoryabsent").toString().equals("是")){
					entity.setTheoryabsent("1");
				}else{entity.setTheoryabsent("0");}
			}
			if(!absentstudent.get("operateabsent").toString().isEmpty()){
				if(absentstudent.get("operateabsent").toString().equals("是")){
					entity.setOperateabsent("1");
				}else{entity.setOperateabsent("0");}
			}
			studentDao.update(entity);
			return "{success:true,info:'录入成功！'}";
		} catch (Exception e) {
			System.out.println("saveAbsentStudent error");
			e.printStackTrace();
			return "{success:false,info:'录入失败！'}";
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteAbsentStudent(List<String> examnums){
		try {
			for(String examnum : examnums){
				ExStudent entity =  studentDao.findByProperty("examnum",examnum).get(0);
				entity.setTheoryabsent("0");
				entity.setOperateabsent("0");
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteAbsentStudent error");
			e.printStackTrace();
			return false;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getAbsentStudentsBySchool(String schoolnum){
		try {
			
			return studentDao.getAbsentStudentsBySchool(schoolnum);
		} catch (Exception e) {
			System.out.println("getAbsentStudentsBySchool error");
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getAllSchoolsAbsent(){
		try {
			
			return studentDao.getAllSchoolsAbsent();
		} catch (Exception e) {
			System.out.println("getAllSchoolsAbsent error");
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getFraudStudentsTotalCount(String schoolnum){
		try {
			return studentDao.getFraudStudentsTotalCount(schoolnum);
		} catch (Exception e) {
			System.out.println("getFraudStudentsTotalCount error");
			e.printStackTrace();
			return 0;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getCheckScoreStudentsTotalCount(String schoolnum){
		try {
			return studentDao.getCheckScoreStudentsTotalCount(schoolnum);
		} catch (Exception e) {
			System.out.println("getFraudStudentsTotalCount error");
			e.printStackTrace();
			return 0;
		}
	}
	public List<Map> getPageFraudStudents(String schoolnum,String pageNum, String pageSize){
			try {
				return studentDao.getPageFraudStudents(schoolnum,Integer.parseInt(pageNum), Integer.parseInt(pageSize));
			} catch (Exception e) {
				System.out.println("getPageFraudStudents error:" + e.getMessage());
				return null;
			}
	}
	public List<Map> getPageCheckScoreStudents(String schoolnum,String pageNum, String pageSize){
		try {
			List<Map> result = this.studentDao.getPageCheckScoreStudents(schoolnum,Integer.parseInt(pageNum), Integer.parseInt(pageSize));
			return result;
		} catch (Exception e) {
			System.out.println("getCheckScoreStudents error:" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveFraudStudent(JSONObject fraudstudent){
		try {
			ExStudent entity =  studentDao.findByProperty("examnum", fraudstudent.get("examnum").toString()).get(0);
			if(entity.getTheoryfraud().equals("1")) {
				return "{success:false,info:'该考生已录入缺考，请确认信息准确性！'}";
			}
			if(!fraudstudent.get("theoryfraud").toString().isEmpty()){
				if(fraudstudent.get("theoryfraud").toString().equals("是")){
					entity.setTheoryfraud("1");
				}else{entity.setTheoryfraud("0");}
			}
			if(!fraudstudent.get("operatefraud").toString().isEmpty()){
				if(fraudstudent.get("operatefraud").toString().equals("是")){
					entity.setOperatefraud("1");
				}else{entity.setOperatefraud("0");}
			}
			studentDao.update(entity);
			return "{success:true,info:'录入成功！'}";
		} catch (Exception e) {
			System.out.println("saveFraudStudent error！");
			e.printStackTrace();
			return "{success:false,info:'录入失败！'}";
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveCheckScoreStudent(JSONObject student){
		try {
			if(studentDao.findByProperty("examnum", student.get("examnum").toString()).size() == 0) {
				return "{success:false,info: '输入信息有误，请确认输入的信息正确！'}";
			}
			ExStudent entity =  studentDao.findByProperty("examnum", student.get("examnum").toString()).get(0);
			ExStudentstatus status = studentstatusDao.findByProperty("statusnum", 7).get(0);
			entity.setExStudentstatus(status);
			studentDao.update(entity);
			return "{success:false,info: '添加成功！'}";
		} catch (Exception e) {
			System.out.println("saveCheckScoreStudent error:" + e.getMessage());
			return "{success:false,info: '添加失败！'}";
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String reCheck(JSONObject checkreason,String idnum){
		try {
			if(studentDao.findByProperty("idnum", idnum).size()==0) {
				return "{success:false,info: '考试信息有误！'}";
			}
			ExStudent entity =  studentDao.findByProperty("examnum", idnum).get(0);
			ExStudentstatus status = studentstatusDao.findByProperty("statusnum", 7).get(0);
			entity.setExStudentstatus(status);
			entity.setRemark(checkreason.get("reason").toString());
			studentDao.update(entity);
			return "{success:false,info: '添加成功！'}";
		} catch (Exception e) {
			System.out.println("saveCheckScoreStudent error:" + e.getMessage());
			return "{success:false,info: '添加失败！'}";
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Map findFraudStudentByExamNum(String examnum){
		try {
			Map student = studentDao.findFraudStudentByExamNum(examnum);
			return student;
		} catch (Exception e) {
			System.out.println("findAbsentStudentByExamNum error:" + e.getMessage());
			return null;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Map findCheckScoreStudentByExamNum(String examnum){
		try {
			Map student = studentDao.findCheckScoreStudentByExamNum(examnum);
			if(studentDao.findFraudStudentByExamNum(examnum).get("theoryfraud").equals("1")){
				student.put("theoryfraud", "是");
			}else{
				student.put("theoryfraud", "否");
			}
			if(studentDao.findFraudStudentByExamNum(examnum).get("operatefraud").equals("1")){
				student.put("operatefraud", "是");
			}else{
				student.put("operatefraud", "否");
			}
			return student;
		} catch (Exception e) {
			System.out.println("findCheckScoreStudentByExamNum error:" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteFraudStudent(List<String> examnums){
		try {
			for(String examnum : examnums){
				ExStudent entity =  studentDao.findByProperty("examnum",examnum).get(0);
				entity.setTheoryfraud("0");
				entity.setOperatefraud("0");
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteFraudStudent error:" + e.getMessage());
			return false;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteCheckScoreStudent(List<String> examnums){
		try {
			for(String examnum : examnums){
				ExStudent entity =  studentDao.findByProperty("examnum",examnum).get(0);
				entity.setExStudentstatus(null);
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteCheckScoreStudent error:" + e.getMessage());
			return false;
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getFraudStudentsBySchool(String schoolnum){
		try {
			return studentDao.getFraudStudentsBySchool(schoolnum);
		} catch (Exception e) {
			System.out.println("getFraudStudentsBySchool error:" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getAllSchoolsFraud(){
		try {
			return studentDao.getAllSchoolsFraud();
		} catch (Exception e) {
			System.out.println("getAllSchoolsFraud error:" + e.getMessage());
			return null;
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getAllCheckScoreStudents(String institutionnum){
		try {
			return studentDao.getAllCheckScoreStudents(institutionnum);
		} catch (Exception e) {
			System.out.println("getAllFraudStudents error:" + e.getMessage());
			return null;
		}
	}	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getAllStudents(String schoolnum){
		try {
			return studentDao.getAllStudents(schoolnum);
		} catch (Exception e) {
			System.out.println("getAllStudents error");
			e.printStackTrace();
			return null;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getAllStudentsCJ(String schoolnum){
		List<Map> result;
		try {
			result = studentDao.getAllStudentsCJ(schoolnum);
		} catch (Exception e) {
			System.out.println("getAllStudents error");
			e.printStackTrace();
			return null;
		}
		for(Map element:result) {
			String sex = element.get("sex").toString();
			String theoryabsent = element.get("theoryabsent").toString();
			String operateabsent = element.get("operateabsent").toString();
			BigDecimal score =new BigDecimal(element.get("score").toString());
			float newscore = score.floatValue();
			if(theoryabsent.equals("1") || operateabsent.equals("1")) {
				element.put("DJ", "Q");
			} else {
				if(newscore < 60.00) {
					element.put("DJ", "F");
				} else if(newscore>=85.00) {
					element.put("DJ", "G");
				} else {
					element.put("DJ", "P");
				}
			}
		}
		return result;
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getAllStudentsUnpaid(String schoolnum){
		try {
			return studentDao.getAllStudentsUnpaid(schoolnum);
		} catch (Exception e) {
			System.out.println("getAllStudentsUnpaid error:" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> backupStudentsExcel(String schoolnum){
		try {
			return studentDao.backupStudentsExcel(schoolnum);
		} catch (Exception e) {
			System.out.println("backupStudentsExcel error:" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<Map> getCollegeSignUpInfo(String schoolnum){
		try {
			List<Map> languagelist = languagedao.getAllLanguage();
			List<Map> collegesignupinfolist = new ArrayList<Map>();
			for(Map language : languagelist){
				String languagenum = language.get("languagenum").toString();
				if(collegesignupinfolist.isEmpty()){
					List<Map> collegesigninfolist = studentDao.getCollegeSignUpInfo(schoolnum, languagenum);
					for(Map collegeinfo : collegesigninfolist){
						Map collegesignupinfo1 = new JSONObject();
						collegesignupinfo1.put("collegename", collegeinfo.get("collegename").toString());
													
						collegesignupinfo1.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
						collegesignupinfolist.add(collegesignupinfo1);
					}
				}else{
					List<Map> collegesigninfolist = (List<Map>)studentDao.getCollegeSignUpInfo(schoolnum, languagenum);
					for(Map collegeinfo : collegesigninfolist){
						boolean inserted = false;
						for(Map collegesignupinfo : collegesignupinfolist){
							if(!inserted && collegesignupinfo.get("collegename").equals(collegeinfo.get("collegename"))){
								
								
								
								collegesignupinfo.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
								
								
								inserted = true;
							}
						}
						if(!inserted){
							Map collegesignupinfo2 = new JSONObject();
							collegesignupinfo2.put("collegename", collegeinfo.get("collegename").toString());
							
							collegesignupinfo2.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
							collegesignupinfolist.add(collegesignupinfo2);
							inserted = true;
						}
					}
				}
			}
			for(Map sumcollegesignup : studentDao.getCollegeSignUpInfo(schoolnum)){
				boolean inserted = false;
				if(!inserted){
					for(Map collegesignupinfo : collegesignupinfolist){
						if(!inserted && collegesignupinfo.get("collegename").equals(sumcollegesignup.get("collegename"))){
							
							
							collegesignupinfo.put("sumstudentcount", sumcollegesignup.get("studentcount").toString());
							inserted = true;
						}
					}
				}else if(!inserted){
					Map collegesignupinfo2 = new JSONObject();
					collegesignupinfo2.put("collegename", sumcollegesignup.get("collegename").toString());
					
					collegesignupinfo2.put("sumstudentcount", sumcollegesignup.get("studentcount").toString());
					collegesignupinfolist.add(collegesignupinfo2);
					inserted = true;
				}
			}
			Map collegesignupinfo3 = new JSONObject();
			collegesignupinfo3.put("collegename", "合计");
			int count = 0;
			for(Map sumcollegesignup : studentDao.getSignUpInfoGroupByLanguage(schoolnum)){
				
				
				collegesignupinfo3.put(sumcollegesignup.get("languagenum").toString()+"studentcount", sumcollegesignup.get("studentcount").toString());
				count += Integer.parseInt(sumcollegesignup.get("studentcount").toString());
			}
			collegesignupinfo3.put("sumstudentcount", count);
			collegesignupinfolist.add(collegesignupinfo3);
			
			//设置空白为0
			for(Map SummaryInfo : collegesignupinfolist){
				
				if(!SummaryInfo.containsKey("sumstudentcount")){
					
					SummaryInfo.put("sumstudentcount", "0");									
				}
				
               if(!SummaryInfo.containsKey("11studentcount")){
					
					SummaryInfo.put("11studentcount", "0");									
				}
               
               if(!SummaryInfo.containsKey("12studentcount")){
					
					SummaryInfo.put("12studentcount", "0");									
				}
               
               if(!SummaryInfo.containsKey("21studentcount")){
					
					SummaryInfo.put("21studentcount", "0");									
				}
               
               if(!SummaryInfo.containsKey("22studentcount")){
					
					SummaryInfo.put("22studentcount", "0");									
				}
               
               if(!SummaryInfo.containsKey("23studentcount")){
					
					SummaryInfo.put("23studentcount", "0");									
				}
                            
               if(!SummaryInfo.containsKey("24studentcount")){
					
					SummaryInfo.put("24studentcount", "0");									
				}
              
              if(!SummaryInfo.containsKey("25studentcount")){
					
					SummaryInfo.put("25studentcount", "0");									
				}
              
              if(!SummaryInfo.containsKey("26studentcount")){
					
					SummaryInfo.put("26studentcount", "0");									
				}
                                
              
              if(!SummaryInfo.containsKey("31studentcount")){
					
					SummaryInfo.put("31studentcount", "0");									
				}
            
            if(!SummaryInfo.containsKey("32studentcount")){
					
					SummaryInfo.put("32studentcount", "0");									
				}
            
            if(!SummaryInfo.containsKey("33studentcount")){
					
					SummaryInfo.put("33studentcount", "0");									
				}
            
            if(!SummaryInfo.containsKey("34studentcount")){
				
				SummaryInfo.put("34studentcount", "0");									
			}
        
           if(!SummaryInfo.containsKey("35studentcount")){
				
				SummaryInfo.put("35studentcount", "0");									
			}
               
			}
			
			
			return collegesignupinfolist;
		} catch (Exception e) {
			System.out.println("getCollegeSignUpInfo error:" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getPassStudentsTotalCount(String whereString,Map<String,Object> valuesMap,String schoolnum){
		try {
			return studentDao.getPassStudentsTotalCount(whereString, valuesMap,schoolnum);
		} catch (Exception e) {
			System.out.println("getPassStudentsTotalCount error:" + e.getMessage());
			return 0;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getPassStudents(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
		try {
			return studentDao.getPassStudents(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
		} catch (Exception e) {
			System.out.println("getPassStudents error:" + e.getMessage());
			return null;
		}
	}
	
	//学院成绩统计
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<Map> summaryScore(JSONObject summaryCondition,String schoolnum){
	    try{
		    //先收索出3个分数段的人数，然后再整合成一个map对象
					
			List<Map> scoreSummaryInfoList = new ArrayList<Map>();
		
			String languagenum =summaryCondition.get("language").toString();//得到语种
			
			//收索出一个学校的每个学院及总人数	
			List<Map> studentScoreInfoList0 = studentDao.getScoreInfo_getCollege(schoolnum, languagenum);
			
			//收索出不及格人数	
			List<Map> studentScoreInfoList1 = studentDao.getScoreInfo_1(schoolnum, languagenum,summaryCondition);
			//收索出及格人数
			List<Map> studentScoreInfoList2 = studentDao.getScoreInfo_2(schoolnum, languagenum,summaryCondition);
			
			//收索出优秀人数
			List<Map> studentScoreInfoList3 = studentDao.getScoreInfo_3(schoolnum, languagenum,summaryCondition);
			
			//开始整合到一个大的List中
			for(Map studentScoreInfo0 : studentScoreInfoList0){
						Map studentScoreInfo = new JSONObject();
						
						studentScoreInfo.put("collegename", studentScoreInfo0.get("collegename").toString());
						int studentCount=Integer.parseInt(studentScoreInfo0.get("studentcount").toString());
						studentScoreInfo.put("StudentCount", studentScoreInfo0.get("studentcount").toString());
						//找到同学院的不及格人数
						for(Map studentScoreInfo1 : studentScoreInfoList1){
							if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo1.get("collegename").toString()))
							{
								studentScoreInfo.put("UnPassStudentCount", studentScoreInfo1.get("studentcount").toString());
								break;
							}
						}
						//找到同学院的及格人数和及格率
						for(Map studentScoreInfo2 : studentScoreInfoList2){
							if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo2.get("collegename").toString()))
							{
								studentScoreInfo.put("PassStudentCount", studentScoreInfo2.get("studentcount").toString());
								float PassStudentPer= Float.parseFloat(studentScoreInfo2.get("studentcount").toString())/studentCount;
								float PassStudentPercent=(float)(Math.round(PassStudentPer*100*100))/100;//保留2位小数
								String PassStudentPercentage=PassStudentPercent+"%";
								studentScoreInfo.put("PassStudentPercent",PassStudentPercentage);
								break;
							}
						}
						//找到同学院的优秀人数和优秀率
						for(Map studentScoreInfo3 : studentScoreInfoList3){
							if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo3.get("collegename").toString()))
							{
								studentScoreInfo.put("ExcellentStudentCount", studentScoreInfo3.get("studentcount").toString());
								float ExcellentStudentPer= Float.parseFloat(studentScoreInfo3.get("studentcount").toString())/studentCount;
								float ExcellentStudentPercent=(float)(Math.round(ExcellentStudentPer*100*100))/100;//保留2位小数
								String ExcellentStudentPercentage=ExcellentStudentPercent+"%";
								studentScoreInfo.put("ExcellentStudentPercent",ExcellentStudentPercentage);
								break;
							}
						}
						
						
						scoreSummaryInfoList.add(studentScoreInfo);
					}
			
			//设置空白为0
			for(Map SummaryInfo : scoreSummaryInfoList){
				
				if(!SummaryInfo.containsKey("UnPassStudentCount")){
					
					SummaryInfo.put("UnPassStudentCount", "0");									
				}
				
               if(!SummaryInfo.containsKey("PassStudentCount")){
					
					SummaryInfo.put("PassStudentCount", "0");									
				}
               
               if(!SummaryInfo.containsKey("ExcellentStudentCount")){
					
					SummaryInfo.put("ExcellentStudentCount", "0");									
				}
               
               if(!SummaryInfo.containsKey("StudentCount")){
					
					SummaryInfo.put("StudentCount", "0");									
				}
               
               if(!SummaryInfo.containsKey("PassStudentPercent")){
					
					SummaryInfo.put("PassStudentPercent", "0%");									
				}
               
               if(!SummaryInfo.containsKey("ExcellentStudentPercent")){
					
					SummaryInfo.put("ExcellentStudentPercent", "0%");									
				}
               
			}
				
			return scoreSummaryInfoList;
		
	    } catch (Exception e) {
			System.out.println("summaryScore error:" + e.getMessage());
			return null;
		}
	}
	
	//得到语种列表
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List getLanguageList(String schoolnum){
		try {
			List<Map> languageList = studentDao.getLanguageList(schoolnum);
			for(Map element : languageList) {
				String language = element.get("languagenum") + "," + element.get("languagename");
				element.put("languagename", language);
			}
			return languageList;
		} catch (Exception e) {
			System.out.println("getLanguageList error!" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List getLanguageList_theory(String schoolnum){
		try {
			List<Map> languageList = studentDao.getLanguageList_theory(schoolnum);
			for(Map element : languageList) {
				String language = element.get("languagenum") + "," + element.get("languagename");
				element.put("languagename", language);
			}
			return languageList;
		} catch (Exception e) {
			System.out.println("getLanguageList error!" + e.getMessage());
			return null;
		}
	}
	
	
	//得到语种列表
		@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
		public List getLanguageList_without_languagenum(String schoolnum){
			try {
				List<Map> languageList = studentDao.getLanguageList(schoolnum);
				return languageList;
			} catch (Exception e) {
				System.out.println("getLanguageList error!" + e.getMessage());
				return null;
			}
		}
//	
//	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
//	public List<Map> getLoadSignedLanguage(String institutionnum) {
//		List<Map> languageList = this.operateExamArrangeDao.getLoadSignedLanguage(institutionnum);
//		for(Map element : languageList) {
//			String language = element.get("languagenum") + "," + element.get("languagename");
//			element.put("languagename", language);
//		}
//		return languageList;
//	}
	
	//年级成绩统计
		@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
		public List<Map> summaryScoreByGrade(JSONObject summaryCondition,String schoolnum){
		    try{
			    //先收索出3个分数段的人数，然后再整合成一个map对象
						
				List<Map> scoreSummaryInfoList = new ArrayList<Map>();
			
				String languagenum =summaryCondition.get("language").toString();//得到语种
				
				//收索出一个学校的每个年级及总人数	
				List<Map> studentScoreInfoList0 = studentDao.getScoreInfo_getGrade(schoolnum, languagenum);
				
				//收索出不及格人数	
				List<Map> studentScoreInfoList1 = studentDao.getScoreInfo_getUnPassStudent(schoolnum, languagenum,summaryCondition);
				//收索出及格人数
				List<Map> studentScoreInfoList2 = studentDao.getScoreInfo_getPassStudent(schoolnum, languagenum,summaryCondition);
				
				//收索出优秀人数
				List<Map> studentScoreInfoList3 = studentDao.getScoreInfo_getExcellentStudent(schoolnum, languagenum,summaryCondition);
				
				//开始整合到一个大的List中
				for(Map studentScoreInfo0 : studentScoreInfoList0){
							Map studentScoreInfo = new JSONObject();
							
							studentScoreInfo.put("grade", studentScoreInfo0.get("grade").toString());
							int studentCount=Integer.parseInt(studentScoreInfo0.get("studentcount").toString());
							studentScoreInfo.put("StudentCount", studentScoreInfo0.get("studentcount").toString());
							//找到同年级的不及格人数
							for(Map studentScoreInfo1 : studentScoreInfoList1){
								if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo1.get("grade").toString()))
								{
									studentScoreInfo.put("UnPassStudentCount", studentScoreInfo1.get("studentcount").toString());
									break;
								}
							}
							//找到同年级的及格人数和及格率
							for(Map studentScoreInfo2 : studentScoreInfoList2){
								if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo2.get("grade").toString()))
								{
									studentScoreInfo.put("PassStudentCount", studentScoreInfo2.get("studentcount").toString());
									float PassStudentPer= Float.parseFloat(studentScoreInfo2.get("studentcount").toString())/studentCount;							
									float PassStudentPercent=(float)(Math.round(PassStudentPer*100*100))/100;//保留2位小数
									String PassStudentPercentage=PassStudentPercent+"%";
									studentScoreInfo.put("PassStudentPercent",PassStudentPercentage);
									break;
								}
							}
							//找到同年级的优秀人数和优秀率
							for(Map studentScoreInfo3 : studentScoreInfoList3){
								if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo3.get("grade").toString()))
								{
									studentScoreInfo.put("ExcellentStudentCount", studentScoreInfo3.get("studentcount").toString());
									float ExcellentStudentPer= Float.parseFloat(studentScoreInfo3.get("studentcount").toString())/studentCount;
									float ExcellentStudentPercent=(float)(Math.round(ExcellentStudentPer*100*100))/100;//保留2位小数
									String ExcellentStudentPercentage=ExcellentStudentPercent+"%";
									studentScoreInfo.put("ExcellentStudentPercent",ExcellentStudentPercentage);
									break;
								}
							}
							
							
							scoreSummaryInfoList.add(studentScoreInfo);
						}
				
				
				//设置空白为0
				for(Map SummaryInfo : scoreSummaryInfoList){
					
					if(!SummaryInfo.containsKey("UnPassStudentCount")){
						
						SummaryInfo.put("UnPassStudentCount", "0");									
					}
					
                   if(!SummaryInfo.containsKey("PassStudentCount")){
						
						SummaryInfo.put("PassStudentCount", "0");									
					}
                   
                   if(!SummaryInfo.containsKey("ExcellentStudentCount")){
						
						SummaryInfo.put("ExcellentStudentCount", "0");									
					}
                   
                   if(!SummaryInfo.containsKey("StudentCount")){
						
						SummaryInfo.put("StudentCount", "0");									
					}
                   
                   if(!SummaryInfo.containsKey("PassStudentPercent")){
						
						SummaryInfo.put("PassStudentPercent", "0%");									
					}
                   
                   if(!SummaryInfo.containsKey("ExcellentStudentPercent")){
						
						SummaryInfo.put("ExcellentStudentPercent", "0%");									
					}
                   
				}
				
				
					
				return scoreSummaryInfoList;
			
		    } catch (Exception e) {
				System.out.println("summaryScoreByGrade error:" + e.getMessage());
				return null;
			}
		}
		
		//得到学院列表
		@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
		public List<Map> getCollegeList(String schoolnum){
			try {
				 List<Map> result=(List<Map>)studentDao.getCollegeList(schoolnum);
				 for (Map temp:result)
				 { 
					 String value=temp.get("name").toString();
					 temp.put("value", value);
					 
				 }
				return result;
			} catch (Exception e) {
				System.out.println("getCollegeList error!" + e.getMessage());
				return null;
			}
		}
		
		
		//按学院各年级成绩统计
				@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
				public List<Map> summaryScoreByCollege_Grade(JSONObject summaryCondition,String schoolnum){
				    try{
					    //先收索出3个分数段的人数，然后再整合成一个map对象
								
						List<Map> scoreSummaryInfoList = new ArrayList<Map>();
					
						String collegename =summaryCondition.get("value").toString();//得到学院 
						String languagenum =summaryCondition.get("language").toString();//得到语种
						
						//收索出一个学校某个学院的每个年级及总人数	
						List<Map> studentScoreInfoList0 = studentDao.getScoreInfo_getGrade(schoolnum,collegename,languagenum);
						
						//收索出不及格人数	
						List<Map> studentScoreInfoList1 = studentDao.getScoreInfo_getUnPassStudent(schoolnum,collegename,languagenum,summaryCondition);
						//收索出及格人数
						List<Map> studentScoreInfoList2 = studentDao.getScoreInfo_getPassStudent(schoolnum,collegename,languagenum,summaryCondition);
						
						//收索出优秀人数
						List<Map> studentScoreInfoList3 = studentDao.getScoreInfo_getExcellentStudent(schoolnum,collegename,languagenum,summaryCondition);
						
						//开始整合到一个大的List中
						for(Map studentScoreInfo0 : studentScoreInfoList0){
									Map studentScoreInfo = new JSONObject();
									
									studentScoreInfo.put("grade", studentScoreInfo0.get("grade").toString());
									int studentCount=Integer.parseInt(studentScoreInfo0.get("studentcount").toString());
									studentScoreInfo.put("StudentCount", studentScoreInfo0.get("studentcount").toString());
									//找到同年级的不及格人数
									for(Map studentScoreInfo1 : studentScoreInfoList1){
										if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo1.get("grade").toString()))
										{
											studentScoreInfo.put("UnPassStudentCount", studentScoreInfo1.get("studentcount").toString());
											break;
										}
									}
									//找到同年级的及格人数和及格率
									for(Map studentScoreInfo2 : studentScoreInfoList2){
										if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo2.get("grade").toString()))
										{
											studentScoreInfo.put("PassStudentCount", studentScoreInfo2.get("studentcount").toString());
											float PassStudentPer= Float.parseFloat(studentScoreInfo2.get("studentcount").toString())/studentCount;							
											float PassStudentPercent=(float)(Math.round(PassStudentPer*100*100))/100;//保留2位小数
											String PassStudentPercentage=PassStudentPercent+"%";
											studentScoreInfo.put("PassStudentPercent",PassStudentPercentage);
											break;
										}
									}
									//找到同年级的优秀人数和优秀率
									for(Map studentScoreInfo3 : studentScoreInfoList3){
										if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo3.get("grade").toString()))
										{
											studentScoreInfo.put("ExcellentStudentCount", studentScoreInfo3.get("studentcount").toString());
											float ExcellentStudentPer= Float.parseFloat(studentScoreInfo3.get("studentcount").toString())/studentCount;
											float ExcellentStudentPercent=(float)(Math.round(ExcellentStudentPer*100*100))/100;//保留2位小数
											String ExcellentStudentPercentage=ExcellentStudentPercent+"%";
											studentScoreInfo.put("ExcellentStudentPercent",ExcellentStudentPercentage);
											break;
										}
									}
									
									
									scoreSummaryInfoList.add(studentScoreInfo);
								}
						
						
						//设置空白为0
						for(Map SummaryInfo : scoreSummaryInfoList){
							
							if(!SummaryInfo.containsKey("UnPassStudentCount")){
								
								SummaryInfo.put("UnPassStudentCount", "0");									
							}
							
	                       if(!SummaryInfo.containsKey("PassStudentCount")){
								
								SummaryInfo.put("PassStudentCount", "0");									
							}
	                       
	                       if(!SummaryInfo.containsKey("ExcellentStudentCount")){
								
								SummaryInfo.put("ExcellentStudentCount", "0");									
							}
	                       
	                       if(!SummaryInfo.containsKey("StudentCount")){
								
								SummaryInfo.put("StudentCount", "0");									
							}
	                       
	                       if(!SummaryInfo.containsKey("PassStudentPercent")){
								
								SummaryInfo.put("PassStudentPercent", "0%");									
							}
	                       
	                       if(!SummaryInfo.containsKey("ExcellentStudentPercent")){
								
								SummaryInfo.put("ExcellentStudentPercent", "0%");									
							}
	                       
						}
							
	      					return scoreSummaryInfoList;
					
				    } catch (Exception e) {
						System.out.println("summaryScoreByCollege_Grade error:" + e.getMessage());
						return null;
					}
				}
				
			//得到年级列表
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List getGradeList(String schoolnum){
					try {
						return studentDao.getGradeList(schoolnum);
					} catch (Exception e) {
						System.out.println("getCollegeList error!" + e.getMessage());
						return null;
					}
				}
				
			//按年级各学院成绩统计
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public List<Map> summaryScoreByGrade_College(JSONObject summaryCondition,String schoolnum){
			    try{
				    //先收索出3个分数段的人数，然后再整合成一个map对象
							
					List<Map> scoreSummaryInfoList = new ArrayList<Map>();
				
					String grade =summaryCondition.get("grade").toString();//得到年级 
					String languagenum =summaryCondition.get("language").toString();//得到语种
					
					//收索出一个学校某个年级的各学院及总人数	
					List<Map> studentScoreInfoList0 = studentDao.getScoreInfo_getCollege(schoolnum,grade,languagenum);
					
					//收索出不及格人数	
					List<Map> studentScoreInfoList1 = studentDao.getScoreInfoByGrade_College_getUnPassStudent(schoolnum,grade,languagenum,summaryCondition);
					//收索出及格人数
					List<Map> studentScoreInfoList2 = studentDao.getScoreInfoByGrade_College_getPassStudent(schoolnum,grade,languagenum,summaryCondition);
					
					//收索出优秀人数
					List<Map> studentScoreInfoList3 = studentDao.getScoreInfoByGrade_College_getExcellentStudent(schoolnum,grade,languagenum,summaryCondition);
					
					//开始整合到一个大的List中
					for(Map studentScoreInfo0 : studentScoreInfoList0){
								Map studentScoreInfo = new JSONObject();
								
								studentScoreInfo.put("collegename", studentScoreInfo0.get("collegename").toString());
								int studentCount=Integer.parseInt(studentScoreInfo0.get("studentcount").toString());
								studentScoreInfo.put("StudentCount", studentScoreInfo0.get("studentcount").toString());
								//找到同年级的不及格人数
								for(Map studentScoreInfo1 : studentScoreInfoList1){
									if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo1.get("collegename").toString()))
									{
										studentScoreInfo.put("UnPassStudentCount", studentScoreInfo1.get("studentcount").toString());
										break;
									}
								}
								//找到同年级的及格人数和及格率
								for(Map studentScoreInfo2 : studentScoreInfoList2){
									if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo2.get("collegename").toString()))
									{
										studentScoreInfo.put("PassStudentCount", studentScoreInfo2.get("studentcount").toString());
										float PassStudentPer= Float.parseFloat(studentScoreInfo2.get("studentcount").toString())/studentCount;							
										float PassStudentPercent=(float)(Math.round(PassStudentPer*100*100))/100;//保留2位小数
										String PassStudentPercentage=PassStudentPercent+"%";
										studentScoreInfo.put("PassStudentPercent",PassStudentPercentage);
										break;
									}
								}
								//找到同年级的优秀人数和优秀率
								for(Map studentScoreInfo3 : studentScoreInfoList3){
									if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo3.get("collegename").toString()))
									{
										studentScoreInfo.put("ExcellentStudentCount", studentScoreInfo3.get("studentcount").toString());
										float ExcellentStudentPer= Float.parseFloat(studentScoreInfo3.get("studentcount").toString())/studentCount;
										float ExcellentStudentPercent=(float)(Math.round(ExcellentStudentPer*100*100))/100;//保留2位小数
										String ExcellentStudentPercentage=ExcellentStudentPercent+"%";
										studentScoreInfo.put("ExcellentStudentPercent",ExcellentStudentPercentage);
										break;
									}
								}
								
								
								scoreSummaryInfoList.add(studentScoreInfo);
							}
					
					//设置空白为0
					for(Map SummaryInfo : scoreSummaryInfoList){
						
						if(!SummaryInfo.containsKey("UnPassStudentCount")){
							
							SummaryInfo.put("UnPassStudentCount", "0");									
						}
						
                       if(!SummaryInfo.containsKey("PassStudentCount")){
							
							SummaryInfo.put("PassStudentCount", "0");									
						}
                       
                       if(!SummaryInfo.containsKey("ExcellentStudentCount")){
							
							SummaryInfo.put("ExcellentStudentCount", "0");									
						}
                       
                       if(!SummaryInfo.containsKey("StudentCount")){
							
							SummaryInfo.put("StudentCount", "0");									
						}
                       
                       if(!SummaryInfo.containsKey("PassStudentPercent")){
							
							SummaryInfo.put("PassStudentPercent", "0%");									
						}
                       
                       if(!SummaryInfo.containsKey("ExcellentStudentPercent")){
							
							SummaryInfo.put("ExcellentStudentPercent", "0%");									
						}
                       
					}
                       
                       
						
						
					return scoreSummaryInfoList;
				
			    } catch (Exception e) {
					System.out.println("summaryScoreByGrade_College error:" + e.getMessage());
					return null;
				}
			}
			
	//得到年级报名信息统计
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public List<Map> getGradeSignUpInfo(String schoolnum){
				try {
					List<Map> languagelist = languagedao.getAllLanguage();
					List<Map> collegesignupinfolist = new ArrayList<Map>();
					for(Map language : languagelist){
						String languagenum = language.get("languagenum").toString();
						if(collegesignupinfolist.isEmpty()){
							List<Map> collegesigninfolist = studentDao.getGradeSignUpInfo(schoolnum, languagenum);
							for(Map collegeinfo : collegesigninfolist){
								Map collegesignupinfo1 = new JSONObject();
								collegesignupinfo1.put("grade", collegeinfo.get("grade").toString());
								collegesignupinfo1.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
								collegesignupinfolist.add(collegesignupinfo1);
							}
						}else{
							List<Map> collegesigninfolist = (List<Map>)studentDao.getGradeSignUpInfo(schoolnum, languagenum);
							for(Map collegeinfo : collegesigninfolist){
								boolean inserted = false;
								for(Map collegesignupinfo : collegesignupinfolist){
									if(!inserted && collegesignupinfo.get("grade").equals(collegeinfo.get("grade"))){
										collegesignupinfo.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
										inserted = true;
									}
								}
								if(!inserted){
									Map collegesignupinfo2 = new JSONObject();
									collegesignupinfo2.put("grade", collegeinfo.get("grade").toString());
									collegesignupinfo2.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
									collegesignupinfolist.add(collegesignupinfo2);
									inserted = true;
								}
							}
						}
					}
					for(Map sumcollegesignup : studentDao.getGradeSignUpInfo(schoolnum)){
						boolean inserted = false;
						if(!inserted){
							for(Map collegesignupinfo : collegesignupinfolist){
								if(!inserted && collegesignupinfo.get("grade").equals(sumcollegesignup.get("grade"))){
									collegesignupinfo.put("sumstudentcount", sumcollegesignup.get("studentcount").toString());
									inserted = true;
								}
							}
						}else if(!inserted){
							Map collegesignupinfo2 = new JSONObject();
							collegesignupinfo2.put("grade", sumcollegesignup.get("grade").toString());
							collegesignupinfo2.put("sumstudentcount", sumcollegesignup.get("studentcount").toString());
							collegesignupinfolist.add(collegesignupinfo2);
							inserted = true;
						}
					}
					Map collegesignupinfo3 = new JSONObject();
					collegesignupinfo3.put("grade", "合计");
					int count = 0;
					for(Map sumcollegesignup : studentDao.getSignUpInfoGroupByLanguage(schoolnum)){
						collegesignupinfo3.put(sumcollegesignup.get("languagenum").toString()+"studentcount", sumcollegesignup.get("studentcount").toString());
						count += Integer.parseInt(sumcollegesignup.get("studentcount").toString());
					}
					collegesignupinfo3.put("sumstudentcount", count);
					collegesignupinfolist.add(collegesignupinfo3);
					
					//设置空白为0
					for(Map SummaryInfo : collegesignupinfolist){
						
						if(!SummaryInfo.containsKey("sumstudentcount")){
							
							SummaryInfo.put("sumstudentcount", "0");									
						}
						
		               if(!SummaryInfo.containsKey("11studentcount")){
							
							SummaryInfo.put("11studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("12studentcount")){
							
							SummaryInfo.put("12studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("21studentcount")){
							
							SummaryInfo.put("21studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("22studentcount")){
							
							SummaryInfo.put("22studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("23studentcount")){
							
							SummaryInfo.put("23studentcount", "0");									
						}
		                            
		               if(!SummaryInfo.containsKey("24studentcount")){
							
							SummaryInfo.put("24studentcount", "0");									
						}
		              
		              if(!SummaryInfo.containsKey("25studentcount")){
							
							SummaryInfo.put("25studentcount", "0");									
						}
		              
		              if(!SummaryInfo.containsKey("26studentcount")){
							
							SummaryInfo.put("26studentcount", "0");									
						}
		                                
		              
		              if(!SummaryInfo.containsKey("31studentcount")){
							
							SummaryInfo.put("31studentcount", "0");									
						}
		            
		            if(!SummaryInfo.containsKey("32studentcount")){
							
							SummaryInfo.put("32studentcount", "0");									
						}
		            
		            if(!SummaryInfo.containsKey("33studentcount")){
							
							SummaryInfo.put("33studentcount", "0");									
						}
		            
		            if(!SummaryInfo.containsKey("34studentcount")){
						
						SummaryInfo.put("34studentcount", "0");									
					}
		        
		           if(!SummaryInfo.containsKey("35studentcount")){
						
						SummaryInfo.put("35studentcount", "0");									
					}
		               
					}
					
					
					
					return collegesignupinfolist;
				} catch (Exception e) {
					System.out.println("getGradeSignUpInfo error:" + e.getMessage());
					return null;
				}
			}
			
			//按年级各学院成绩统计
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public List<Map> getScoreInfo(String IDnum){
			    try{
			    	return studentDao.getScoreInfo(IDnum);
			    } catch (Exception e) {
					System.out.println("getScoreInfo error:" + e.getMessage());
					return null;
				}
			}
			
			
			//得到学校名称
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getSchoolName(String schoolnum){
					try {
						return studentDao.getSchoolName(schoolnum);
					} catch (Exception e) {
						System.out.println("getSchoolName error!" + e.getMessage());
						return null;
					}
				}
			
			
			//得到考场总数
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public long getTotalCount(String whereString,Map<String,Object> valuesMap,String schoolnum){
				try {
					return studentDao.getTotalCount(whereString, valuesMap,schoolnum);
				} catch (Exception e) {
					System.out.println("getTotalCount error:" + e.getMessage());
					return 0;
				}
			}
			
			//得到专业列表
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getProfessionList(String schoolnum){
				try {
					List<Map> result=(List<Map>)studentDao.getProfessionList(schoolnum);
					 for (Map temp:result)
					 { 
						 String value=temp.get("name").toString();
						 temp.put("value", value);
						 
					 }
					return result;					
				} catch (Exception e) {
					System.out.println("getProfessionList error!" + e.getMessage());
					return null;
				}
			}
			//得到某语种某专业的所有学生   
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getStudentByProfession_language(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
				try {
					return studentDao.getStudentByProfession_language(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
				} catch (Exception e) {
					System.out.println("getStudentByProfession_language error:" + e.getMessage());
					return null;
				}
			}
			//得到某语种某专业的学生 总人数
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public long getStudentsTotalCountByProfession_language(String whereString,Map<String,Object> valuesMap,String schoolnum){
				try {
					return studentDao.getStudentsTotalCountByProfession_language(whereString, valuesMap,schoolnum);
				} catch (Exception e) {
					System.out.println("getStudentsTotalCountByProfession_language error:" + e.getMessage());
					return 0;
				}
			}
			//得到某学生的整组数据
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public  Map getStudentAllInfo(String IDnum){
				try{
					Map sutdentinfo = studentDao.getStudentAllInfo(IDnum);
					if(studentDao.getStudentAllInfo(IDnum).get("sex").equals("M")){
						sutdentinfo.put("sex", "男");
					}else{
						sutdentinfo.put("sex", "女");
					}
					if(studentDao.getStudentAllInfo(IDnum).get("paied").equals("1")){
						sutdentinfo.put("paied", "是");
					}else{
						sutdentinfo.put("paied", "否");
					}
					if(studentDao.getStudentAllInfo(IDnum).get("studentcategory").equals("1")){
						sutdentinfo.put("studentcategory", "普通本科");
					}else if(studentDao.getStudentAllInfo(IDnum).get("studentcategory").equals("2")){
						sutdentinfo.put("studentcategory", "普通专科");
					}else{
						sutdentinfo.put("studentcategory", "其他");
					}
					return sutdentinfo;
				} catch (Exception e) {
					System.out.println("getStudentAllInfo error:" + e.getMessage());
					e.printStackTrace();
			return null;
				}
			}
			
			//得到某语种的所有学生 的准考证号
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getAdmissionNum(String logicExamRoomNum,String schoolnum){
				try {
					return studentDao.getAdmissionNum(logicExamRoomNum,schoolnum);
				} catch (Exception e) {
					System.out.println("getAdmissionNum error:" + e.getMessage());
					return null;
				}
			}
			
			//学校集体报名汇总
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public List<Map> getSignUpInfo(String schoolnum){
				try {
					List<Map> languagelist = languagedao.getAllLanguage();
					List<Map> collegesignupinfolist = new ArrayList<Map>();
					for(Map language : languagelist){
						String languagenum = language.get("languagenum").toString();
						if(collegesignupinfolist.isEmpty()){
							List<Map> collegesigninfolist = studentDao.getAllGrade_SignUpInfo(schoolnum, languagenum);
							for(Map collegeinfo : collegesigninfolist){
								Map collegesignupinfo1 = new JSONObject();
								collegesignupinfo1.put("grade", collegeinfo.get("grade").toString());
								collegesignupinfo1.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
								collegesignupinfolist.add(collegesignupinfo1);
							}
						}else{
							List<Map> collegesigninfolist = (List<Map>)studentDao.getAllGrade_SignUpInfo(schoolnum, languagenum);
							for(Map collegeinfo : collegesigninfolist){
								boolean inserted = false;
								for(Map collegesignupinfo : collegesignupinfolist){
									if(!inserted && collegesignupinfo.get("grade").equals(collegeinfo.get("grade"))){
										collegesignupinfo.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
										inserted = true;
									}
								}
								if(!inserted){
									Map collegesignupinfo2 = new JSONObject();
									collegesignupinfo2.put("grade", collegeinfo.get("grade").toString());
									collegesignupinfo2.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
									collegesignupinfolist.add(collegesignupinfo2);
									inserted = true;
								}
							}
						}
					}
					for(Map sumcollegesignup : studentDao.getAllGrade_SignUpInfo(schoolnum)){
						boolean inserted = false;
						if(!inserted){
							for(Map collegesignupinfo : collegesignupinfolist){
								if(!inserted && collegesignupinfo.get("grade").equals(sumcollegesignup.get("grade"))){
									collegesignupinfo.put("sumstudentcount", sumcollegesignup.get("studentcount").toString());
									collegesignupinfo.put("sumfee", Integer.parseInt(sumcollegesignup.get("studentcount").toString())*30);
									
									inserted = true;
								}
							}
						}else if(!inserted){
							Map collegesignupinfo2 = new JSONObject();
							collegesignupinfo2.put("grade", sumcollegesignup.get("grade").toString());
							collegesignupinfo2.put("sumstudentcount", sumcollegesignup.get("studentcount").toString());
							collegesignupinfo2.put("sumfee", Integer.parseInt(sumcollegesignup.get("studentcount").toString())*30);			
							collegesignupinfolist.add(collegesignupinfo2);
							inserted = true;
						}
					}
					Map collegesignupinfo3 = new JSONObject();
					collegesignupinfo3.put("grade", "合计");
					
				/*	Map collegesignupinfo4 = new JSONObject();
					collegesignupinfo4.put("grade", "考场");
					
					Map collegesignupinfo5 = new JSONObject();
					collegesignupinfo5.put("grade", "考场");*/
					
					int count = 0;
					for(Map sumcollegesignup : studentDao.getSignUpInfoGroupByLanguage(schoolnum)){
						collegesignupinfo3.put(sumcollegesignup.get("languagenum").toString()+"studentcount", sumcollegesignup.get("studentcount").toString());
						count += Integer.parseInt(sumcollegesignup.get("studentcount").toString());
					/*
						int signalLanguageCount=Integer.parseInt(sumcollegesignup.get("studentcount").toString());
						int flag=1;
						if(signalLanguageCount%30==0)flag=0;
						collegesignupinfo4.put(sumcollegesignup.get("languagenum").toString()+"studentcount", signalLanguageCount/30);
						collegesignupinfo5.put(sumcollegesignup.get("languagenum").toString()+"studentcount",flag);
					*/
					}
					collegesignupinfo3.put("sumstudentcount", count);
					collegesignupinfo3.put("sumfee", count*30);
					
				/*	collegesignupinfo4.put("sumstudentcount", "标准");
					collegesignupinfo5.put("sumstudentcount", "非标准");
					collegesignupinfo4.put("sumfee", " ");
					collegesignupinfo5.put("sumfee", " ");*/
					collegesignupinfolist.add(collegesignupinfo3);
				//	collegesignupinfolist.add(collegesignupinfo4);
				//	collegesignupinfolist.add(collegesignupinfo5);
					
					//设置空白为0
					for(Map SummaryInfo : collegesignupinfolist){
						
						if(!SummaryInfo.containsKey("sumstudentcount")){
							
							SummaryInfo.put("sumstudentcount", "0");									
						}
						
		               if(!SummaryInfo.containsKey("11studentcount")){
							
							SummaryInfo.put("11studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("12studentcount")){
							
							SummaryInfo.put("12studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("21studentcount")){
							
							SummaryInfo.put("21studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("22studentcount")){
							
							SummaryInfo.put("22studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("23studentcount")){
							
							SummaryInfo.put("23studentcount", "0");									
						}
		                            
		               if(!SummaryInfo.containsKey("24studentcount")){
							
							SummaryInfo.put("24studentcount", "0");									
						}
		              
		              if(!SummaryInfo.containsKey("25studentcount")){
							
							SummaryInfo.put("25studentcount", "0");									
						}
		              
		              if(!SummaryInfo.containsKey("26studentcount")){
							
							SummaryInfo.put("26studentcount", "0");									
						}
		                                
		              
		              if(!SummaryInfo.containsKey("31studentcount")){
							
							SummaryInfo.put("31studentcount", "0");									
						}
		            
		            if(!SummaryInfo.containsKey("32studentcount")){
							
							SummaryInfo.put("32studentcount", "0");									
						}
		            
		            if(!SummaryInfo.containsKey("33studentcount")){
							
							SummaryInfo.put("33studentcount", "0");									
						}
		            
		            if(!SummaryInfo.containsKey("34studentcount")){
						
						SummaryInfo.put("34studentcount", "0");									
					}
		        
		           if(!SummaryInfo.containsKey("35studentcount")){
						
						SummaryInfo.put("35studentcount", "0");									
					}
		           
		           if(!SummaryInfo.containsKey("sumfee")){
						
						SummaryInfo.put("sumfee", "0");									
					}
		               
					}	
																			
					return collegesignupinfolist;
				} catch (Exception e) {
					System.out.println("getSignUpInfo error:" + e.getMessage());
					return null;
				}
			}
			
			//根据给定条件查询出学校集体报名汇总
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public List<Map> getSignUpInfo(JSONObject Condition,String schoolnum){
				try {
					String campusnum =Condition.get("campusnum").toString();//得到校区 
					if(campusnum.equals("不填则打印全校"))campusnum="";
					int    AGradeFee=Integer.parseInt(Condition.get("AGradeFee").toString());//得到一级报名费
					int    BGradeFee=Integer.parseInt(Condition.get("BGradeFee").toString());//得到二级报名费
					int    CGradeFee=Integer.parseInt(Condition.get("CGradeFee").toString());//得到三级报名费
					
					List<Map> languagelist = languagedao.getAllLanguage();
					List<Map> collegesignupinfolist = new ArrayList<Map>();
					for(Map language : languagelist){
						String languagenum = language.get("languagenum").toString();
						if(collegesignupinfolist.isEmpty()){
							List<Map> collegesigninfolist = studentDao.getAllGrade_SignUpInfo(campusnum,schoolnum, languagenum);
							for(Map collegeinfo : collegesigninfolist){
								Map collegesignupinfo1 = new JSONObject();
								collegesignupinfo1.put("grade", collegeinfo.get("grade").toString());
								collegesignupinfo1.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
								collegesignupinfolist.add(collegesignupinfo1);
							}
						}else{
							List<Map> collegesigninfolist = (List<Map>)studentDao.getAllGrade_SignUpInfo(campusnum,schoolnum, languagenum);
							for(Map collegeinfo : collegesigninfolist){
								boolean inserted = false;
								for(Map collegesignupinfo : collegesignupinfolist){
									if(!inserted && collegesignupinfo.get("grade").equals(collegeinfo.get("grade"))){
										collegesignupinfo.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
										inserted = true;
									}
								}
								if(!inserted){
									Map collegesignupinfo2 = new JSONObject();
									collegesignupinfo2.put("grade", collegeinfo.get("grade").toString());
									collegesignupinfo2.put(languagenum + "studentcount", collegeinfo.get("studentcount").toString());
									collegesignupinfolist.add(collegesignupinfo2);
									inserted = true;
								}
							}
						}
					}
					int summaryMoney=0;
					//得到各个年级的报考人数
					for(Map sumcollegesignup : studentDao.getAllGrade_studentcount(campusnum,schoolnum)){
						boolean inserted = false;
						if(!inserted){
							for(Map collegesignupinfo : collegesignupinfolist){
								if(!inserted && collegesignupinfo.get("grade").equals(sumcollegesignup.get("grade"))){
									collegesignupinfo.put("sumstudentcount", sumcollegesignup.get("studentcount").toString());
									//计算报名费
									int sumfee=0;
									if(collegesignupinfo.containsKey("11studentcount"))					
									sumfee+=Integer.parseInt(collegesignupinfo.get("11studentcount").toString())*AGradeFee;						
									if(collegesignupinfo.containsKey("12studentcount"))
									sumfee+=Integer.parseInt(collegesignupinfo.get("12studentcount").toString())*AGradeFee;
									if(collegesignupinfo.containsKey("21studentcount"))	
									sumfee+=Integer.parseInt(collegesignupinfo.get("21studentcount").toString())*BGradeFee;
									if(collegesignupinfo.containsKey("22studentcount"))	
									sumfee+=Integer.parseInt(collegesignupinfo.get("22studentcount").toString())*BGradeFee;
									if(collegesignupinfo.containsKey("23studentcount"))
									sumfee+=Integer.parseInt(collegesignupinfo.get("23studentcount").toString())*BGradeFee;
									if(collegesignupinfo.containsKey("24studentcount"))
									sumfee+=Integer.parseInt(collegesignupinfo.get("24studentcount").toString())*BGradeFee;
									if(collegesignupinfo.containsKey("25studentcount"))	
									sumfee+=Integer.parseInt(collegesignupinfo.get("25studentcount").toString())*BGradeFee;
									if(collegesignupinfo.containsKey("26studentcount"))
									sumfee+=Integer.parseInt(collegesignupinfo.get("26studentcount").toString())*BGradeFee;
									if(collegesignupinfo.containsKey("31studentcount"))	
									sumfee+=Integer.parseInt(collegesignupinfo.get("31studentcount").toString())*CGradeFee;
									if(collegesignupinfo.containsKey("32studentcount"))
									sumfee+=Integer.parseInt(collegesignupinfo.get("32studentcount").toString())*CGradeFee;
									if(collegesignupinfo.containsKey("33studentcount"))
									sumfee+=Integer.parseInt(collegesignupinfo.get("33studentcount").toString())*CGradeFee;
									if(collegesignupinfo.containsKey("34studentcount"))
									sumfee+=Integer.parseInt(collegesignupinfo.get("34studentcount").toString())*BGradeFee;
									if(collegesignupinfo.containsKey("35studentcount"))	
									sumfee+=Integer.parseInt(collegesignupinfo.get("35studentcount").toString())*CGradeFee;
									summaryMoney+=sumfee;
									collegesignupinfo.put("sumfee", sumfee);
									
									inserted = true;
								}
							}
						}else if(!inserted){
							Map collegesignupinfo2 = new JSONObject();
							collegesignupinfo2.put("grade", sumcollegesignup.get("grade").toString());
							collegesignupinfo2.put("sumstudentcount", sumcollegesignup.get("studentcount").toString());
							//collegesignupinfo2.put("sumfee", Integer.parseInt(sumcollegesignup.get("studentcount").toString())*30);			
							
							collegesignupinfolist.add(collegesignupinfo2);
							inserted = true;
						}
					}
					//得到合计
					Map collegesignupinfo3 = new JSONObject();
					collegesignupinfo3.put("grade", "合计");
					
				/*	Map collegesignupinfo4 = new JSONObject();
					collegesignupinfo4.put("grade", "考场");
					
					Map collegesignupinfo5 = new JSONObject();
					collegesignupinfo5.put("grade", "考场");  */
					
					int count = 0;
					for(Map sumcollegesignup : studentDao.getSignUpInfoGroupByLanguage(campusnum,schoolnum)){
						collegesignupinfo3.put(sumcollegesignup.get("languagenum").toString()+"studentcount", sumcollegesignup.get("studentcount").toString());		
						count += Integer.parseInt(sumcollegesignup.get("studentcount").toString());
					/*
						int signalLanguageCount=Integer.parseInt(sumcollegesignup.get("studentcount").toString());
						int flag=1;
						if(signalLanguageCount%30==0)flag=0;
						collegesignupinfo4.put(sumcollegesignup.get("languagenum").toString()+"studentcount", signalLanguageCount/30);
						collegesignupinfo5.put(sumcollegesignup.get("languagenum").toString()+"studentcount",flag);
					*/
					
					}
					collegesignupinfo3.put("sumstudentcount", count);
					collegesignupinfo3.put("sumfee",summaryMoney);
					
				/*	collegesignupinfo4.put("sumstudentcount", "标准");
					collegesignupinfo5.put("sumstudentcount", "非标准");
					collegesignupinfo4.put("sumfee", " ");
					collegesignupinfo5.put("sumfee", " ");*/
					collegesignupinfolist.add(collegesignupinfo3);
				//	collegesignupinfolist.add(collegesignupinfo4);
				//	collegesignupinfolist.add(collegesignupinfo5);
					
					
					//设置空白为0
					for(Map SummaryInfo : collegesignupinfolist){
						
						if(!SummaryInfo.containsKey("sumstudentcount")){
							
							SummaryInfo.put("sumstudentcount", "0");									
						}
						
		               if(!SummaryInfo.containsKey("11studentcount")){
							
							SummaryInfo.put("11studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("12studentcount")){
							
							SummaryInfo.put("12studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("21studentcount")){
							
							SummaryInfo.put("21studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("22studentcount")){
							
							SummaryInfo.put("22studentcount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("23studentcount")){
							
							SummaryInfo.put("23studentcount", "0");									
						}
		                            
		               if(!SummaryInfo.containsKey("24studentcount")){
							
							SummaryInfo.put("24studentcount", "0");									
						}
		              
		              if(!SummaryInfo.containsKey("25studentcount")){
							
							SummaryInfo.put("25studentcount", "0");									
						}
		              
		              if(!SummaryInfo.containsKey("26studentcount")){
							
							SummaryInfo.put("26studentcount", "0");									
						}
		                                
		              
		              if(!SummaryInfo.containsKey("31studentcount")){
							
							SummaryInfo.put("31studentcount", "0");									
						}
		            
		            if(!SummaryInfo.containsKey("32studentcount")){
							
							SummaryInfo.put("32studentcount", "0");									
						}
		            
		            if(!SummaryInfo.containsKey("33studentcount")){
							
							SummaryInfo.put("33studentcount", "0");									
						}
		            
		            if(!SummaryInfo.containsKey("34studentcount")){
						
						SummaryInfo.put("34studentcount", "0");									
					}
		        
		           if(!SummaryInfo.containsKey("35studentcount")){
						
						SummaryInfo.put("35studentcount", "0");									
					}
		           
		           if(!SummaryInfo.containsKey("sumfee")){
						
						SummaryInfo.put("sumfee", "0");									
					}
		               
					}	
					
					
			
					return collegesignupinfolist;
				} catch (Exception e) {
					System.out.println("getSignUpInfo error:" + e.getMessage());
					return null;
				}
			}
			
			
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getStudentAdmissionInfo(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
				try {
					return studentDao.getStudentAdmissionInfo(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public long getCountAllStudentArrage(String whereString,Map<String,Object> valuesMap,String schoolnum){    //getCountAllStudentArrage(String schoolnum)
				try {
					return studentDao.getCountAllStudentArrage(whereString, valuesMap,schoolnum);//getCountAllStudentArrage(schoolnum)
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
			
			//得到符合条件的所有学生 成绩
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getStudentScoreByCondition(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
				try {
					return studentDao.getStudentScoreByCondition(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
				} catch (Exception e) {
					System.out.println("getStudentScoreByCondition error:" + e.getMessage());
					return null;
				}
			}
			
			//得到某语种的所有理论监考教师
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getExaminerTeacher(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
				try {
                    List<Map> logicroomAndLocalroomAndLanguage=studentDao.logicroomAndLocalroomAndLanguage(whereString, valuesMap, Integer.parseInt(pageNum),Integer.parseInt(pageSize),schoolnum);
					
					for(Map element:logicroomAndLocalroomAndLanguage)
					{			
						String logicexamroomnum = element.get("logicexamroomnum").toString();
						String examroom = element.get("examroom").toString();
						String campusname = element.get("campusname").toString();
						String languagename = element.get("languagename").toString();		
						
						List<Map> ExaminerTeacherInfoList=studentDao.getExaminerTeacher(logicexamroomnum,examroom,campusname,languagename,schoolnum);
					    String supervisor="";
						for(Map ExaminerTeacherInfo : ExaminerTeacherInfoList){
							if(supervisor.equals("")){supervisor+=ExaminerTeacherInfo.get("supervisor").toString();}
							else supervisor+=","+ExaminerTeacherInfo.get("supervisor").toString();
						}
						element.put("supervisor", supervisor);	
					}
					
					return logicroomAndLocalroomAndLanguage;
					
					
					
					
					
					
				/*	
					List<Map> ExaminerTeacherInfoList=studentDao.getExaminerTeacher(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
					List<Map> infoList = new ArrayList<Map>();
							
					for(Map ExaminerTeacherInfo : ExaminerTeacherInfoList){
						int existFlag=0;//flag等于0表示此条记录在infoList中还没有
						String logicexamroomnum = ExaminerTeacherInfo.get("logicexamroomnum").toString();
						//if(infoList.isEmpty()){ infoList.add(ExaminerTeacherInfo); }
						
						for(Map Info : infoList){
							if(logicexamroomnum.equals(Info.get("logicexamroomnum").toString()))
							{
								String supervisor=Info.get("supervisor").toString();
								supervisor+=","+ExaminerTeacherInfo.get("supervisor").toString();
								Info.put("supervisor", supervisor);						
								existFlag=1;
								break;
								}
							
						}
						
						if(existFlag==0){
							
							infoList.add(ExaminerTeacherInfo); 		
						}
							
						}
					return infoList;*/
				} catch (Exception e) {
					System.out.println("getExaminerTeacher error:" + e.getMessage());
					return null;
				}
			}
			
			//得到理论监考教师总人数
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public long getLogicExamRoomTotalCountBylanguage(String whereString,Map<String,Object> valuesMap,String schoolnum){
				try {
					return studentDao.getLogicExamRoomTotalCountBylanguage(whereString, valuesMap,schoolnum);
				} catch (Exception e) {
					System.out.println("getLogicExamRoomTotalCountBylanguage error:" + e.getMessage());
					return 0;
				}
			}
			
			//得到考场号及考场地点
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getLogicExamRoom(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
				try {
					return studentDao.getLogicExamRoom(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
				} catch (Exception e) {
					System.out.println("getLogicExamRoom error:" + e.getMessage());
					return null;
				}
			}
			    
			//得到考场号及考场地点
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getSection(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
				try {
					return studentDao.getSection(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
				} catch (Exception e) {
					System.out.println("getSection error:" + e.getMessage());
					return null;
				}
			}
			
			//得到考场号及考场地点
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getStudentInfo(String logicExamRoomNum,String schoolnum){
				try {
					return studentDao.getStudentInfo(logicExamRoomNum,schoolnum);
				} catch (Exception e) {
					System.out.println("getStudentInfo error:" + e.getMessage());
					return null;
				}
			}
			
			//得到学生考场信息
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getStuExamInfo(String studentidnum){
				try {
					List<Map> list = new ArrayList<Map>();
					Map stuexaminfo = studentDao.getStuExamInfo(studentidnum);
					list.add(stuexaminfo);
					return list;
				} catch (Exception e) {
					System.out.println("getStuExamInfo error:" + e.getMessage());
					return null;
				}
			}
			
			//接受学生成绩
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public String acceptStudentCJ(int studentcount,Object rowObject[],String fieldNames[]){
				String result = "SUCCESS";
				try {
					int indexOfExamnum,indexOfIdnum,indexOfScore,indexOfName;
					indexOfScore=indexOfIdnum=indexOfExamnum=indexOfName=-1;
					for(int index = 0;index<fieldNames.length;index++) {
						if(fieldNames[index].toLowerCase().trim().equals("zkzh")) {
							indexOfExamnum = index;
						}
//						if(fieldNames[index].toLowerCase().trim().equals("sfzh")) {
//							indexOfIdnum = index;
//						}
						if(fieldNames[index].toLowerCase().trim().equals("fs")) {
							indexOfScore = index;
						}
						if(fieldNames[index].toLowerCase().trim().equals("xm")) {
							indexOfName = index;
						}
					}
					if(indexOfExamnum == -1) {
						result = "上传的文件没有准考证号（ZKZH）字段，上传失败！";
						return result;
					}
					if(indexOfScore == -1) {
						result = "上传的文件没有分数(FS)字段，上传失败！";
						return result;
					}
					ExStudent student;
					String tempExamnum = rowObject[indexOfExamnum].toString().trim();
					if(tempExamnum.equals("")) {
						return "第" + studentcount + "个学生准考证为空,请确认数据正确后再导入成绩！";
					}
					student = studentDao.findByProperty("examnum", tempExamnum).get(0);
					
					
					
					if(null == student) {
						result = "导入的成绩文件中准考证号(" + tempExamnum + ")的考生,在系统不存在！";
						return result;
					}
					
/*					if(indexOfIdnum!=-1) {
						String tempIdnum = rowObject[indexOfIdnum].toString().trim();
						if(!tempIdnum.equals(student.getIdnum().trim())){
							result = "上传文件中准考证号为" + rowObject[indexOfExamnum] +"的考生身份证号："
									+ tempIdnum + "与考务系统中的身份证号："
									+ student.getIdnum().trim() + "不相符";
							return result;
						}
					}*/
					if(indexOfName != -1) {
						String tempName = rowObject[indexOfName].toString().trim();
						if(!tempName.equals(student.getName().trim())){
							result = "上传文件中准考证号为" + rowObject[indexOfExamnum] +"的考生姓名:"
									+ tempName + "与考务系统中的考生姓名:"
									+ student.getName() + "不相符";
							return result;
						}
					}
					student.setScore(new BigDecimal(rowObject[indexOfScore].toString()));
					studentDao.update(student);
					if(studentcount%50==0){
						studentDao.getSession().flush();
						studentDao.getSession().clear();
					}
					return result;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("acceptStudentCJ error:" + e.getMessage());
					return "接收成绩失败！";
				}
			}
			
			//历史考生导入
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public boolean importStuHistoryInfo(List<JSONObject> students,String schoolnum){
				try {
					int studentcount = 0;
					ExStudent entity;
					for(JSONObject student : students){
						if(studentDao.findByProperty("idnum",student.get("idnum").toString()).size()>0){
							entity = studentDao.findByProperty("idnum", student.get("idnum").toString()).get(0);
						}else{
							entity = new ExStudent();
						}
						ExInstitution school = institutiondao.getInstitutionByInstitutionNum(schoolnum);
						entity.setExInstitution(school);
						if((!student.get("languagename").toString().equals("")) && languagedao.findByProperty("name", student.get("languagename").toString()).size()>0){
							entity.setExLanguage(languagedao.findByProperty("name", student.get("languagename").toString()).get(0));
						}else{
							ExLanguage newlanguage = new ExLanguage();
							newlanguage.setName(student.get("languagename").toString());
							languagedao.save(newlanguage);
							entity.setExLanguage(newlanguage);
						}
						if((!student.get("campusname").toString().equals("")) && campusdao.findByProperty("name", student.get("campusname").toString(), schoolnum).size()>0){
							entity.setExCampus(campusdao.findByProperty("name", student.get("campusname").toString(), schoolnum).get(0));
						}else{
							ExCampus newcampus = new ExCampus();
							newcampus.setCampusnum("1");
							newcampus.setName(student.get("campusname").toString());
							newcampus.setExInstitution(school);
							campusdao.save(newcampus);
							entity.setExCampus(newcampus);
						}
						if((!student.get("collegename").toString().equals("")) && collegedao.findByProperty("name", student.get("collegename").toString(), schoolnum).size()>0){
							entity.setExCollege(collegedao.findByProperty("name", student.get("collegename").toString(), schoolnum).get(0));
						}else{
							ExCollege newcollege = new ExCollege();
							newcollege.setName(student.get("collegename").toString());
							newcollege.setExInstitution(school);
							collegedao.save(newcollege);
							entity.setExCollege(newcollege);
						}
						if((!student.get("professorname").toString().equals("")) && professiondao.findByProperty("name", student.get("professorname").toString(), schoolnum).size()>0){
							entity.setExProfession(professiondao.findByProperty("name", student.get("professorname").toString(), schoolnum).get(0));
						}else{
							ExProfession newprofession = new ExProfession();
							newprofession.setName(student.get("professorname").toString());
							newprofession.setInstitutionid(schoolnum);
							professiondao.save(newprofession);
							entity.setExProfession(newprofession);
						}
						entity.setExamnum(student.get("examnum").toString());
						entity.setGrade(student.get("grade").toString());
						entity.setClassnum(student.get("classnum").toString());
						entity.setLengthofyear(student.get("lengthofyear").toString());
						entity.setStudentnum(student.get("studentnum").toString());
						entity.setName(student.get("name").toString());
						if(student.get("sex").toString().equals("男") || student.get("sex").toString().equals("M")){
							entity.setSex("M");
						}else{entity.setSex("F");}
						entity.setIdnum(student.get("idnum").toString());
						BigDecimal score = new BigDecimal(student.get("score").toString());
						entity.setScore(score);
						entity.setTheoryabsent(student.get("theoryabsent").toString());
						entity.setTheoryfraud(student.get("theoryfraud").toString());
						entity.setOperateabsent(student.get("operateabsent").toString());
						entity.setOperatefraud(student.get("operatefraud").toString());
						if(student.get("paied").toString().equals("是")||student.get("paied").toString().equals("1")){
							entity.setPaied("1");
						}else{entity.setPaied("0");}
						if(student.get("studentcategory").toString().equals("1")||student.get("studentcategory").toString().equals("普通本科")){
							entity.setStudentcategory("1");
						}else if(student.get("studentcategory").toString().equals("2")||student.get("studentcategory").toString().equals("普通专科")){
							entity.setStudentcategory("2");
						}else{
							entity.setStudentcategory("3");
						}
						studentDao.save(entity);
						if(studentcount%50==0){
							studentDao.getSession().flush();
							studentDao.getSession().clear();
						}
						studentcount++;
					}
					return true;
				} catch (Exception e) {
					System.out.println("importStuHistoryInfo error:" + e.getMessage());
					studentDao.getSession().getTransaction().rollback();
					return false;
				}
			}
			
			//得到上机考试的场次及考场地点
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getSectionAndLocationRoom(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
				try {
					return studentDao.getSectionAndLocationRoom(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
				} catch (Exception e) {
					System.out.println("getSectionAndLocationRoom error:" + e.getMessage());
					return null;
				}
			}
			
			//得到上机考试的教室总数
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public long getOperateSectionTotalCount(String whereString,Map<String,Object> valuesMap,String schoolnum){
				try {
					return studentDao.getOperateSectionTotalCount(whereString, valuesMap,schoolnum);
				} catch (Exception e) {
					System.out.println("getOperateSectionTotalCount error:" + e.getMessage());
					return 0;
				}
			}
			
			//得到理论考场总数
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public long getLogicExamRoomTotalCount(String whereString,Map<String,Object> valuesMap,String schoolnum){
				try {
					return studentDao.getLogicExamRoomTotalCount(whereString, valuesMap,schoolnum);
				} catch (Exception e) {
					System.out.println("getLogicExamRoomTotalCount error:" + e.getMessage());
					return 0;
				}
			}
			
			//得到上机考试每个教室的学生信息
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getStudentInfoBySectionAndLocationRoom(String sectionnum,String roomlocation,String languagename,String schoolnum){
				try {
					return studentDao.getStudentInfoBySectionAndLocationRoom(sectionnum,roomlocation,languagename,schoolnum);
				} catch (Exception e) {
					System.out.println("getStudentInfoBySectionAndLocationRoom error:" + e.getMessage());
					return null;
				}
			}
			
			//得到某语种的所有上机监考教师
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getOperateExaminerTeacher(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
				try {
					List<Map> sectionAndRoomAndLanguage=studentDao.getSectionAndRoomAndLanguage(whereString, valuesMap, Integer.parseInt(pageNum),Integer.parseInt(pageSize),schoolnum);
					
					for(Map element:sectionAndRoomAndLanguage)
					{			
						String sectionnum = element.get("sectionnum").toString();
						String examroom = element.get("examroom").toString();
						String campusname = element.get("campusname").toString();
						String languagename = element.get("languagename").toString();		
						
						List<Map> ExaminerTeacherInfoList=studentDao.getOperateExaminerTeacher(sectionnum,examroom,campusname,languagename,schoolnum);
					    String supervisor="";
						for(Map ExaminerTeacherInfo : ExaminerTeacherInfoList){
							if(supervisor.equals("")){supervisor+=ExaminerTeacherInfo.get("supervisor").toString();}
							else supervisor+=","+ExaminerTeacherInfo.get("supervisor").toString();
						}
						element.put("supervisor", supervisor);	
					}
					
					return sectionAndRoomAndLanguage;
					
				} catch (Exception e) {
					System.out.println("getOperateExaminerTeacherBysectionAndRoomAndLanguage error:" + e.getMessage());
					return null;
				}
			}
			
			//得到上机按照场次教室校区语种分组的总数
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public long getOperateExaminerTeacherTotalCount(String whereString,Map<String,Object> valuesMap,String schoolnum){
				try {
					return studentDao.getOperateExaminerTeacherTotalCount(whereString, valuesMap,schoolnum);
				} catch (Exception e) {
					System.out.println("getOperateExaminerTeacherTotalCount error:" + e.getMessage());
					return 0;
				}
			}
			
			////学校上传ksdb到市地考试院
			final static Integer SCHOOL_NEEDIMPORT_NUM=1;
			final static Integer  SCHOOL_NEEDCHECK_NUM=2;
			final static Integer  CITY_NEEDCHECK_NUM=3;
			final static Integer CITY_CHECKED_NUM=4;
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public String importKsdb(List<JSONObject> students,String institutionnum){
				try {
					ExInstitutionstatus nextStatus=this.institutionstatusDao.findByProperty("statusnum", SCHOOL_NEEDCHECK_NUM).get(0);//可上报时段
					ExInstitution currentInstitution= this.institutiondao.findByProperty("institutionnum",institutionnum ).get(0);
					ExInstitutionstatus currentStatus= currentInstitution.getInstitutionstatus();
					String initLanguageNum="XX";
//					String initcampusNum="XXXX";
					ExLanguage language=null;
					ExCampus campus = null;
					int studentcount = 0;
					Integer now=currentStatus.getStatusnum();
					//只要处于第二状态的时间段内，且状态为1或者2就应当可以更新
					if(currentStatus.getStatusnum() !=SCHOOL_NEEDIMPORT_NUM && currentStatus.getStatusnum() !=SCHOOL_NEEDCHECK_NUM){
						return "{ success: false, errors:{info: '您已处于："+currentInstitution.getInstitutionstatus().getName()+"阶段。<br>不能上报!'}}";
					}
					Date  curDate=  new Date(System.currentTimeMillis());
					if(curDate.before(nextStatus.getEndtime())&&curDate.after(nextStatus.getStarttime())){
						ClearHistoryDataService clearHistoryDataService=new ClearHistoryDataService();
						clearHistoryDataService.clear(institutionnum);
						for(JSONObject student : students){
							String shcoolnum = student.get("examnum").toString().substring(3, 6);
							String languagenum = student.get("examnum").toString().substring(7, 9);
							String campusnum = student.get("examnum").toString().substring(6, 7);
							if(!languagenum.trim().equals(initLanguageNum)){
								language = languagedao.findByProperty("languagenum", languagenum).get(0);
								initLanguageNum=language.getLanguagenum();
							}
							ExStudent entity;
							/* 用身份证确定学生
							if(studentDao.findByProperty("",student.get("idnum").toString()).size()>0){
								entity = studentDao.findByProperty("idnum", student.get("idnum").toString()).get(0);
							}else{
								entity = new ExStudent();
							}
							*/
							/* 用准考证确定学生*/
							if(studentDao.findByProperty("examnum",student.get("examnum").toString()).size()>0){
								entity = studentDao.findByProperty("examnum", student.get("examnum").toString()).get(0);
							}else{
								entity = new ExStudent();
							}
							if(campusdao.findByProperty("campusnum", campusnum, shcoolnum).size()>0){
								campus = campusdao.findByProperty("campusnum", campusnum, shcoolnum).get(0);
							}else{
								campus = new ExCampus();
								campus.setCampusnum(campusnum);
								campus.setName("");
								campus.setExInstitution(currentInstitution);
								campusdao.save(campus);
							}
							entity.setExInstitution(currentInstitution);
							entity.setExCampus(campus);
							entity.setExLanguage(language);
							entity.setIdnum(student.get("idnum").toString());
							entity.setExamnum(student.get("examnum").toString());
							entity.setName(student.get("name").toString());
							entity.setTheoryabsent("0");
							entity.setOperateabsent("0");
							entity.setTheoryfraud("0");
							entity.setOperatefraud("0");
							entity.setPaied("1");
							entity.setSex(student.get("sex").toString());
							studentDao.save(entity);
							if(studentcount%50==0){
								studentDao.getSession().flush();
								studentDao.getSession().clear();
							}
							studentcount++;
						}
						currentInstitution.setInstitutionstatus(nextStatus);
						institutiondao.update(currentInstitution);
						return "{ success: true, errors:{info: '上报数据成功!'}}";
					}else{
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						return "{ success: false, errors:{info: '还未到上报时间！<br>请在以下时间段内上报数据:"+dateFormat.format(nextStatus.getStarttime())+
								"~"+dateFormat.format(nextStatus.getEndtime())+
								"'}}";
					}
				} catch (Exception e) {
					e.printStackTrace();
					return "{ success: false, errors:{info: '后台出错!'}}";
				}
				
			}
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public boolean importFraud(List<JSONObject> students){
				try {
					
					for(JSONObject student : students){
						String examnum = student.get("examnum").toString();
						if(studentDao.findByProperty("examnum",examnum).size() == 0) 
						{
							continue;
						}
						ExStudent entity = studentDao.findByProperty("examnum",examnum).get(0);
						if(student.get("theoryfraud").equals("是") || student.get("theoryfraud").equals("1")) {
							entity.setTheoryfraud("1");
						}else
							entity.setTheoryfraud("0");
						if(student.get("operatefraud").equals("是") || student.get("operatefraud").equals("1")) {
							entity.setOperatefraud("1");
						}else
							entity.setOperatefraud("0");
						studentDao.update(entity);
					}
					return true;
				} catch (Exception e) {
					System.out.println("importZBK error");
					e.printStackTrace();
					return false;
				}
				
			}			
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public boolean importAbsent(List<JSONObject> students){
				try {
					
					for(JSONObject student : students){
						String examnum = student.get("examnum").toString().trim();
						if(studentDao.findByProperty("examnum",examnum).size() == 0) 
						{
							continue;
						}
						ExStudent entity = studentDao.findByProperty("examnum",examnum).get(0);
						if(student.get("theoryabsent").equals("是") || student.get("theoryabsent").equals("1")) {
							entity.setTheoryabsent("1");
						}else
							entity.setTheoryabsent("0");
						if(student.get("operateabsent").equals("是") || student.get("operateabsent").equals("1")) {
							entity.setOperateabsent("1");
						}else
							entity.setOperateabsent("0");
						studentDao.update(entity);
					}
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("importZBK error");
					return false;
				}
				
			}	

			public List<Map> statisticAllLanguageBySchool(String institutionnum, String category) {
				try {
					String currentSchoolID;
					String institutionname = institutiondao.getInstitutionByInstitutionNum(institutionnum).getName();
					List<Map> schoolList=institutiondao.loadManagedInstitutionList(institutionnum,category);
					List<Map> languageNumList=this.languagedao.loadlanguageList();
					List<Map> result= new ArrayList<Map>();
					Map total = new HashMap();
					for(Map eachLanguage:languageNumList){
						total.put(eachLanguage.get("languagenum"), (long)0);
					}
					total.put("institutionname",institutionname + "小计");
					total.put("institutionnum","");
					total.put("count", (long)0);
					total.put("currentState","");
					for(Map element:schoolList){
						Map temp=new HashMap();
						Long count=(long) 0;
						temp.put("institutionname",  (String)element.get("institutionname"));
						temp.put("institutionnum",  (String)element.get("institutionnum"));
						for(Map eachLanguage:languageNumList){
							temp.put(eachLanguage.get("languagenum"), 0);
						}
						
						currentSchoolID=(String) element.get("id");
						List<Map>stastistics=this.studentDao.statisticAllLanguageBySchoolID(currentSchoolID,category);
						
						for(Map eachLanguage:stastistics){
							count+=(Long)eachLanguage.get("studentCount");
							temp.put(eachLanguage.get("languagenum"),eachLanguage.get("studentCount"));
							total.put(eachLanguage.get("languagenum"), (Long)total.get(eachLanguage.get("languagenum"))+(Long)eachLanguage.get("studentCount"));
						}
						temp.put("count", count);
						total.put("count", (Long)total.get("count")+count);
						temp.put("currentState", this.institutiondao.getInstitutionByInstitutionID(currentSchoolID).getInstitutionstatus().getName());
						
						result.add(temp);
					}
					result.add(total);
					return result;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			public List<Map> statisticAllLevelByInstitution(String institutionnum, String category) {
				try {
					String currentSchoolID;
					String institutionname = institutiondao.getInstitutionByInstitutionNum(institutionnum).getName();
					List<Map> schoolList=institutiondao.loadManagedInstitutionList(institutionnum,category);
					List<Map> languageNumList=this.languagedao.loadlanguageList();
					List<Map> result= new ArrayList<Map>();
					Map total = new HashMap();
					String levelNum = "";
					for(Map eachLanguage:languageNumList){
						if(!eachLanguage.get("languagenum").toString().substring(0, 1).equals(levelNum)){
							total.put(eachLanguage.get("languagenum").toString().substring(0, 1), (long)0);
						} 	
						levelNum = eachLanguage.get("languagenum").toString().substring(0, 1);
					}
					total.put("institutionname",institutionname + "小计");
					total.put("institutionnum","");
					total.put("count", (long)0);
					total.put("currentState","");
					for(Map element:schoolList){
						Map temp=new HashMap();
						Long count=(long) 0;
						temp.put("institutionname",  (String)element.get("institutionname"));
						temp.put("institutionnum",  (String)element.get("institutionnum"));
						levelNum = "";
						for(Map eachLanguage:languageNumList){
							if(!eachLanguage.get("languagenum").toString().substring(0, 1).equals(levelNum)){
								temp.put(eachLanguage.get("languagenum").toString().substring(0, 1), (long)0);
							} 	
							levelNum = eachLanguage.get("languagenum").toString().substring(0, 1);
						}
						currentSchoolID=(String) element.get("id");
						List<Map> stastistics = this.studentDao.statisticAllLanguageBySchoolID(currentSchoolID,category);
						long levelCount = 0;
						levelNum = "";
						for(Map eachLanguage:stastistics){
							count+=(Long)eachLanguage.get("studentCount"); 
							if(!eachLanguage.get("languagenum").toString().substring(0, 1).equals(levelNum)){
								levelCount = (Long)eachLanguage.get("studentCount");
								temp.put(eachLanguage.get("languagenum").toString().substring(0, 1), levelCount);
								total.put(eachLanguage.get("languagenum").toString().substring(0, 1), (Long)total.get(eachLanguage.get("languagenum").toString().substring(0, 1))+(Long)eachLanguage.get("studentCount"));
							}else{
								levelCount += (Long)eachLanguage.get("studentCount");
								temp.put(eachLanguage.get("languagenum").toString().substring(0, 1), levelCount);
								total.put(eachLanguage.get("languagenum").toString().substring(0, 1), (Long)total.get(eachLanguage.get("languagenum").toString().substring(0, 1))+(Long)eachLanguage.get("studentCount"));
							} 
							levelNum = eachLanguage.get("languagenum").toString().substring(0, 1);
							
						}
						temp.put("count", count);
						total.put("count", (Long)total.get("count")+count);
						temp.put("currentState", this.institutiondao.getInstitutionByInstitutionID(currentSchoolID).getInstitutionstatus().getName());
						
						result.add(temp);
					}
					result.add(total);
					return result;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			public List<Map> AllProvinceStuInfoSum(String institutionnum, String category, boolean isByLanguage){

				try {
					List<Map> result= new ArrayList<Map>();
					if(isByLanguage){
						if(category.equals("city")) {
							String institutionname = institutiondao.getInstitutionByInstitutionNum(institutionnum).getName();
							//Map total = new HashMap();
							List<Map> cityList=institutiondao.loadManagedInstitutionList(institutionnum,category);
							for(Map city : cityList){
								String citynum = city.get("institutionnum").toString();
								List<Map> cityResult = this.statisticAllLanguageBySchool(citynum,"school");
								result.addAll(cityResult);
							}
							List<Map> cityStuInfoSum = this.statisticAllLanguageBySchool(institutionnum, category);
							Map proviceTotal = cityStuInfoSum.get(cityStuInfoSum.size()-1);
							result.add(proviceTotal);
							return result;
						}
						
						List<Map> cityResult = this.statisticAllLanguageBySchool(institutionnum, category);
						result.addAll(cityResult);
						return result;

						
					}else{
						if(category.equals("city")){
							String institutionname = institutiondao.getInstitutionByInstitutionNum(institutionnum).getName();
							Map total = new HashMap();
							List<Map> cityList=institutiondao.loadManagedInstitutionList(institutionnum,category);
							
							for(Map city : cityList){
								String citynum = city.get("institutionnum").toString();
								List<Map> cityResult = this.statisticAllLevelByInstitution(citynum,"school");
								result.addAll(cityResult);
							}
							List<Map> cityStuInfoSum = this.statisticAllLevelByInstitution(institutionnum, category);
							Map proviceTotal = cityStuInfoSum.get(cityStuInfoSum.size()-1);
							result.add(proviceTotal);
							
						}else if(category.equals("school")){
							String institutionname = institutiondao.getInstitutionByInstitutionNum(institutionnum).getName();
							Map total = new HashMap();
//							List<Map> schoolList=institutiondao.loadManagedInstitutionList(institutionnum,category);
							List<Map> cityResult = this.statisticAllLevelByInstitution(institutionnum, category);
							result.addAll(cityResult);
						}
						return result;
					}

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			//收索出所有未报名学生的信息
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> unSignUpStudentShow(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String schoolnum){
				try {
					return studentDao.unSignUpStudentShow(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),schoolnum);
				} catch (Exception e) {
					System.out.println("unSignUpStudentShow error:" + e.getMessage());
					return null;
				}
			}

			//得到未报名学生的总人数
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public long getUnSignUpStudentsTotalCount(String whereString,Map<String,Object> valuesMap,String schoolnum){
				try {
					return studentDao.getUnSignUpStudentsTotalCount(whereString, valuesMap,schoolnum);
				} catch (Exception e) {
					System.out.println("getUnSignUpStudentsTotalCount error:" + e.getMessage());
					return 0;
				}
			}



            //得到所有未报名学生的信息
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getAllStudentsUnsignup(String schoolnum){
				try {
					return studentDao.getAllStudentsUnsignup(schoolnum);
				} catch (Exception e) {
					System.out.println("getAllStudentsUnsignup error:" + e.getMessage());
					return null;
				}
			}
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public String checkJFStudentsData(List<JSONObject> students) {
				try {
					int count = 0;
					for(JSONObject student : students ) {
						count ++;
						String error = this.checkJFData(student);
						if(!error.equals("输入正确！")) {
							return "第" + count + "行" + error;
						}
						
					}
					return null;
				} catch(Exception e) {
					e.printStackTrace();
					return e.getMessage();
				}
			}
			public String checkJFData(JSONObject student){
				IdCardCheck idcardcheck = new IdCardCheck();
				
				if(!student.get("paied").toString().equals("1")){
					return "交费列应该为1，因为您导入的是已缴费考生";
				}
				
				if(!student.get("studentnum").toString().matches("\\d+")){
					return "学号格式错误！";
				}
				if(!idcardcheck.isValidatedAllIdcard(student.get("idnum").toString())){
					return "身份证格式错误！";
				}
				return "输入正确！";
			}
			//检查未报名考生的数据
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String checkUnSignUpStudentsData(List<JSONObject> students){
		try {
			int studentcount = 1;
			for(JSONObject student : students){
				studentcount++;
				String error = checkData(student);
				if(!error.equals("输入正确！")){
					return "第" + studentcount + "行" + error;
				}	
			}
			return null;
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	public String checkData(JSONObject student){
		if(!student.get("exLanguage_name").toString().equals("")){
			return "语种名称应该为空，因为您导入的是未报名考生!";
		}
		if(!student.get("exLanguage_num").toString().equals("")){
			return "语种代码应该为空，因为您导入的是未报名考生!";
		}
		if(student.get("paied").toString().equals("1")){
			return "交费列应该为0，因为您导入的是未报名考生!";
		}
		if(student.get("exCampus_num").toString().equals("")){
			return "校区代码为空！";
		}else{
			if(!student.get("exCampus_num").toString().matches("\\d")){
				return "校区代码格式错误!";
			}
		}
		if(!student.get("exCollege_num").toString().equals("")){
			if(!student.get("exCollege_num").toString().matches("\\d+")){
				return "学院代码格式错误！";
			}
		}
//		if(!student.get("exProfession_num").toString().equals("")){
//			if(!student.get("exProfession_num").toString().matches("\\d+")){
//				return "专业代码格式错误！";
//			}
//		}
//		if(!student.get("studentnum").toString().matches("\\d+")){
//			return "学号格式错误！";
//		}
//		if(!idcardcheck.isValidatedAllIdcard(student.get("idnum").toString())){
//			return "身份证格式错误！";
//		}
		if(student.get("studentnum").toString().equals("")){
			return "学号不能为空！";
		}
		if(student.get("name").toString().equals("")){
			return "姓名不能为空！";
		}
		if(student.get("name").toString().contains(" ")){
			return "姓名中有空格！";
		}
		if(student.get("classnum").toString().equals("")){
			return "行政班字段不能为空！";
		}
		if(student.get("studentcategory").toString().equals("")){
			return "考生类别不能为空！";
		}

		return "输入正确！";
	}		
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean importJFStudents(List<JSONObject> students,String schoolnum){
		
		try {
			for(JSONObject student:students) {
				if(this.studentDao.findByProperty("idnum", student.get("idnum").toString(), schoolnum).size() > 0) {
					ExStudent entity = this.studentDao.findByProperty("idnum",  student.get("idnum"), schoolnum).get(0);
					entity.setPaied("1");
					this.studentDao.update(entity);
				}
				else {
					continue;
				}
				
			}
			return true;
		} catch (Exception e) {
			System.out.println("importJFStudents error");
			e.printStackTrace();
			studentDao.getSession().getTransaction().rollback();
			return false;
		}
	}		
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> calcPaperBagsForSchool(String langugaenum,String schoolnum){
		try {
			List<Map> stuCountByCampus = studentDao.getStuCountByLangGroupByCampus(langugaenum,schoolnum);
			List<Map> paperBagsByCampus = new ArrayList<Map>();
			int bzbags;
			int stucount;
			int fbzbags;
			int bybags;
			int bags;
			for(Map stuCount : stuCountByCampus){
				Map paperBags = new HashMap();
				stucount = Integer.parseInt(stuCount.get("stucount").toString());
				bzbags = stucount/30;
				if(stucount%30>0){
					fbzbags = 1;
				}else{
					fbzbags = 0;
				}
				bags = bzbags + fbzbags;
				bybags = (bags/10);
				if(bags%10>0){
					bybags++;
				}
				paperBags.put("bags", bags);
				paperBags.put("bybags", bybags);
				paperBags.put("campusnum", stuCount.get("campusnum"));
				paperBagsByCampus.add(paperBags);
			}
			return paperBagsByCampus;
		} catch (Exception e) {
					// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	private List<Map> paperBagsByExamCollege = new ArrayList<Map>();
	private Map provicepaperbags = new HashMap();
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> calcPaperBagsForExamCollege(String institutionnum, String category, boolean direct){
		
		try {
			List<Map> theoryLanguageList = languagedao.getTheoryLanguageList();
			if(direct) {
				paperBagsByExamCollege.clear();
				for(Map theoryLanguage : theoryLanguageList) {
					provicepaperbags.put(theoryLanguage.get("languagenum").toString()+"bags", 0);
					provicepaperbags.put(theoryLanguage.get("languagenum").toString()+"bybags", 0);
				}
			}
			if(category.equals("city")) {
				Map citypaperbags = new HashMap();
				for(Map theoryLanguage : theoryLanguageList) {
							citypaperbags.put(theoryLanguage.get("languagenum").toString()+"bags", 0);
							citypaperbags.put(theoryLanguage.get("languagenum").toString()+"bybags", 0);
						}
						List<Map> schoolList=institutiondao.loadManagedInstitutionList(institutionnum,"school");
						citypaperbags.put("institutionnum", institutionnum);
						String institutionname = institutiondao.getInstitutionByInstitutionNum(institutionnum).getName();
						citypaperbags.put("institutionname", institutionname + "（小计）" );
						for(Map school : schoolList){
							Map schoolpaperbags = new HashMap();
							int bags;
							int bybags;
							schoolpaperbags.put("institutionnum", school.get("institutionnum").toString());
							schoolpaperbags.put("institutionname", school.get("institutionname").toString());
							for(Map theoryLanguage : theoryLanguageList) {
								bags=0;
								bybags=0;
								List<Map> campusbags =  this.calcPaperBagsForSchool(theoryLanguage.get("languagenum").toString(), school.get("institutionnum").toString());
								for(Map campusbag : campusbags ) {
									bags+= (Integer)campusbag.get("bags");
									bybags+= (Integer)campusbag.get("bybags");
								}
								schoolpaperbags.put(theoryLanguage.get("languagenum").toString()+"bags", bags);
								schoolpaperbags.put(theoryLanguage.get("languagenum").toString()+"bybags", bybags);
								citypaperbags.put(theoryLanguage.get("languagenum").toString()+"bags", (Integer)citypaperbags.get(theoryLanguage.get("languagenum").toString()+"bags") + bags);
								citypaperbags.put(theoryLanguage.get("languagenum").toString()+"bybags", (Integer)citypaperbags.get(theoryLanguage.get("languagenum").toString()+"bybags") + bybags);
								provicepaperbags.put(theoryLanguage.get("languagenum").toString()+"bags", (Integer)provicepaperbags.get(theoryLanguage.get("languagenum").toString()+"bags") + bags);
								provicepaperbags.put(theoryLanguage.get("languagenum").toString()+"bybags", (Integer)provicepaperbags.get(theoryLanguage.get("languagenum").toString()+"bybags") + bybags);
							}
							paperBagsByExamCollege.add(schoolpaperbags);
						}
						paperBagsByExamCollege.add(citypaperbags);
						return paperBagsByExamCollege;
					}else if(category.equals("province")){
						List<Map> cityList=institutiondao.loadManagedInstitutionList(institutionnum,"city");
						String provincename = institutiondao.getInstitutionByInstitutionNum(institutionnum).getName();
						provicepaperbags.put("institutionnum", institutionnum);
						provicepaperbags.put("institutionname", provincename + "合计");
						for(Map city : cityList){
							this.calcPaperBagsForExamCollege(city.get("institutionnum").toString(), "city",false);
						}
						paperBagsByExamCollege.add(provicepaperbags);
						return paperBagsByExamCollege;
					}else if(category.equals("school")){
						Map schoolpaperbags = new HashMap();
						paperBagsByExamCollege.addAll(this.calcCampusPaperBags(institutionnum));
						int bags;
						int bybags;
						schoolpaperbags.put("institutionnum",institutionnum);
						String schoolname = institutiondao.getInstitutionByInstitutionNum(institutionnum).getName();
						schoolpaperbags.put("institutionname", schoolname);
						for(Map theoryLanguage : theoryLanguageList){

							bybags=0;
							bags = 0;
							schoolpaperbags.put(theoryLanguage.get("languagenum").toString()+"bags", 0);
							schoolpaperbags.put(theoryLanguage.get("languagenum").toString()+"bybags", 0);
							List<Map> campusbags =  this.calcPaperBagsForSchool(theoryLanguage.get("languagenum").toString(), institutionnum);
							for(Map campusbag : campusbags ){
								bags+= (Integer)campusbag.get("bags");
								bybags+= (Integer)campusbag.get("bybags");
							}
							schoolpaperbags.put(theoryLanguage.get("languagenum").toString()+"bags", bags);
							schoolpaperbags.put(theoryLanguage.get("languagenum").toString()+"bybags", bybags);
						}
						paperBagsByExamCollege.add(schoolpaperbags);
						return paperBagsByExamCollege;
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return null;
				} finally {
					return paperBagsByExamCollege;
				}
			}
			
			public List<Map> calcCampusPaperBags(String institutionnum){
				try {
					List <Map> schoolpaperbags = new ArrayList<Map>();;
					int stucount = 0;
					int bzbags = 0;
					int bags = 0;
					int bybags = 0;
					int fbzbags = 0;
					List<Map> campuses = studentDao.getStuCampus(institutionnum);
					List<Map> theoryLanguageList = languagedao.getTheoryLanguageList();
					for(Map campus : campuses){
						Map campuspaperbags = new HashMap();
						campuspaperbags.put("institutionnum", campus.get("campusnum").toString());
						campuspaperbags.put("institutionname", campus.get("campusname").toString());
						for(Map theoryLanguage : theoryLanguageList){
							stucount = 0;
							campuspaperbags.put(theoryLanguage.get("languagenum").toString()+"bzbags", 0);
							campuspaperbags.put(theoryLanguage.get("languagenum").toString()+"fbzbags", 0);
							campuspaperbags.put(theoryLanguage.get("languagenum").toString()+"bybags", 0);
							Map count = studentDao.calcStuCountCampus(institutionnum, campus.get("campusnum").toString(), theoryLanguage.get("languagenum").toString());
							stucount = Integer.parseInt(count.get("stucount").toString());
							bzbags = stucount/30;
							if(stucount%30>0){
								fbzbags = 1;
							}else{
								fbzbags = 0;
							}
							bags = bzbags + fbzbags;
							bybags = (bags/10);
							if(bags%10>0){
								bybags++;
							}
							campuspaperbags.put(theoryLanguage.get("languagenum").toString()+"bags", bags);
							campuspaperbags.put(theoryLanguage.get("languagenum").toString()+"bybags", bybags);
						}
						schoolpaperbags.add(campuspaperbags);
					}
					return schoolpaperbags;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return null;
				}

			}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void clearUnPaiedStudent(String schoolnum) {
		this.studentDao.clearUnPaiedStudent(schoolnum);
	}
			
			//得到单个考生的打印所需信息
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getOneStudentAdmissionInfo(String whereString, Map<String,Object> valuesMap,String pageNum, String pageSize,String idnum){
				try {					
						return studentDao.getOneStudentAdmission(whereString, valuesMap, Integer.parseInt(pageNum), Integer.parseInt(pageSize),idnum);
					
				} catch (Exception e) {
					System.out.println("getOneStudentAdmissionInfo error:" + e.getMessage());
					return null;
				}
			}
			
			//得到班级列表
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getClassNameList(String collegename,String schoolnum){
				try { 
					List<Map> lastRes= new ArrayList<Map>();
					Map blank=new HashMap();
					blank.put("name", "不选");
					blank.put("value", "");
					lastRes.add(blank);
					List<Map> result=(List<Map>)studentDao.getClassNameList(collegename,schoolnum);
					 for (Map temp:result)
					 { 
						 String value=temp.get("name").toString();
						 temp.put("value", value);
						 lastRes.add(temp);
					 }
					 								 
					return lastRes;					
				} catch (Exception e) {
					System.out.println("getClassNameList error!" + e.getMessage());
					return null;
				}
			}
			
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public String checkStudentsExists(List<JSONObject> students){
				try {
					String studentExamnums = "";
					for(JSONObject student : students){
						String examnum = student.get("examnum").toString().trim();
						if(studentDao.findByProperty("examnum",examnum).size() == 0) 
						{
							studentExamnums += examnum + ",";
							continue;
						}
					}
					if(studentExamnums.equals("")){
						return "true";
					}else
						return "未找到准考证号为" + studentExamnums + "的考生,请核对该准考证号考生！";
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("checkStudentsExists error");
					return "后台错误,请联系技术人员！";
				}
				
			}
			
			//从数据库中获取打印准考证所需的那句注释			
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getRemarkAboutZKZ(){
				try {
					return parameterDao.getRemarkAboutZKZ();
				} catch (Exception e) {
					System.out.println("getRemarkAboutZKZ error!" + e.getMessage());
					return null;
				}
			}
			
			

			
			
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public boolean saveRemarkAboutZKZ(String remark)
			{				
				try{					
						String name="打印准考证备注";
						if(parameterDao.findByProperty("name", name).size() == 0)
						{
							ExParameter ep=new ExParameter();
							ep.setParamvalue(remark);	
							ep.setName(name);
							parameterDao.update(ep);
						}
						else
						{
							ExParameter entity = parameterDao.findByProperty("name", name).get(0);
							entity.setParamvalue(remark);	
							parameterDao.update(entity);
						}
						return true;
					} 
				catch (Exception e) 
				{
					e.printStackTrace();
					return false;
				}
			}
			
		
			
			@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
			public List<Map> getStudentAdmissionExcel(String schoolnum){
				try {
					return studentDao.getStudentAdmissionExcel(schoolnum);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			
			
			//学院成绩统计
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public List<Map> summaryScore(HashMap summaryCondition,String schoolnum){
			    try{
			    	
			    	
			    	
				    //先收索出3个分数段的人数，然后再整合成一个map对象
							
					List<Map> scoreSummaryInfoList = new ArrayList<Map>();
				
					String languagenum =summaryCondition.get("language").toString();//得到语种
					
					//收索出一个学校的每个学院及总人数	
					List<Map> studentScoreInfoList0 = studentDao.getScoreInfo_getCollege(schoolnum, languagenum);
					
					//收索出不及格人数	
					List<Map> studentScoreInfoList1 = studentDao.getScoreInfo_1(schoolnum, languagenum,summaryCondition);
					//收索出及格人数
					List<Map> studentScoreInfoList2 = studentDao.getScoreInfo_2(schoolnum, languagenum,summaryCondition);
					
					//收索出优秀人数
					List<Map> studentScoreInfoList3 = studentDao.getScoreInfo_3(schoolnum, languagenum,summaryCondition);
					
					//开始整合到一个大的List中
					for(Map studentScoreInfo0 : studentScoreInfoList0){
								Map studentScoreInfo = new JSONObject();
								
								studentScoreInfo.put("collegename", studentScoreInfo0.get("collegename").toString());
								int studentCount=Integer.parseInt(studentScoreInfo0.get("studentcount").toString());
								studentScoreInfo.put("StudentCount", studentScoreInfo0.get("studentcount").toString());
								//找到同学院的不及格人数
								for(Map studentScoreInfo1 : studentScoreInfoList1){
									if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo1.get("collegename").toString()))
									{
										studentScoreInfo.put("UnPassStudentCount", studentScoreInfo1.get("studentcount").toString());
										break;
									}
								}
								//找到同学院的及格人数和及格率
								for(Map studentScoreInfo2 : studentScoreInfoList2){
									if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo2.get("collegename").toString()))
									{
										studentScoreInfo.put("PassStudentCount", studentScoreInfo2.get("studentcount").toString());
										float PassStudentPer= Float.parseFloat(studentScoreInfo2.get("studentcount").toString())/studentCount;
										float PassStudentPercent=(float)(Math.round(PassStudentPer*100*100))/100;//保留2位小数
										String PassStudentPercentage=PassStudentPercent+"%";
										studentScoreInfo.put("PassStudentPercent",PassStudentPercentage);
										break;
									}
								}
								//找到同学院的优秀人数和优秀率
								for(Map studentScoreInfo3 : studentScoreInfoList3){
									if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo3.get("collegename").toString()))
									{
										studentScoreInfo.put("ExcellentStudentCount", studentScoreInfo3.get("studentcount").toString());
										float ExcellentStudentPer= Float.parseFloat(studentScoreInfo3.get("studentcount").toString())/studentCount;
										float ExcellentStudentPercent=(float)(Math.round(ExcellentStudentPer*100*100))/100;//保留2位小数
										String ExcellentStudentPercentage=ExcellentStudentPercent+"%";
										studentScoreInfo.put("ExcellentStudentPercent",ExcellentStudentPercentage);
										break;
									}
								}
								
								
								scoreSummaryInfoList.add(studentScoreInfo);
							}
					
					//设置空白为0
					for(Map SummaryInfo : scoreSummaryInfoList){
						
						if(!SummaryInfo.containsKey("UnPassStudentCount")){
							
							SummaryInfo.put("UnPassStudentCount", "0");									
						}
						
		               if(!SummaryInfo.containsKey("PassStudentCount")){
							
							SummaryInfo.put("PassStudentCount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("ExcellentStudentCount")){
							
							SummaryInfo.put("ExcellentStudentCount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("StudentCount")){
							
							SummaryInfo.put("StudentCount", "0");									
						}
		               
		               if(!SummaryInfo.containsKey("PassStudentPercent")){
							
							SummaryInfo.put("PassStudentPercent", "0%");									
						}
		               
		               if(!SummaryInfo.containsKey("ExcellentStudentPercent")){
							
							SummaryInfo.put("ExcellentStudentPercent", "0%");									
						}
		               
					}
					
					return scoreSummaryInfoList;
				
			    } catch (Exception e) {
					System.out.println("summaryScore error:" + e.getMessage());
					return null;
				}
			    			    
			}
			
			
			
			//年级成绩统计
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public List<Map> summaryScoreByGrade(HashMap summaryCondition,String schoolnum){
			    try{
				    //先收索出3个分数段的人数，然后再整合成一个map对象
							
					List<Map> scoreSummaryInfoList = new ArrayList<Map>();
				
					String languagenum =summaryCondition.get("language").toString();//得到语种
					
					//收索出一个学校的每个年级及总人数	
					List<Map> studentScoreInfoList0 = studentDao.getScoreInfo_getGrade(schoolnum, languagenum);
					
					//收索出不及格人数	
					List<Map> studentScoreInfoList1 = studentDao.getScoreInfo_getUnPassStudent(schoolnum, languagenum,summaryCondition);
					//收索出及格人数
					List<Map> studentScoreInfoList2 = studentDao.getScoreInfo_getPassStudent(schoolnum, languagenum,summaryCondition);
					
					//收索出优秀人数
					List<Map> studentScoreInfoList3 = studentDao.getScoreInfo_getExcellentStudent(schoolnum, languagenum,summaryCondition);
					
					//开始整合到一个大的List中
					for(Map studentScoreInfo0 : studentScoreInfoList0){
								Map studentScoreInfo = new JSONObject();
								
								studentScoreInfo.put("grade", studentScoreInfo0.get("grade").toString());
								int studentCount=Integer.parseInt(studentScoreInfo0.get("studentcount").toString());
								studentScoreInfo.put("StudentCount", studentScoreInfo0.get("studentcount").toString());
								//找到同年级的不及格人数
								for(Map studentScoreInfo1 : studentScoreInfoList1){
									if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo1.get("grade").toString()))
									{
										studentScoreInfo.put("UnPassStudentCount", studentScoreInfo1.get("studentcount").toString());
										break;
									}
								}
								//找到同年级的及格人数和及格率
								for(Map studentScoreInfo2 : studentScoreInfoList2){
									if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo2.get("grade").toString()))
									{
										studentScoreInfo.put("PassStudentCount", studentScoreInfo2.get("studentcount").toString());
										float PassStudentPer= Float.parseFloat(studentScoreInfo2.get("studentcount").toString())/studentCount;							
										float PassStudentPercent=(float)(Math.round(PassStudentPer*100*100))/100;//保留2位小数
										String PassStudentPercentage=PassStudentPercent+"%";
										studentScoreInfo.put("PassStudentPercent",PassStudentPercentage);
										break;
									}
								}
								//找到同年级的优秀人数和优秀率
								for(Map studentScoreInfo3 : studentScoreInfoList3){
									if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo3.get("grade").toString()))
									{
										studentScoreInfo.put("ExcellentStudentCount", studentScoreInfo3.get("studentcount").toString());
										float ExcellentStudentPer= Float.parseFloat(studentScoreInfo3.get("studentcount").toString())/studentCount;
										float ExcellentStudentPercent=(float)(Math.round(ExcellentStudentPer*100*100))/100;//保留2位小数
										String ExcellentStudentPercentage=ExcellentStudentPercent+"%";
										studentScoreInfo.put("ExcellentStudentPercent",ExcellentStudentPercentage);
										break;
									}
								}
								
								
								scoreSummaryInfoList.add(studentScoreInfo);
							}
					
					
					//设置空白为0
					for(Map SummaryInfo : scoreSummaryInfoList){
						
						if(!SummaryInfo.containsKey("UnPassStudentCount")){
							
							SummaryInfo.put("UnPassStudentCount", "0");									
						}
						
	                   if(!SummaryInfo.containsKey("PassStudentCount")){
							
							SummaryInfo.put("PassStudentCount", "0");									
						}
	                   
	                   if(!SummaryInfo.containsKey("ExcellentStudentCount")){
							
							SummaryInfo.put("ExcellentStudentCount", "0");									
						}
	                   
	                   if(!SummaryInfo.containsKey("StudentCount")){
							
							SummaryInfo.put("StudentCount", "0");									
						}
	                   
	                   if(!SummaryInfo.containsKey("PassStudentPercent")){
							
							SummaryInfo.put("PassStudentPercent", "0%");									
						}
	                   
	                   if(!SummaryInfo.containsKey("ExcellentStudentPercent")){
							
							SummaryInfo.put("ExcellentStudentPercent", "0%");									
						}
	                   
					}
					
					
						
					return scoreSummaryInfoList;
				
			    } catch (Exception e) {
					System.out.println("summaryScoreByGrade error:" + e.getMessage());
					return null;
				}
			}
			
			
			//按学院各年级成绩统计
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public List<Map> summaryScoreByCollege_Grade(HashMap summaryCondition,String schoolnum){
			    try{
				    //先收索出3个分数段的人数，然后再整合成一个map对象
							
					List<Map> scoreSummaryInfoList = new ArrayList<Map>();
				
					String collegename =summaryCondition.get("value").toString();//得到学院 
					String languagenum =summaryCondition.get("language").toString();//得到语种
					
					//收索出一个学校某个学院的每个年级及总人数	
					List<Map> studentScoreInfoList0 = studentDao.getScoreInfo_getGrade(schoolnum,collegename,languagenum);
					
					//收索出不及格人数	
					List<Map> studentScoreInfoList1 = studentDao.getScoreInfo_getUnPassStudent(schoolnum,collegename,languagenum,summaryCondition);
					//收索出及格人数
					List<Map> studentScoreInfoList2 = studentDao.getScoreInfo_getPassStudent(schoolnum,collegename,languagenum,summaryCondition);
					
					//收索出优秀人数
					List<Map> studentScoreInfoList3 = studentDao.getScoreInfo_getExcellentStudent(schoolnum,collegename,languagenum,summaryCondition);
					
					//开始整合到一个大的List中
					for(Map studentScoreInfo0 : studentScoreInfoList0){
								Map studentScoreInfo = new JSONObject();
								
								studentScoreInfo.put("grade", studentScoreInfo0.get("grade").toString());
								int studentCount=Integer.parseInt(studentScoreInfo0.get("studentcount").toString());
								studentScoreInfo.put("StudentCount", studentScoreInfo0.get("studentcount").toString());
								//找到同年级的不及格人数
								for(Map studentScoreInfo1 : studentScoreInfoList1){
									if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo1.get("grade").toString()))
									{
										studentScoreInfo.put("UnPassStudentCount", studentScoreInfo1.get("studentcount").toString());
										break;
									}
								}
								//找到同年级的及格人数和及格率
								for(Map studentScoreInfo2 : studentScoreInfoList2){
									if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo2.get("grade").toString()))
									{
										studentScoreInfo.put("PassStudentCount", studentScoreInfo2.get("studentcount").toString());
										float PassStudentPer= Float.parseFloat(studentScoreInfo2.get("studentcount").toString())/studentCount;							
										float PassStudentPercent=(float)(Math.round(PassStudentPer*100*100))/100;//保留2位小数
										String PassStudentPercentage=PassStudentPercent+"%";
										studentScoreInfo.put("PassStudentPercent",PassStudentPercentage);
										break;
									}
								}
								//找到同年级的优秀人数和优秀率
								for(Map studentScoreInfo3 : studentScoreInfoList3){
									if(studentScoreInfo0.get("grade").toString().equals(studentScoreInfo3.get("grade").toString()))
									{
										studentScoreInfo.put("ExcellentStudentCount", studentScoreInfo3.get("studentcount").toString());
										float ExcellentStudentPer= Float.parseFloat(studentScoreInfo3.get("studentcount").toString())/studentCount;
										float ExcellentStudentPercent=(float)(Math.round(ExcellentStudentPer*100*100))/100;//保留2位小数
										String ExcellentStudentPercentage=ExcellentStudentPercent+"%";
										studentScoreInfo.put("ExcellentStudentPercent",ExcellentStudentPercentage);
										break;
									}
								}
								
								
								scoreSummaryInfoList.add(studentScoreInfo);
							}
					
					
					//设置空白为0
					for(Map SummaryInfo : scoreSummaryInfoList){
						
						if(!SummaryInfo.containsKey("UnPassStudentCount")){
							
							SummaryInfo.put("UnPassStudentCount", "0");									
						}
						
                       if(!SummaryInfo.containsKey("PassStudentCount")){
							
							SummaryInfo.put("PassStudentCount", "0");									
						}
                       
                       if(!SummaryInfo.containsKey("ExcellentStudentCount")){
							
							SummaryInfo.put("ExcellentStudentCount", "0");									
						}
                       
                       if(!SummaryInfo.containsKey("StudentCount")){
							
							SummaryInfo.put("StudentCount", "0");									
						}
                       
                       if(!SummaryInfo.containsKey("PassStudentPercent")){
							
							SummaryInfo.put("PassStudentPercent", "0%");									
						}
                       
                       if(!SummaryInfo.containsKey("ExcellentStudentPercent")){
							
							SummaryInfo.put("ExcellentStudentPercent", "0%");									
						}
                       
					}
						
      					return scoreSummaryInfoList;
				
			    } catch (Exception e) {
					System.out.println("summaryScoreByCollege_Grade error:" + e.getMessage());
					return null;
				}
			}
			
			
			//按年级各学院成绩统计
			@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
			public List<Map> summaryScoreByGrade_College(HashMap summaryCondition,String schoolnum){
			    try{
				    //先收索出3个分数段的人数，然后再整合成一个map对象
							
					List<Map> scoreSummaryInfoList = new ArrayList<Map>();
				
					String grade =summaryCondition.get("grade").toString();//得到年级 
					String languagenum =summaryCondition.get("language").toString();//得到语种
					
					//收索出一个学校某个年级的各学院及总人数	
					List<Map> studentScoreInfoList0 = studentDao.getScoreInfo_getCollege(schoolnum,grade,languagenum);
					
					//收索出不及格人数	
					List<Map> studentScoreInfoList1 = studentDao.getScoreInfoByGrade_College_getUnPassStudent(schoolnum,grade,languagenum,summaryCondition);
					//收索出及格人数
					List<Map> studentScoreInfoList2 = studentDao.getScoreInfoByGrade_College_getPassStudent(schoolnum,grade,languagenum,summaryCondition);
					
					//收索出优秀人数
					List<Map> studentScoreInfoList3 = studentDao.getScoreInfoByGrade_College_getExcellentStudent(schoolnum,grade,languagenum,summaryCondition);
					
					//开始整合到一个大的List中
					for(Map studentScoreInfo0 : studentScoreInfoList0){
								Map studentScoreInfo = new JSONObject();
								
								studentScoreInfo.put("collegename", studentScoreInfo0.get("collegename").toString());
								int studentCount=Integer.parseInt(studentScoreInfo0.get("studentcount").toString());
								studentScoreInfo.put("StudentCount", studentScoreInfo0.get("studentcount").toString());
								//找到同年级的不及格人数
								for(Map studentScoreInfo1 : studentScoreInfoList1){
									if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo1.get("collegename").toString()))
									{
										studentScoreInfo.put("UnPassStudentCount", studentScoreInfo1.get("studentcount").toString());
										break;
									}
								}
								//找到同年级的及格人数和及格率
								for(Map studentScoreInfo2 : studentScoreInfoList2){
									if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo2.get("collegename").toString()))
									{
										studentScoreInfo.put("PassStudentCount", studentScoreInfo2.get("studentcount").toString());
										float PassStudentPer= Float.parseFloat(studentScoreInfo2.get("studentcount").toString())/studentCount;							
										float PassStudentPercent=(float)(Math.round(PassStudentPer*100*100))/100;//保留2位小数
										String PassStudentPercentage=PassStudentPercent+"%";
										studentScoreInfo.put("PassStudentPercent",PassStudentPercentage);
										break;
									}
								}
								//找到同年级的优秀人数和优秀率
								for(Map studentScoreInfo3 : studentScoreInfoList3){
									if(studentScoreInfo0.get("collegename").toString().equals(studentScoreInfo3.get("collegename").toString()))
									{
										studentScoreInfo.put("ExcellentStudentCount", studentScoreInfo3.get("studentcount").toString());
										float ExcellentStudentPer= Float.parseFloat(studentScoreInfo3.get("studentcount").toString())/studentCount;
										float ExcellentStudentPercent=(float)(Math.round(ExcellentStudentPer*100*100))/100;//保留2位小数
										String ExcellentStudentPercentage=ExcellentStudentPercent+"%";
										studentScoreInfo.put("ExcellentStudentPercent",ExcellentStudentPercentage);
										break;
									}
								}
								
								
								scoreSummaryInfoList.add(studentScoreInfo);
							}
					
					//设置空白为0
					for(Map SummaryInfo : scoreSummaryInfoList){
						
						if(!SummaryInfo.containsKey("UnPassStudentCount")){
							
							SummaryInfo.put("UnPassStudentCount", "0");									
						}
						
                       if(!SummaryInfo.containsKey("PassStudentCount")){
							
							SummaryInfo.put("PassStudentCount", "0");									
						}
                       
                       if(!SummaryInfo.containsKey("ExcellentStudentCount")){
							
							SummaryInfo.put("ExcellentStudentCount", "0");									
						}
                       
                       if(!SummaryInfo.containsKey("StudentCount")){
							
							SummaryInfo.put("StudentCount", "0");									
						}
                       
                       if(!SummaryInfo.containsKey("PassStudentPercent")){
							
							SummaryInfo.put("PassStudentPercent", "0%");									
						}
                       
                       if(!SummaryInfo.containsKey("ExcellentStudentPercent")){
							
							SummaryInfo.put("ExcellentStudentPercent", "0%");									
						}
                       
					}
                       
                       
						
						
					return scoreSummaryInfoList;
				
			    } catch (Exception e) {
					System.out.println("summaryScoreByGrade_College error:" + e.getMessage());
					return null;
				}
			}
			
			
			
			
			
			
	
				public List<Map> calcStuForCollege(String institutionnum){
				try {
					List <Map> result = new ArrayList<Map>();;
					int stucount = 0;
		
					List<Map> colleges = collegedao.getAllColleges(institutionnum);
			
					List<Map> LanguageList = languagedao.loadlanguageList();
					for(Map college : colleges){
				
						Map temp = new HashMap();
			//			temp.put("collegenum", college.get("collegenum").toString());
						temp.put("collegename", college.get("collegename").toString());
						for(Map Language : LanguageList){
							stucount = 0;

							Map count = studentDao.calcStuCountCollege(institutionnum, college.get("collegename").toString(), Language.get("languagenum").toString());
							stucount = Integer.parseInt(count.get("stucount").toString());
							temp.put(Language.get("languagenum").toString()+"count", stucount);
						
							
						}
						result.add(temp);
					}
					return result;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					return null;
				}

			}
				
				
				
				
				
				
				public List<Map> calcStuForGrade(String institutionnum){
					try {
						List <Map> result = new ArrayList<Map>();;
						int stucount = 0;

						List<Map> grades = studentDao.getAllGrades(institutionnum);

						List<Map> LanguageList = languagedao.loadlanguageList();
						for(Map grade : grades){
							Map temp = new HashMap();
							temp.put("gradename", grade.get("gradename").toString());
							for(Map Language : LanguageList){
								stucount = 0;
								Map count = studentDao.calcStuCountGrade(institutionnum, grade.get("gradename").toString(), Language.get("languagenum").toString());
								stucount = Integer.parseInt(count.get("stucount").toString());
								temp.put(Language.get("languagenum").toString()+"count", stucount);
							
								
							}
							long grade_TotalStu;
							grade_TotalStu = studentDao.calc_Grade_TotalStu(institutionnum,grade.get("gradename").toString());
							temp.put("total",grade_TotalStu) ;
							result.add(temp);
						}
						return result;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						return null;
					}

				}
				
				//单独用于打印报名汇总表，
				public List<Map> calcStuForGrade_SignUpSummary(String institutionnum){
					try {
						List <Map> result = new ArrayList<Map>();;
						int stucount = 0;

						List<Map> grades = studentDao.getAllGrades(institutionnum);

						List<Map> LanguageList = studentDao.getLanguageList(institutionnum);
						for(Map grade : grades){
							Map temp = new HashMap();
							temp.put("gradename", grade.get("gradename").toString());
							for(Map Language : LanguageList){
								stucount = 0;
								Map count = studentDao.calcStuCountGrade(institutionnum, grade.get("gradename").toString(), Language.get("languagenum").toString());
								stucount = Integer.parseInt(count.get("stucount").toString());
								temp.put(Language.get("languagenum").toString()+"count", stucount);
							
								
							}
							result.add(temp);
						}
						return result;
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						return null;
					}

				}
				
				
				
				//用于显示在考试院,所有高校的缺考信息
				@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
				public List<Map> getPageAbsentStudentsAllSchools(String pageNum, String pageSize){
					try {
						return studentDao.getPageAbsentStudentsAllSchools(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
					} catch (Exception e) {
						System.out.println("getPageAbsentStudentsAllSchools error");
						e.printStackTrace();
						return null;
					}
				}
				
				//用于显示在考试院,所有高校的缺考信息
				@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
				public long getAbsentStudentsTotalCountAllSchools(){
					try {
						return studentDao.getAbsentStudentsTotalCountAllSchools();
					} catch (Exception e) {
						System.out.println("getAbsentStudentsTotalCountAllSchools error");
						e.printStackTrace();
						return 0;
					}
				}
				
				
				//用于显示在考试院,所有高校的作弊信息
				public List<Map> getPageFraudStudentsAllSchools(String pageNum, String pageSize){
					try {
						return studentDao.getPageFraudStudentsAllSchools(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
					} catch (Exception e) {
						System.out.println("getPageFraudStudentsAllSchools error:" + e.getMessage());
						return null;
					}
			}
				
				
				//用于显示在考试院,所有高校的作弊信息
				@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
				public long getFraudStudentsTotalCountAllSchools(){
					try {
						return studentDao.getFraudStudentsTotalCountAllSchools();
					} catch (Exception e) {
						System.out.println("getFraudStudentsTotalCountAllSchools error");
						e.printStackTrace();
						return 0;
					}
				}
				
}
	
