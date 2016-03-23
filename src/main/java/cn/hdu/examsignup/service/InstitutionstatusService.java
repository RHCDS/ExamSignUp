package cn.hdu.examsignup.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.InstitutionDao;
import cn.hdu.examsignup.dao.InstitutionstatusDao;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExInstitutionstatus;

@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class InstitutionstatusService {
	@Autowired
	private InstitutionstatusDao institutionstatusDao;
	@Autowired
	private InstitutionDao institutionDao;

	public List<Map> loadinstitutionstatus() {
		try {
			List<Map> result = new ArrayList();
			List<ExInstitutionstatus> exInstitutionstatusList= institutionstatusDao.loadinstitutionstatusExceptMaxAndMinNum();
			SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			for (ExInstitutionstatus element : exInstitutionstatusList) {
				Map temp = new HashMap();
				Date startTime = element.getStarttime();
				Date endtime =   element.getEndtime();
				temp.put("id",element.getId());
				temp.put("remarks", element.getRemarks());
				temp.put("startDateValue", dataFormat.format(startTime));
				temp.put("startTimeValue", timeFormat.format(startTime));
				temp.put("endDateValue", dataFormat.format(endtime));
				temp.put("endTimeValue", timeFormat.format(endtime));
				result.add(temp);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String updateInstitutionTime(Map timeMap) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date startTime = null;
			Date endTime = null;
			
			String id=(String)timeMap.get("id");
			if(id==null)
				return "{ success: false, errors:{info: '传入id参数错误!'}}";
			ExInstitutionstatus status=this.institutionstatusDao.findById(id.trim());
			try {
				startTime = dateFormat.parse(timeMap.get("startDateValue")+" "+timeMap.get("startTimeValue").toString());
				endTime = dateFormat.parse(timeMap.get("endDateValue").toString()+" "+timeMap.get("endTimeValue").toString());
				if(endTime.before(startTime)){
					return "{ success: false, errors:{info: '开始时间应当晚于结束时间！'}}";
				}
				status.setStarttime(startTime);
				status.setEndtime(endTime);
				this.institutionstatusDao.update(status);
			} catch (ParseException e1) {
				e1.printStackTrace();
				return  "{ success: false, errors:{info: '时间格式错误!'}}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{ success: false, errors:{info: '后台错误!'}}";
		}
		return "{ success: true, errors:{info: '更新成功!'}}";
	}

	public String getCurrenStatus(String institutionnum) {
		try {
			return "{ success: true, errors:{info: '所处状态:"+this.institutionDao.findByProperty("institutionnum", institutionnum).get(0).getInstitutionstatus().getName()+"'}}";
			} catch (Exception e) {
			e.printStackTrace();
			return "{ success: false, errors:{info: '后台出错!'}}";
		}
	}

	final static Integer SCHOOL_NEEDIMPORT_NUM=1;
	final static Integer  SCHOOL_NEEDCHECK_NUM=2;
	final static Integer  CITY_NEEDCHECK_NUM=3;
	final static Integer CITY_CHECKED_NUM=4;
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String schoolCheckOK(String institutionnum) {
		try {
			ExInstitution school=this.institutionDao.findByProperty("institutionnum", institutionnum).get(0);
			if(school.getInstitutionstatus().getStatusnum()==SCHOOL_NEEDIMPORT_NUM){
				return "{ success: false, errors:{info: '您尚未上报数据!'}}";
			}else if(school.getInstitutionstatus().getIndexnum() > this.institutionstatusDao.findByProperty("statusnum", SCHOOL_NEEDCHECK_NUM).get(0).getIndexnum()){
				return "{ success: false, errors:{info: '警告，不得重复确认!'}}";
			}else{
				ExInstitutionstatus nextStatus=this.institutionstatusDao.findByProperty("statusnum",CITY_NEEDCHECK_NUM).get(0);
				ExInstitutionstatus curStatus=this.institutionstatusDao.findByProperty("statusnum",SCHOOL_NEEDCHECK_NUM).get(0);
				Date  curDate=  new Date(System.currentTimeMillis());
				if(curDate.before(curStatus.getEndtime())&&curDate.after(curStatus.getStarttime())){
					school.setInstitutionstatus(nextStatus);
					this.institutionDao.update(school);
					return "{ success: true, errors:{info: '确认成功!'}}";
				}
				else{
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					return "{ success: false, errors:{info: '还未到确认时间！<br>请在以下时间段内确认:"+dateFormat.format(curStatus.getStarttime())+
							"~"+dateFormat.format(curStatus.getEndtime())+
							"'}}";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{ success: false, errors:{info: '后台出错!'}}";
		}
	}

	public List<Map> loadStatusList() {
		try {
			return this.institutionstatusDao.loadStatusList();
			} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String updateSchoolStatus(Map param) {
		try {
			String institutionid=(String) param.get("id");
			String newStatusID=(String) param.get("newStatusID");
			if(institutionid==null || newStatusID==null || institutionid.trim().isEmpty()|| newStatusID.trim().isEmpty()){
				return "{ success: false, errors:{info: '参数有误!'}}";
			}
			ExInstitution institution=this.institutionDao.findById(institutionid);
			ExInstitutionstatus newStatus=this.institutionstatusDao.findById(newStatusID);
			ExInstitutionstatus lowestStatus=this.institutionstatusDao.findByProperty("statusnum",CITY_NEEDCHECK_NUM).get(0);//市考试院最低级别"等待市考试院确认"
			if(institution.getCategory().equals("city")){
				if(newStatus.getIndexnum()<=lowestStatus.getIndexnum())
					newStatus=lowestStatus;
				institution.setInstitutionstatus(newStatus);
				this.institutionDao.update(institution);
			}else if(institution.getCategory().equals("school")){
				institution.setInstitutionstatus(newStatus);
				institution.getHigherInstitution().setInstitutionstatus(lowestStatus);
				this.institutionDao.update(institution);
			}
			return "{ success: true, errors:{info: '更新状态成功!'}}";
			} catch (Exception e) {
			e.printStackTrace();
			return "{ success: false, errors:{info: '后台出错!'}}";
		}
	}
}
