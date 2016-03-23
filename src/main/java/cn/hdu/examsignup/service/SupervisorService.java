package cn.hdu.examsignup.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.ArrangeSupervisorDao;
import cn.hdu.examsignup.dao.CollegeDao;
import cn.hdu.examsignup.dao.InstitutionDao;
import cn.hdu.examsignup.dao.SupervisorDao;
import cn.hdu.examsignup.dao.TheoryExamArrangeDao;
import cn.hdu.examsignup.model.ExArrangement;
import cn.hdu.examsignup.model.ExCampus;
import cn.hdu.examsignup.model.ExCollege;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExMainmenu;
import cn.hdu.examsignup.model.ExPhysicexamroom;
import cn.hdu.examsignup.model.ExSupervisor;

@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class SupervisorService {
	
	@Autowired 
	SupervisorDao supervisordao;
	
	@Autowired 
	InstitutionDao institutiondao;
	
	@Autowired
	ArrangeSupervisorDao arrangesupervisordao;
	
	@Autowired
	private CollegeDao collegeDao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getSupervisorTotalCount(String institutionnum,String operateOrTheory){
		try {
			return supervisordao.getSupervisorTotalCount(institutionnum,operateOrTheory);
		} catch (Exception e) {
			System.out.println("getSupervisorTotalCount error:" + e.getMessage());
			return 0;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getPageSupervisors(String institutionnum, String pageNum, String pageSize,String operateOrthery){
		try {
			return supervisordao.getPageSupervisors(institutionnum,Integer.parseInt(pageNum), Integer.parseInt(pageSize),operateOrthery);
		} catch (Exception e) {
			System.out.println("getPageSupervisors error:" + e.getMessage());
			return null;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getAllSupervisors(String institutionnum){
		try {
			return supervisordao.getAllSupervisors(institutionnum);
		} catch (Exception e) {
			System.out.println("getPageSupervisors error:");
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Boolean saveSupervisor(ExInstitution institution,JSONObject supervisor){
		try {
			ExSupervisor entity = new  ExSupervisor();
//			entity.setId(supervisor.get("id").toString());
			entity.setExInstitution(institution);
			entity.setName(supervisor.get("name").toString());
			if(collegeDao.findByProperty("name", supervisor.get("exCollege").toString(), institution.getInstitutionnum()).size()>0){
				entity.setExCollege(collegeDao.findByProperty("name", supervisor.get("exCollege").toString(), institution.getInstitutionnum()).get(0));
			}else{
				ExCollege newcollege = new ExCollege();
				newcollege.setCollegenum("1");
				newcollege.setName(supervisor.get("exCollege").toString());
				newcollege.setExInstitution(institution);
				collegeDao.save(newcollege);
				entity.setExCollege(newcollege);
			}
			entity.setSupervisornum(supervisor.get("supervisornum").toString());
			entity.setContact(supervisor.get("contact").toString());
			entity.setOperateflag(supervisor.get("operateflag").toString());
			entity.setTheoryflag(supervisor.get("theoryflag").toString());
			entity.setPrimaryflag(supervisor.get("primaryflag").toString());
			supervisordao.save(entity);
			return true;
		} catch (Exception e) {
			System.out.println("saveSupervisor error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Boolean updateSupervisor(ExInstitution institution,JSONObject supervisor){
		try {
			ExSupervisor entity = supervisordao.findById(supervisor.get("id").toString());
			entity.setName(supervisor.get("name").toString());
			entity.setSupervisornum(supervisor.get("supervisornum").toString());
			entity.setContact(supervisor.get("contact").toString());
			entity.setPrimaryflag(supervisor.get("primaryflag").toString());
			if(collegeDao.findByProperty("name", supervisor.get("exCollege").toString(), institution.getInstitutionnum()).size()>0){
				entity.setExCollege(collegeDao.findByProperty("name", supervisor.get("exCollege").toString(), institution.getInstitutionnum()).get(0));
			}else{
				ExCollege newcollege = new ExCollege();
				newcollege.setCollegenum("1");
				newcollege.setName(supervisor.get("exCollege").toString());
				newcollege.setExInstitution(institution);
				collegeDao.save(newcollege);
				entity.setExCollege(newcollege);
			}
			supervisordao.update(entity);
			return true;
		} catch (Exception e) {
			System.out.println("saveSupervisor error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteSupervisor(List<String> ids){
		try {
			for(String id : ids){
				ExSupervisor entity = supervisordao.findById(id);
				if(arrangesupervisordao.findByProperty("exSupervisor", entity).size()==0){
					supervisordao.delete(entity);
				}else
					return false;
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteSupervisor error!" + e.getMessage());
			return false;
		}

	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean importSupervisors(List<JSONObject> supervisors,String schoolnum){
		try {
			int roomcount = 0;
			ExSupervisor entity;
			ExInstitution school = institutiondao.getInstitutionByInstitutionNum(schoolnum);
			for(JSONObject supervisor : supervisors){
				if(supervisordao.findByProperty("supervisornum",supervisor.get("supervisornum").toString()).size()>0){
					entity = supervisordao.findByProperty("supervisornum", supervisor.get("supervisornum").toString()).get(0);
				}else{
					entity = new ExSupervisor();
				}
				
				
				entity.setExInstitution(school);
				entity.setSupervisornum(supervisor.get("supervisornum").toString());
				entity.setName(supervisor.get("name").toString());
				
				List list = collegeDao.findByProperty("name", supervisor.get("exCollege").toString(), school.getInstitutionnum());
				if(collegeDao.findByProperty("name", supervisor.get("exCollege").toString(), school.getInstitutionnum()).size()>0){
					entity.setExCollege(collegeDao.findByProperty("name", supervisor.get("exCollege").toString(), school.getInstitutionnum()).get(0));
				}else{
					ExCollege newcollege = new ExCollege();
					newcollege.setCollegenum("1");
					newcollege.setName(supervisor.get("exCollege").toString());
					newcollege.setExInstitution(school);
					collegeDao.save(newcollege);
					entity.setExCollege(newcollege);
				}
				entity.setContact(supervisor.get("contact").toString());
				if(supervisor.get("theoryflag").toString().equals("是")||supervisor.get("theoryflag").toString().equals("1")){
					entity.setTheoryflag("1");
				}else{entity.setTheoryflag("0");}
				if(supervisor.get("operateflag").toString().equals("是")||supervisor.get("operateflag").toString().equals("1")){
					entity.setOperateflag("1");
				}else{entity.setOperateflag("0");}
				if(supervisor.get("primaryflag").toString().equals("是")||supervisor.get("primaryflag").toString().equals("1")){
					entity.setPrimaryflag("1");
				} else {
					entity.setPrimaryflag("0");
				}
				supervisordao.save(entity);
				if(roomcount%50==0){
					supervisordao.getSession().flush();
					supervisordao.getSession().clear();
				}
				roomcount++;
			}
		return true;
	} catch (Exception e) {
		System.out.println("importStudents error:" + e.getMessage());
		supervisordao.getSession().getTransaction().rollback();
		return false;
	}
}

}
