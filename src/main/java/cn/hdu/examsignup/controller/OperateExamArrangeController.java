package cn.hdu.examsignup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.hdu.examsignup.model.ExInstitution;
import cn.hdu.examsignup.model.ExLanguage;
import cn.hdu.examsignup.model.ExSection;
import cn.hdu.examsignup.service.InstitutionService;
import cn.hdu.examsignup.service.LanguageService;
import cn.hdu.examsignup.service.OperateExamArrangeService;
import cn.hdu.examsignup.service.SectionService;

@RemoteProxy(creator = SpringCreator.class)
public class OperateExamArrangeController {
	@Autowired
	private OperateExamArrangeService operateExamArrangeService;
	@Autowired
	private InstitutionService institutionService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private LanguageService languageService;

	@RemoteMethod
	public Map loadSignedLanguage() {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> result = operateExamArrangeService
				.getLoadSignedLanguage(institutionnum);

		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}

	@RemoteMethod
	public Map loadArrangeInfo(String languageNum) {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		if (languageNum == null || languageNum.trim().isEmpty()
				|| languageNum.trim().equals("0")) {
			List<Map> emptyList = new ArrayList<Map>();
			map.put("totalProperty", 0);
			map.put("root", emptyList);
			return map;
		}

		List<Map> result = operateExamArrangeService.loadArrangeInfo(
				institutionnum, languageNum);
		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}
	
	@RemoteMethod
	public String saveOperateExamroom(String selectNum,String languageNum,List<Map> changedItems){
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		ExInstitution institution =institutionService.getInstitutionByInstitutionNum(institutionnum);
		ExSection section= sectionService.getSectionBySectionnumAndType(institutionnum,selectNum,"operate");
		ExLanguage language= languageService.getLanguageBylanguageNumAndType(languageNum,"operate");
		if(institution == null)
		{
			return "{ success: false, errors:{info: '无法找到学校代码为"+institutionnum+"的学校信息！'}}";
		}
		if(section == null)
		{
			return "{ success: false, errors:{info: '无法找到场次代码为"+section+"的场次信息！'}}";
		}
		if(language ==null)
		{
			return "{ success: false, errors:{info: '无法找到语种代码为"+section+"的语种信息！'}}";
		}
		return this.operateExamArrangeService.saveOperateExamroom(section,language,institution,changedItems);
	}

	@RemoteMethod
	public String loadOperateExamroom(String sectionnumParam,String languagenum){
		int maintenance,sectionnum;
		List<Map> availableOperateExamroom,selectedOperateExamroom;
		JSONArray selectedJson = new JSONArray();
		JSONArray unselectedJson = new JSONArray();
		JSONObject result = new JSONObject();
		
		if(sectionnumParam == null || sectionnumParam.trim().isEmpty())
			return null;
		sectionnum=Integer.parseInt(sectionnumParam.trim());
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		
		availableOperateExamroom = this.operateExamArrangeService.loadAvailableOperateExamroom(institutionnum,sectionnum,languagenum);
		selectedOperateExamroom = this.operateExamArrangeService.loadSelectedOperateExamroom(institutionnum, sectionnum, languagenum);
		
		for(Map element:availableOperateExamroom){
			if(Integer.parseInt(element.get("capacity").toString()) == 0) {
				continue;
			}
			JSONArray temp=new JSONArray();
			temp.add(((String)element.get("id")));
			temp.add(element.get("campusname")+":"+element.get("location")+"(可容纳"+element.get("capacity").toString()+"人)");
			temp.add("new");
			unselectedJson.add(temp);
		}
		result.put("unselected", unselectedJson);
		
		for(Map element:selectedOperateExamroom){
			JSONArray temp=new JSONArray();
			temp.add(((String)element.get("id")));
			temp.add(element.get("campusname") + ":" + element.get("location") + "(可容纳"+element.get("capacity").toString()+"人)");
			temp.add("old");
			selectedJson.add(temp);
		}
		
		result.put("selected", selectedJson);
		result.put("success", true);
		
		return result.toString();
	}
	
	@RemoteMethod
	public Map loadAllSections(){
		Map map = new HashMap();
		
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> result=this.operateExamArrangeService.loadAllSections(institutionnum);
		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}
	//获取已安排考生
	@RemoteMethod
	public Map loadSpecialArrangedStudent(String arrangeid,String languagenum){
		Map map = new HashMap();
		
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> result=this.operateExamArrangeService.loadSpecialArrangedStudent(institutionnum,arrangeid,languagenum);
		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}
	//使已安排的考生变为未安排
	@RemoteMethod
	public String deleteArrangedStudent(List<Map> unArranged){
		return this.operateExamArrangeService.deleteArrangedStudent(unArranged);
	}
	//获取未安排考生
	@RemoteMethod
	public Map loadSpecialUnarrangedStudent(String languagenum) {
		Map map = new HashMap();
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> result = this.operateExamArrangeService.loadSpecialUnarrangedStudent(institutionnum,languagenum);
		map.put("totalProperty", result.size());
		map.put("root", result);
		return map;
	}
	//安排考生
	@RemoteMethod
	public String saveArrangedStudent(List<Map> readyToArrange,String arrangeid ){
		if(arrangeid.trim().isEmpty()){
			return "{ success: false, errors:{info: '没有场次信息！'}}";
		}
		return this.operateExamArrangeService.saveArrangedStudent(readyToArrange,arrangeid);
	}
	//自动安排考生
	@RemoteMethod
	public String autoArrange(String languagenum) {
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		return this.operateExamArrangeService.autoArrange(institutionnum,languagenum);
	}
	//取消安排
	@RemoteMethod
	public String cancelArrange(String languagenum){
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		return this.operateExamArrangeService.cancelArrange(institutionnum,languagenum);
	}
	//获取未安排逻辑考场号信息
	@RemoteMethod
	public Map getUnarrangedExamroom(String languagenum,String arrangeid) {
		Map map = new HashMap();
		String institutionnum =(String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		List<Map> root = this.operateExamArrangeService.getUnarrangedExamroom(institutionnum, languagenum, arrangeid);
		map.put("totalProperty", root.size());
		map.put("root", root);
		return map;
	}
	@RemoteMethod
	public String arrangeByExamroom(String arrangeid,String examroomnum) {
		String institutionnum = (String)WebContextFactory.get().getSession().getAttribute("institution");
		String result = this.operateExamArrangeService.arrangeByExamroom(institutionnum,arrangeid,examroomnum);
		return result;
	}


	//删除已安排考场
	@RemoteMethod
	public String deleteArrangedExamroom(List<Map> examroom) {
		String institutionnum = (String) WebContextFactory.get().getSession()
				.getAttribute("institution");
		return this.operateExamArrangeService.deleteArrangedExamroom(examroom,institutionnum);
	}
	//得到某语种考试安排统计信息
	@RemoteMethod
	public String  getStatisticsByLang(String languagenum) {
		String institutinnum = (String)WebContextFactory.get().getSession()
				.getAttribute("institution");
		return this.operateExamArrangeService.getStatisticsByLang(institutinnum,languagenum);
	}

	//得到某语种按校区分类的考试安排统计信息
		@RemoteMethod
		public Map  getStatisticsByLangAndCampus(String languagenum) {
			String institutinnum = (String)WebContextFactory.get().getSession()
					.getAttribute("institution");
			Map map = new HashMap();
			List<Map> root=this.operateExamArrangeService.getStatisticsByLangAndCampus(institutinnum,languagenum);
			map.put("totalProperty", root.size());
			map.put("root",root);
			return map;
		}
}
