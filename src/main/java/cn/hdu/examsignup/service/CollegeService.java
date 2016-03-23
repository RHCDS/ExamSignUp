package cn.hdu.examsignup.service;

import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContextFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.hdu.examsignup.dao.CollegeDao;
import cn.hdu.examsignup.dao.InstitutionDao;
import cn.hdu.examsignup.dao.StudentDao;
import cn.hdu.examsignup.model.ExCollege;
import cn.hdu.examsignup.model.ExInstitution;

@Service
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class CollegeService {
	
	@Autowired 
	CollegeDao collegedao;
	
	@Autowired
	private InstitutionService institutionservice;
	
	@Autowired 
	InstitutionDao institutiondao;
	
	@Autowired
	StudentDao studentdao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public long getCollegeTotalCount(String institutionnum){
		try {
			return collegedao.getCollegeTotalCount(institutionnum);
		} catch (Exception e) {
			System.out.println("getCollegeTotalCount error:" + e.getMessage());
			return 0;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map> getPageColleges(String institutionnum,String pageNum, String pageSize){
		try {
			return collegedao.getPageColleges(institutionnum, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		} catch (Exception e) {
			System.out.println("getPageColleges error:" + e.getMessage());
			return null;
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean saveCollege(JSONObject college){
		try {
			ExCollege entity = new  ExCollege();
			entity.setName(college.get("name").toString());
			entity.setCollegenum(college.get("collegenum").toString());
			String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
			ExInstitution institution = institutionservice.getInstitutionByInstitutionNum(institutionnum);
			entity.setExInstitution(institution);
			entity.setRemark(college.get("remark").toString());
			collegedao.save(entity);
			return true;
		} catch (Exception e) {
			System.out.println("saveCollege error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean updateCollege(JSONObject college){
		try {
			ExCollege entity = collegedao.findById(college.get("id").toString());
			entity.setName(college.get("name").toString());
			entity.setCollegenum(college.get("collegenum").toString());
			entity.setRemark(college.get("remark").toString());
			collegedao.update(entity);
			return true;
		} catch (Exception e) {
			System.out.println("saveCollege error!" + e.getMessage());
			return false;
		}	
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean deleteCollege(List<String> ids){
		try {
			for(String id : ids){
				ExCollege entity = collegedao.findById(id);
				if(studentdao.findByProperty("exCollege", entity).size()==0){
					collegedao.delete(entity);
				}else
					return false;
			}
			return true;
		} catch (Exception e) {
			System.out.println("deleteCollege error!" + e.getMessage());
			return false;
		}

	}

	

}
