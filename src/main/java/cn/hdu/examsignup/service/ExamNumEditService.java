package cn.hdu.examsignup.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.ExamNumEditDao;
import cn.hdu.examsignup.dao.InstitutionDao;
import cn.hdu.examsignup.dao.StudentDao;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExStudent;

@Service
public class ExamNumEditService {

	@Autowired
	ExamNumEditDao examNumEditDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private InstitutionDao institutionDao;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getPageExamNumEdit(String whereString, Map<String,Object> valuesMap, String institutionnum, Integer pageNum,
			Integer pageSize) {
		List<Map> result = null;
		try {
			result = this.examNumEditDao.getPageExamNumEdit(whereString, valuesMap, institutionnum,
					pageNum, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadExamNumLanguageInfo(String institutionnum) {
		List<Map> result = null;
		try {
			result = this.examNumEditDao
					.loadExamNumLanguageInfo(institutionnum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getExamNumEditTotalCount(String whereString, Map<String,Object> valuesMap, String institutionnum) {
		long result = 0;
		try {
			result = this.examNumEditDao
					.getExamNumEditTotalCount(whereString, valuesMap,institutionnum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param institutionnum
	 * @return
	 * 高校点击编排准考证之前首先验证是否所有考生都已经完成缴费
	 * 
	 * 编排准考证之前需要所有考生都已经完成缴费
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String validateBeforeArrange(String institutionnum) {
		// 检查是否有未缴费学生
		Long result = this.examNumEditDao.countUnpaiedStudent(institutionnum);
		List<String> temp;
		if (result != 0)
			return "{ success: false, errors:{info: '有" + result.toString()
					+ "个考生没有缴费，所有考生缴费完成后才能进行编排准考证号！'}}";
		
		temp=this.examNumEditDao.campusIsNull(institutionnum);
		if (temp.size()!=0)
			return "{ success: false, errors:{info: '有" + temp.size()
					+ "个考生没有校区信息，请完善资料！'}}";
		temp=this.examNumEditDao.validateCampus(institutionnum);
		if(temp.size()!=0)
			return "{ success: false, errors:{info: '有" + temp.size()
					+ "个校区的校区代码不为数字或者长度大于1位！'}}";
		
		temp=this.examNumEditDao.validateStudentCategory(institutionnum);
		if(temp.size()!=0)
			return "{ success: false, errors:{info: '有" + temp.size()
					+ "考生类别代码不为数字或者长度大于1位！'}}";
		
		if(this.examNumEditDao.collegeIsNull(institutionnum).size()!=0 ||
				this.examNumEditDao.professionIsNull(institutionnum).size()!=0)
			return "{ success: true, errors:{info: '由于部分学生的院系与专业信息不全，排序项中将缺失这几个备选项！'}}";
		
		return "{ success: true, errors:{info: '通过数据校验,准备开始编排'}}";
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<String> collegeIsNull(String institutionnum) {
		return this.examNumEditDao.collegeIsNull(institutionnum);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<String> professionIsNull(String institutionnum) {
		return this.examNumEditDao.professionIsNull(institutionnum);
	}

	/**
	 * @param arrangeParam
	 * @return
	 * 实际编排准考证
	 * 准考证编排按照先校区、后语种
	 * 准考证共15位：
	 * 		考试时间3位，比如151，代表15年上半年
	 * 		学校编号3位，比如451代表杭电
	 * 		校区编号1位，比如1、2、3....
	 * 		语种编号2位，比如11代表一级Linux
	 * 		考试级别1位，1代表本科生等
	 * 		考场号3位，比如001代表考场第一个，考场号最大为999
	 * 		考场号内部编号，默认一个考场30人，也就是1--30
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String startArrange(Map arrangeParam) {
		// TODO Auto-generated method stub
		List<Map> signedLanguage=this.examNumEditDao.getSignedLanguage((String)arrangeParam.get("institutionnum"));
		ExInstitution institution = institutionDao.getInstitutionByInstitutionNum(arrangeParam.get("institutionnum").toString());
		List<ExStudent> students = studentDao.findByProperty("exInstitution", institution);
		String appendFlag = arrangeParam.get("appendflag").toString();
		IdCardCheck idcardcheck = new IdCardCheck();
		
		int capacity=Integer.parseInt((String) arrangeParam.get("capacity"));
		String examTime=(String)arrangeParam.get("examtime");
		String institutionnum=(String)arrangeParam.get("institutionnum");
		
		List<String> temp;
		List<String> languageList=this.examNumEditDao.getLanguageListInStudent((String)arrangeParam.get("institutionnum"));
		List<String> campusList=this.examNumEditDao.getCampusListInStudent((String)arrangeParam.get("institutionnum"));
		
		JSONArray result = new JSONArray();
		String examNum=null;
		//check id num
		int IDErrorCount=0;
		String ErrorTemp;
		ErrorTemp="{ success: false, errors:{info: '以下学号身份证号码有误：<br>";
		
		// 编排准考证之前首先检查身份证是否合理
		
		for(ExStudent student : students) {
			if(!idcardcheck.isValidatedAllIdcard(student.getIdnum())){
				ErrorTemp  += student.getStudentnum()+":"+student.getIdnum()+"<br>";
				IDErrorCount++;
			}
		}
		if(IDErrorCount!=0){
			ErrorTemp+="共"+IDErrorCount+"条错误数据！'}}";
			return ErrorTemp;
		}
		ErrorTemp="";
		IDErrorCount=0;
		// 设置的考场容量是否合理
		if(capacity==0)
			return "{ success: false, errors:{info: '单个考场容量不能为0，请重新设置考场容量！'}}";
		if(capacity/99>0)
			return "{ success: false, errors:{info: '单个考场容量不能大于两位，请缩小考场容量！'}}";
		if(examTime==null || examTime.trim().isEmpty())
			return "{ success: false, errors:{info: '请输入正确的考试时间，请重新设置考试时间！'}}";
		if(!examTime.matches("\\d{2}[1,2]"))
			return "{ success: false, errors:{info: '输入的考试时间不合法，请重新设置考试时间！'}}";
		
		long totalSigned=0;
		for(int i=0;i<signedLanguage.size();i++){
			totalSigned += (Long)signedLanguage.get(i).get("count");
		}
		if(totalSigned/(capacity*999)>0)
			return "{ success: false, errors:{info: '单个考场容量过小，导致考场序号超过三位，请扩大考场容量！'}}";
		
		int examroomNum;
		if(appendFlag.equals("append")) {
			examroomNum = this.examNumEditDao.getMaxExamroomNum((String)arrangeParam.get("institutionnum"));
		} else {
			examroomNum = 1;
		}
		int currentStudent=0;
		int waitingForArrangementSize;
		/**
		 * 准考证编排按照先校区、后语种
		 * 准考证共15位：
		 * 		考试时间3位，比如151，代表15年上半年
		 * 		学校编号3位，比如451代表杭电
		 * 		校区编号1位，比如1、2、3....
		 * 		语种编号2位，比如11代表一级Linux
		 * 		考试级别1位，1代表本科生等
		 * 		考场号3位，比如001代表考场第一个，考场号最大为999
		 * 		考场号内部编号，默认一个考场30人，也就是1--30
		 * 
		**/
		for(String campustElem : campusList){
			for(String languageElem : languageList){
				// 当前校区当前语种所有考生信息
				List<Map> waitingForArrangement=
						this.examNumEditDao.getWaitingArrangeInfo((String)arrangeParam.get("institutionnum"),campustElem,languageElem,appendFlag);
				if(null == waitingForArrangement) { continue; }
				// 当前剩余需要编排的考生数量
				waitingForArrangementSize=waitingForArrangement.size();
				if(waitingForArrangementSize!=0){
					// 根据考场一个一个编排
					for(currentStudent=0;currentStudent<waitingForArrangementSize;examroomNum++){
						if(examroomNum>999){
							return "{ success: false, errors:{info: '单个考场容量过小，导致考场序号超过三位，请扩大考场容量！'}}";
						}
						// 每个考场内部默认30名考试编排
						for(int studentNum=1;studentNum<=capacity && currentStudent<waitingForArrangementSize;studentNum++){
							// 每个准考证号编排，15位长度
							examNum=examTime
									+institutionnum
									+campustElem.trim().charAt(0)
									+languageElem.trim().substring(0, 2)
									+((String)waitingForArrangement.get(currentStudent).get("studentcategory")).trim().charAt(0)
									+String.format("%03d", examroomNum)
									+String.format("%02d", studentNum);
							waitingForArrangement.get(currentStudent).put("examNum", examNum);
							currentStudent++;
						}
					}
				}
				// 每个语种安排完毕并保存
				temp=this.examNumEditDao.saveExamNum(waitingForArrangement);
				if(temp.size()!=0)
				{
					for(int j=0;j<temp.size();j++)
					{
						result.add(temp.get(j));
					}
				}
			}
		}
		if(result.size()!=0)
		{
			return "{ success:false, errors:{info: '以下身份证号的考生生成准考证号出错:\n"+result.toString()+"'}}";
		}
		else
		{
			return "{ success:true, errors:{info: '生成准考证号成功！'}}";
		}
	}
}
