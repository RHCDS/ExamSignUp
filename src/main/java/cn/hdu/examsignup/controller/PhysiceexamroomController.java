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
import cn.hdu.examsignup.model.ExMainmenu;
import cn.hdu.examsignup.model.ExPhysicexamroom;
import cn.hdu.examsignup.model.ExUser;
import cn.hdu.examsignup.model.Function;
import cn.hdu.examsignup.service.InstitutionService;
import cn.hdu.examsignup.service.PhysiceexamroomService;

@RemoteProxy(creator = SpringCreator.class)
public class PhysiceexamroomController {
	
	@Autowired
	private PhysiceexamroomService physiceexamroomService;
	
	@Autowired
	private InstitutionService institutionservice;
	
	@RemoteMethod
	public Map paginationShow(String pageNum, String pageSize) {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = physiceexamroomService.getPageExamrooms(institutionnum, pageNum, pageSize);
		map.put("totalProperty", physiceexamroomService.getExamroomTotalCount(institutionnum));
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public String savePhysicexamroom(JSONObject examroom){
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		ExInstitution institution = institutionservice.getInstitutionByInstitutionNum(institutionnum);
		if(examroom.get("id").toString().equals("")||examroom.get("id").toString() == ""){
			return physiceexamroomService.savePhysicexamroom(examroom,institution);
		}else{
			return physiceexamroomService.updatePhysicexamroom(examroom,institution);
		}
	}
	
	@RemoteMethod
	public boolean deletePhysicexamroom(List<String> ids){
		return physiceexamroomService.deletePhysicexamroom(ids);
	}

	@RemoteMethod
	public boolean importExamrooms(List<JSONObject> examrooms){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return physiceexamroomService.importExamrooms(examrooms,schoolnum);
	}
}
