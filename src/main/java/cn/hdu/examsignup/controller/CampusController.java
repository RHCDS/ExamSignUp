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

import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.service.CampusService;
import cn.hdu.examsignup.service.InstitutionService;
import cn.hdu.examsignup.model.ExCampus;

@RemoteProxy(creator = SpringCreator.class)
public class CampusController {

	@Autowired
	private CampusService campusservice;
	
	@Autowired
	private InstitutionService institutionservice;
	
	@RemoteMethod
	public Map paginationShow(String pageNum, String pageSize){
			Map map = new HashMap();
			String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = campusservice.getPageCampuss(institutionnum, pageNum, pageSize);
		map.put("totalProperty", campusservice.getCampusTotalCount(institutionnum));
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public boolean saveCampus(JSONObject campus){
			String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
			ExInstitution institution = institutionservice.getInstitutionByInstitutionNum(institutionnum);
		if(campus.get("id").toString().equals("")||campus.get("id").toString() == ""){
			return campusservice.saveCampus(campus,institution);
		}else{
			return campusservice.updateCampus(campus,institution);
		}
	}


	@RemoteMethod
	public boolean deleteCampus(List<String> ids){
			return campusservice.deleteCampus(ids);
	}
	
	@RemoteMethod
	public List getCampusList(){
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return campusservice.getCampusList(institutionnum);
	}

}
