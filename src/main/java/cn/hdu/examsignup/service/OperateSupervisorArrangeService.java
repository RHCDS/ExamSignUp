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
import cn.hdu.examsignup.dao.OperateExamArrangeDao;
import cn.hdu.examsignup.dao.OperateSupervisorArrangeDao;
import cn.hdu.examsignup.model.ExArrangeSupervisor;

@Service
public class OperateSupervisorArrangeService {
	@Autowired
	private OperateSupervisorArrangeDao operateSupervisorArrangeDao;
	@Autowired
	private ArrangeSupervisorDao arrangeSupervisorDao;
	@Autowired
	private OperateExamArrangeDao operateExamArrangeDao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadArrangeInfo(String institutionnum) {
		List<Map> result = this.operateSupervisorArrangeDao.loadArrangeInfo(institutionnum);
		for(Map element:result){
			String arrangeid = element.get("id").toString();
			long capacity = this.operateExamArrangeDao.getStudentCountByArrangeId(arrangeid, institutionnum);
			List supervisor = this.arrangeSupervisorDao.getSupervisorByArrangeid(arrangeid, institutionnum);
			element.put("capacity", capacity);
			element.put("supervisor", supervisor);
		}
		return result;
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadArrangedSupervisor(String arrangeid) {
		return this.operateSupervisorArrangeDao.loadArrangedSupervisor(arrangeid);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadUnarrangedSupervisor(String sectionid,String institutionnum) {
		return this.operateSupervisorArrangeDao.loadUnarrangedSupervisor(sectionid, institutionnum);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String deleteArrangedSupervisor(List<Map> supervisors,String arrangeid) {
		return this.arrangeSupervisorDao.deleteArrangedSupervisor(supervisors,arrangeid);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String arrangeSupervisor(String supervisorid,String arrangeid,String institutionnum) {
		return this.arrangeSupervisorDao.arrangeSupervisor(supervisorid,arrangeid,institutionnum);
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String cancelArrange(String institutionnum)  {
		String operate = "operate";
		return this.arrangeSupervisorDao.cancelArrange(institutionnum,operate);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String checkArrangeStatus(String institutionnum)  {
		try {
			return this.arrangeSupervisorDao.checkArrangeStatus(institutionnum);
		} catch (Exception e) {
			System.out.println("checkArrangeStatus error"+e.getMessage());
		}
		return "false";
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String MergeSupervisor(List<Map> readyToMerger,String key,String institutionnum) {
		int keyI = Integer.parseInt(key);
		Map keyArrange = readyToMerger.get(keyI);
		String arrangeid = keyArrange.get("id").toString();
		List<Map> supervisors = this.operateSupervisorArrangeDao.loadArrangedSupervisor(arrangeid, institutionnum);
		
		for(int i=0;i<readyToMerger.size();i++) {
			if(i == keyI) {
				continue;
			}
			Map element =  readyToMerger.get(i);
			String toArrangeid = element.get("id").toString();
			boolean flag = true;
			for(Map supervisor:supervisors) {
				String supervisorid = supervisor.get("id").toString();
				flag = this.arrangeSupervisorDao.arrangeOneSupervisor(supervisorid, toArrangeid, institutionnum);
				if(flag == false) {
					return "{success:false,errors:{info: '操作失败！'}}";
				}
			}
		}
		return "{success:false,errors:{info: '操作成功！'}}";
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String autoArrange(String institutionnum) {
		
		List<Map> sections = this.operateSupervisorArrangeDao.getSection(institutionnum);
		for(Map section:sections) {
			String sectionid = section.get("id").toString();
			List arrangements = this.operateSupervisorArrangeDao.getArrangementsBySection(sectionid, institutionnum);
			
			for(Object arrangement:arrangements) {
				String arrangeid = arrangement.toString();
				
				String supervisorid = this.operateSupervisorArrangeDao.getOneUnarrangedSupervisor(sectionid, institutionnum);
				if(null == supervisorid) {
					break;
				}
				
				boolean flag  = false;
				flag = this.arrangeSupervisorDao.arrangeOneSupervisor(supervisorid, arrangeid, institutionnum);
				if(flag == false) {
					return "{success:false,errors:{info: '操作失败！'}}";
				}
			}
		}
		return "{success:true,errors:{info: '操作成功！'}}";
		
	}
}
