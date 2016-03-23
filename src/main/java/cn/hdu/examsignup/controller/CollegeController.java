package cn.hdu.examsignup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

//import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.service.CollegeService;
//import cn.hdu.examsignup.service.InstitutionService;
import cn.hdu.examsignup.model.ExCollege;

@RemoteProxy(creator = SpringCreator.class)
public class CollegeController {
	
	@Autowired
	private CollegeService collegeservice;
	
	@RemoteMethod
	public Map paginationShow(String pageNum, String pageSize){
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = collegeservice.getPageColleges(institutionnum, pageNum, pageSize);
		map.put("totalProperty", collegeservice.getCollegeTotalCount(institutionnum));
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public boolean saveCollege(JSONObject college){
		if(college.get("id").toString().equals("")||college.get("id").toString() == ""){
			return collegeservice.saveCollege(college);
		}else{
			return collegeservice.updateCollege(college);
		}
	}


	@RemoteMethod
	public boolean deleteCollege(List<String> ids){
			return collegeservice.deleteCollege(ids);
	}


}
