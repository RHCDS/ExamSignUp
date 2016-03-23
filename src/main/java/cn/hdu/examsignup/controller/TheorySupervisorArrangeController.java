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

import cn.hdu.examsignup.service.TheorySupervisorArrangeService;

@RemoteProxy(creator = SpringCreator.class)
public class TheorySupervisorArrangeController {
	@Autowired
	private TheorySupervisorArrangeService theorySupervisorArrangeService;
	//获取场次信息
	@RemoteMethod
	public Map loadArrangeInfo() {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = this.theorySupervisorArrangeService.loadArrangeInfo(institutionnum);
		map.put("totalProperty", result.size());
		map.put("root",result);
		return map;
	}
	//根据一个arrange获取该arrange的监考人员
	@RemoteMethod
	public Map loadArrangedSupervisor(String examrooomnum,String sectionid) {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		List<Map> result = this.theorySupervisorArrangeService.loadArrangedSupervisor(examrooomnum,institutionnum,sectionid);
		map.put("totalProperty", result.size());
		map.put("root",result);
		return map;
	}
	//根据场次信息，获得一个arrange可以安排的监考人员
	@RemoteMethod
	public Map loadUnarrangedSupervisor(String sectionid) {
		Map map = new HashMap();
		List<Map> result;
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		result = this.theorySupervisorArrangeService.loadUnarrangedSupervisor(sectionid, institutionnum);
		map.put("totalProperty", result.size());
		map.put("root",result);
		return map;
	}
	//取消莫arrange已安排的监考人员
	@RemoteMethod
	public String deleteArrangedSupervisor(List<Map> supervisors,String examroomnum) {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.theorySupervisorArrangeService.deleteArrangedSupervisor(supervisors,examroomnum,institutionnum);
	}
	//共享监考老师
	@RemoteMethod
	public String MergeSupervisor(List<Map> readyToMerger,String key) {
		String institutionnum = (String ) WebContextFactory.get().getSession().getAttribute("institution");
		return this.theorySupervisorArrangeService.MergeSupervisor(readyToMerger,key,institutionnum);
	}
	@RemoteMethod
	public String arrangeSupervisor(String supervisorid,String examroomnum,String arrangeid) {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.theorySupervisorArrangeService.arrangeSupervisor(supervisorid,examroomnum,arrangeid,institutionnum);
	}
	@RemoteMethod
	public String cancelArrange() {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.theorySupervisorArrangeService.cancelArrange(institutionnum);
	}
	@RemoteMethod
	public String checkArrangeStatus() {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		return this.theorySupervisorArrangeService.checkArrangeStatus(institutionnum);
	}
	@RemoteMethod
	public String autoArrange() {
		String institutionnum = (String) WebContextFactory.get().getSession().getAttribute("institution");
		this.theorySupervisorArrangeService.cancelArrange(institutionnum);
		return this.theorySupervisorArrangeService.autoArrange(institutionnum);
	}
}
