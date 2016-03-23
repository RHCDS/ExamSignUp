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

import cn.hdu.examsignup.service.OperateSupervisorArrangeService;

@RemoteProxy(creator = SpringCreator.class)
public class OperateSupervisorArrangeController {
	@Autowired
	private OperateSupervisorArrangeService operateSupervisorArrangeService;
	//获取场次信息
	@RemoteMethod
	public Map loadArrangeInfo() {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = this.operateSupervisorArrangeService.loadArrangeInfo(institutionnum);
		map.put("totalProperty", result.size());
		map.put("root",result);
		return map;
	}
	//根据一个arrange获取该arrange的监考人员
	@RemoteMethod
	public Map loadArrangedSupervisor(String arrangeid) {
		Map map = new HashMap();
		List<Map> result = this.operateSupervisorArrangeService.loadArrangedSupervisor(arrangeid);
		map.put("totalProperty", result.size());
		map.put("root",result);
		return map;
	}
	//根据场次信息，获得yigearrange可以安排的监考人员
	@RemoteMethod
	public Map loadUnarrangedSupervisor(String sectionid) {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = this.operateSupervisorArrangeService.loadUnarrangedSupervisor(sectionid, institutionnum);
		map.put("totalProperty", result.size());
		map.put("root",result);
		return map;
	}
	//取消莫arrange已安排的监考人员
	@RemoteMethod
	public String deleteArrangedSupervisor(List<Map> supervisors,String arrangeid) {
		return this.operateSupervisorArrangeService.deleteArrangedSupervisor(supervisors,arrangeid);
	}
	//
	@RemoteMethod
	public String arrangeSupervisor(String supervisorid,String arrangeid) {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.operateSupervisorArrangeService.arrangeSupervisor(supervisorid,arrangeid,institutionnum);
	}
	@RemoteMethod
	public String cancelArrange() {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.operateSupervisorArrangeService.cancelArrange(institutionnum);
	}
	
	@RemoteMethod
	public String checkArrangeStatus(){
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.operateSupervisorArrangeService.checkArrangeStatus(institutionnum);
	}
	//共享监考老师
	@RemoteMethod
	public String MergeSupervisor(List<Map> readyToMerger,String key) {
		String institutionnum = (String ) WebContextFactory.get().getSession().getAttribute("institution");
		return this.operateSupervisorArrangeService.MergeSupervisor(readyToMerger,key,institutionnum);
	}
	@RemoteMethod
	public String autoArrange() {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		this.operateSupervisorArrangeService.cancelArrange(institutionnum);
		return this.operateSupervisorArrangeService.autoArrange(institutionnum);
	}
}
