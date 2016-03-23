package cn.hdu.examsignup.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.CampusDao;
import cn.hdu.examsignup.dao.PhysiceexamroomDao;
import cn.hdu.examsignup.dao.StudentDao;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExStudent;
import cn.hdu.examsignup.model.ExCampus;


@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class CampusService {
	
	@Autowired 
	CampusDao campusdao;
	
	@Autowired
	StudentDao studentdao;
	
	@Autowired
	PhysiceexamroomDao examroomdao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getCampusTotalCount(String institutionnum){
		try {
			return campusdao.getCampusTotalCount(institutionnum);
		} catch (Exception e) {
			System.out.println("getCampusTotalCount error:" + e.getMessage());
			return 0;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getPageCampuss(String institutionnum,String pageNum, String pageSize){
		try {
			return campusdao.getPageCampuss(institutionnum, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		} catch (Exception e) {
			System.out.println("getPageCampuss error:" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean saveCampus(JSONObject campus,ExInstitution exInstitution){
		try {
			ExCampus entity = new  ExCampus();
			//entity.setId(campus.get("id").toString());
			entity.setName(campus.get("name").toString());
			entity.setCampusnum(campus.get("campusnum").toString());
			entity.setExInstitution(exInstitution);
			//entity.setExStudents(exStudents);
			entity.setRemark(campus.get("remark").toString());
			campusdao.save(entity);
			return true;
		} catch (Exception e) {
			System.out.println("saveCampus error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean updateCampus(JSONObject campus,ExInstitution exInstitution){
		try {
			ExCampus entity = campusdao.findById(campus.get("id").toString());
			entity.setName(campus.get("name").toString());
			entity.setCampusnum(campus.get("campusnum").toString());
			entity.setExInstitution(exInstitution);
			//entity.setExStudents(exStudents);
			entity.setRemark(campus.get("remark").toString());
			campusdao.update(entity);
			return true;
		} catch (Exception e) {
			System.out.println("saveCampus error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteCampus(List<String> ids){
		try {
			for(String id : ids){
				ExCampus entity = campusdao.findById(id);
				if((studentdao.findByProperty("exCampus", entity).size()==0) && (examroomdao.findByProperty("exCampus", entity).size()==0)){
					campusdao.delete(entity);
				}else
					return false;
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteCampus error!" + e.getMessage());
			return false;
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List getCampusList(String institutionnum){
		try {
			return campusdao.getCampusList(institutionnum);
		} catch (Exception e) {
			System.out.println("getCampusList error!" + e.getMessage());
			return null;
		}
	}
	
}
