package cn.hdu.examsignup.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.relation.Role;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.InstitutionDao;
import cn.hdu.examsignup.dao.InstitutionstatusDao;
import cn.hdu.examsignup.dao.RoleDao;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExInstitutionstatus;
import cn.hdu.examsignup.model.ExRole;


@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class InstitutionService {

	@Autowired
	private InstitutionDao institutionDao;
	@Autowired
	private InstitutionstatusDao institutionststusDao;
	@Autowired
	private RoleDao roleDao;

	public InstitutionService() {
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<String[]> getSchoolNumName() {
		return this.institutionDao.getSchoolNumName();
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List getSchoolNum() {
		return this.institutionDao.getSchoolNum();
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<String[]> getInstitutionNumName() {
		return this.institutionDao.getInstitutionNumName();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ExInstitution getInstitutionByInstitutionNum(String institutionnum){
		try {
			return institutionDao.getInstitutionByInstitutionNum(institutionnum);
		} catch (Exception e) {
			System.out.println("getInstitutionByInstitutionNum error!" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadManagedSchoolList(String institutionnum){
		try {
			return institutionDao.loadManagedInstitutionList(institutionnum,"school");
		} catch (Exception e) {
			System.out.println("getInstitutionByInstitutionNum error!" + e.getMessage());
			return null;
		}
	}
	

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ExInstitution getInstitutionByInstitutionID(String institutionid) {
		try {
			return institutionDao.getInstitutionByInstitutionID(institutionid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> loadChildInstitution(String institutionnum){
		try {
			return institutionDao.loadChildInstitution(institutionnum);
		} catch (Exception e) {
			System.out.println("loadChildInstitution error!" + e.getMessage());
			return null;
		}
	}
	
	final static Integer SCHOOL_NEEDIMPORT_NUM=1;
	final static Integer  SCHOOL_NEEDCHECK_NUM=2;
	final static Integer  CITY_NEEDCHECK_NUM=3;
	final static Integer CITY_CHECKED_NUM=4;
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String setInstitutionstatusByString(List<Map> readyToConfirmList,Integer roleNum,String institutionnum,Integer nextStatusNum) {
		try {
			ExRole role=this.roleDao.findByProperty("rolenum", roleNum).get(0);
			ExInstitution  institution =this.institutionDao.findByProperty("institutionnum", institutionnum).get(0);
			ExInstitutionstatus currStatus=this.institutionststusDao.findByProperty("indexnum",
					this.institutionststusDao.findByProperty("statusnum", nextStatusNum).get(0).getIndexnum()-1).get(0);//当前应当满足的条件
			ExInstitutionstatus nextStatus=this.institutionststusDao.findByProperty("statusnum",nextStatusNum).get(0);
			String rolename=role.getName().trim();
			
			if(currStatus.getRolename().equals(rolename)){//只能进行同级确认
				Date  curDate=  new Date(System.currentTimeMillis());
				if(curDate.before(currStatus.getEndtime())&&curDate.after(currStatus.getStarttime())){
					//首先更新子项目,后更新他本身
					try{
						//判断是否为上下属关系，一般不会存在问题
						List<Map> childInstitutions= this.institutionDao.loadChildInstitution(institutionnum);//直属机构
						
						List<String>  readyInstitutionnumList=new ArrayList();
						for(Map element:readyToConfirmList){
							readyInstitutionnumList.add((String) element.get("institutionnum"));
						}
						
						List<String> childInstitutionList=new ArrayList();
						for(Map element:childInstitutions){
							childInstitutionList.add((String) element.get("institutionnum"));
						}
						List<String>  tempInsitututionnumList1=new ArrayList();
						List<String>  tempInsitututionnumList2=new ArrayList();
						tempInsitututionnumList1.addAll(readyInstitutionnumList);
						tempInsitututionnumList2.addAll(readyInstitutionnumList);
						tempInsitututionnumList1.retainAll(childInstitutionList);
						tempInsitututionnumList2.removeAll(tempInsitututionnumList1);
						if(tempInsitututionnumList2.size()!=0){
							String temp="";
							for(String element:tempInsitututionnumList2){
								temp+=element+"<br>";
							}
							temp=temp.subSequence(0, temp.length()-4).toString();
							return "{ success: false, errors:{info: '以下子机构不属于您的直属机构："+temp+".'}}";
						}
						
						//检查是否有小于待跳转状态的机构
						List<Map> uncheckStateResult=this.institutionDao.hasSameStatusByInstitutionnum(readyInstitutionnumList,currStatus.getIndexnum());
						if(uncheckStateResult.size()!=0){
							String temp="";
							for(Map element:uncheckStateResult){
								temp+=(String)element.get("institutionname")+"<br>";
								temp=temp.subSequence(0, temp.length()-4).toString();
							}
							temp=temp.subSequence(0, temp.length()-4).toString();
							return "{ success: false, errors:{info: '以下子机构未处于<br>\""+currStatus.getName()+"\"状态，请重新选择：<br>"+temp+".'}}";
						}
						
						for(String element: readyInstitutionnumList){//对其所有下属机构状态进行确认
							ExInstitution temp= this.institutionDao.findByProperty("institutionnum", element).get(0);
							temp.setInstitutionstatus(nextStatus);
							this.institutionDao.update(temp);
						}
						//institution.setInstitutionstatus(nextStatus);//对机构本身状态进行确认
						//this.institutionDao.update(institution);
					}catch(Exception e){
						e.printStackTrace();
						return  "{ success: false, errors:{info: '对部分学校确认时出错！'}}";
					}
				}
				else{
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					return "{ success: false, errors:{info: '还未到确认时间！<br>请在以下时间段内确认:"+dateFormat.format(nextStatus.getStarttime())+
							"~"+dateFormat.format(nextStatus.getEndtime())+
							"'}}";
				}
			}else{
				return "{ success: false, errors:{info: '请等待\""+currStatus.getRolename()+"\"完成确认！'}}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return "{ success: true, errors:{info: '完成对所选学校的确认！'}}";
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String cityManagerSelfCheckPass(String institutionnum,Integer nextStatusNum) {
		try{
			ExInstitution currentInstitution=this.institutionDao.findByProperty("institutionnum", institutionnum).get(0);
			ExInstitutionstatus nextStatus=this.institutionststusDao.findByProperty("statusnum", nextStatusNum).get(0);
			currentInstitution.setInstitutionstatus(nextStatus);
			this.institutionDao.update(currentInstitution);
			List<Map>result=this.institutionDao.loadChildInstitution(currentInstitution.getInstitutionnum());
			List<String> childInstitutionList= new ArrayList<String>();
			for(Map element:result){
				childInstitutionList.add((String) element.get("institutionnum"));
			}
			result=this.institutionDao.childStatusGroupCount(childInstitutionList);
			String groupResult="";
			for(Map element:result){
				groupResult+=(String)element.get("name")+":共"+((Long)element.get("count")).toString()+";<br>";
			}
			groupResult.subSequence(0, groupResult.length()-4).toString();
			return "{ success: true, errors:{info: '确认成功!其中<br>"+groupResult+"'}}";
		}catch(Exception e){
			e.printStackTrace();
			return "{ success: true, errors:{info: '后台出错！'}}";
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String getCityManagerStatus(String institutionnum) {
		try{
			ExInstitutionstatus curStatus=this.institutionDao.findByProperty("institutionnum", institutionnum).get(0).getInstitutionstatus();
			//分水岭，小于该状态indexnum的所有状态都返回未确认!,大于则返回对应的状态
			ExInstitutionstatus boundryStatus= this.institutionststusDao.findByProperty("statusnum", 4).get(0);
			if(curStatus.getIndexnum()<boundryStatus.getIndexnum()){
				return "市考试院尚未确认";
			}else{
				return curStatus.getName();
			}
		}catch(Exception e){
			e.printStackTrace();
			return "{ success: true, errors:{info: '后台出错！'}}";
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getInstitutionByType(String type){
		try {
			return institutionDao.getInstitutionByType(type);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String saveInstitution(JSONObject institution, String category){
		try {
			ExInstitution entity;
			if(institution.get("id").toString().equals("")){
				if(institutionDao.findByProperty("institutionnum", institution.get("institutionnum").toString()).size()>0){
					return "学校已存在！请更换别的学校代码！";
				}
				entity = new ExInstitution();
				entity.setInstitutionnum((institution.get("institutionnum").toString()));
				ExInstitution parentinstitution;
				parentinstitution = institutionDao.findByProperty("name", institution.get("higherInstitution").toString()).get(0);
				entity.setHigherInstitution(parentinstitution);
				entity.setName((institution.get("name").toString()));
				entity.setCategory(category);
				entity.setRemark((institution.get("remark").toString()));
				entity.setContact((institution.get("contact").toString()));
				entity.setInstitutionstatus(this.institutionststusDao.findByProperty("statusnum", 1).get(0));
				institutionDao.save(entity);
			}else{
				entity = institutionDao.findById(institution.get("id").toString());
				entity.setInstitutionnum((institution.get("institutionnum").toString()));
				ExInstitution parentinstitution;
				parentinstitution = institutionDao.findByProperty("name", institution.get("higherInstitution").toString()).get(0);
				entity.setHigherInstitution(parentinstitution);
				entity.setName((institution.get("name").toString()));
				entity.setCategory(category);
				entity.setRemark((institution.get("remark").toString()));
				entity.setContact((institution.get("contact").toString()));
				entity.setInstitutionstatus(this.institutionststusDao.findByProperty("statusnum", 1).get(0));
				institutionDao.update(entity);
			}
			return "保存成功！";
		} catch (Exception e) {
			System.out.println("saveinstitution error" + e.getMessage());
			return "保存失败！";
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String deleteInstitutions(List<String> ids){
		try {
			for(String id : ids){
				ExInstitution institution = institutionDao.findById(id);
				if(institution.getExInstitutions().size()>0){
					if(institution.getCategory().equals("city")){
						return "请先删除该考试院下的所有高校！";
					}else if(institution.getCategory().equals("school")){
						return "请先删除该高校下的所有考试单位！";
					}else
						return "请先删除所有市地考试院！";
				}
				if(institution.getExStudents().size()>0){
					return "请先删除该高校下的所有考生！";
				}
				institutionDao.delete(institution);
			}
			return "删除成功！";
		} catch (Exception e) {
			System.out.println("deleteInstitutions error" + e.getMessage());
			return "删除失败！";
		}
	}

	public List<Map> getSchoolAndCityStatusInfo(String institutionnum) {
		try {
			List<Map> IDList= this.loadManagedSchoolList(institutionnum);
			List <Map> resultTemp= institutionDao.getSchoolAndCityStatusInfo(IDList);
			List <Map> allCity= new ArrayList();
			String currHigherInstitutionnum="";
			String currStatusName="";
			String temp="";
			for(Map element:resultTemp){
				temp=(String) element.get("higherInstitutionnum");
				if(!temp.equals(currHigherInstitutionnum)){
					currHigherInstitutionnum=temp;
					currStatusName=this.getCityManagerStatus(currHigherInstitutionnum);
					Map cityManager = new HashMap();
					ExInstitution city=this.institutionDao.findByProperty("institutionnum", currHigherInstitutionnum).get(0);
					cityManager.put("id", city.getId());
					cityManager.put("higherInstitutionnum", city.getHigherInstitution().getInstitutionnum());
					cityManager.put("schoolNum", city.getInstitutionnum());
					cityManager.put("schoolName", city.getName());
					cityManager.put("schoolStatus", currStatusName);
					cityManager.put("schoolStatusId", city.getInstitutionstatus().getId());
					cityManager.put("cityNameAndStatue", city.getName()+",所处状态:"+currStatusName);
					allCity.add(cityManager);
				}
				element.put("cityNameAndStatue", (String)element.get("higherName")+",所处状态:"+currStatusName);
				element.remove("higherName");
				element.remove("higherStatus");
			}
			resultTemp.addAll(allCity);
			return resultTemp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String turnOnSignUp(String schoolnum) {
		ExInstitution entity = this.institutionDao.getInstitutionByInstitutionNum(schoolnum);
		try{
			entity.setSignupflag("1");
		} catch(Exception e) {
			e.printStackTrace();
			return "{success:false,error:{info:'开启报名失败'}}";
		}
		return "{success:true,error:{info:'开启报名成功'}}";
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String turnDownSignUp(String schoolnum) {
		ExInstitution entity = this.institutionDao.getInstitutionByInstitutionNum(schoolnum);
		try{
			entity.setSignupflag("0");
		} catch(Exception e) {
			e.printStackTrace();
			return "{success:false,error:{info:'关闭报名失败'}}";
		}
		return "{success:true,error:{info:'关闭报名成功'}}";
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String checkSignUpStatus(String schoolnum) {
		ExInstitution institution = this.institutionDao.findByProperty("institutionnum", schoolnum).get(0);
		String flag = institution.getSignupflag();
		if(flag == null){
			return "{checkScoreflag:'Off'}";
		}
		if(flag.equals("1")) {
			return "{signupflag:'On'}";
		} else {
			return "{signupflag:'Off'}";
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String turnOnCheckScore(String schoolnum) {
		ExInstitution entity = this.institutionDao.getInstitutionByInstitutionNum(schoolnum);
		try{
			entity.setCheckscoreflag("1");
		} catch(Exception e) {
			e.printStackTrace();
			return "{success:false,error:{info:'开启查分失败'}}";
		}
		return "{success:true,error:{info:'开启查分成功'}}";
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String turnOffCheckScore(String schoolnum) {
		ExInstitution entity = this.institutionDao.getInstitutionByInstitutionNum(schoolnum);
		try{
			entity.setCheckscoreflag("0");
		} catch(Exception e) {
			e.printStackTrace();
			return "{success:false,error:{info:'关闭查分失败'}}";
		}
		return "{success:true,error:{info:'关闭查分成功'}}";
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public String checkCheckScoreStatus(String schoolnum) {
		ExInstitution institution = this.institutionDao.findByProperty("institutionnum", schoolnum).get(0);
		String flag = institution.getCheckscoreflag();
		if(flag == null){
			return "{checkScoreflag:'Off'}";
		}
		if(flag.equals("1")) {
			return "{checkScoreflag:'On'}";
		} else {
			return "{checkScoreflag:'Off'}";
		}
	}
	
}
