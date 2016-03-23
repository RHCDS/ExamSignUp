package cn.hdu.examsignup.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.ArrangeSupervisorDao;
import cn.hdu.examsignup.dao.CampusDao;
import cn.hdu.examsignup.dao.InstitutionDao;
import cn.hdu.examsignup.dao.PhysiceexamroomDao;
import cn.hdu.examsignup.dao.TheoryExamArrangeDao;
import cn.hdu.examsignup.model.ExCampus;
import cn.hdu.examsignup.model.ExCollege;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExLanguage;
import cn.hdu.examsignup.model.ExMainmenu;
import cn.hdu.examsignup.model.ExPhysicexamroom;
import cn.hdu.examsignup.model.ExProfession;
import cn.hdu.examsignup.model.ExStudent;

@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class PhysiceexamroomService {
	
	@Autowired 
	PhysiceexamroomDao examroomdao;
	
	@Autowired
	CampusDao campusdao;
	
	@Autowired
	InstitutionDao institutiondao;
	
	@Autowired
	TheoryExamArrangeDao arrangementdao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getExamroomTotalCount(String institutionnum){
		try {
			return examroomdao.getExamroomTotalCount(institutionnum);
		} catch (Exception e) {
			System.out.println("getExamroomTotalCount error:" + e.getMessage());
			return 0;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getPageExamrooms(String institutionnum, String pageNum, String pageSize){
		try {
			return examroomdao.getPageExamrooms(institutionnum, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		} catch (Exception e) {
			System.out.println("getPageExamrooms error:");
			e.printStackTrace();
			return null;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED,readOnly = true) 
	public List<Map> getAllExamrooms(String institution) {
		try{
			return examroomdao.getAllExamrooms(institution);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String savePhysicexamroom(JSONObject examroom,ExInstitution institution){
		try {
			ExPhysicexamroom entity = new  ExPhysicexamroom();
			entity.setRoomlocation(examroom.get("roomlocation").toString());
			entity.setExInstitution(institution);
			if(campusdao.findByProperty("name", examroom.get("exCampus").toString(), institution.getInstitutionnum()).size()>0){
				entity.setExCampus(campusdao.findByProperty("name", examroom.get("exCampus").toString(), institution.getInstitutionnum()).get(0));
			}else{
				ExCampus newcampus = new ExCampus();
				newcampus.setCampusnum("1");
				newcampus.setName(examroom.get("exCampus").toString());
				newcampus.setExInstitution(institution);
				campusdao.save(newcampus);
				entity.setExCampus(newcampus);
			}
			entity.setCapacity(Integer.parseInt(examroom.get("capacity").toString()));
			if(examroom.get("theoryoroperate").toString().equals("理论")){
				entity.setTheoryflag("1");
				entity.setOperateflag("0");
			} else {
				entity.setTheoryflag("0");
				entity.setOperateflag("1");
			}
			
			examroomdao.save(entity);
			return "{ success: true}";
		} catch (Exception e) {
			System.out.println("savePhysicexamroom error!" + e.getMessage());
			return "{ success: false}";
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public String updatePhysicexamroom(JSONObject examroom,ExInstitution institution){
		try {
			ExPhysicexamroom entity = examroomdao.findById(examroom.get("id").toString());
			entity.setRoomlocation(examroom.get("roomlocation").toString());
			entity.setExInstitution(institution);
			if(campusdao.findByProperty("name", examroom.get("exCampus").toString(), institution.getInstitutionnum()).size()>0){
				entity.setExCampus(campusdao.findByProperty("name", examroom.get("exCampus").toString(), institution.getInstitutionnum()).get(0));
			}else{
				ExCampus newcampus = new ExCampus();
				newcampus.setCampusnum("1");
				newcampus.setName(examroom.get("exCampus").toString());
				newcampus.setExInstitution(institution);
				campusdao.save(newcampus);
				entity.setExCampus(newcampus);
			}
			entity.setCapacity(Integer.parseInt(examroom.get("capacity").toString()));
			if(examroom.get("theoryoroperate").toString().equals("理论")){
				entity.setTheoryflag("1");
				entity.setOperateflag("0");
			}else if(examroom.get("theoryoroperate").toString().equals("上机")){
				entity.setTheoryflag("0");
				entity.setOperateflag("1");
			}
			examroomdao.save(entity);
			return"{ success: true}";
		} catch (Exception e) {
			System.out.println("savePhysicexamroom error!" + e.getMessage());
			e.printStackTrace();
			return "{ success: false}";
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deletePhysicexamroom(List<String> ids){
		try {
			for(String id : ids){
				ExPhysicexamroom entity = examroomdao.findById(id);
				if(arrangementdao.findByProperty("exPhysicexamroom", entity).size()==0){
					examroomdao.delete(entity);
				}else
					return false;
			}
			return true;
		} catch (Exception e) {
			System.out.println("deletePhysicexamroom error!" + e.getMessage());
			return false;
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean importExamrooms(List<JSONObject> examrooms,String schoolnum){
		try {
			int roomcount = 0;
			ExPhysicexamroom entity;
			for(JSONObject examroom : examrooms){
				if(examroomdao.findByProperty("roomlocation",examroom.get("roomlocation").toString()).size()>0){
					entity = examroomdao.findByProperty("roomlocation", examroom.get("roomlocation").toString()).get(0);
				}else{
					entity = new ExPhysicexamroom();
				}
				
				ExInstitution school = institutiondao.getInstitutionByInstitutionNum(schoolnum);
				entity.setExInstitution(school);
				if((!examroom.get("campusnum").toString().equals("")) && campusdao.findByProperty("campusnum", examroom.get("campusnum").toString(), schoolnum).size()>0){
					entity.setExCampus(campusdao.findByProperty("campusnum", examroom.get("campusnum").toString(), schoolnum).get(0));
				}else{
					ExCampus newcampus = new ExCampus();
					if((!examroom.get("campusnum").toString().equals(""))){
						newcampus.setCampusnum(examroom.get("campusnum").toString());
						newcampus.setName(examroom.get("campusnum").toString());
						newcampus.setExInstitution(school);
						campusdao.save(newcampus);
					}else{
						newcampus.setCampusnum("1");
						newcampus.setName("1");
						newcampus.setExInstitution(school);
						campusdao.save(newcampus);
						}
					}
				
					entity.setRoomlocation(examroom.get("roomlocation").toString());
					entity.setCapacity(Integer.parseInt(examroom.get("capacity").toString()));
					if(examroom.get("theoryflag").toString().equals("是")||examroom.get("theoryflag").toString().equals("1")){
						entity.setTheoryflag("1");
					}else{entity.setTheoryflag("0");}
					if(examroom.get("operateflag").toString().equals("是")||examroom.get("operateflag").toString().equals("1")){
						entity.setOperateflag("1");
					}else{entity.setOperateflag("0");}
					examroomdao.save(entity);
					if(roomcount%50==0){
						examroomdao.getSession().flush();
						examroomdao.getSession().clear();
					}
					roomcount++;
				}
			return true;
		} catch (Exception e) {
			System.out.println("importStudents error:" + e.getMessage());
			examroomdao.getSession().getTransaction().rollback();
			return false;
		}
	}

}
