package cn.hdu.examsignup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hdu.examsignup.service.InstitutionstatusService;

@RemoteProxy(creator = SpringCreator.class)
public class InstitutionstatusController {
	@Autowired
	private InstitutionstatusService institutionstatusService;
	
	@RemoteMethod
	public Map loadinstitutionstatus() {
		Map map = new HashMap();
		List<Map> result = institutionstatusService.loadinstitutionstatus();
		map.put("totalProperty", result ==null?0:result.size());
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public String updateInstitutionTime(Map  timeMap) {
		return this.institutionstatusService.updateInstitutionTime(timeMap);
	}
	
	@RemoteMethod
	public String getCurrenStatus() {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.institutionstatusService.getCurrenStatus(institutionnum);
	}
	
	@RemoteMethod
	public String schoolCheckOK() {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.institutionstatusService.schoolCheckOK(institutionnum);
	}
	@RemoteMethod
	public Map loadStatusList() {
		Map map = new HashMap();
		List<Map> result = this.institutionstatusService.loadStatusList();
		map.put("totalProperty", result ==null?0:result.size());
		map.put("root", result);
		return map;
	}
	@RemoteMethod
	public String updateSchoolStatus(Map param) {
		return this.institutionstatusService.updateSchoolStatus(param);
	}
}
