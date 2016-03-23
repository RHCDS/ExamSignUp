package cn.hdu.examsignup.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.ProfessionDao;
import cn.hdu.examsignup.dao.StudentDao;
//import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExProfession;

@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = false)
public class ProfessionService {
	
	@Autowired 
	ProfessionDao professiondao;
	
	@Autowired
	StudentDao studentdao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getProfessionTotalCount(String institutionnum){
		try {
			return professiondao.getProfessionTotalCount(institutionnum);
		} catch (Exception e) {
			System.out.println("getProfessionTotalCount error:" + e.getMessage());
			return 0;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getPageProfessions(String institutionnum, String pageNum, String pageSize){
		try {
			return professiondao.getPageProfessions(institutionnum, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		} catch (Exception e) {
			System.out.println("getPageProfessions error:" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean saveProfession(JSONObject profession,String institutionnum){
		try {
			ExProfession entity = new  ExProfession();
			entity.setName(profession.get("name").toString());
			entity.setProfessionnum(profession.get("professionnum").toString());
			entity.setInstitutionid(institutionnum);
			entity.setRemark(profession.get("remark").toString());
			professiondao.save(entity);
			return true;
		} catch (Exception e) {
			System.out.println("saveProfession error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean updateProfession(JSONObject profession){
		try {
			ExProfession entity = professiondao.findById(profession.get("id").toString());
			entity.setName(profession.get("name").toString());
			entity.setProfessionnum(profession.get("professionnum").toString());
		//	entity.setInstitutionid(profession.get("institutionid").toString());
			entity.setRemark(profession.get("remark").toString());
			professiondao.update(entity);
			return true;
		} catch (Exception e) {
			System.out.println("saveProfession error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteProfession(List<String> ids){
		try {
			for(String id : ids){
				ExProfession entity = professiondao.findById(id);
				if(studentdao.findByProperty("exProfession", entity).size()==0){
					professiondao.delete(entity);
				}else
					return false;
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteProfession error!" + e.getMessage());
			return false;
		}

	}

}
