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

import cn.hdu.examsignup.model.ExArrangement;
import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExMainmenu;
import cn.hdu.examsignup.model.ExSupervisor;
import cn.hdu.examsignup.model.ExUser;
import cn.hdu.examsignup.model.Function;
import cn.hdu.examsignup.service.ArrangementsService;
import cn.hdu.examsignup.service.InstitutionService;
import cn.hdu.examsignup.service.SupervisorService;

@RemoteProxy(creator = SpringCreator.class)
public class SupervisorController{

	@Autowired
	private SupervisorService supervisorservice;

	@Autowired
	private ArrangementsService arrangementsservice;
	
	@Autowired
	private InstitutionService institutionservice;

	@RemoteMethod
	public Map paginationShow(String pageNum, String pageSize,String operateOrThoery){
			Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = supervisorservice.getPageSupervisors(institutionnum,pageNum, pageSize,operateOrThoery);
		map.put("totalProperty", supervisorservice.getSupervisorTotalCount(institutionnum,operateOrThoery));
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public boolean saveSupervisor(JSONObject supervisor){
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
			//ExArrangement exArrangement = arrangementsservice.;//如何处获得参数exArrangement，传给下面
		ExInstitution institution = institutionservice.getInstitutionByInstitutionNum(institutionnum);
		String primaryflag = supervisor.get("primaryflag").toString();
		if(primaryflag.equals("是")){
			primaryflag = "1";
		} else {
			primaryflag = "0";
		}
		supervisor.put("primaryflag", primaryflag);
		if(supervisor.get("id").toString().equals("")||supervisor.get("id").toString() == null){
			return supervisorservice.saveSupervisor(institution,supervisor);
		}else{
			return supervisorservice.updateSupervisor(institution,supervisor);
		}
	}


	@RemoteMethod
	public boolean deleteSupervisor(List<String> ids){
			return supervisorservice.deleteSupervisor(ids);
	}

	@RemoteMethod
	public boolean importSupervisors(List<JSONObject> supervisors){
		String schoolnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return supervisorservice.importSupervisors(supervisors,schoolnum);
	}
}