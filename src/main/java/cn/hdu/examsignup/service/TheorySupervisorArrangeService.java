package cn.hdu.examsignup.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.ArrangeSupervisorDao;
import cn.hdu.examsignup.dao.TheoryExamArrangeDao;
import cn.hdu.examsignup.dao.TheorySupervisorArrangeDao;
import cn.hdu.examsignup.model.ExArrangeSupervisor;

@Service
public class TheorySupervisorArrangeService {
	@Autowired
	private TheorySupervisorArrangeDao theorySupervisorArrangeDao;
	@Autowired
	private ArrangeSupervisorDao arrangeSupervisorDao;
	@Autowired
	private TheoryExamArrangeDao theoryExamArrangeDao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadArrangeInfo(String institutionnum) {
		List<Map> result = this.theorySupervisorArrangeDao.loadArrangeInfo(institutionnum);
		for(Map element:result){
			String examroomnum = element.get("examroomnum").toString();
			Map examroominfo = this.theorySupervisorArrangeDao.getExamroomInfo(examroomnum, institutionnum);
			if(null == examroominfo) {
				continue;
			}
			String arrangeid = examroominfo.get("arrangeid").toString();
			long capacity = this.theorySupervisorArrangeDao.getExamroomCapacity(institutionnum, examroomnum);
			List supervisor = this.arrangeSupervisorDao.getSupervisorByExamroomnum(examroomnum, institutionnum);
			element.put("capacity", capacity);
			element.put("supervisor", supervisor);
			element.put("sectionid",examroominfo.get("sectionid"));
			element.put("sectioninfo",examroominfo.get("sectioninfo"));
			element.put("physicexamroomid",examroominfo.get("physicexamroomid"));
			element.put("roomlocation",examroominfo.get("roomlocation"));
			element.put("campusname",examroominfo.get("campusname"));
			element.put("language",examroominfo.get("language"));
			element.put("arrangeid",arrangeid);
		}
		return result;
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadArrangedSupervisor(String examroomnum,String institutionnum,String sectionid) {
		return this.theorySupervisorArrangeDao.loadArrangedSupervisor(examroomnum,institutionnum,sectionid);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadUnarrangedSupervisor(String sectionid,String institutionnum) {
		return this.theorySupervisorArrangeDao.loadUnarrangedSupervisor(sectionid, institutionnum);
	}
	//取消监考老师安排
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String deleteArrangedSupervisor(List<Map> supervisors,String examroomnum,String institutionnum) {
		return this.arrangeSupervisorDao.deleteArrangedSupervisor(supervisors,examroomnum,institutionnum);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String arrangeSupervisor(String supervisorid,String examroomnum,String arrangeid,String institutionnum) {
		return this.arrangeSupervisorDao.arrangeSupervisor(supervisorid,examroomnum,arrangeid,institutionnum);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String cancelArrange(String institutionnum)  {
		String theory = "theory";
		return this.arrangeSupervisorDao.cancelArrange(institutionnum,theory);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String checkArrangeStatus(String institutionnum) {
		return this.theorySupervisorArrangeDao.checkArrangeStatus(institutionnum);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String MergeSupervisor(List<Map> readyToMerger,String key,String institutionnum) {
		int keyI = Integer.parseInt(key);
		Map keyExamroom = readyToMerger.get(keyI);
		String examroomnum = keyExamroom.get("examroomnum").toString();
		String sectionid = keyExamroom.get("sectionid").toString();
		List<Map> supervisors = this.theorySupervisorArrangeDao.loadArrangedSupervisor(examroomnum, institutionnum, sectionid);
		
		for(int i=0;i<readyToMerger.size();i++) {
			if(i == keyI) {
				continue;
			}
			Map element =  readyToMerger.get(i);
			String arrangeid = element.get("arrangeid").toString();
			String ToArrangeExamroomnum = element.get("examroomnum").toString();
			boolean flag = true;
			for(Map supervisor:supervisors) {
				String supervisorid = supervisor.get("id").toString();
				flag = this.arrangeSupervisorDao.arrangeOneSupervisor(supervisorid, ToArrangeExamroomnum, arrangeid, institutionnum);
				if(flag == false) {
					return "{success:false,errors:{info: '操作失败！'}}";
				}
			}
		}
		return "{success:false,errors:{info: '操作成功！'}}";
	
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String autoArrange(String institutionnum) {
		List<Map> sections = this.theorySupervisorArrangeDao.getSection(institutionnum);
		for(Map section:sections) {
			String sectionid = section.get("id").toString();
			long examroomCount = this.theorySupervisorArrangeDao.getExamroomCountBySection(sectionid, institutionnum);
			long primarySupervisorCount = this.theorySupervisorArrangeDao.getPrimarySupervisorCount(institutionnum);
			long supervisorCount = this.theorySupervisorArrangeDao.getSupervisorCount(institutionnum);
			if (supervisorCount == 0) {
				return "{success: false,errors: {info: '监考老师人数不足！'}}";
			}
			if(primarySupervisorCount<examroomCount) {
				return "{success: false,errors: {info: '第" + section.get("sectionnum").toString() + "场次主监考老师人数不足！'}}";
			}
		}
		for(Map section:sections) {
			String sectionid = section.get("id").toString();
			List examrooms = this.theorySupervisorArrangeDao.getExamroomBySection(sectionid, institutionnum);
			
		outer:
			for(Object examroom:examrooms) {
				String examroomnum = examroom.toString();
				String arrangeid = this.theorySupervisorArrangeDao.getArrangeid(examroomnum,institutionnum);
				long studentCount = this.theoryExamArrangeDao.getExamroomCapacity(institutionnum, examroomnum);
				if(studentCount != 30) {
					continue;
				}
			//	if(studentCount>15) { //15人一个监考老师
				for(int i=0;i<2;i++) {
					String supervisorid = "";
					if(i == 0) {
						supervisorid = this.theorySupervisorArrangeDao.getOneUnarrangedPrimarySupervisor(sectionid, institutionnum);
					} else {
						supervisorid = this.theorySupervisorArrangeDao.getOneUnarrangedSupervisor(sectionid, institutionnum);
					}
					if(null == supervisorid) {
						break outer;
					}
					boolean flag = this.arrangeSupervisorDao.arrangeOneSupervisor
							(supervisorid, examroomnum, arrangeid, institutionnum);
					if(flag == false) {
						this.cancelArrange(institutionnum);
						return "{success:false,errors:{info: '操作失败！'}}";
					}
				}
			}
		}
		return "{success:true,errors:{info: '操作成功！'}}";
		
	}
}
